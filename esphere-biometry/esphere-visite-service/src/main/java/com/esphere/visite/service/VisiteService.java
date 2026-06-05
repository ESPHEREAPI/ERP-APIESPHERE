package com.esphere.visite.service;

import com.esphere.visite.dto.request.VisiteRequest;
import com.esphere.visite.dto.response.*;
import com.esphere.visite.entity.*;
import com.esphere.visite.exception.VisiteException;
import com.esphere.visite.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.esphere.visite.dto.response.VisiteWebserviceResponse;
import com.esphere.visite.dto.response.WebserviceResponse;
import com.esphere.visite.client.AdherentClient;
import com.esphere.visite.client.AuthClient;
import com.esphere.visite.client.AyantDroitDto;
import com.esphere.visite.client.LoginRequestDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VisiteService {

    private final VisiteRepository        visiteRepository;
    private final ConsultationRepository  consultationRepository;
    private final PrestationRepository    prestationRepository;
    private final LignePrestationRepository ligneRepository;
    private final AdherentClient  adherentClient;
private final AuthClient      authClient;


    // ── Visite ───────────────────────────────────────────────────────

    @Transactional
    public VisiteResponse creerVisite(VisiteRequest request) {
        String codeCourt = genererCodeCourt();
        String annee     = String.valueOf(Year.now().getValue());
        String id        = annee + "_" + request.getPrestataireId() + "_" + codeCourt;

        Visite visite = Visite.builder()
                .id(id)
                .codeAdherent(request.getCodeAdherent())
                .codeAyantDroit(request.getCodeAyantDroit())
                .prestataireId(request.getPrestataireId())
                .codeCourt(codeCourt)
                .telephone(request.getTelephone())
                .date(LocalDateTime.now())
                .build();

        visiteRepository.save(visite);
        log.info("Visite créée : {} | code_court : {}", id, codeCourt);

        return toVisiteResponse(visite);
    }
    
      @Transactional
    public int marquerPrestationsSansLignesCommeSupprimees() {
        return prestationRepository.updateSupprimeForPrestationsSansLignes();
    }

    @Transactional(readOnly = true)
    public VisiteResponse getVisite(String id) {
        return visiteRepository.findById(id)
                .map(this::toVisiteResponse)
                .orElseThrow(() -> new VisiteException("Visite introuvable : " + id, 404));
    }

    @Transactional(readOnly = true)
    public VisiteResponse getVisiteByCodeCourt(String codeCourt) {
        return visiteRepository.findByCodeCourt(codeCourt)
                .map(this::toVisiteResponse)
                .orElseThrow(() -> new VisiteException("Code court invalide : " + codeCourt, 404));
    }

    @Transactional(readOnly = true)
    public List<VisiteResponse> getVisitesByPrestataire(String prestataireId) {
        return visiteRepository.findByPrestataire(prestataireId)
                .stream().map(this::toVisiteResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VisiteResponse> getVisitesByAdherent(String codeAdherent) {
        return visiteRepository.findByAdherent(codeAdherent)
                .stream().map(this::toVisiteResponse)
                .collect(Collectors.toList());
    }

    // ── Consultation ─────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ConsultationResponse getConsultation(String visiteId) {
        return consultationRepository.findByVisite(visiteId)
                .map(this::toConsultationResponse)
                .orElseThrow(() -> new VisiteException("Consultation introuvable pour la visite : " + visiteId, 404));
    }

    @Transactional(readOnly = true)
    public List<ConsultationResponse> getConsultationsEnAttente() {
        return consultationRepository.findEnAttente()
                .stream().map(this::toConsultationResponse)
                .collect(Collectors.toList());
    }

    // ── Prestation ───────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<PrestationResponse> getPrestationsByVisite(String visiteId) {
        return prestationRepository.findByVisite(visiteId)
                .stream().map(this::toPrestationResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrestationResponse> getPrestationsEnAttente() {
        return prestationRepository.findEnAttente()
                .stream().map(this::toPrestationResponse)
                .collect(Collectors.toList());
    }

    // ── Génération code_court ────────────────────────────────────────

//    private String genererCodeCourt() {
//        // 6 caractères alphanumériques majuscules
//        return UUID.randomUUID().toString()
//                .replace("-", "")
//                .substring(0, 6)
//                .toUpperCase();
//    }

    // ── Mappers ──────────────────────────────────────────────────────

    private VisiteResponse toVisiteResponse(Visite v) {
        return VisiteResponse.builder()
                .id(v.getId())
                .codeAdherent(v.getCodeAdherent())
                .codeAyantDroit(v.getCodeAyantDroit())
                .prestataireId(v.getPrestataireId())
                .employeId(v.getEmployeId())
                .codeCourt(v.getCodeCourt())
                .telephone(v.getTelephone())
                .date(v.getDate())
                .build();
    }

    private ConsultationResponse toConsultationResponse(Consultation c) {
        return ConsultationResponse.builder()
                .id(c.getId())
                .visiteId(c.getVisiteId())
                .employeValideRejeteId(c.getEmployeValideRejeteId())
                .taux(c.getTaux())
                .typeConsultation(c.getTypeConsultation())
                .natureConsultation(c.getNatureConsultation())
                .natureAffection(c.getNatureAffection())
                .montant(c.getMontant())
                .montantModif(c.getMontantModif())
                .date(c.getDate())
                .dateValideRejete(c.getDateValideRejete())
                .observations(c.getObservations())
                .etatConsultation(c.getEtatConsultation())
                .build();
    }

    private PrestationResponse toPrestationResponse(Prestation p) {
        List<LignePrestationResponse> lignes = ligneRepository
                .findByPrestation(p.getId())
                .stream().map(this::toLigneResponse)
                .collect(Collectors.toList());

        return PrestationResponse.builder()
                .id(p.getId())
                .visiteId(p.getVisiteId())
                .prestataireId(p.getPrestataireId())
                .naturePrestation(p.getNaturePrestation())
                .date(p.getDate())
                .lignes(lignes)
                .build();
    }

    private LignePrestationResponse toLigneResponse(LignePrestation l) {
        return LignePrestationResponse.builder()
                .id(l.getId())
                .prestationId(l.getPrestation().getId())
                .prestataireId(l.getPrestataireId())
                .employeValideRejeteId(l.getEmployeValideRejeteId())
                .medicamentId(l.getMedicamentId())
                .examenId(l.getExamenId())
                .taux(l.getTaux())
                .typeExamen(l.getTypeExamen())
                .descriptionSoins(l.getDescriptionSoins())
                .dentsConcernees(l.getDentsConcernees())
                .codification(l.getCodification())
                .nom(l.getNom())
                .valeur(l.getValeur())
                .nbre(l.getNbre())
                .actePrelevement(l.getActePrelevement())
                .valeurModif(l.getValeurModif())
                .nbreModif(l.getNbreModif())
                .actePrelevementModif(l.getActePrelevementModif())
                .posologie(l.getPosologie())
                .observations(l.getObservations())
                .date(l.getDate())
                .dateValideRejete(l.getDateValideRejete())
                .dateEncaisse(l.getDateEncaisse())
                .etat(l.getEtat())
                .build();
    }
    
    
    // ── Imports à ajouter ─────────────────────────────────


// ── Dans la classe VisiteService ──────────────────────


// ── Méthode genererVisiteLogin ────────────────────────

@Transactional
public WebserviceResponse<VisiteWebserviceResponse>
genererVisiteLogin(
        String codeAdherent,
        String prestataireId,
        String codeAyantDroit,
        String login,
        String telephone) {

    String dateServeur = LocalDate.now()
        .format(DateTimeFormatter.ISO_LOCAL_DATE);

    // 1. Vérifier l'adhérent via Feign
    try {
        var adherent = adherentClient
            .getAdherent(codeAdherent);

        if (adherent == null) {
            return WebserviceResponse.error(
                "Impossible de trouver l'adherent");
        }

        // Vérifier statut
        if ("-1".equals(adherent.getStatut())) {
            return WebserviceResponse.error(
                "Le contrat de l'assure a ete suspendu\n" +
                "Veuillez le diriger vers Zenithe Insurance");
        }

        // Vérifier échéance police
        if (adherent.getEcheancePolice() != null
            && adherent.getEcheancePolice()
                   .compareTo(dateServeur) < 0) {
            return WebserviceResponse.error(
                "Le contrat de l'assure a expire");
        }

    } catch (Exception e) {
        log.error("Erreur appel adherent-service : {}",
                  e.getMessage());
        return WebserviceResponse.error(
            "Impossible de trouver l'adherent");
    }

    // 2. Générer codes visite
    String annee           = String.valueOf(
        LocalDate.now().getYear());
    String codeCourtVisite = genererCodeCourt();
    String idVisite        = annee + "_"
        + prestataireId + "_"
        + codeCourtVisite;

    // 3. Vérifier unicité du code court
    int tentatives = 0;
    while (visiteRepository
               .findByCodeCourt(codeCourtVisite)
               .isPresent()
           && tentatives < 10) {
        codeCourtVisite = genererCodeCourt();
        idVisite = annee + "_"
            + prestataireId + "_"
            + codeCourtVisite;
        tentatives++;
    }

    // 4. Créer la visite
    Visite visite = Visite.builder()
        .id(idVisite)
        .codeAdherent(codeAdherent)
        .codeAyantDroit(codeAyantDroit)
        .prestataireId(prestataireId)
        .codeCourt(codeCourtVisite)
        .telephone(telephone)
        .date(LocalDateTime.now())
        .build();

    visiteRepository.save(visite);

    log.info("Visite créée ✓ id={} court={} " +
             "adherent={} prestataire={}",
             idVisite, codeCourtVisite,
             codeAdherent, prestataireId);

    // 5. Construire réponse
    VisiteWebserviceResponse data =
        VisiteWebserviceResponse.builder()
            .id(codeCourtVisite)
            .idVisite(idVisite)
            .idVisiteCrypte(idVisite)
            .codeAdherent(codeAdherent)
            .codeAyantDroit(codeAyantDroit)
            .prestataire(prestataireId)
            .build();

    return WebserviceResponse.ok(data);
}

// ── Méthode connexionLegacy ───────────────────────────

public WebserviceResponse<String> connexionLegacy(LoginRequestDto request) {
    try {
        var response = authClient.loginLegacy(request);
        if (response != null
                && response.getToken() != null) {
            return WebserviceResponse.ok("1");
        }
        return WebserviceResponse.error(
            "Login or password incorrect");
    } catch (Exception e) {
        return WebserviceResponse.error(
            "Login or password incorrect");
    }
}

// ── Générateur code court ─────────────────────────────

/**
 * Génère un code alphanumérique de 6 caractères.
 * Exclut les caractères ambigus (0, O, I, 1).
 * Ex : ME6ED5, 3KP7QX
 */
private String genererCodeCourt() {
    String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    StringBuilder code = new StringBuilder();
    Random random = new Random();
    for (int i = 0; i < 6; i++) {
        code.append(
            chars.charAt(random.nextInt(chars.length()))
        );
    }
    return code.toString();
}

@Transactional(readOnly = true)
public List<AyantDroitWebserviceResponse>
getListeAyantDroit(String codeAdherent) {

    try {
        // Appel vers adherent-service via Feign
        List<AyantDroitDto> ayantsDroit =
            adherentClient.getAyantsDroit(codeAdherent);

        if (ayantsDroit == null) {
            return List.of();
        }

        return ayantsDroit.stream()
            .map(a -> AyantDroitWebserviceResponse.builder()
                .codeAyantDroit(a.getCodeAyantDroit())
                .codeAdherent(codeAdherent)
                .nom(a.getNom())
                .sexe(a.getSexe())
                .naissance(a.getNaissance())
                .police(a.getPolice())
                .telephone(a.getTelephone())
                .build())
            .collect(Collectors.toList());

    } catch (Exception e) {
        log.error("Erreur getListeAyantDroit : {}",
                  e.getMessage());
        return List.of();
    }
}
}