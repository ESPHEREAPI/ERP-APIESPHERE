package com.esphere.reporting.controller;

import com.esphere.reporting.dto.response.*;
import com.esphere.reporting.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;

@RestController
@RequestMapping("/reporting")
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService reportingService;

    // ── TABLEAU DE BORD AGENT SS ──────────────────────────────────

    // GET /reporting/dashboard/ss/{employeId}?annee=2026
    @GetMapping("/dashboard/ss/{employeId}")
    public ResponseEntity<DashboardSsResponse> getDashboardSs(
            @PathVariable String employeId,
            @RequestParam(defaultValue = "0") int annee) {
        if (annee == 0) annee = Year.now().getValue();
        return ResponseEntity.ok(
                reportingService.getDashboardSs(employeId, annee));
    }

    // ── TABLEAU DE BORD PRESTATAIRE ───────────────────────────────

    // GET /reporting/dashboard/prestataire/{prestataireId}?categorieId=&annee=
    @GetMapping("/dashboard/prestataire/{prestataireId}")
    public ResponseEntity<DashboardPrestataireResponse> getDashboardPrestataire(
            @PathVariable String prestataireId,
            @RequestParam String categorieId,
            @RequestParam(defaultValue = "0") int annee) {
        if (annee == 0) annee = Year.now().getValue();
        return ResponseEntity.ok(
                reportingService.getDashboardPrestataire(
                        prestataireId, categorieId, annee));
    }

    // ── CONSOMMATION ADHERENT ─────────────────────────────────────

    // GET /reporting/consommation/{codeAdherent}?annee=2026
    @GetMapping("/consommation/{codeAdherent}")
    public ResponseEntity<ConsommationAdherentResponse> getConsommation(
            @PathVariable String codeAdherent,
            @RequestParam(defaultValue = "0") int annee) {
        if (annee == 0) annee = Year.now().getValue();
        return ResponseEntity.ok(
                reportingService.getConsommationAdherent(codeAdherent, annee));
    }

    // ── ÉTAT DES PRESTATIONS — PRESTATAIRE ───────────────────────

    // GET /reporting/prestations/prestataire/{prestataireId}
    //     ?nature=&statut=&mois=0&annee=2026&page=0&size=20
    @GetMapping("/prestations/prestataire/{prestataireId}")
    public ResponseEntity<EtatPrestationPageResponse> getEtatPrestationsPrestataire(
            @PathVariable String prestataireId,
            @RequestParam(required = false) String nature,
            @RequestParam(required = false) String statut,
            @RequestParam(defaultValue = "0") int mois,
            @RequestParam(defaultValue = "0") int annee,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        if (annee == 0) annee = Year.now().getValue();
        Integer moisParam = mois > 0 ? mois : null;
        return ResponseEntity.ok(
                reportingService.getEtatPrestationsPrestataire(
                        prestataireId, nature, statut, moisParam, annee, page, size));
    }
}