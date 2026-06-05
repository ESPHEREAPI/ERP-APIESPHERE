package com.esphere.reporting.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsommationAyantDroitResponse {

    private String codeAyantDroit;
    private String nom;
    private String lienPare;
    private Double montantConsomme;
}