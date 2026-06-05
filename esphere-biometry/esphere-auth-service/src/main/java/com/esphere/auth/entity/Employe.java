package com.esphere.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbx45ty_employe")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false, unique = true)
    private Utilisateur utilisateur;

    // Profil chargé immédiatement : toujours nécessaire pour les droits
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profil_id", nullable = false)
    private Profil profil;

    // Agence ou filiale (optionnel)
    @Column(name = "filiale_agence_id")
    private Integer filialeAgenceId;

    /**
     * FK vers dbx45ty_prestataire.
     * NULL pour les agents SS et admins.
     * Renseigné pour les prestataires (médecins, pharmaciens, etc.)
     */
    @Column(name = "prestataire_id")
    private String prestataireId;

    /**
     * Détermine depuis quelle application l'utilisateur se connecte.
     * Valeurs : "biometry" (interface prestataire) | "admin" (back-office SS)
     */
    @Column(name = "connexion_appli", nullable = false)
    private String connexionAppli;
    /**
     * Numéro de série du lecteur biométrique SecuGen
     * installé chez le prestataire.
     * Lu depuis Resources\configServiceSecugen.xml :
     * <serial value="F5C240600712"/>
     * Enregistré par l'admin lors de l'installation.
     */
    @Column(name = "serial_biometrie", length = 50)
    private String serialBiometrie;
}
