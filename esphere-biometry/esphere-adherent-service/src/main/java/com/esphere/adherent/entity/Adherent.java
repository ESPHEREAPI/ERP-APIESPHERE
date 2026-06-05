package com.esphere.adherent.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dbx45ty_adherent")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adherent {

    // Clé primaire : code_adherent (varchar)
    @Id
    @Column(name = "code_adherent", nullable = false)
    private String codeAdherent;

    // Numéro auto-increment (non PK mais indexé)
    @Column(name = "numero", insertable = false, updatable = false)
    private Integer numero;

    @Column(name = "assure_principal")
    private String assurePrincipal;

    @Column(name = "naissance")
    private LocalDate naissance;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "telephone")
    private String telephone;

    // double en base → Double en Java
    @Column(name = "taux")
    private Double taux;

    // double en base → Double en Java (plafond assuré)
    @Column(name = "plafond_assurep")
    private Double plafondAssurep;

    // double en base → Double en Java (consommation AP)
    @Column(name = "cons_ap")
    private Double consAp;

    @Column(name = "ville")
    private String ville;

    @Column(name = "souscripteur")
    private String souscripteur;

    @Column(name = "police")
    private String police;

    @Column(name = "effet_police")
    private LocalDate effetPolice;

    @Column(name = "echeance_police")
    private LocalDate echeancePolice;

    // SMALLINT en base → Short en Java
    @Column(name = "groupe")
    private Short groupe;

    @Column(name = "enrole", nullable = false)
    private String enrole;

    @Column(name = "date_enrole")
    private LocalDateTime dateEnrole;

    @Column(name = "imprime", nullable = false)
    private String imprime;

    // statut : 1 = actif | -1 = inactif
    @Column(name = "statut", nullable = false)
    private String statut;

    @OneToMany(mappedBy = "adherent", fetch = FetchType.LAZY)
    private List<AyantDroit> ayantsDroit;
}