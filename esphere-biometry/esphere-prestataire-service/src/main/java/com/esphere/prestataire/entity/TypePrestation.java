package com.esphere.prestataire.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbx45ty_type_prestation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypePrestation {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "nom", nullable = false)
    private String nom;

    // int en base → Integer en Java
    // -1 = non affiché | 1 = affiché
    @Column(name = "affiche", nullable = false)
    private Integer affiche;

    @Column(name = "categorie", nullable = false)
    private String categorie;
}