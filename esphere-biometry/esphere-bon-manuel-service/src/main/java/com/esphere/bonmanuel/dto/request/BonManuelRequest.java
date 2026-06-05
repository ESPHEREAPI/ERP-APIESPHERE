package com.esphere.bonmanuel.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BonManuelRequest {

    @NotBlank(message = "Le numéro de proforma est obligatoire")
    private String numeroProforma;

    @NotBlank(message = "La visite est obligatoire")
    private String visiteId;

    @NotBlank(message = "Le prestataire est obligatoire")
    private String prestataireId;

    @NotBlank(message = "Le code adhérent est obligatoire")
    private String codeAdherent;

    private String codeAyantDroit;

    @NotNull(message = "Le montant du proforma est obligatoire")
    private Double montantProforma;

    private String observations;

    private Integer employeId;
}