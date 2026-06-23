package com.esphere.notification.dto.request;

import lombok.Data;

@Data
public class FraudeCheckRequest {
    private String prestataireId;
    private String prestataireNom;
    private int nbPrestationsJour;
}
