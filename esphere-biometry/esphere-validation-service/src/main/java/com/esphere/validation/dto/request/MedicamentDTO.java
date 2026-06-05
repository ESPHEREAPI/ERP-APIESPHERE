/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER01
 */
// MedicamentDTO.java
@Data @AllArgsConstructor @NoArgsConstructor
public class MedicamentDTO {
    private Integer    id;
    private String nom;
    private Double prix;
    private String code;
    private String categorie;
    private boolean nouveau; // true = vient d'être créé
}
    

