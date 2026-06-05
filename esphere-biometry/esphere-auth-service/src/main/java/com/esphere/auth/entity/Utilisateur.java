package com.esphere.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dbx45ty_utilisateur")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "genre")
    private String genre;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "lieu_naissance")
    private String lieuNaissance;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "telephone_iso2")
    private String telephoneIso2;

    @Column(name = "telephone_dial_code")
    private Integer telephoneDialCode;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    /**
     * Mot de passe hashé via loginBiometrie :
     * SHA1( MD5("RS_" + motDePasse + "-er") )
     */
    @Column(name = "mot_passe")
    private String motPasse;

    // SMALLINT en base → Short en Java
    @Column(name = "langue_defaut")
    private Short langueDefaut;

    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "supprime", nullable = false)
    private String supprime;

    @Column(name = "newsletter")
    private String newsletter;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "oauth_uid")
    private String oauthUid;

    @Column(name = "localisation")
    private String localisation;

    @Column(name = "activite")
    private String activite;

    @Column(name = "situation_matrimoniale")
    private String situationMatrimoniale;

    @OneToOne(mappedBy = "utilisateur", fetch = FetchType.LAZY)
    private Employe employe;
}