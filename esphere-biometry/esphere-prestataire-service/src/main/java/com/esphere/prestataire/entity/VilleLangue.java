package com.esphere.prestataire.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbx45ty_ville_langue")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class VilleLangue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "langue_id")
    private Short langueId;

    @Column(name = "ville_id")
    private Integer villeId;

    private String nom;
}
