package com.esphere.prestataire.dto.request;

import lombok.Data;

@Data
public class PrestataireRequest {
    private String id;
    private String categorieId;
    private Integer villeId;
    private String nom;
    private String adresse;
    private String email;
    private String telephone;
    private String registre;
}
