package com.esphere.validation.service;

import com.esphere.validation.dto.request.ConsultationSoumissionRequest;
import com.esphere.validation.dto.request.PrestationSoumissionRequest;
import com.esphere.validation.dto.request.ValidationConsultationRequest;
import com.esphere.validation.dto.request.ValidationLigneRequest;
import com.esphere.validation.dto.response.ConsultationEnAttenteResponse;
import com.esphere.validation.dto.response.LigneEnAttenteResponse;
import com.esphere.validation.dto.response.PrestationResponse;
import com.esphere.validation.dto.response.VisiteInfoResponse;
import com.esphere.validation.entity.*;
import com.esphere.validation.exception.ValidationException;
import com.esphere.validation.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final ConsultationRepository consultationRepository;
    private final LignePrestationRepository ligneRepository;
    private final VisiteRepository visiteRepository;
    private final PrestationRepository prestationRepository;
    private final PrestataireRepository prestataireRepository;
    private final AdherentRepository adherentRepository;
    private final AdherentExterneService adherentExterneService;
    private final TauxPrestationRepository tauxPrestationRepository;
   
    // ── CONSULTATIONS ─────────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<ConsultationEnAttenteResponse> getConsultationsEnAttente() {
        return consultationRepository.findEnAttente()
                .stream()
                .map(this::toConsultationResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConsultationEnAttenteResponse> getConsultationsEnAttenteByPrestataire(
            String prestataireId) {
        return consultationRepository.findEnAttenteByPrestataire(prestataireId)
                .stream()
                .map(this::toConsultationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ConsultationEnAttenteResponse validerConsultation(
            Integer consultationId,
            ValidationConsultationRequest request) {

        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ValidationException(
                "Consultation introuvable : " + consultationId, 404));

        if (!"attente_validation".equals(consultation.getEtatConsultation())) {
            throw new ValidationException(
                    "Cette consultation n'est pas en attente de validation. "
                    + "Etat actuel : " + consultation.getEtatConsultation(), 400);
        }

        consultation.setEtatConsultation(request.getDecision());
        consultation.setEmployeValideRejeteId(request.getEmployeId());
        consultation.setDateValideRejete(LocalDateTime.now());
        consultation.setObservations(request.getObservations());
        consultation.setTaux(request.getTaux());
        consultation.setMontantModif(
                request.getMontantModif() != null
                ? request.getMontantModif()
                : consultation.getMontant());

        consultationRepository.save(consultation);
        log.info("Consultation {} {} par employé {}",
                consultationId, request.getDecision(), request.getEmployeId());

        return toConsultationResponse(consultation);
    }

    // ── LIGNES DE PRESTATION ──────────────────────────────────────
    @Transactional(readOnly = true)
    public List<LigneEnAttenteResponse> getLignesEnAttente() {
        return ligneRepository.findEnAttente()
                .stream()
                .map(l -> toLigneResponse(l, null))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LigneEnAttenteResponse> getLignesEnAttenteByPrestataire(
            String prestataireId) {
        return ligneRepository.findEnAttenteByPrestataire(prestataireId)
                .stream()
                .map(l -> toLigneResponse(l, null))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LigneEnAttenteResponse> getLignesByPrestation(Integer prestationId) {
        Prestation prestation = prestationRepository.findById(prestationId)
                .orElseThrow(() -> new ValidationException(
                "Prestation introuvable : " + prestationId, 404));

        Visite visite = visiteRepository
                .findById(prestation.getVisiteId()).orElse(null);

        return ligneRepository.findByPrestation(prestationId)
                .stream()
                .map(l -> toLigneResponse(l, visite))
                .collect(Collectors.toList());
    }

    @Transactional
    public LigneEnAttenteResponse validerLigne(
            Integer ligneId,
            ValidationLigneRequest request) {

        LignePrestation ligne = ligneRepository.findById(ligneId)
                .orElseThrow(() -> new ValidationException(
                "Ligne de prestation introuvable : " + ligneId, 404));

        if (!"attente_validation".equals(ligne.getEtat())) {
            throw new ValidationException(
                    "Cette ligne n'est pas en attente de validation. "
                    + "Etat actuel : " + ligne.getEtat(), 400);
        }

        ligne.setEtat(request.getDecision());
        ligne.setEmployeValideRejeteId(request.getEmployeId());
        ligne.setDateValideRejete(LocalDateTime.now());
        ligne.setObservations(request.getObservations());
        ligne.setValeurModif(
                request.getValeurModif() != null
                ? request.getValeurModif()
                : ligne.getValeur());
        ligne.setNbreModif(
                request.getNbreModif() != null
                ? request.getNbreModif()
                : ligne.getNbre());
        ligne.setTaux(
                request.getTaux() != null
                ? request.getTaux()
                : ligne.getTaux());
        if (request.getActePrelevementModif() != null) {
            ligne.setActePrelevementModif(request.getActePrelevementModif());
        }

        ligneRepository.save(ligne);
        log.info("Ligne {} {} par employé {}",
                ligneId, request.getDecision(), request.getEmployeId());

        Prestation pr = prestationRepository.findById(ligne.getPrestationId())
                .orElseThrow(() -> new ValidationException(
                "Prestation introuvable", 400));
        Visite visite = visiteRepository.findById(pr.getVisiteId())
                .orElseThrow(() -> new ValidationException(
                "Visite introuvable", 400));

        return toLigneResponse(ligne, visite);
    }

    // ── ENCAISSEMENT ─────────────────────────────────────────────
    @Transactional
    public void encaisserConsultation(Integer consultationId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ValidationException(
                "Consultation introuvable : " + consultationId, 404));

        if (!"valide".equals(consultation.getEtatConsultation())) {
            throw new ValidationException(
                    "Seules les consultations validées peuvent être encaissées.", 400);
        }

        consultation.setEtatConsultation("encaisse");
        consultationRepository.save(consultation);
        log.info("Consultation {} encaissée", consultationId);
    }

    @Transactional
    public void encaisserLigne(Integer ligneId) {
        LignePrestation ligne = ligneRepository.findById(ligneId)
                .orElseThrow(() -> new ValidationException(
                "Ligne introuvable : " + ligneId, 404));

        if (!"valide".equals(ligne.getEtat())) {
            throw new ValidationException(
                    "Seules les lignes validées peuvent être encaissées.", 400);
        }

        ligne.setEtat("encaisse");
        ligne.setDateEncaisse(LocalDateTime.now());
        ligneRepository.save(ligne);
        log.info("Ligne {} encaissée", ligneId);
    }

    // ── PAGINATION CONSULTATIONS ──────────────────────────────────
    @Transactional(readOnly = true)
    public Map<String, Object> getConsultationsPaginees(
            int page, int size,
            String prestataireId,
            String etat,
            String typeConsultation,
            String souscripteur,
            String nomAdherent,
            String nomAyantDroit,
            String dateMin,
            String dateMax) {

        Pageable pageable = PageRequest.of(page, size);

        LocalDateTime dateMinDt = parseDate(dateMin, false);
        LocalDateTime dateMaxDt = parseDate(dateMax, true);

        Page<Consultation> pageResult = consultationRepository.findAllActives(
                (prestataireId != null && !prestataireId.isBlank()) ? prestataireId : null,
                (etat != null && !etat.isBlank()) ? etat : null,
                (typeConsultation != null && !typeConsultation.isBlank()) ? typeConsultation : null,
                (souscripteur != null && !souscripteur.isBlank()) ? souscripteur : null,
                (nomAdherent != null && !nomAdherent.isBlank()) ? nomAdherent : null,
                dateMinDt,
                dateMaxDt,
                pageable
        );

        List<ConsultationEnAttenteResponse> content = pageResult.getContent()
                .stream()
                .map(this::toConsultationResponse)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("content", content);
        response.put("totalElements", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("currentPage", page);
        response.put("pageSize", size);
        return response;
    }

    // ── PAGINATION PRESTATIONS (ordonnance / examen) ──────────────
    @Transactional(readOnly = true)
    public Map<String, Object> getPrestationsPaginees(
            int page, int size, String nature,
            String prestataireId,
            String dateMin, String dateMax,
            String souscripteur,
            String adherent,
            String ayantDroit,
            String etat) {

        Pageable pageable = PageRequest.of(page, size);

        LocalDateTime dMin = (dateMin != null && !dateMin.isEmpty())
                ? LocalDateTime.parse(dateMin + "T00:00:00") : null;
        LocalDateTime dMax = (dateMax != null && !dateMax.isEmpty())
                ? LocalDateTime.parse(dateMax + "T23:59:59") : null;

        Page<Prestation> pageResult = prestationRepository.findAllByNature(
                nature,
                (prestataireId != null && !prestataireId.isEmpty()) ? prestataireId : null,
                (etat != null && !etat.isEmpty()) ? etat : null,
                dMin, dMax,
                (souscripteur != null && !souscripteur.isEmpty()) ? souscripteur : null,
                (adherent != null && !adherent.isEmpty()) ? adherent : null,
                (ayantDroit != null && !ayantDroit.isEmpty()) ? ayantDroit : null,
                pageable
        );

        List<PrestationResponse> content = pageResult.getContent()
                .stream()
                .map(this::toPrestationResponse)
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("content", content);
        response.put("totalElements", pageResult.getTotalElements());
        response.put("totalPages", pageResult.getTotalPages());
        response.put("currentPage", page);
        response.put("pageSize", size);
        return response;
    }

    // ── UTILITAIRES ───────────────────────────────────────────────
    private LocalDateTime parseDate(String dateStr, boolean endOfDay) {
        if (dateStr == null || dateStr.isBlank()) {
            return null;
        }
        try {
            LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
            return endOfDay ? date.atTime(23, 59, 59) : date.atStartOfDay();
        } catch (Exception e) {
            log.warn("Date invalide ignorée : {}", dateStr);
            return null;
        }
    }

    public boolean policeValide(String visiteId) {
        LocalDate today = LocalDate.now();
        Visite v = visiteRepository.findById(visiteId).orElse(null);
        if (v == null) {
            return false;
        }

        Adherent adhe = adherentRepository
                .findActiveByCode(v.getCodeAdherent()).orElse(null);
        if (adhe == null) {
            return false;
        }

        LocalDate effet = adhe.getEffetPolice();
        LocalDate echeance = adhe.getEcheancePolice();
        if (effet == null || echeance == null) {
            return false;
        }

        return !effet.isAfter(today) && !echeance.isBefore(today);
    }

    // ── MAPPERS ───────────────────────────────────────────────────
    private ConsultationEnAttenteResponse toConsultationResponse(Consultation c) {
        Visite visite = visiteRepository.findById(c.getVisiteId()).orElse(null);

        Adherent adherent = adherentRepository
                .findActiveByCode(visite.getCodeAdherent())
                .orElseThrow(() -> new ValidateException(
                "Assuré Principal introuvable : "
                + visite.getCodeAdherent()));

        AyantDroit ay = null;
        if (visite.getCodeAyantDroit() != null) {
            ay = adherent.getAyantsDroit().stream()
                    .filter(adh -> adh.getCodeAyantDroit()
                    .equals(visite.getCodeAyantDroit()))
                    .findFirst()
                    .orElse(null);
        }

        return ConsultationEnAttenteResponse.builder()
                .id(c.getId())
                .visiteId(c.getVisiteId())
                .codeAdherent(visite.getCodeAdherent())
                .codeAyantDroit(visite.getCodeAyantDroit())
                .prestataireId(visite.getPrestataireId())
                .prestataireNom(prestataireRepository
                        .findActiveById(visite.getPrestataireId())
                        .map(Prestataire::getNom)
                        .orElse(visite.getPrestataireId()))
                .typeConsultation(c.getTypeConsultation())
                .natureConsultation(c.getNatureConsultation())
                .natureAffection(c.getNatureAffection())
                .montant(c.getMontant())
                .taux(c.getTaux())
                .observations(c.getObservations())
                .etatConsultation(c.getEtatConsultation())
                .date(c.getDate())
                .groupe(adherent.getGroupe())
                .souscripteur(adherent.getSouscripteur())
                .montantValide(c.getMontantModif() != null
                        ? c.getMontantModif() : c.getMontant())
                .partZenithe(calculPartZenithe(
                        c.getMontantModif() != null
                        ? c.getMontantModif() : c.getMontant(),
                        c.getTaux()))
                .partAssure(calculPartAssure(
                        c.getMontantModif() != null
                        ? c.getMontantModif() : c.getMontant(),
                        c.getTaux()))
                .build();
    }

    // ── toLigneResponse — VERSION UNIQUE ─────────────────────────
    private LigneEnAttenteResponse toLigneResponse(
            LignePrestation l, Visite visite) {

        // Nom prestataire
        String nomPrestataire = null;
        if (l.getPrestataireId() != null) {
            var prestataire = prestataireRepository
                    .findById(l.getPrestataireId()).orElse(null);
            if (prestataire != null) {
                nomPrestataire = prestataire.getNom();
            }
        }

        // Si visite null → retourner une réponse minimale
        if (visite == null) {
            return LigneEnAttenteResponse.builder()
                    .id(l.getId())
                    .prestationId(l.getPrestationId())
                    .prestataireId(l.getPrestataireId())
                    .nomPrestataire(nomPrestataire)
                    .nom(l.getNom())
                    .codification(l.getCodification())
                    .typeExamen(l.getTypeExamen())
                    .descriptionSoins(l.getDescriptionSoins())
                    .dentsConcernees(l.getDentsConcernees())
                    .posologie(l.getPosologie())
                    .valeur(l.getValeur())
                    .nbre(l.getNbre())
                    .actePrelevement(l.getActePrelevement())
                    .taux(l.getTaux())
                    .valeurModif(l.getValeurModif())
                    .nbreModif(l.getNbreModif())
                    .actePrelevementModif(l.getActePrelevementModif())
                    .observations(l.getObservations())
                    .etat(l.getEtat())
                    .date(l.getDate())
                    .build();
        }

        Adherent adherent = adherentRepository
                .findActiveByCode(visite.getCodeAdherent())
                .orElseThrow(() -> new ValidateException(
                "Assuré Principal introuvable : "
                + visite.getCodeAdherent()));

        AyantDroit ay = null;
        if (visite.getCodeAyantDroit() != null) {
            ay = adherent.getAyantsDroit().stream()
                    .filter(adh -> adh.getCodeAyantDroit()
                    .equals(visite.getCodeAyantDroit()))
                    .findFirst()
                    .orElse(null);
        }

        Consultation cons = consultationRepository
                .findByVisite(visite.getId()).orElse(null);
        Prestation prestation=prestationRepository.findById(l.getPrestationId()).orElse(null);
        String natureAffectation=cons==null ? prestation.getNatureAffection():cons.getNatureAffection();
        

        return LigneEnAttenteResponse.builder()
                .id(l.getId())
                .prestationId(l.getPrestationId())
                .prestataireId(l.getPrestataireId())
                .nomPrestataire(nomPrestataire)
                .visiteId(visite.getId())
                // Infos adhérent
                .codeAdherent(visite.getCodeAdherent())
                .nomAssure(adherent.getAssurePrincipal())
                .codeAyantDroit(visite.getCodeAyantDroit())
                .nomAyantDroit(ay != null ? ay.getNom() : null)
                .groupe(adherent.getGroupe())
                .souscripteur(adherent.getSouscripteur())
                .natureAffection(natureAffectation != null ? natureAffectation.toUpperCase() : "NON RENSEIGNE...")
                // Médicament / acte
                .nom(l.getNom())
                .codification(l.getCodification())
                .typeExamen(l.getTypeExamen())
                .descriptionSoins(l.getDescriptionSoins())
                .dentsConcernees(l.getDentsConcernees())
                .posologie(l.getPosologie())
                // Valeurs originales
                .valeur(l.getValeur())
                .nbre(l.getNbre())
                .actePrelevement(l.getActePrelevement())
                .taux(l.getTaux())
                // Valeurs modifiées
                .valeurModif(l.getValeurModif())
                .nbreModif(l.getNbreModif())
                .actePrelevementModif(l.getActePrelevementModif())
                .observations(l.getObservations())
                .etat(l.getEtat())
                .date(l.getDate())
                .build();
    }

    // ── toPrestationResponse ──────────────────────────────────────
    private PrestationResponse toPrestationResponse(Prestation p) {
        Visite visite = visiteRepository.findById(p.getVisiteId()).orElse(null);

        String codeAdherent = visite != null ? visite.getCodeAdherent() : null;
        String codeAyantDroit = visite != null ? visite.getCodeAyantDroit() : null;
        String nomAssure = null;
        String nomAyantDroit = null;
        String souscripteur = null;
        Short groupe = null;
        String natureAffection = null;

        if (codeAdherent != null) {
            var adherent = adherentRepository
                    .findActiveByCode(codeAdherent).orElse(null);
            if (adherent != null) {
                nomAssure = adherent.getAssurePrincipal();
                souscripteur = adherent.getSouscripteur();
                groupe = adherent.getGroupe();

                if (codeAyantDroit != null) {
                    var ay = adherent.getAyantsDroit().stream()
                            .filter(adh -> adh.getCodeAyantDroit()
                            .equals(codeAyantDroit))
                            .findFirst().orElse(null);
                    if (ay != null) {
                        nomAyantDroit = ay.getNom();
                    }
                }
            }
        }

        if (visite != null) {
            var consultation = consultationRepository
                    .findByVisite(visite.getId()).orElse(null);
    
       
            if (consultation != null) {
                natureAffection = consultation.getNatureAffection();
            }else {
                natureAffection = p.getNatureAffection();
            }
        }

        // Stats lignes
        List<LignePrestation> lignes = p.getLignes() != null
                ? p.getLignes() : List.of();

        long total = lignes.size();
        long enAttente = lignes.stream()
                .filter(l -> "attente_validation".equals(l.getEtat())).count();
        long encaisses = lignes.stream()
                .filter(l -> "encaisse".equals(l.getEtat())).count();
        long valides = lignes.stream()
                .filter(l -> "valide".equals(l.getEtat())).count();
        long rejetes = lignes.stream()
                .filter(l -> "rejete".equals(l.getEtat())).count();

        String etatGlobal;
        if (total == 0 || enAttente == total) {
            // Toutes en attente ou aucune ligne
            etatGlobal = "attente_validation";
        } else if (encaisses == total) {
            // Toutes encaissées
            etatGlobal = "encaisse";
        } else if (enAttente == 0 && rejetes == 0 && encaisses == 0) {
            // Toutes validées (pas encore encaissées)
            etatGlobal = "valide";
        } else if (enAttente == 0 && (encaisses + rejetes) == total) {
            // Toutes traitées : mix encaissées + rejetées → encaissé partiel
            etatGlobal = "encaisse";
        } else if (enAttente == 0) {
            // Plus d'attente mais mix valide/rejete/encaisse
            etatGlobal = "valide";
        } else {
            // Encore des lignes en attente + d'autres déjà traitées
            etatGlobal = "partiel";
        }

        // Nom prestataire
        String nomPrestataire = null;
        if (p.getPrestataireId() != null) {
            var prestataire = prestataireRepository
                    .findById(p.getPrestataireId()).orElse(null);
            if (prestataire != null) {
                nomPrestataire = prestataire.getNom();
            }
        }

        return PrestationResponse.builder()
                .id(p.getId())
                .visiteId(p.getVisiteId())
                .prestataireId(p.getPrestataireId())
                .nomPrestataire(nomPrestataire)
                .naturePrestation(p.getNaturePrestation())
                .date(p.getDate())
                .codeAdherent(codeAdherent)
                .codeAyantDroit(codeAyantDroit)
                .nomAssure(nomAssure)
                .nomAyantDroit(nomAyantDroit)
                .souscripteur(souscripteur)
                .groupe(groupe)
                .natureAffection(natureAffection!=null ? natureAffection.toUpperCase():"NON RENSEIGNE...")
                .nbreLignes(total)
                .nbreLignesEnAttente(enAttente)
                .etatGlobal(etatGlobal)
                .build();
    }

    // ── Calculs ───────────────────────────────────────────────────
    private double calculPartZenithe(Double montant, Double taux) {
        if (montant == null || taux == null) {
            return 0;
        }
        return Math.round((montant * taux) / 100.0);
    }

    private double calculPartAssure(Double montant, Double taux) {
        if (montant == null) {
            return 0;
        }
        return montant - calculPartZenithe(montant, taux);
    }
//    
//    @Transactional(readOnly = true)
//public VisiteInfoResponse getVisiteInfo(String codeVisite) {
//
//    Visite visite = visiteRepository
//        .findById(codeVisite)
//        .orElseThrow(() -> new ValidationException(
//            "Visite introuvable : " + codeVisite, 404));
//
//    Adherent adherent = adherentRepository
//        .findActiveByCode(visite.getCodeAdherent())
//        .orElse(null);
//
//    String nomAssure    = null;
//    String nomAyantDroit = null;
//    String souscripteur  = null;
//    Short  groupe        = null;
//
//    if (adherent != null) {
//        nomAssure    = adherent.getAssurePrincipal();
//        souscripteur = adherent.getSouscripteur();
//        groupe       = adherent.getGroupe();
//
//        if (visite.getCodeAyantDroit() != null) {
//            var ay = adherent.getAyantsDroit().stream()
//                .filter(a -> a.getCodeAyantDroit()
//                    .equals(visite.getCodeAyantDroit()))
//                .findFirst().orElse(null);
//            if (ay != null) nomAyantDroit = ay.getNom();
//        }
//    }
//
//    return VisiteInfoResponse.builder()
//        .codeVisite(codeVisite)
//        .codeAdherent(visite.getCodeAdherent())
//        .codeAyantDroit(visite.getCodeAyantDroit())
//        .nomAssure(nomAssure)
//        .nomAyantDroit(nomAyantDroit)
//        .souscripteur(souscripteur)
//        .groupe(groupe)
//        .prestataireId(visite.getPrestataireId())
//        .build();
//}

@Transactional(readOnly = true)
public VisiteInfoResponse getVisiteInfo(String codeVisite) {

    // 1. Chercher par code court d'abord
    Visite visite = visiteRepository
        .findByCodeCourt(codeVisite)
        .orElse(null);

    // 2. Si pas trouvé → chercher par id long
    if (visite == null) {
        visite = visiteRepository
            .findById(codeVisite)
            .orElseThrow(() -> new ValidationException(
                "Visite introuvable : " + codeVisite, 404));
    }

    // 3. Charger l'adhérent
    Adherent adherent = adherentRepository
        .findActiveByCode(visite.getCodeAdherent())
        .orElse(null);

    String nomAssure     = null;
    String nomAyantDroit = null;
    String lienParente   = null;
    String souscripteur  = null;
    Short  groupe        = null;

    if (adherent != null) {
        nomAssure    = adherent.getAssurePrincipal();
        souscripteur = adherent.getSouscripteur();
        groupe       = adherent.getGroupe();

        // 4. Charger l'ayant droit SI c'est lui le concerné
        //    codeAyantDroit != null → la visite concerne
        //    un ayant droit
        final String codeAyantDroitFinal = visite.getCodeAyantDroit();
        if (visite.getCodeAyantDroit() != null
                && !visite.getCodeAyantDroit().isBlank()) {

            var ay = adherent.getAyantsDroit()
                .stream()
                .filter(a -> a.getCodeAyantDroit()
                    .equals(codeAyantDroitFinal))
                .findFirst()
                .orElse(null);

            if (ay != null) {
                nomAyantDroit = ay.getNom();
                lienParente   = ay.getLienPare();
            }
        }
    }

    log.info("VisiteInfo — code={} adherent={} " +
             "ayantDroit={} prestataire={}",
             codeVisite,
             visite.getCodeAdherent(),
             visite.getCodeAyantDroit(),
             visite.getPrestataireId());

    return VisiteInfoResponse.builder()
        .codeVisite(visite.getCodeCourt())
        .codeAdherent(visite.getCodeAdherent())
        .codeAyantDroit(visite.getCodeAyantDroit())
        .nomAssure(nomAssure)
        .nomAyantDroit(nomAyantDroit)
        .lienParente(lienParente)
        .souscripteur(souscripteur)
        .groupe(groupe)
        .prestataireId(visite.getPrestataireId())
        .build();
    
}

@Transactional
public ConsultationEnAttenteResponse soumettreConsultation(
        ConsultationSoumissionRequest request) {

    // 1. Vérifier que la visite existe
    Visite visite = visiteRepository
        .findByCodeCourt(request.getVisiteId())
        .orElse(null);

    if (visite == null) {
        visite = visiteRepository
            .findById(request.getVisiteId())
            .orElseThrow(() -> new ValidationException(
                "Visite introuvable : " +
                request.getVisiteId(), 404));
    }

    // 2. Vérifier qu'une consultation n'existe pas déjà
    boolean existe = consultationRepository
        .findByVisite(visite.getId()).isPresent();

    if (existe) {
        throw new ValidationException(
            "Une consultation existe déjà " +
            "pour cette visite", 400);
    }

    // 3. Créer la consultation avec les bons champs
    Consultation consultation = Consultation.builder()
        .visiteId(visite.getId())
        .typeConsultation(request.getTypeConsultation())
        .natureConsultation(
            Boolean.TRUE.equals(request.getPayante())
            ? "payante" : "gratuite")
        .montant(
            Boolean.TRUE.equals(request.getPayante())
            ? (request.getMontant() != null
               ? request.getMontant() : 0.0)
            : 0.0)
        .taux(80.0)
        .etatConsultation("attente_validation")
        .date(LocalDateTime.now())
        .supprime("-1")
        .build();

    consultationRepository.save(consultation);

    log.info("Consultation soumise ✓ — visite={} " +
             "type={} montant={}",
             visite.getId(),
             request.getTypeConsultation(),
             request.getMontant());

    return toConsultationResponse(consultation);
}

/**
 * Récupère le taux de couverture :
 * 1. Dans taux_prestation (police + groupe + type)
 * 2. Sinon → taux de l'adhérent
 */
private double getTaux(
        String police,
        short groupe,
        String typePrestationId,
        String codeAdherent,String naturePrestation) {

    // 1. Chercher dans taux_prestation
    if (police != null && typePrestationId != null  && "examen".equals(naturePrestation)) {
        Optional<TauxPrestation> tp =
            tauxPrestationRepository
                .findByPoliceAndGroupeAndTypePrestationId(
                    police, groupe, typePrestationId);

        if (tp.isPresent() && tp.get().getTaux() != null) {
            log.info("Taux trouvé en base : {}% " +
                     "police={} groupe={} type={}",
                     tp.get().getTaux(),
                     police, groupe, typePrestationId);
            return tp.get().getTaux().doubleValue();
        }
    }

    // 2. Fallback → taux adhérent
    if (codeAdherent != null) {
        try {
            Adherent adherent = adherentRepository
                .findActiveByCode(codeAdherent)
                .orElse(null);
            if (adherent != null
                    && adherent.getTaux() != null) {
                log.info("Taux fallback adhérent : {}%",
                         adherent.getTaux());
                return adherent.getTaux().doubleValue();
            }
        } catch (Exception e) {
            log.warn("Impossible de récupérer taux " +
                     "adhérent : {}", e.getMessage());
        }
    }

    // 3. Taux par défaut
    log.warn("Taux non trouvé — utilisation taux " +
             "par défaut 80%");
    return 0.0;
}

@Transactional
public void soumettrePrestation(
        PrestationSoumissionRequest request) {
    
    
    

    // 1. Vérifier visite
    Visite visite = visiteRepository
        .findByCodeCourt(request.getVisiteId())
        .orElse(null);

    if (visite == null) {
        visite = visiteRepository
            .findById(request.getVisiteId())
            .orElseThrow(() -> new ValidationException(
                "Visite introuvable : " +
                request.getVisiteId(), 404));
    }
    
//    //creation de la nature d affection sa se creer dans la table consultation avec CSO gratuit
//    Consultation consultation = Consultation.builder()
//        .visiteId(visite.getId())
//        .typeConsultation("CS0")
//        .natureConsultation("gratuite")
//        .montant(0.0)
//           
//        .taux(0.0)
//        .etatConsultation("attente_validation")
//        .date(LocalDateTime.now())
//        .supprime("-1")
//        .build();

//    consultationRepository.save(consultation);

    final String visiteIdFinal   = visite.getId();
    final String codeAdherent    = visite.getCodeAdherent();

    // 2. Récupérer police + groupe via adherent
    String police = null;
    short  groupe = 0;
    try {
        Adherent adherent = adherentRepository
            .findActiveByCode(codeAdherent)
            .orElse(null);
        if (adherent != null) {
            police = adherent.getPolice();
            groupe = adherent.getGroupe() != null
                     ? adherent.getGroupe() : 0;
        }
    } catch (Exception e) {
        log.warn("Adherent non trouvé : {}",
                 e.getMessage());
    }

    final String policeFinal = police;
    final short  groupeFinal = groupe;
Prestation prestation =prestationRepository.findByVisiteIdAndNaturePrestation(visiteIdFinal, request.getNaturePrestation()).orElse(null);

    if (prestation==null) {
//            3. Créer la prestation
     prestation = Prestation.builder()
        .visiteId(visiteIdFinal)
        .prestataireId(request.getPrestataireId())
        .naturePrestation(request.getNaturePrestation())
        .natureAffection(request.getNatureAffection().toUpperCase())
        .date(LocalDateTime.now())
        .supprime("-1")
        .build(); 
    }


    prestation = prestationRepository.save(prestation);
    final Integer prestationId = prestation.getId();

    // 4. Créer les lignes avec taux correct
    if (request.getLignes() != null
            && !request.getLignes().isEmpty()) {

        List<LignePrestation> lignes = request.getLignes()
            .stream()
            .map(l -> {
                // Récupérer le taux par type prestation
                String typeId = l.getTypeExamen() ;

                double taux = getTaux(
                    policeFinal,
                    groupeFinal,
                    typeId,
                    codeAdherent,request.getNaturePrestation());

                return LignePrestation.builder()
                    .prestationId(prestationId)
                    .prestataireId(
                        request.getPrestataireId())
                    .medicamentId(l.getMedicamentId())
                    .examenId(l.getExamenId())
                    .typeExamen(l.getTypeExamen())
                    .nom(l.getNom())
                    .valeur(l.getValeur() != null
                        ? l.getValeur() : 0.0)
                    .nbre(l.getNbre() != null
                        ? l.getNbre() : 1.0)
                    .actePrelevement(
                        l.getActePrelevement() != null
                        ? l.getActePrelevement() : 0.0)
                    .actePrelevementModif(0.0)
                    .posologie(l.getPosologie())
                    .observations(l.getObservations())
                    .taux(taux)
                    .date(LocalDateTime.now())
                    .dateValideRejete(LocalDateTime.now())
                    .etat("attente_validation")
                    .supprime("-1")
                    .build();
            })
            .collect(Collectors.toList());

        ligneRepository.saveAll(lignes);
    }

    log.info("Prestation soumise ✓ — visite={} " +
             "nature={} lignes={}",
             visiteIdFinal,
             request.getNaturePrestation(),
             request.getLignes() != null
                 ? request.getLignes().size() : 0);
}
// Renommez getTaux() en getTauxPublic() ou
// ajoutez cette méthode publique
public double getTauxPublic(
        String police,
        short  groupe,
        String typePrestation,
        String codeAdherent) {

    return getTaux(
        police, groupe,
        typePrestation, codeAdherent,"examen");
}
}
