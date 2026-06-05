package com.esphere.validation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationConsultationRequest {

    // valide | rejete
    @NotBlank(message = "La décision est obligatoire")
    private String decision;

    // Obligatoire si rejet
    private String observations;

    // Montant modifié si partiel
    private Double montantModif;
     private Double taux;

    // Id de l'agent qui valide/rejette
    private Integer employeId;
}