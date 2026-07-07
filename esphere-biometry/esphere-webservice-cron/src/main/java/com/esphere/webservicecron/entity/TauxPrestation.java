package com.esphere.webservicecron.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "dbx45ty_taux_prestation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TauxPrestation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type_prestation_id")
    private String typePrestationId;

    @Column(name = "police")
    private String police;

    @Column(name = "groupe")
    private short groupe;

    @Column(name = "taux")
    private Integer taux;

    @Column(name = "plafond")
    private Float plafond;
}
