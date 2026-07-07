package com.esphere.webservicecron.controller;

import com.esphere.webservicecron.dto.response.HistoriqueSyncResponse;
import com.esphere.webservicecron.dto.response.PageResponse;
import com.esphere.webservicecron.dto.response.WebserviceResponse;
import com.esphere.webservicecron.service.HistoriqueSynchronisationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Consultation de l'historique des synchronisations (manuelles ou
 * planifiées) : permet de voir tout ce qui s'est passé lors des
 * passages de la tâche planifiée (06h30 / 18h00 / 20h00) ou des
 * déclenchements manuels.
 */
@Slf4j
@RestController
@RequestMapping("/webservice/historique")
@RequiredArgsConstructor
public class HistoriqueController {

    private final HistoriqueSynchronisationService historiqueSynchronisationService;

    /**
     * GET /webservice/historique
     *
     * Paramètres optionnels :
     *   type      : ADHERENT | AYANT_DROIT | TAUX_PRESTATION |
     *               DESACTIVATION_ADHERENT | DESACTIVATION_AYANT_DROIT
     *   dateDebut, dateFin : bornes ISO-8601 (ex: 2026-06-01T00:00:00)
     *   page, size  : pagination (défaut page=0, size=20)
     */
    @GetMapping
    public ResponseEntity<WebserviceResponse<PageResponse<HistoriqueSyncResponse>>> lister(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateDebut"));
        PageResponse<HistoriqueSyncResponse> resultat = historiqueSynchronisationService
                .rechercherHistorique(type, dateDebut, dateFin, pageable);

        return ResponseEntity.ok(WebserviceResponse.ok(resultat));
    }

    // GET /webservice/historique/{id}
    @GetMapping("/{id}")
    public ResponseEntity<WebserviceResponse<HistoriqueSyncResponse>> detail(@PathVariable Long id) {
        HistoriqueSyncResponse detail = historiqueSynchronisationService.obtenirDetail(id);
        if (detail == null) {
            return ResponseEntity.ok(WebserviceResponse.error("Historique introuvable : " + id));
        }
        return ResponseEntity.ok(WebserviceResponse.ok(detail));
    }
}
