package com.esphere.prestataire.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TauxPrestationResponse {

    private Integer id;
    private String  typePrestationId;
    private String  typePrestationNom;
    private String  police;
    private Short   groupe;
    private Integer taux;
    private Float   plafond;
}