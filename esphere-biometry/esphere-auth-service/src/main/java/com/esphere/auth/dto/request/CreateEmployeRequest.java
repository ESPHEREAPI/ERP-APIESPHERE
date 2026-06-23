package com.esphere.auth.dto.request;

import lombok.Data;

@Data
public class CreateEmployeRequest {
    private String nom;
    private String prenom;
    private String genre;
    private String email;
    private String login;
    private String motPasse;
    private String telephone;
    private Integer profilId;
    private String prestataireId;
    private Short langueDefaut;
    private String connexionAppli;
    private String serialBiometrie;
}
