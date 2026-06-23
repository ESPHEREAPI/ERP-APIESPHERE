package com.esphere.validation.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dbx45ty_parametre")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parametre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cle", nullable = false, unique = true, length = 100)
    private String cle;

    @Column(name = "valeur", nullable = false, length = 255)
    private String valeur;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;
}
