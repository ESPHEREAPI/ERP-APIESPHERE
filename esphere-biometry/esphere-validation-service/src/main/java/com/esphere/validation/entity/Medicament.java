/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/**
 *
 * @author USER01
 */


@Entity
@Table(name = "dbx45ty_medicament")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicament implements Serializable {

     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code")
    private String code;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "origine")
    private String origine;

    @Column(name = "prix")
    private Double prix;

    @Column(name = "quantite")
    private Double quantite;

    @Column(name = "categorie", nullable = false)
    private String categorie;

    @Column(name = "prestataire_id")
    private String prestataireId;

    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "supprime", nullable = false)
    private String supprime;
    
}
