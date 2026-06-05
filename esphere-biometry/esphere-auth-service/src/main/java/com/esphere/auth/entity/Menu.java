package com.esphere.auth.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dbx45ty_menu")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pere_id")
    private Integer pereId;

    @Column(name = "nom_controlleur")
    private String nomControlleur;

    @Column(name = "nom_module", nullable = false)
    private String nomModule;

    @Column(name = "nom_action")
    private String nomAction;

    // SMALLINT en base → Short en Java (pas Integer)
    @Column(name = "numero_ordre", nullable = false)
    private Short numeroOrdre;

    @Column(name = "class_image")
    private String classImage;

    @Column(name = "type", nullable = false)
    private String type;

    // SMALLINT en base → Short en Java
    @Column(name = "position", nullable = false)
    private Short position;

    @Column(name = "apparait_nav", nullable = false)
    private String apparaitNav;

    @Column(name = "apparait_nav_bar", nullable = false)
    private String apparaitNavBar;

    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "supprime", nullable = false)
    private String supprime;

    @Column(name = "chemin_pere")
    private String cheminPere;
}