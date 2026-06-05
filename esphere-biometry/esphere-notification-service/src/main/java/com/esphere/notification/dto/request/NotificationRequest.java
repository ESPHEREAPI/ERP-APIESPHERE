package com.esphere.notification.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    @NotBlank(message = "Le destinataire est obligatoire")
    private String destinataireId;

    // prestataire | agent_ss | assure
    @NotBlank(message = "Le type de destinataire est obligatoire")
    private String typeDest;

    // sms | email | alerte
    @NotBlank(message = "Le canal est obligatoire")
    private String canal;

    private String sujet;

    @NotBlank(message = "Le message est obligatoire")
    private String message;

    // Téléphone pour SMS
    private String telephone;

    // Email pour email
    private String emailDest;

    private String referenceId;

    // Employé qui envoie manuellement
    private Integer envoyePar;
    private String eventType;
}