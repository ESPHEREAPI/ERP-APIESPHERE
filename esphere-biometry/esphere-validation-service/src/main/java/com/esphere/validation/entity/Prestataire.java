package com.esphere.validation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbx45ty_prestataire")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestataire {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id", nullable = false)
    private CategoriePrestataire categorie;

    // int unsigned en base → Integer en Java
    @Column(name = "ville_id")
    private Integer villeId;

    @Column(name = "nom")
    private String nom;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "email")
    private String email;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "registre")
    private String registre;

    @Column(name = "logo")
    private String logo;

    // statut : 1 = actif | -1 = inactif
    @Column(name = "statut", nullable = false)
    private String statut;

    // supprime : -1 = non supprimé | 1 = supprimé
    @Column(name = "supprime", nullable = false)
    private String supprime;
}