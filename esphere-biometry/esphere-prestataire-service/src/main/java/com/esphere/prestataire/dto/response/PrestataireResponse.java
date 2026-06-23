package com.esphere.prestataire.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestataireResponse {

    private String id;
    private String categorieId;
    private String categorieNom;
    private Integer villeId;
    private String villeNom;
    private String nom;
    private String adresse;
    private String email;
    private String telephone;
    private String registre;
    private String logo;
    private String statut;
}