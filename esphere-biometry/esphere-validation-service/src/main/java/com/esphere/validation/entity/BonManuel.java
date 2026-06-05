package com.esphere.validation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dbx45ty_bon_manuel")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BonManuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "visite_id", nullable = false)
    private String visiteId;

    @Column(name = "code_adherent", nullable = false)
    private String codeAdherent;

    @Column(name = "code_ayant_droit")
    private String codeAyantDroit;

    @Column(name = "montant_proforma", nullable = false)
    private Double montantProforma;

    @Column(name = "montant_confirme")
    private Double montantConfirme;

    // en_attente | confirme | encaisse | rejete
    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "supprime", nullable = false)
    private String supprime;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;
}