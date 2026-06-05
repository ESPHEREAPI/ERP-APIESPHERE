package com.esphere.validation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationLigneRequest {

    // valide | rejete
    @NotBlank(message = "La décision est obligatoire")
    private String decision;

    private String observations;

    // Valeurs modifiées si validation partielle
    private Double valeurModif;
    private Double nbreModif;
       private Double taux;
    private Double actePrelevementModif;

    private Integer employeId;
}