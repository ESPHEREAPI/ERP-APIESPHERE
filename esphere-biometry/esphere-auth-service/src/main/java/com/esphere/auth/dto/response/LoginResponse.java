package com.esphere.auth.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class LoginResponse {

    private String        token;
    private String        tokenType;
    private long          expiresIn;
    private Integer       userId;
    private String        login;
    private String        nom;
    private String        prenom;
    private String        email;
    private String        profilCode;
    private String        profilLibelle;
    private String        connexionAppli;
    private String        prestataireId;
    private List<MenuResponse> menus;

    // ── Champs OTP — remplis uniquement après validateOtp ─
    /** Code visite pour pré-remplir le formulaire Angular */
    private String codeVisite;

    /** Année de la visite */
    private String annee;

    /** Type : consultation | ordonnance | examen */
    private String naturePrestation;
}