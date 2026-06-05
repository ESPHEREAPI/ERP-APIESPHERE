/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.dto.request;

/**
 *
 * @author USER01
 */
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TypePrestationDTO {
    private String  id;
    private String  nom;
    private int     affiche;
    private String  categorie;
}
