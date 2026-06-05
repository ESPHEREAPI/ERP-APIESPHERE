package com.esphere.validation.service;

import com.esphere.validation.dto.response.AdherentExterneDTO;
import com.esphere.validation.dto.response.ConsommationResponse;
import com.esphere.validation.entity.Visite;
import com.esphere.validation.exception.ValidationException;
import com.esphere.validation.repository.AdherentRepository;
import com.esphere.validation.repository.BonManuelConsommationRepository;

import com.esphere.validation.repository.ConsultationRepository;
import com.esphere.validation.repository.VisiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConsommationService {
    
    private final ConsultationRepository consommationRepository;
    private final BonManuelConsommationRepository bonManuelRepository;
    private final VisiteRepository visiteRepository;
    private final AdherentRepository adherentRepository;
    private final AdherentExterneService adherentExterneService;
    private final AdherentWriteService    adherentWriteService;   // ← injecter
    
    @Transactional(readOnly = true)
    public ConsommationResponse getConsommation(String visiteId) {
        
        int annee = LocalDate.now().getYear();

        // 1. Visite → vrai codeAdherent
        Visite visite = visiteRepository.findById(visiteId)
                .orElseThrow(() -> new ValidationException(
                "Visite introuvable : " + visiteId, 404));
        String codeAdherent = visite.getCodeAdherent();
        Double plafond = 0.0;
        // 2. Adhérent → plafond + infos
        var adherent = adherentRepository.findActiveByCode(codeAdherent)
                .orElseThrow(() -> new ValidationException(
                "Adhérent introuvable : " + codeAdherent, 404));
        plafond = adherent.getPlafondAssurep() != null
                ? adherent.getPlafondAssurep() : 0.0;

        if (plafond == 0.0) {
            AdherentExterneDTO ext = adherentExterneService
                    .findByCodeAdherent(codeAdherent).orElse(null);

            System.out.println("adherentExterneDTO " + ext);

            plafond = ext == null ? 0.0 : getPlafond(ext);

            if (plafond > 0.0) {
                // appel dans une nouvelle transaction en écriture
                adherentWriteService.updatePlafond(codeAdherent, plafond);
            }
        }

//        plafond = adherent.getPlafondAssurep() != null
//                ? adherent.getPlafondAssurep() : 0.0;
        // 3. Encaissés
        double consultEnc = consommationRepository
                .sumConsultationsEncaissees(codeAdherent, annee);
        double ordoEnc = consommationRepository
                .sumOrdonnancesEncaissees(codeAdherent, annee);
        double examEnc = consommationRepository
                .sumExamensEncaisses(codeAdherent, annee);
        double bonEnc = bonManuelRepository
                .sumBonsManuelsEncaisses(codeAdherent, annee);
        
        long nbreConsult = consommationRepository
                .countConsultationsEncaissees(codeAdherent, annee);
        long nbreOrdo = consommationRepository
                .countOrdonnancesEncaissees(codeAdherent, annee);
        long nbreExam = consommationRepository
                .countExamensEncaisses(codeAdherent, annee);
        long nbreBon = bonManuelRepository
                .countBonsManuelsEncaisses(codeAdherent, annee);
        
        double totalEnc = consultEnc + ordoEnc + examEnc + bonEnc;

        // 4. En cours
        double consultCours = consommationRepository
                .sumConsultationsEnCours(codeAdherent, annee);
        double ordoCours = consommationRepository
                .sumOrdonnancesEnCours(codeAdherent, annee);
        double examCours = consommationRepository
                .sumExamensEnCours(codeAdherent, annee);
        double bonCours = bonManuelRepository
                .sumBonsManuelsEnCours(codeAdherent, annee);
        
        double totalCours = consultCours + ordoCours + examCours + bonCours;

        // 5. Projection
        double totalProjecte = totalEnc + totalCours;
        double soldeEnc = plafond - totalEnc;
        double soldeProjete = plafond - totalProjecte;
        double pctEnc = plafond > 0
                ? (totalEnc / plafond) * 100 : 0.0;
        double pctProj = plafond > 0
                ? (totalProjecte / plafond) * 100 : 0.0;

        // 6. Niveau d'alerte
        String alerte, message;
        if (pctProj >= 100) {
            alerte = "CRITIQUE";
            message = String.format(
                    "Plafond dépassé ! Projection %.0f FCFA / Plafond %.0f FCFA",
                    totalProjecte, plafond);
        } else if (pctProj >= 80) {
            alerte = "ATTENTION";
            message = String.format(
                    "Plafond bientôt atteint (%.0f%%). Solde projeté : %.0f FCFA",
                    pctProj, soldeProjete);
        } else {
            alerte = "NORMAL";
            message = String.format(
                    "Solde disponible : %.0f FCFA (%.0f%% consommé)",
                    soldeEnc, pctEnc);
        }
        
        return ConsommationResponse.builder()
                .codeAdherent(codeAdherent)
                .nomAssure(adherent.getAssurePrincipal())
                .souscripteur(adherent.getSouscripteur())
                .groupe(adherent.getGroupe())
                .annee(annee)
                .plafondGlobal(plafond)
                .montantConsultationsEncaissees(consultEnc)
                .nbreConsultationsEncaissees(nbreConsult)
                .montantOrdonnancesEncaissees(ordoEnc)
                .nbreOrdonnancesEncaissees(nbreOrdo)
                .montantExamensEncaisses(examEnc)
                .nbreExamensEncaisses(nbreExam)
                .montantBonsManuelsEncaisses(bonEnc)
                .nbreBonsManuelsEncaisses(nbreBon)
                .totalEncaisse(totalEnc)
                .montantConsultationsEnCours(consultCours)
                .montantOrdonnancesEnCours(ordoCours)
                .montantExamensEnCours(examCours)
                .montantBonsManuelsEnCours(bonCours)
                .totalEnCours(totalCours)
                .totalProjecte(totalProjecte)
                .soldeApresEncaisse(soldeEnc)
                .soldeApresProjection(soldeProjete)
                .pourcentageEncaisse(pctEnc)
                .pourcentageProjecte(pctProj)
                .niveauAlerte(alerte)
                .messageAlerte(message)
                .build();
    }

    /**
     * Résout le plafond applicable selon la priorité : 1. PLAFOND_FAMILLE
     * (plafond global famille) 2. PLAFOND_PERSONNE (plafond individuel) 3.
     * PLAFOND_ASSUREP (plafond assuré principal) 4. PLAFOND_MEMBRE (plafond
     * membre/ayant-droit) 5. 0.0 (aucun plafond défini)
     */
    private Double getPlafond(AdherentExterneDTO dto) {
        if (hasValue(dto.getPlafondFamille())) {
            return parse(dto.getPlafondFamille());
        }
        if (hasValue(dto.getPlafondPersonne())) {
            return parse(dto.getPlafondPersonne());
        }
        if (hasValue(dto.getPlafondAssurep())) {
            return parse(dto.getPlafondAssurep());
        }
        if (hasValue(dto.getPlafondMembre())) {
            return parse(dto.getPlafondMembre());
        }
        return 0.0;
    }
    
    private boolean hasValue(String val) {
        return val != null && !val.isBlank();
    }
    
    private Double parse(String val) {
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
        
    }
}
