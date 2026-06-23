package com.esphere.notification.dto.request;

import lombok.Data;

@Data
public class SoumissionNotifRequest {
    private String prestataireId;
    private String prestataireNom;
    private String naturePrestation;
    private String codeVisite;
    private String codeAdherent;
    private String codeAyantDroit;
    private int nbLignes;
}
