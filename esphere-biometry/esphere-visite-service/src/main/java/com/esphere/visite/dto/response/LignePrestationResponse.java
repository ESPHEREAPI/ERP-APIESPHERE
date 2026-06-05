package com.esphere.visite.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LignePrestationResponse {

    private Integer       id;
    private Integer       prestationId;
    private String        prestataireId;
    private Integer       employeValideRejeteId;
    private Integer       medicamentId;
    private Integer       examenId;
    private Double        taux;
    private String        typeExamen;
    private String        descriptionSoins;
    private String        dentsConcernees;
    private String        codification;
    private String        nom;
    private Double        valeur;
    private Double        nbre;
    private Double        actePrelevement;
    private Double        valeurModif;
    private Double        nbreModif;
    private Double        actePrelevementModif;
    private String        posologie;
    private String        observations;
    private LocalDateTime date;
    private LocalDateTime dateValideRejete;
    private LocalDateTime dateEncaisse;
    private String        etat;
}