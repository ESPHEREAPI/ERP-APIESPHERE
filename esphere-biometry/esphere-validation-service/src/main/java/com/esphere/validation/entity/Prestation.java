package com.esphere.validation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dbx45ty_prestation")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "visite_id", nullable = false)
    private String visiteId;

    @Column(name = "prestataire_id")
    private String prestataireId;

    @Column(name = "nature_prestation", nullable = false)
    private String naturePrestation;
     @Column(name = "nature_affection")
    private String natureAffection;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "supprime", nullable = false)
    private String supprime;

    @OneToMany(mappedBy = "prestationId", fetch = FetchType.LAZY)
    private List<LignePrestation> lignes;
}