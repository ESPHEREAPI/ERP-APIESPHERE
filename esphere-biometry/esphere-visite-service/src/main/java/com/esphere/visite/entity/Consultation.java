package com.esphere.visite.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dbx45ty_consultation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // int unsigned → Integer
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "visite_id", nullable = false)
    private String visiteId;

    // int unsigned → Integer
    @Column(name = "employe_valide_rejete_id")
    private Integer employeValideRejeteId;

    // double → Double
    @Column(name = "taux")
    private Double taux;

    @Column(name = "type_consultation", nullable = false)
    private String typeConsultation;

    @Column(name = "nature_consultation", nullable = false)
    private String natureConsultation;

    @Column(name = "nature_affection")
    private String natureAffection;

    // double unsigned → Double
    @Column(name = "montant", nullable = false)
    private Double montant;

    // double unsigned → Double
    @Column(name = "montant_modif")
    private Double montantModif;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "date_valide_rejete")
    private LocalDateTime dateValideRejete;

    @Column(name = "observations")
    private String observations;

    // etat_consultation : EN_ATTENTE_VALIDATION | VALIDE | REJETE | ENCAISSE
    @Column(name = "etat_consultation", nullable = false)
    private String etatConsultation;

    // supprime : -1 = non supprimé | 1 = supprimé
    @Column(name = "supprime", nullable = false)
    private String supprime;
}