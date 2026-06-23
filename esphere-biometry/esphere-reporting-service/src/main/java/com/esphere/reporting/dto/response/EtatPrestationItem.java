package com.esphere.reporting.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EtatPrestationItem {

    private Long    id;
    private String  nature;
    private String  date;
    private String  codeAdherent;
    private String  codeAyantDroit;
    private String  nomAssure;
    private String  nomAyantDroit;
    private String  souscripteur;
    private Integer taux;
    private String  etatGlobal;
    private Integer nbreLignes;
    private Double  montantSoumis;
    private Double  montantValide;
}
