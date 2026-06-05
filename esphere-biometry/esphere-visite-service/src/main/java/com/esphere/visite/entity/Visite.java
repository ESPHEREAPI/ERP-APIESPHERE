package com.esphere.visite.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dbx45ty_visite")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visite {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "code_adherent", nullable = false)
    private String codeAdherent;

    // NULL si l'assuré consulte pour lui-même
    @Column(name = "code_ayant_droit")
    private String codeAyantDroit;

    @Column(name = "prestataire_id", nullable = false)
    private String prestataireId;

    // int unsigned → Integer en Java
    @Column(name = "employe_id")
    private Integer employeId;

    @Column(name = "code_court", nullable = false)
    private String codeCourt;

    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;
}