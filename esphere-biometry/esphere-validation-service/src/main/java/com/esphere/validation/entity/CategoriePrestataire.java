package com.esphere.validation.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbx45ty_categorie_prestataire")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriePrestataire {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "nom", nullable = false)
    private String nom;

    // statut : 1 = actif | -1 = inactif
    @Column(name = "statut", nullable = false)
    private String statut;
}