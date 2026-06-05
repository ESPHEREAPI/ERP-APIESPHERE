package com.esphere.reporting.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardPrestataireResponse {

    private String prestataireId;
    private String categorieId;

    // ── Compteurs du jour ─────────────────────────────────────────
    private Long visitesAujourdhui;
    private Long prestationsEnAttenteAujourdhui;

    // ── Compteurs du mois ─────────────────────────────────────────
    private Long   consultationsMois;
    private Long   ordonnancesMois;
    private Long   examensMois;
    private Long   bonsManuelsMois;
    private Long   validesMois;
    private Long   rejetesMois;
    private Long   encaissesMois;
    private Double montantEncaisseMois;

    // ── Compteurs année ───────────────────────────────────────────
    private Long   totalVisitesAnnee;
    private Double montantTotalAnnee;

    // ── Graphiques ────────────────────────────────────────────────
    private List<StatMoisResponse> encaissementsParMois;
    private List<StatTypeResponse> repartitionParType;

    // ── Alertes non lues ─────────────────────────────────────────
    private Long alertesNonLues;

    // ── Droits d'accès selon catégorie ───────────────────────────
    private Boolean accesConsultation;
    private Boolean accesOrdonnance;
    private Boolean accesExamen;
    private Boolean accesBonManuel;
    private Boolean accesHospitalisation; // 🔜 prévu
}