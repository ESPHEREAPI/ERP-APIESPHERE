package com.esphere.notification.dto.request;

import lombok.Data;

@Data
public class PrestationNotifRequest {
    private String prestationId;
    private String etat;
    private String codeAdherent;
    private String codeAyantDroit;
    private String prestataireId;
    private String prestataireNom;
    private String naturePrestation;
    private String codeVisite;
    private double montantValide;
    private double montantZenithe;
    private double montantPartAssure;
    private String telephoneAdherent;
    private String telephoneAyantDroit;
    private Short langueAssure;
}
