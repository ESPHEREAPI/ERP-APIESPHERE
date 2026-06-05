package com.esphere.bonmanuel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonManuelLigneRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    private String codification;

    @NotNull(message = "La quantité est obligatoire")
    private Double quantite;

    @NotNull(message = "Le montant unitaire est obligatoire")
    private Double montantUnitaire;

    private String observations;
}