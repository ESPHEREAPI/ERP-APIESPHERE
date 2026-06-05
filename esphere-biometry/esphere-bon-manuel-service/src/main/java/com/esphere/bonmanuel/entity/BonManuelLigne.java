package com.esphere.bonmanuel.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbx45ty_bon_manuel_ligne")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BonManuelLigne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bon_manuel_id", nullable = false)
    private BonManuel bonManuel;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "codification")
    private String codification;

    @Column(name = "quantite", nullable = false)
    private Double quantite;

    @Column(name = "montant_unitaire", nullable = false)
    private Double montantUnitaire;

    @Column(name = "montant_total", nullable = false)
    private Double montantTotal;

    @Column(name = "observations")
    private String observations;
}