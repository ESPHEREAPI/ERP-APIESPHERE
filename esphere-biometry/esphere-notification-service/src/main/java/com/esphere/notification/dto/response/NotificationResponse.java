package com.esphere.notification.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {

    private Integer       id;
    private String        destinataireId;
    private String        typeDest;
    private String        canal;
    private String        sujet;
    private String        message;
    private String        statut;
    private String        eventType;
    private String        referenceId;
    private Boolean       lu;
    private Integer       envoyePar;
    private LocalDateTime dateLecture;
    private LocalDateTime dateEnvoi;
    private LocalDateTime dateCreation;
    private String        erreur;
}