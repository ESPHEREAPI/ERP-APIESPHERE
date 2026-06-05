package com.esphere.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsManuelRequest {

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "Le message est obligatoire")
    private String message;

    // Référence assuré ou prestataire concerné
    private String referenceId;

    // Employé SS qui envoie
    private Integer envoyePar;
}