/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;

import db.biometry.biometry.dtos.AdherentDTO;
import db.biometry.biometry.dtos.AdherentFilterDTO;
import db.biometry.biometry.dtos.DetailConsomationAdherentDTO;
import db.biometry.biometry.entite.Dbx45tyAdherent;
import db.biometry.biometry.entite.Dbx45tyAyantDroit;
import db.biometry.biometry.entite.Dbx45tyConsultation;
import db.biometry.biometry.entite.Dbx45tyLignePrestation;
import db.biometry.biometry.exceptions.ResourceNotFoundException;
import db.biometry.biometry.repositories.AdherentRepository;
import db.biometry.biometry.repositories.AyantDroitRepository;
import db.biometry.biometry.repositories.ConsultationRepository;
import db.biometry.biometry.repositories.LignePrestationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des adhérents
 *
 * @author JIATOU FRANCK
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdherentService {

    private final AdherentRepository adherentRepository;
    private final AyantDroitRepository ayantDroitRepository;
    private final ConsultationRepository consultationRepository;
    private final LignePrestationRepository lignePrestationRepository;
    private List<Dbx45tyConsultation> listesAllConsultationsByAdherent = new ArrayList<>();
    private List<Dbx45tyLignePrestation> listesAllPrestationsByAdherent = new ArrayList<>();

    /**
     * Recherche des adhérents avec filtres et pagination
     *
     * @param filter Critères de filtrage
     * @return Page d'adhérents
     */
    public Page<AdherentDTO> searchAdherents(AdherentFilterDTO filter) {
        log.info("Recherche d'adhérents avec filtres: {}", filter);

        // Configuration de la pagination et du tri
        Sort sort = Sort.by(
                "DESC".equalsIgnoreCase(filter.getSortDirection())
                ? Sort.Direction.DESC
                : Sort.Direction.ASC,
                filter.getSortBy() != null ? filter.getSortBy() : "assurePrincipal"
        );

        Pageable pageable = PageRequest.of(
                filter.getPage() != null ? filter.getPage() : 0,
                filter.getSize() != null ? filter.getSize() : 20,
                sort
        );

        // Exécution de la requête avec filtres
        Page<Dbx45tyAdherent> adherents = adherentRepository.searchAdherents(
                filter.getSearch(),
                filter.getSouscripteur(),
                filter.getPolice(),
                filter.getGroupe(),
                filter.getStatut(),
                filter.getSexe(),
                filter.getEnrole(),
                filter.getDateAdhesionMin(),
                filter.getDateAdhesionMax(),
                pageable
        );

        // Conversion en DTO
        return adherents.map(this::convertToSimpleDTO);
    }

    /**
     * Récupère le profil détaillé d'un adhérent
     *
     * @param codeAdherent Code de l'adhérent
     * @return Profil détaillé
     */
    public AdherentDTO getAdherentProfile(String codeAdherent) {
        log.info("Récupération du profil de l'adhérent: {}", codeAdherent);

        Dbx45tyAdherent adherent = adherentRepository.findByCodeAdherent(codeAdherent)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Adhérent non trouvé avec le code: " + codeAdherent));

        return convertToDetailedDTO(adherent);
    }

    /**
     * Crée un nouvel adhérent
     *
     * @param adherentDTO Données de l'adhérent
     * @return Adhérent créé
     */
    @Transactional
    public AdherentDTO createAdherent(AdherentDTO adherentDTO) {
        log.info("Création d'un nouvel adhérent: {}", adherentDTO.getAssurePrincipal());

        // Vérification de l'unicité du matricule
        if (adherentDTO.getMatricule() != null
                && adherentRepository.existsByMatriculeAndSouscripteur(
                        adherentDTO.getMatricule(),
                        adherentDTO.getSouscripteur())) {
            throw new IllegalArgumentException(
                    "Un adhérent avec ce matricule existe déjà pour ce souscripteur");
        }

        // Conversion DTO vers entité
        Dbx45tyAdherent adherent = convertToEntity(adherentDTO);

        // Génération du code adhérent si nécessaire
        if (adherent.getCodeAdherent() == null || adherent.getCodeAdherent().isEmpty()) {
            adherent.setCodeAdherent(generateCodeAdherent(adherentDTO.getSouscripteur()));
        }

        // Enregistrement
        Dbx45tyAdherent savedAdherent = adherentRepository.save(adherent);

        log.info("Adhérent créé avec succès: {}", savedAdherent.getCodeAdherent());
        return convertToDetailedDTO(savedAdherent);
    }

    /**
     * Met à jour un adhérent existant
     *
     * @param codeAdherent Code de l'adhérent
     * @param adherentDTO Nouvelles données
     * @return Adhérent mis à jour
     */
    @Transactional
    public AdherentDTO updateAdherent(String codeAdherent, AdherentDTO adherentDTO) {
        log.info("Mise à jour de l'adhérent: {}", codeAdherent);

        Dbx45tyAdherent adherent = adherentRepository.findByCodeAdherent(codeAdherent)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Adhérent non trouvé avec le code: " + codeAdherent));

        // Mise à jour des champs
        updateEntityFromDTO(adherent, adherentDTO);

        // Enregistrement
        Dbx45tyAdherent updatedAdherent = adherentRepository.save(adherent);

        log.info("Adhérent mis à jour avec succès: {}", codeAdherent);
        return convertToDetailedDTO(updatedAdherent);
    }

    /**
     * Supprime logiquement un adhérent
     *
     * @param codeAdherent Code de l'adhérent
     */
    @Transactional
    public void deleteAdherent(String codeAdherent) {
        log.info("Suppression de l'adhérent: {}", codeAdherent);

        Dbx45tyAdherent adherent = adherentRepository.findByCodeAdherent(codeAdherent)
                .orElseThrow(() -> new ResourceNotFoundException(
                "Adhérent non trouvé avec le code: " + codeAdherent));

        // Suppression logique
        adherent.setStatut("SUPPRIME");
        adherentRepository.save(adherent);

        log.info("Adhérent supprimé avec succès: {}", codeAdherent);
    }

    /**
     * Conversion entité vers DTO simple
     */
    private AdherentDTO convertToSimpleDTO(Dbx45tyAdherent adherent) {
        return AdherentDTO.builder()
                .codeAdherent(adherent.getCodeAdherent())
                .numero(adherent.getNumero())
                .assurePrincipal(adherent.getAssurePrincipal())
                .naissance(adherent.getNaissance())
                .sexe(adherent.getSexe())
                .matricule(adherent.getMatricule())
                .telephone(adherent.getTelephone())
                .taux(adherent.getTaux())
                .souscripteur(adherent.getSouscripteur())
                .police(adherent.getPolice())
                .groupe(adherent.getGroupe())
                .statut(adherent.getStatut())
                .enrole(adherent.getEnrole())
                .souscripteur(adherent.getSouscripteur())
                .police(adherent.getPolice())
                //                .effetPolice(adherent.getEffetPolice())
                //                .echeancePolice(adherent.getEcheancePolice())
                .build();
    }

    /**
     * Conversion entité vers DTO détaillé
     */
    private AdherentDTO convertToDetailedDTO(Dbx45tyAdherent adherent) {
        AdherentDTO dto = convertToSimpleDTO(adherent);

        // Ajout des informations détaillées
        dto.setEffetPolice(adherent.getEffetPolice());
        dto.setEcheancePolice(adherent.getEcheancePolice());
        dto.setDateEnrole(adherent.getDateEnrole() != null
                ? LocalDateTime.ofInstant(adherent.getDateEnrole().toInstant(),
                        java.time.ZoneId.systemDefault()) : null);
        dto.setImprime(adherent.getImprime());

        // Récupération des ayants droit
        List<Dbx45tyAyantDroit> ayantsDroits = ayantDroitRepository.findByAdherent(
                adherent.getCodeAdherent());
        dto.setAyantsDroits(ayantsDroits.stream()
                .map(this::convertAyantDroitToSimpleDTO)
                .collect(Collectors.toList()));

        // Calcul de la consommation
        dto.setConsommation(calculateConsommation(adherent.getCodeAdherent()));

        // Calcul des plafonds
        dto.setPlafonds(calculatePlafonds(adherent));

        return dto;
    }

    /**
     * Conversion ayant droit vers DTO simple
     */
    private AdherentDTO.AyantDroitSimpleDTO convertAyantDroitToSimpleDTO(Dbx45tyAyantDroit ayantDroit) {
        return AdherentDTO.AyantDroitSimpleDTO.builder()
                .codeAyantDroit(ayantDroit.getCodeAyantDroit())
                .nom(ayantDroit.getNom())
                .sexe(ayantDroit.getSexe())
                .naissance(ayantDroit.getNaissance())
                .lienpare(ayantDroit.getLienpare())
                .telephone(ayantDroit.getTelephone())
                .statut(ayantDroit.getStatut())
                .build();
    }

    /**
     * Calcule la consommation d'un adhérent
     */
    private AdherentDTO.ConsommationAdherentDTO calculateConsommation(String codeAdherent) {
        LocalDateTime debutAnnee = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime maintenant = LocalDateTime.now();

        //this.listesAllConsultationsByAdherent=consultationRepository.listeConsultation(codeAdherent,police);
        // long nombreConsultationEncaisse=this.listesAllConsultationsByAdherent.stream()
        //   .filter(con -> con.getEtatConsultation()=="encaisse")
        // .count();
        //BigDecimal
        BigDecimal montantRembourseConsultations = consultationRepository.getMontantRemboursePourAdherent(
                codeAdherent, debutAnnee, maintenant);

        BigDecimal montantRemboursePrestations = lignePrestationRepository.getMontantRemboursePourAdherent(
                codeAdherent, debutAnnee, maintenant);

        BigDecimal montantTotalRembourse = montantRembourseConsultations.add(montantRemboursePrestations);

        return AdherentDTO.ConsommationAdherentDTO.builder()
                .montantTotalRembourse(montantTotalRembourse)
                .montantTotalDepense(BigDecimal.ZERO) // À calculer
                .montantTotalACharge(BigDecimal.ZERO) // À calculer
                .nombreConsultations(0) // À calculer
                .nombrePrestations(0) // À calculer
                .tendance("STABLE")
                .build();
    }

    /**
     * Calcule les plafonds d'un adhérent
     */
    private AdherentDTO.PlafondAdherentDTO calculatePlafonds(Dbx45tyAdherent adherent) {
        // TODO: Implémenter le calcul des plafonds en fonction de la police et du groupe
        return AdherentDTO.PlafondAdherentDTO.builder()
                .plafondGlobal(BigDecimal.ZERO)
                .montantUtilise(BigDecimal.ZERO)
                .montantRestant(BigDecimal.ZERO)
                .pourcentageUtilisation(BigDecimal.ZERO)
                .detailsParCategorie(List.of())
                .build();
    }

    /**
     * Génère un code adhérent unique
     */
    private String generateCodeAdherent(String souscripteur) {
        String prefix = souscripteur.substring(0, Math.min(3, souscripteur.length())).toUpperCase();
        long timestamp = System.currentTimeMillis();
        return String.format("%s-%d", prefix, timestamp);
    }

    /**
     * Conversion DTO vers entité
     */
    private Dbx45tyAdherent convertToEntity(AdherentDTO dto) {
        Dbx45tyAdherent adherent = new Dbx45tyAdherent();
        adherent.setCodeAdherent(dto.getCodeAdherent());
        adherent.setNumero(dto.getNumero() != null ? dto.getNumero() : 0);
        adherent.setAssurePrincipal(dto.getAssurePrincipal());
        adherent.setNaissance(dto.getNaissance());
        adherent.setSexe(dto.getSexe());
        adherent.setMatricule(dto.getMatricule());
        adherent.setTelephone(dto.getTelephone());
        adherent.setTaux(dto.getTaux());
        adherent.setSouscripteur(dto.getSouscripteur());
        adherent.setPolice(dto.getPolice());
        adherent.setEffetPolice(dto.getEffetPolice());
        adherent.setGroupe(dto.getGroupe());
        adherent.setEnrole(dto.getEnrole() != null ? dto.getEnrole() : "N");
        adherent.setImprime(dto.getImprime() != null ? dto.getImprime() : "N");
        adherent.setStatut(dto.getStatut() != null ? dto.getStatut() : "ACTIF");

        return adherent;
    }

    /**
     * Mise à jour de l'entité depuis le DTO
     */
    private void updateEntityFromDTO(Dbx45tyAdherent adherent, AdherentDTO dto) {
        if (dto.getAssurePrincipal() != null) {
            adherent.setAssurePrincipal(dto.getAssurePrincipal());
        }
        if (dto.getNaissance() != null) {
            adherent.setNaissance(dto.getNaissance());
        }
        if (dto.getSexe() != null) {
            adherent.setSexe(dto.getSexe());
        }
        if (dto.getTelephone() != null) {
            adherent.setTelephone(dto.getTelephone());
        }
        if (dto.getTaux() != null) {
            adherent.setTaux(dto.getTaux());
        }
        if (dto.getStatut() != null) {
            adherent.setStatut(dto.getStatut());
        }
    }

    public List<DetailConsomationAdherentDTO> detailConsomationAdherent(LocalDateTime dateDebut, LocalDateTime dateFin, String souscripteur) {
        List<DetailConsomationAdherentDTO> listeAllConsommation = new ArrayList<>();
        DetailConsomationAdherentDTO consomationAdherentDTO;
        Dbx45tyConsultation consult = null;
        Dbx45tyLignePrestation lignePres = null;
        List<Dbx45tyLignePrestation> listeConsomationPrestation = new ArrayList<>();
        List<Dbx45tyConsultation> listeConsomationConsultations = new ArrayList<>();
        listeConsomationConsultations = consultationRepository.listeConsulTationDetailAdherent(souscripteur, dateDebut, dateFin);
        listeConsomationPrestation = lignePrestationRepository.listeDeatailPrestation(souscripteur, dateDebut, dateFin);
        int consultation = 0;
        int ordonnanceExamen = 0;
        while (listeConsomationConsultations.size() > listeConsomationPrestation.size() && consultation < listeConsomationConsultations.size()) {
            consult = listeConsomationConsultations.get(consultation);
            if (listeConsomationPrestation.size() > consultation) {
                lignePres = listeConsomationPrestation.get(consultation);
            }
            consomationAdherentDTO = DetailConsomationAdherentDTO.builder()
                    .montant(consult.getVisiteId().getCodeAyantDroit() == null ? new BigDecimal(consult.getMontantModif().doubleValue()) : BigDecimal.ZERO)
                    .montantAyantDroit(consult.getVisiteId().getCodeAyantDroit() != null ? new BigDecimal(consult.getMontantModif().doubleValue()) : BigDecimal.ZERO)
                    .ayantDroit(consult.getVisiteId().getCodeAyantDroit() != null ? ayantDroitRepository.findByCodeAyantDroit(consult.getVisiteId().getCodeAyantDroit()).get().getNom() : "")
                    .etat(consult.getEtatConsultation())
                    .taux(new BigDecimal(consult.getTaux()))
                    .montantPriseEnCharge(new BigDecimal(consult.getMontantModif().doubleValue()))
                    .prestation(consult.getNatureConsultation())
                    .prestatire(consult.getVisiteId().getPrestataireId())
                    .date(consult.getDate())
                    .build();
            listeAllConsommation.add(consomationAdherentDTO);
            BigDecimal prise = new BigDecimal(lignePres.getValeurModif()).multiply(new BigDecimal(lignePres.getNbreModif()));
            if (lignePres.getTaux().intValue() < 100) {
                prise = prise.subtract(prise.multiply(new BigDecimal(lignePres.getTaux()).divide(new BigDecimal(100.0))));
            }
            consomationAdherentDTO = DetailConsomationAdherentDTO.builder()
                    .montant(lignePres.getPrestationId().getVisiteId().getCodeAyantDroit() == null ? new BigDecimal(lignePres.getValeurModif().doubleValue()) : BigDecimal.ZERO)
                    .montantAyantDroit(lignePres.getPrestationId().getVisiteId().getCodeAyantDroit() != null ? new BigDecimal(lignePres.getValeurModif().doubleValue()) : BigDecimal.ZERO)
                    .ayantDroit(lignePres.getPrestationId().getVisiteId().getCodeAyantDroit() != null ? ayantDroitRepository.findByCodeAyantDroit(lignePres.getPrestationId().getVisiteId().getCodeAyantDroit()).get().getNom() : "")
                    .etat(lignePres.getEtat())
                    .taux(new BigDecimal(lignePres.getTaux()))
                    .montantPriseEnCharge(new BigDecimal(consult.getMontantModif().doubleValue()))
                    .prestation(lignePres.getPrestationId().getNaturePrestation())
                    .prestatire(lignePres.getPrestationId().getPrestataireId().getNom())
                    .date(lignePres.getDate())
                    .date(lignePres.getDate())
                    .valeur(lignePres.getValeurModif().intValue())
                    .build();

            listeAllConsommation.add(consomationAdherentDTO);

            consultation++;
        }
        while (listeConsomationConsultations.size() < listeConsomationPrestation.size() && ordonnanceExamen < listeConsomationConsultations.size()) {
            
             lignePres = listeConsomationPrestation.get(ordonnanceExamen);
            if (listeConsomationConsultations.size() > ordonnanceExamen) {
               consult = listeConsomationConsultations.get(ordonnanceExamen);
            }
            consomationAdherentDTO = DetailConsomationAdherentDTO.builder()
                    .montant(consult.getVisiteId().getCodeAyantDroit() == null ? new BigDecimal(consult.getMontantModif().doubleValue()) : BigDecimal.ZERO)
                    .montantAyantDroit(consult.getVisiteId().getCodeAyantDroit() != null ? new BigDecimal(consult.getMontantModif().doubleValue()) : BigDecimal.ZERO)
                    .ayantDroit(consult.getVisiteId().getCodeAyantDroit() != null ? ayantDroitRepository.findByCodeAyantDroit(consult.getVisiteId().getCodeAyantDroit()).get().getNom() : "")
                    .etat(consult.getEtatConsultation())
                    .taux(new BigDecimal(consult.getTaux()))
                    .montantPriseEnCharge(new BigDecimal(consult.getMontantModif().doubleValue()))
                    .prestation(consult.getNatureConsultation())
                    .prestatire(consult.getVisiteId().getPrestataireId())
                    .date(consult.getDate())
                    .build();
            listeAllConsommation.add(consomationAdherentDTO);
            BigDecimal prise = new BigDecimal(lignePres.getValeurModif()).multiply(new BigDecimal(lignePres.getNbreModif()));
            if (lignePres.getTaux().intValue() < 100) {
                prise = prise.subtract(prise.multiply(new BigDecimal(lignePres.getTaux()).divide(new BigDecimal(100.0))));
            }
            consomationAdherentDTO = DetailConsomationAdherentDTO.builder()
                    .montant(lignePres.getPrestationId().getVisiteId().getCodeAyantDroit() == null ? new BigDecimal(lignePres.getValeurModif().doubleValue()) : BigDecimal.ZERO)
                    .montantAyantDroit(lignePres.getPrestationId().getVisiteId().getCodeAyantDroit() != null ? new BigDecimal(lignePres.getValeurModif().doubleValue()) : BigDecimal.ZERO)
                    .ayantDroit(lignePres.getPrestationId().getVisiteId().getCodeAyantDroit() != null ? ayantDroitRepository.findByCodeAyantDroit(lignePres.getPrestationId().getVisiteId().getCodeAyantDroit()).get().getNom() : "")
                    .etat(lignePres.getEtat())
                    .taux(new BigDecimal(lignePres.getTaux()))
                    .montantPriseEnCharge(new BigDecimal(consult.getMontantModif().doubleValue()))
                    .prestation(lignePres.getPrestationId().getNaturePrestation())
                    .prestatire(lignePres.getPrestationId().getPrestataireId().getNom())
                    .date(lignePres.getDate())
                    .date(lignePres.getDate())
                    .valeur(lignePres.getValeurModif().intValue())
                    .build();

            listeAllConsommation.add(consomationAdherentDTO);

            ordonnanceExamen++;
        }
        
        return listeAllConsommation;

    }

   
public List<AdherentDTO>allAdherentBySoucripteur(String souscripteur){
    return adherentRepository.findBySouscripteur(souscripteur).stream()
            .filter(ad -> ad.getStatut()=="-1")
            .map(adherent -> convertToDetailedDTO(adherent)).toList();
            
}
}
