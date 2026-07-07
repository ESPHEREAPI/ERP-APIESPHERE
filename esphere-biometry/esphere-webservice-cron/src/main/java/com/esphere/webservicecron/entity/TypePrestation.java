package com.esphere.webservicecron.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "dbx45ty_type_prestation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypePrestation implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "affiche")
    private int affiche;

    @Column(name = "categorie")
    private String categorie;
}
