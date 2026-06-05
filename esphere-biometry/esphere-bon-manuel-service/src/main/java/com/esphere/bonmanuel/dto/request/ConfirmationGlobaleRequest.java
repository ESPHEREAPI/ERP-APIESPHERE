package com.esphere.bonmanuel.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationGlobaleRequest {

    @NotNull(message = "Le montant confirmé est obligatoire")
    private Double montantConfirme;

    private String observations;

    private Integer employeId;
}