/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.dto.request;

import lombok.*;

/**
 *
 * @author USER01
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ExamenDTO {
    private Integer id;
    private String  code;
    private String  nom;
    private short   cotation;
    private Double  prix,valeur;
    private String  statut;
    private String  supprime;
    private boolean nouveau; // true = vient d'être créé

    public ExamenDTO(String nom, Double prix) {
        this.nom = nom;
        this.prix = prix;
        this.valeur=prix;
    }
    
    
}