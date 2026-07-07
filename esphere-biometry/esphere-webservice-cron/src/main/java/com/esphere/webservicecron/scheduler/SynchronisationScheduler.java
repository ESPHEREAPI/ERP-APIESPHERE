package com.esphere.webservicecron.scheduler;

import com.esphere.webservicecron.service.HistoriqueSynchronisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.esphere.webservicecron.service.HistoriqueSynchronisationService.DECLENCHEUR_CRON;

/**
 * Tâches planifiées équivalentes aux appels HTTP périodiques qui
 * déclenchaient les routes PHP /webservice/recuperer-donnees-* et
 * /webservice/desactiver-donnees-*.
 *
 * Exécute la séquence complète (adhérents -> ayants droit -> taux de
 * prestation -> désactivations) 3 fois par jour : 06h30, 18h00, 20h00.
 * Chaque passage est tracé dans l'historique (voir HistoriqueController).
 *
 * Désactivable via esphere.webservice-cron.enabled=false (utile en dev).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SynchronisationScheduler {

    private final HistoriqueSynchronisationService historiqueSynchronisationService;

    @Value("${esphere.webservice-cron.enabled:false}")
    private boolean activerCron;

    @Scheduled(cron = "${esphere.webservice-cron.cron-matin:0 30 6 * * *}")
    public void synchronisationMatin() {
        executerSiActif();
    }

    @Scheduled(cron = "${esphere.webservice-cron.cron-soir-18h:0 0 18 * * *}")
    public void synchronisationSoir18h() {
        executerSiActif();
    }

    @Scheduled(cron = "${esphere.webservice-cron.cron-soir-20h:0 0 20 * * *}")
    public void synchronisationSoir20h() {
        executerSiActif();
    }

    private void executerSiActif() {
        if (!activerCron) {
            return;
        }
        try {
            historiqueSynchronisationService.executerSequenceComplete(DECLENCHEUR_CRON);
        } catch (Exception e) {
            log.error("Echec de la sequence de synchronisation planifiee : {}", e.getMessage(), e);
        }
    }
}
