package com.esphere.media.service;

import com.esphere.media.config.StorageConfig;
import com.esphere.media.dto.response.MediaResponse;
import com.esphere.media.entity.MediaPrestation;
import com.esphere.media.exception.MediaException;
import com.esphere.media.repository.MediaRepository;
import com.esphere.media.util.MediaValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaRepository mediaRepository;
    private final StorageConfig   storageConfig;
    private final RestTemplate    restTemplate;

    @Value("${services.visite-url:http://localhost:8084}")
    private String visiteServiceUrl;

    @Value("${services.validation-url:http://localhost:8085}")
    private String validationUrl;

    @Value("${services.adherent-url:http://localhost:8082}")
    private String adherentServiceUrl;

    // ── UPLOAD via code_court (mobile prestataire) ────────────────

    @Transactional
    public MediaResponse uploadParCodeCourt(
            String codeCourt,
            MultipartFile fichier,
            Integer employeId,
            boolean demandeParSs,
            Integer prestationId,
            String naturePrestation) {

        Map<String, Object> visite = getVisiteParCodeCourt(codeCourt);
        String visiteId       = (String) visite.get("id");
        String codeAdherent   = (String) visite.get("codeAdherent");
        String codeAyantDroit = (String) visite.get("codeAyantDroit");
        String prestataireId  = (String) visite.get("prestataireId");

        return upload(fichier, visiteId, codeAdherent, codeAyantDroit,
                prestataireId, employeId, demandeParSs, prestationId, naturePrestation);
    }

    // ── UPLOAD direct avec visiteId ───────────────────────────────

    @Transactional
    public MediaResponse uploadParVisiteId(
            String visiteId,
            MultipartFile fichier,
            Integer employeId,
            boolean demandeParSs,
            Integer prestationId,
            String naturePrestation) {

        Map<String, Object> visite = getVisiteParId(visiteId);
        String codeAdherent   = (String) visite.get("codeAdherent");
        String codeAyantDroit = (String) visite.get("codeAyantDroit");
        String prestataireId  = (String) visite.get("prestataireId");

        return upload(fichier, visiteId, codeAdherent, codeAyantDroit,
                prestataireId, employeId, demandeParSs, prestationId, naturePrestation);
    }

    // ── APPROUVER un document ─────────────────────────────────────

    @Transactional
    public MediaResponse approuver(Integer mediaId, Integer employeId) {
        MediaPrestation media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new MediaException("Média introuvable : " + mediaId, 404));
        media.setStatutDocument("approuve");
        media.setCommentaireRejet(null);
        media.setEmployeId(employeId);
        return toResponse(mediaRepository.save(media));
    }

    // ── REJETER un document ───────────────────────────────────────

    @Transactional
    public MediaResponse rejeter(Integer mediaId, String commentaire, Integer employeId) {
        if (commentaire == null || commentaire.isBlank()) {
            throw new MediaException("Un commentaire est obligatoire pour rejeter le document.", 400);
        }
        MediaPrestation media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new MediaException("Média introuvable : " + mediaId, 404));
        media.setStatutDocument("rejete");
        media.setCommentaireRejet(commentaire);
        media.setEmployeId(employeId);
        MediaResponse response = toResponse(mediaRepository.save(media));

        rejeterPrestationsVisite(media.getVisiteId(), commentaire);

        return response;
    }

    private void rejeterPrestationsVisite(String visiteId, String commentaire) {
        try {
            String url = validationUrl + "/validations/rejet-document";
            new org.springframework.web.client.RestTemplate().postForObject(
                    url,
                    java.util.Map.of("visiteId", visiteId, "commentaire", commentaire),
                    java.util.Map.class);
            log.info("Prestations rejetées suite rejet document visite {}", visiteId);
        } catch (Exception e) {
            log.error("Erreur rejet prestations visite {} : {}", visiteId, e.getMessage());
        }
    }

    // ── Lecture par prestation ────────────────────────────────────

    @Transactional(readOnly = true)
    public List<MediaResponse> getParPrestation(Integer prestationId) {
        return mediaRepository.findByPrestationId(prestationId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ── Méthode principale d'upload ───────────────────────────────

    private MediaResponse upload(
            MultipartFile fichier,
            String visiteId,
            String codeAdherent,
            String codeAyantDroit,
            String prestataireId,
            Integer employeId,
            boolean demandeParSs,
            Integer prestationId,
            String naturePrestation) {

        // 2. Extraire et valider l'extension
        String extension = MediaValidator.extraireExtension(
                fichier.getOriginalFilename());
        MediaValidator.valider(fichier, extension);

        String typeMedia = MediaValidator.detecterTypeMedia(extension);

        // 3. Récupérer souscripteur + police depuis Adherent Service
        Map<String, Object> adherent = getAdherent(codeAdherent);
        String souscripteur = (String) adherent.get("souscripteur");
        String police       = (String) adherent.get("police");

        if (souscripteur == null || police == null) {
            throw new MediaException(
                    "Souscripteur ou police manquant pour l'adhérent : "
                    + codeAdherent, 400);
        }

        // Nettoyer les noms pour le système de fichiers
        String souscripteurSafe = souscripteur.replaceAll("[^a-zA-Z0-9_-]", "_");
        String policeSafe       = police.replaceAll("[^a-zA-Z0-9_-]", "_");
        String adherentSafe     = codeAdherent.replaceAll("[^a-zA-Z0-9_-]", "_");

        // 4. Construire le chemin de stockage
        // C:/biometry-media/{souscripteur}/{police}/{code_adherent}/
        Path dossier = storageConfig.getDossierAdherent(
                souscripteurSafe, policeSafe, adherentSafe);

        try {
            storageConfig.creerDossierSiAbsent(dossier);
        } catch (IOException e) {
            throw new MediaException("Impossible de créer le dossier de stockage.", 500);
        }

        // 5. Générer le nom de fichier unique
        String timestamp  = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String visteIdSafe = visiteId.replaceAll("[^a-zA-Z0-9_-]", "_");
        String nomFichier = visteIdSafe + "_" + timestamp
                + "_" + fichier.getOriginalFilename()
                       .replaceAll("[^a-zA-Z0-9._-]", "_");

        Path cheminFichier = dossier.resolve(nomFichier);

        // 6. Sauvegarder le fichier sur disque
        try {
            Files.copy(fichier.getInputStream(), cheminFichier,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Erreur sauvegarde fichier : {}", e.getMessage());
            throw new MediaException("Erreur lors de la sauvegarde du fichier.", 500);
        }

        // 7. Enregistrer les métadonnées en base
        MediaPrestation media = MediaPrestation.builder()
                .visiteId(visiteId)
                .prestationId(prestationId)
                .naturePrestation(naturePrestation)
                .codeAdherent(codeAdherent)
                .codeAyantDroit(codeAyantDroit)
                .prestataireId(prestataireId)
                .souscripteur(souscripteur)
                .police(police)
                .nomFichier(nomFichier)
                .chemin(cheminFichier.toString())
                .typeMedia(typeMedia)
                .extension(extension)
                .taille(fichier.getSize())
                .demandeParSs(demandeParSs ? Boolean.TRUE : Boolean.FALSE)
                .employeId(employeId)
                .dateUpload(LocalDateTime.now())
                .supprime("-1")
                .statutDocument("en_attente_revue")
                .build();

        mediaRepository.save(media);

        log.info("Média sauvegardé : {} | type : {} | taille : {} octets",
                nomFichier, typeMedia, fichier.getSize());

        return toResponse(media);
    }

    // ── LECTURES ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<MediaResponse> getParVisite(String visiteId) {
        return mediaRepository.findByVisite(visiteId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MediaResponse> getParAdherent(String codeAdherent) {
        return mediaRepository.findByAdherent(codeAdherent)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MediaResponse> getParPrestataire(String prestataireId) {
        return mediaRepository.findByPrestataire(prestataireId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MediaResponse> getDemandesParSs() {
        return mediaRepository.findDemandesParSs()
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── TÉLÉCHARGEMENT ───────────────────────────────────────────

    public byte[] telecharger(Integer mediaId) {
        MediaPrestation media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new MediaException(
                        "Média introuvable : " + mediaId, 404));

        try {
            return Files.readAllBytes(Path.of(media.getChemin()));
        } catch (IOException e) {
            throw new MediaException("Fichier introuvable sur le disque.", 404);
        }
    }

    public String getContentType(Integer mediaId) {
        MediaPrestation media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new MediaException(
                        "Média introuvable : " + mediaId, 404));

        return switch (media.getTypeMedia()) {
            case "image"    -> "image/" + media.getExtension();
            case "document" -> "application/pdf";
            case "video"    -> "video/" + media.getExtension();
            default         -> "application/octet-stream";
        };
    }

    // ── Appels inter-services ────────────────────────────────────

    @SuppressWarnings("unchecked")
    private Map<String, Object> getVisiteParCodeCourt(String codeCourt) {
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    visiteServiceUrl + "/visites/code/" + codeCourt,
                    HttpMethod.GET, null, Map.class);
            return response.getBody();
        } catch (Exception e) {
            throw new MediaException(
                    "Visite introuvable pour le code court : " + codeCourt, 404);
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getVisiteParId(String visiteId) {
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    visiteServiceUrl + "/visites/" + visiteId,
                    HttpMethod.GET, null, Map.class);
            return response.getBody();
        } catch (Exception e) {
            throw new MediaException(
                    "Visite introuvable : " + visiteId, 404);
        }
    }

    @SuppressWarnings("unchecked")
//    private Map<String, Object> getAdherent(String codeAdherent) {
//        try {
//            ResponseEntity<Map> response = restTemplate.exchange(
//                    adherentServiceUrl + "/adherents/" + codeAdherent,
//                    HttpMethod.GET, null, Map.class);
//            return response.getBody();
//        } catch (Exception e) {
//            throw new MediaException(
//                    "Adhérent introuvable : " + codeAdherent, 404);
//        }
//    }
    private Map<String, Object> getAdherent(String codeAdherent) {
    try {
        String urlEncodee = adherentServiceUrl + "/adherents/" 
                + org.springframework.web.util.UriUtils.encodePath(
                        codeAdherent, "UTF-8");
        
        ResponseEntity<Map> response = restTemplate.exchange(
                urlEncodee, HttpMethod.GET, null, Map.class);
        return response.getBody();
    } catch (Exception e) {
        log.error("Erreur appel Adherent Service : {}", e.getMessage());
        throw new MediaException(
                "Adhérent introuvable : " + codeAdherent, 404);
    }
}

    // ── Mapper ───────────────────────────────────────────────────

    private MediaResponse toResponse(MediaPrestation m) {
        return MediaResponse.builder()
                .id(m.getId())
                .visiteId(m.getVisiteId())
                .prestationId(m.getPrestationId())
                .naturePrestation(m.getNaturePrestation())
                .codeAdherent(m.getCodeAdherent())
                .codeAyantDroit(m.getCodeAyantDroit())
                .prestataireId(m.getPrestataireId())
                .souscripteur(m.getSouscripteur())
                .police(m.getPolice())
                .nomFichier(m.getNomFichier())
                .chemin(m.getChemin())
                .typeMedia(m.getTypeMedia())
                .extension(m.getExtension())
                .taille(m.getTaille())
                .demandeParSs(m.getDemandeParSs())
                .employeId(m.getEmployeId())
                .dateUpload(m.getDateUpload())
                .statutDocument(m.getStatutDocument())
                .commentaireRejet(m.getCommentaireRejet())
                .build();
    }
}