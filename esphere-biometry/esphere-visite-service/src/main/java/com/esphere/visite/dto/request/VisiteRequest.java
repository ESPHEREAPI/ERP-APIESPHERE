package com.esphere.visite.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisiteRequest {

    @NotBlank(message = "Le code adhérent est obligatoire")
    private String codeAdherent;

    // Null si assuré direct, renseigné si ayant droit
    private String codeAyantDroit;

    @NotBlank(message = "Le prestataire est obligatoire")
    private String prestataireId;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;
}