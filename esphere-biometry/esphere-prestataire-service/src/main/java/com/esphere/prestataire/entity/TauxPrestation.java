package com.esphere.prestataire.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbx45ty_taux_prestation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TauxPrestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_prestation_id", nullable = false)
    private TypePrestation typePrestation;

    @Column(name = "police", nullable = false)
    private String police;

    // smallint unsigned → Short en Java
    @Column(name = "groupe", nullable = false)
    private Short groupe;

    // int unsigned → Integer en Java
    @Column(name = "taux")
    private Integer taux;

    // float unsigned → Float en Java
    @Column(name = "plafond")
    private Float plafond;
}