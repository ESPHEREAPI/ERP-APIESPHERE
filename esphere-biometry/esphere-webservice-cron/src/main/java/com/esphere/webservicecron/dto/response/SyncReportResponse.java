package com.esphere.webservicecron.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Rapport d'exécution d'une synchronisation (équivalent du fichier
 * "echec_*.txt" écrit par le PHP legacy, mais retourné en JSON).
 */
@Data
@Builder
public class SyncReportResponse {
    private int nbTraites;
    private int nbEchecs;
    /** Identifiants des enregistrements traités avec succès (créés ou mis à jour). */
    private List<String> lignesTraitees;
    private List<String> lignesEnEchec;
}
