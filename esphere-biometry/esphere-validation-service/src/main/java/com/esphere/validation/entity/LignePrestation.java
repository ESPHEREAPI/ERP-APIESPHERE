package com.esphere.validation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dbx45ty_ligne_prestation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LignePrestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "prestation_id", nullable = false)
    private Integer prestationId;

    @Column(name = "prestataire_id")
    private String prestataireId;
     

    @Column(name = "employe_valide_rejete_id")
    private Integer employeValideRejeteId;

    @Column(name = "medicament_id")
    private Integer medicamentId;

    @Column(name = "examen_id")
    private Integer examenId;

    @Column(name = "taux")
    private Double taux;

    @Column(name = "type_examen")
    private String typeExamen;

    @Column(name = "description_soins")
    private String descriptionSoins;

    @Column(name = "dents_concernees")
    private String dentsConcernees;

    @Column(name = "codification")
    private String codification;

    @Column(name = "nom")
    private String nom;

    @Column(name = "valeur")
    private Double valeur;

    @Column(name = "nbre")
    private Double nbre;

    @Column(name = "acte_prelevement", nullable = false)
    private Double actePrelevement;

    @Column(name = "valeur_modif")
    private Double valeurModif;

    @Column(name = "nbre_modif")
    private Double nbreModif;

    @Column(name = "acte_prelevement_modif", nullable = false)
    private Double actePrelevementModif;

    @Column(name = "posologie")
    private String posologie;

    @Column(name = "observations")
    private String observations;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "date_valide_rejete", nullable = false)
    private LocalDateTime dateValideRejete;

    @Column(name = "date_encaisse")
    private LocalDateTime dateEncaisse;

    // enregistre | attente_validation | valide | rejete | encaisse
    @Column(name = "etat", nullable = false)
    private String etat;

    @Column(name = "supprime", nullable = false)
    private String supprime;
}