package com.esphere.webservicecron.controller;

import com.esphere.webservicecron.dto.response.SyncReportResponse;
import com.esphere.webservicecron.dto.response.WebserviceResponse;
import com.esphere.webservicecron.service.HistoriqueSynchronisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.esphere.webservicecron.service.HistoriqueSynchronisationService.DECLENCHEUR_MANUEL;

/**
 * Déclenchement manuel des synchronisations — équivalent des routes
 * /webservice/recuperer-donnees-* et /webservice/desactiver-donnees-*
 * du module PHP Zend "Webservice".
 *
 * Même format de réponse legacy : { "status", "status_message", "data" }
 * Chaque appel est tracé dans l'historique (voir HistoriqueController).
 */
@Slf4j
@RestController
@RequestMapping("/webservice")
@RequiredArgsConstructor
public class SynchronisationController {

    private final HistoriqueSynchronisationService historiqueSynchronisationService;

    // GET|POST /webservice/recuperer-donnees-adherent[/{police}]
    @RequestMapping(value = {"/recuperer-donnees-adherent", "/recuperer-donnees-adherent/{police}"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<WebserviceResponse<SyncReportResponse>> recupererDonneesAdherent(
            @PathVariable(required = false) String police) {
        log.info("recuperer-donnees-adherent police={}", police);
        SyncReportResponse rapport = historiqueSynchronisationService
                .executerImportAdherents(police, DECLENCHEUR_MANUEL);
        return ResponseEntity.ok(WebserviceResponse.ok(rapport));
    }

    // GET|POST /webservice/recuperer-donnees-ayant-droit[/{police}]
    @RequestMapping(value = {"/recuperer-donnees-ayant-droit", "/recuperer-donnees-ayant-droit/{police}"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<WebserviceResponse<SyncReportResponse>> recupererDonneesAyantDroit(
            @PathVariable(required = false) String police) {
        log.info("recuperer-donnees-ayant-droit police={}", police);
        SyncReportResponse rapport = historiqueSynchronisationService
                .executerImportAyantsDroit(police, DECLENCHEUR_MANUEL);
        return ResponseEntity.ok(WebserviceResponse.ok(rapport));
    }

    // GET|POST /webservice/recuperer-donnees-taux-prestation[/{police}]
    @RequestMapping(value = {"/recuperer-donnees-taux-prestation", "/recuperer-donnees-taux-prestation/{police}"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<WebserviceResponse<SyncReportResponse>> recupererDonneesTauxPrestation(
            @PathVariable(required = false) String police) {
        log.info("recuperer-donnees-taux-prestation police={}", police);
        SyncReportResponse rapport = historiqueSynchronisationService
                .executerImportTauxPrestation(police, DECLENCHEUR_MANUEL);
        return ResponseEntity.ok(WebserviceResponse.ok(rapport));
    }

    // GET|POST /webservice/desactiver-donnees-adherent[/{police}]
    @RequestMapping(value = {"/desactiver-donnees-adherent", "/desactiver-donnees-adherent/{police}"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<WebserviceResponse<SyncReportResponse>> desactiverDonneesAdherent(
            @PathVariable(required = false) String police) {
        log.info("desactiver-donnees-adherent police={}", police);
        SyncReportResponse rapport = historiqueSynchronisationService
                .executerDesactivationAdherents(police, DECLENCHEUR_MANUEL);
        return ResponseEntity.ok(WebserviceResponse.ok(rapport));
    }

    // GET|POST /webservice/desactiver-donnees-ayant-droit[/{codeAdherent}]
    @RequestMapping(value = {"/desactiver-donnees-ayant-droit", "/desactiver-donnees-ayant-droit/{codeAdherent}"},
            method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<WebserviceResponse<SyncReportResponse>> desactiverDonneesAyantDroit(
            @PathVariable(required = false) String codeAdherent) {
        log.info("desactiver-donnees-ayant-droit codeAdherent={}", codeAdherent);
        SyncReportResponse rapport = historiqueSynchronisationService
                .executerDesactivationAyantsDroit(codeAdherent, DECLENCHEUR_MANUEL);
        return ResponseEntity.ok(WebserviceResponse.ok(rapport));
    }

    // GET|POST /webservice/synchronisation-complete
    // Déclenche manuellement la même séquence que le cron (06h30/18h/20h)
    @RequestMapping(value = "/synchronisation-complete", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<WebserviceResponse<String>> synchronisationComplete() {
        log.info("synchronisation-complete (declenchement manuel)");
        historiqueSynchronisationService.executerSequenceComplete(DECLENCHEUR_MANUEL);
        return ResponseEntity.ok(WebserviceResponse.ok("Sequence de synchronisation lancee"));
    }
}
