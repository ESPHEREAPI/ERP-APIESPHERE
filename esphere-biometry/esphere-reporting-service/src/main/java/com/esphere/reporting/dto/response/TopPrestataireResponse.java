package com.esphere.reporting.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopPrestataireResponse {

    private String prestataireId;
    private String prestataireNom;
    private Long   nombrePrestations;
    private Double montantTotal;
}