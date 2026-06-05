package com.esphere.adherent.dto.response;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AyantDroitResponse {

    private String    codeAyantDroit;
    private String    codeAdherent;
    private String    nom;
    private String    sexe;
    private LocalDate naissance;
    private String    lienPare;
    private String    telephone;
    private String    police;
    private String    statut;
}