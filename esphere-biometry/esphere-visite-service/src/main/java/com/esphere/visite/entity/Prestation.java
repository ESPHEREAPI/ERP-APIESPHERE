package com.esphere.visite.entity;

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
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "visite_id", nullable = false)
    private String visiteId;

    @Column(name = "prestataire_id")
    private String prestataireId;

    // nature_prestation : ordonnance | examen
    @Column(name = "nature_prestation", nullable = false)
    private String naturePrestation;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    // supprime : -1 = non supprimé | 1 = supprimé
    @Column(name = "supprime", nullable = false)
    private String supprime;

    @OneToMany(mappedBy = "prestation", fetch = FetchType.LAZY)
    private List<LignePrestation> lignes;
}