package com.esphere.validation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dbx45ty_ayant_droit")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AyantDroit {

    @Id
    @Column(name = "code_ayant_droit", nullable = false)
    private String codeAyantDroit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_adherent", nullable = false)
    private Adherent adherent;

    @Column(name = "nom")
    private String nom;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "naissance")
    private LocalDate naissance;

    @Column(name = "lienpare", nullable = false)
    private String lienPare;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "police")
    private String police;

    @Column(name = "enrole", nullable = false)
    private String enrole;

    @Column(name = "date_enrole")
    private LocalDateTime dateEnrole;

    // statut : 1 = actif | -1 = inactif
    @Column(name = "statut", nullable = false)
    private String statut;
}