package com.esphere.webservicecron.service;

import com.esphere.webservicecron.dto.response.DashboardStatsResponse;
import com.esphere.webservicecron.dto.response.HistoriqueSyncResponse;
import com.esphere.webservicecron.dto.response.PageResponse;
import com.esphere.webservicecron.dto.response.SyncReportResponse;
import com.esphere.webservicecron.entity.WebserviceSyncHistory;
import com.esphere.webservicecron.repository.WebserviceSyncHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Orchestre les synchronisations (déclenchées manuellement via le
 * controller ou automatiquement via le scheduler) et trace chaque
 * exécution dans webservice_sync_history afin de pouvoir consulter
 * l'historique des passages.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HistoriqueSynchronisationService {

    public static final String TYPE_ADHERENT = "ADHERENT";
    public static final String TYPE_AYANT_DROIT = "AYANT_DROIT";
    public static final String TYPE_TAUX_PRESTATION = "TAUX_PRESTATION";
    public static final String TYPE_DESACTIVATION_ADHERENT = "DESACTIVATION_ADHERENT";
    public static final String TYPE_DESACTIVATION_AYANT_DROIT = "DESACTIVATION_AYANT_DROIT";

    public static final String DECLENCHEUR_CRON = "CRON";
    public static final String DECLENCHEUR_MANUEL = "MANUEL";

    private static final int MAX_DETAILS_ECHECS_LENGTH = 20_000;

    private final SynchronisationService synchronisationService;
    private final WebserviceSyncHistoryRepository historyRepository;

    public SyncReportResponse executerImportAdherents(String police, String declencheur) {
        return tracer(TYPE_ADHERENT, declencheur, police,
                h -> synchronisationService.importerAdherents(police));
    }

    public SyncReportResponse executerImportAyantsDroit(String codeAdherent, String declencheur) {
        return tracer(TYPE_AYANT_DROIT, declencheur, codeAdherent,
                h -> synchronisationService.importerAyantsDroit(codeAdherent));
    }

    public SyncReportResponse executerImportTauxPrestation(String police, String declencheur) {
        return tracer(TYPE_TAUX_PRESTATION, declencheur, police,
                h -> synchronisationService.importerTauxPrestation(police));
    }

    public SyncReportResponse executerDesactivationAdherents(String police, String declencheur) {
        return tracer(TYPE_DESACTIVATION_ADHERENT, declencheur, police,
                h -> synchronisationService.desactiverAdherents(police));
    }

    public SyncReportResponse executerDesactivationAyantsDroit(String codeAdherent, String declencheur) {
        return tracer(TYPE_DESACTIVATION_AYANT_DROIT, declencheur, codeAdherent,
                h -> synchronisationService.desactiverAyantsDroit(codeAdherent));
    }

    /**
     * Lance la séquence complète, dans l'ordre attendu par le legacy PHP :
     * adhérents -> ayants droit -> taux de prestation -> désactivations.
     * Utilisé par le scheduler (06h30 / 18h00 / 20h00) ; chaque étape est
     * tracée indépendamment dans l'historique.
     */
    public void executerSequenceComplete(String declencheur) {
        log.info("[{}] Debut de la sequence complete de synchronisation", declencheur);
        executerImportAdherents(null, declencheur);
        executerImportAyantsDroit(null, declencheur);
        executerImportTauxPrestation(null, declencheur);
        executerDesactivationAdherents(null, declencheur);
        executerDesactivationAyantsDroit(null, declencheur);
        log.info("[{}] Fin de la sequence complete de synchronisation", declencheur);
    }

    @Transactional
    public PageResponse<HistoriqueSyncResponse> rechercherHistorique(
            String type, LocalDateTime debut, LocalDateTime fin, Pageable pageable) {

        Page<WebserviceSyncHistory> page;
        if (type != null && debut != null && fin != null) {
            page = historyRepository.findByTypeSynchronisationAndDateDebutBetweenOrderByDateDebutDesc(
                    type, debut, fin, pageable);
        } else if (type != null) {
            page = historyRepository.findByTypeSynchronisationOrderByDateDebutDesc(type, pageable);
        } else if (debut != null && fin != null) {
            page = historyRepository.findByDateDebutBetweenOrderByDateDebutDesc(debut, fin, pageable);
        } else {
            page = historyRepository.findAllByOrderByDateDebutDesc(pageable);
        }
        return PageResponse.from(page.map(this::toResponse));
    }

    @Transactional
    public HistoriqueSyncResponse obtenirDetail(Long id) {
        return historyRepository.findById(id).map(this::toResponse).orElse(null);
    }

    private static final List<String> TOUS_LES_TYPES = List.of(
            TYPE_ADHERENT, TYPE_AYANT_DROIT, TYPE_TAUX_PRESTATION,
            TYPE_DESACTIVATION_ADHERENT, TYPE_DESACTIVATION_AYANT_DROIT);

    /**
     * Agrège les chiffres clés pour le dashboard : volumes du jour,
     * répartition par statut, dernière exécution de chacun des 5 types
     * de traitement, et flux des 10 dernières exécutions.
     */
    @Transactional
    public DashboardStatsResponse obtenirStatistiquesDashboard() {
        LocalDateTime debutJournee = LocalDate.now().atStartOfDay();

        Map<String, HistoriqueSyncResponse> dernieresParType = new LinkedHashMap<>();
        for (String type : TOUS_LES_TYPES) {
            historyRepository.findTopByTypeSynchronisationOrderByDateDebutDesc(type)
                    .ifPresent(h -> dernieresParType.put(type, toResponse(h)));
        }

        List<HistoriqueSyncResponse> dernieresExecutions = historyRepository
                .findTop10ByOrderByDateDebutDesc()
                .stream().map(this::toResponse).toList();

        return DashboardStatsResponse.builder()
                .totalExecutions(historyRepository.count())
                .executionsAujourdhui(historyRepository.countByDateDebutAfter(debutJournee))
                .executionsCronAujourdhui(historyRepository.countByDeclencheurAndDateDebutAfter(
                        DECLENCHEUR_CRON, debutJournee))
                .succes(historyRepository.countByStatut("SUCCES"))
                .partiel(historyRepository.countByStatut("PARTIEL"))
                .echec(historyRepository.countByStatut("ECHEC"))
                .enCours(historyRepository.countByStatut("EN_COURS"))
                .dernieresExecutionsParType(dernieresParType)
                .dernieresExecutions(dernieresExecutions)
                .build();
    }

    // ── Implémentation interne ──────────────────────────────────────

    private SyncReportResponse tracer(String type, String declencheur, String filtre,
                                       Function<WebserviceSyncHistory, SyncReportResponse> action) {

        WebserviceSyncHistory historique = WebserviceSyncHistory.builder()
                .typeSynchronisation(type)
                .declencheur(declencheur)
                .policeFiltre(filtre)
                .dateDebut(LocalDateTime.now())
                .statut("EN_COURS")
                .build();
        historique = historyRepository.save(historique);

        try {
            SyncReportResponse rapport = action.apply(historique);

            historique.setDateFin(LocalDateTime.now());
            historique.setNbTraites(rapport.getNbTraites());
            historique.setNbEchecs(rapport.getNbEchecs());
            historique.setStatut(rapport.getNbEchecs() > 0 ? "PARTIEL" : "SUCCES");
            if (rapport.getLignesEnEchec() != null && !rapport.getLignesEnEchec().isEmpty()) {
                String details = String.join("\n", rapport.getLignesEnEchec());
                if (details.length() > MAX_DETAILS_ECHECS_LENGTH) {
                    details = details.substring(0, MAX_DETAILS_ECHECS_LENGTH) + "... (tronque)";
                }
                historique.setDetailsEchecs(details);
            }
            historyRepository.save(historique);

            log.info("[{}] {} termine en {} ms - traites={} echecs={}",
                    declencheur, type,
                    Duration.between(historique.getDateDebut(), historique.getDateFin()).toMillis(),
                    rapport.getNbTraites(), rapport.getNbEchecs());

            return rapport;

        } catch (Exception e) {
            log.error("[{}] {} a echoue : {}", declencheur, type, e.getMessage(), e);
            historique.setDateFin(LocalDateTime.now());
            historique.setStatut("ECHEC");
            historique.setMessageErreur(e.getMessage());
            historyRepository.save(historique);
            throw e;
        }
    }

    private HistoriqueSyncResponse toResponse(WebserviceSyncHistory h) {
        Long dureeMs = (h.getDateDebut() != null && h.getDateFin() != null)
                ? Duration.between(h.getDateDebut(), h.getDateFin()).toMillis()
                : null;

        return HistoriqueSyncResponse.builder()
                .id(h.getId())
                .typeSynchronisation(h.getTypeSynchronisation())
                .declencheur(h.getDeclencheur())
                .policeFiltre(h.getPoliceFiltre())
                .dateDebut(h.getDateDebut())
                .dateFin(h.getDateFin())
                .dureeMs(dureeMs)
                .statut(h.getStatut())
                .nbTraites(h.getNbTraites())
                .nbEchecs(h.getNbEchecs())
                .detailsEchecs(h.getDetailsEchecs())
                .messageErreur(h.getMessageErreur())
                .build();
    }
}
