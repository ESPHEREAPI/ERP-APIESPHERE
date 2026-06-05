package com.esphere.reporting.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatTypeResponse {

    private String type;      // consultation | ordonnance | examen | bon_manuel
    private Long   nombre;
    private Double montant;
    private Double pourcentage;
}