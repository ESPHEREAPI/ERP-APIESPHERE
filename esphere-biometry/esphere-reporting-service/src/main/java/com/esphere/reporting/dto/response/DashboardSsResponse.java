package com.esphere.reporting.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSsResponse {

    // ── Compteurs en attente ──────────────────────────────────────
    private Long consultationsEnAttente;
    private Long ordonnancesEnAttente;
    private Long examensEnAttente;
    private Long bonsManuelEnAttente;

    // ── Compteurs du mois ─────────────────────────────────────────
    private Long   totalValidesMois;
    private Long   totalRejetesMois;
    private Long   totalEncaissesMois;
    private Double montantEncaisseMois;

    // ── Compteurs globaux ─────────────────────────────────────────
    private Long   totalVisitesAnnee;
    private Double montantTotalAnnee;

    // ── Graphique par mois ────────────────────────────────────────
    private List<StatMoisResponse> consultationsParMois;
    private List<StatMoisResponse> ordonnancesParMois;
    private List<StatMoisResponse> examensParMois;
    private List<StatMoisResponse> montantsParMois;

    // ── Top prestataires ──────────────────────────────────────────
    private List<TopPrestataireResponse> topPrestataires;

    // ── Alertes non lues ─────────────────────────────────────────
    private Long alertesNonLues;
}