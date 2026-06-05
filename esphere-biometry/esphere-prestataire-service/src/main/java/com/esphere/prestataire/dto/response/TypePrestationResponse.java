package com.esphere.prestataire.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypePrestationResponse {

    private String  id;
    private String  nom;
    private Integer affiche;
    private String  categorie;
}