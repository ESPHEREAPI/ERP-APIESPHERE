package com.esphere.webservicecron.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class DashboardStatsResponse {
    private long totalExecutions;
    private long executionsAujourdhui;
    private long executionsCronAujourdhui;
    private long succes;
    private long partiel;
    private long echec;
    private long enCours;
    /** Dernière exécution connue pour chacun des 5 types de traitement. */
    private Map<String, HistoriqueSyncResponse> dernieresExecutionsParType;
    /** Les 10 dernières exécutions tous types confondus, pour le flux du dashboard. */
    private List<HistoriqueSyncResponse> dernieresExecutions;
}
