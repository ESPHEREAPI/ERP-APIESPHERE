package com.esphere.validation.entity;

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
    @Column(name = "id")
    private Integer id;

    @Column(name = "visite_id", nullable = false)
    private String visiteId;

    @Column(name = "employe_valide_rejete_id")
    private Integer employeValideRejeteId;

    @Column(name = "taux")
    private Double taux;

    @Column(name = "type_consultation", nullable = false)
    private String typeConsultation;

    @Column(name = "nature_consultation", nullable = false)
    private String natureConsultation;

    @Column(name = "nature_affection")
    private String natureAffection;

    @Column(name = "montant", nullable = false)
    private Double montant;

    @Column(name = "montant_modif")
    private Double montantModif;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "date_valide_rejete")
    private LocalDateTime dateValideRejete;

    @Column(name = "observations")
    private String observations;

    // valide | rejete | encaisse
    @Column(name = "etat_consultation", nullable = false)
    private String etatConsultation;

    @Column(name = "supprime", nullable = false)
    private String supprime;
}