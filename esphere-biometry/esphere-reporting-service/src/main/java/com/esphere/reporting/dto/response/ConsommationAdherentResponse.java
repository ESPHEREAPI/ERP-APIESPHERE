package com.esphere.reporting.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsommationAdherentResponse {

    private String codeAdherent;
    private String assurePrincipal;
    private String police;
    private String souscripteur;

    // Plafond et consommation
    private Double plafondAssurep;
    private Double consomme;
    private Double restant;
    private Double tauxConsommation; // en %

    // Détail par type
    private Double montantConsultations;
    private Double montantOrdonnances;
    private Double montantExamens;
    private Double montantBonsManuel;

    // Ayants droit
    private List<ConsommationAyantDroitResponse> ayantsDroit;

    // Historique visites
    private List<StatMoisResponse> consommationParMois;
}