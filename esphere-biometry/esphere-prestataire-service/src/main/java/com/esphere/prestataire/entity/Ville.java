package com.esphere.prestataire.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbx45ty_ville")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ville {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "region_id")
    private Integer regionId;

    private String code;

    @Column(name = "code_zone")
    private String codeZone;

    private String statut;
    private String supprime;
}
