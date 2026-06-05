package com.esphere.auth.dto.response;

import lombok.*;

/**
 * Réponse du endpoint GET /auth/me
 * Retourne les infos de l'utilisateur actuellement connecté.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private Integer id;
    private String  login;
    private String  nom;
    private String  prenom;
    private String  email;
    private String  genre;
    private String  telephone;
    private String  statut;
    private String  profilCode;
    private String  profilLibelle;
    private String  connexionAppli;
    private String  prestataireId;
}
