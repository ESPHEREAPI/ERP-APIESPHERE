package com.esphere.bonmanuel.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonManuelLigneResponse {

    private Integer id;
    private String  nom;
    private String  codification;
    private Double  quantite;
    private Double  montantUnitaire;
    private Double  montantTotal;
    private String  observations;
}