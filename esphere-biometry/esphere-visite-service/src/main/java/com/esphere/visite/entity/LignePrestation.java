package com.esphere.visite.entity;

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
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestation_id", nullable = false)
    private Prestation prestation;

    @Column(name = "prestataire_id")
    private String prestataireId;

    // int unsigned → Integer
    @Column(name = "employe_valide_rejete_id")
    private Integer employeValideRejeteId;

    // int unsigned → Integer
    @Column(name = "medicament_id")
    private Integer medicamentId;

    // int unsigned → Integer
    @Column(name = "examen_id")
    private Integer examenId;

    // double → Double
    @Column(name = "taux")
    private Double taux;

    // varchar(5)
    @Column(name = "type_examen")
    private String typeExamen;

    // varchar(5)
    @Column(name = "description_soins")
    private String descriptionSoins;

    @Column(name = "dents_concernees")
    private String dentsConcernees;

    @Column(name = "codification")
    private String codification;

    @Column(name = "nom")
    private String nom;

    // double → Double
    @Column(name = "valeur")
    private Double valeur;

    // double → Double
    @Column(name = "nbre")
    private Double nbre;

    // double unsigned → Double
    @Column(name = "acte_prelevement", nullable = false)
    private Double actePrelevement;
   

    // double → Double
    @Column(name = "valeur_modif")
    private Double valeurModif;

    // double → Double
    @Column(name = "nbre_modif")
    private Double nbreModif;

//    // double → Double
//    @Column(name = "acte_prelevement_modif", nullable = false)
//    private Double actePrelevementModif;
    
     // Après — double unsigned NOT NULL default 0
@Column(name = "acte_prelevement_modif", nullable = false)
private Double actePrelevementModif = 0.0;

    @Column(name = "posologie")
    private String posologie;

    @Column(name = "observations")
    private String observations;

    @Column(name = "observations_acte_prelevement")
    private String observationsActePrelevement;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "date_valide_rejete", nullable = false)
    private LocalDateTime dateValideRejete;

    @Column(name = "date_encaisse")
    private LocalDateTime dateEncaisse;

    // etat : EN_ATTENTE | VALIDE | REJETE | ENCAISSE
    @Column(name = "etat", nullable = false)
    private String etat;

    // supprime : -1 = non supprimé | 1 = supprimé
    @Column(name = "supprime", nullable = false)
    private String supprime;
}