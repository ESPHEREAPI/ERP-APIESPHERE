package com.esphere.reporting.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatMoisResponse {

    private Integer mois;
    private String  libelleMois;
    private Long    nombre;
    private Double  montant;
}