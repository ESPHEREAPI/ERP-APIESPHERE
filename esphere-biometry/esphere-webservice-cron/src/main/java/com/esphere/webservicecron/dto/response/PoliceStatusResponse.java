package com.esphere.webservicecron.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PoliceStatusResponse {
    private String police;
    private String souscripteur;
    private LocalDate dateEffet;
    private LocalDate dateEcheance;
    private Long nbJoursRestants;
    /** ACTIF si dateEffet > date du jour, sinon DESACTIVE. */
    private String statut;
    private long nbAdherentsActifs;
    private long nbAdherentsDesactives;
    private long nbAyantsDroitActifs;
    private long nbAyantsDroitDesactives;
}
