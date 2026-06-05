package com.esphere.adherent.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdherentResponse {

    private String    codeAdherent;
    private String    assurePrincipal;
    private LocalDate naissance;
    private String    sexe;
    private String    matricule;
    private String    telephone;
    private Double    taux;
    private Double    plafondAssurep;
    private Double    consAp;
    private String    ville;
    private String    souscripteur;
    private String    police;
    private LocalDate effetPolice;
    private LocalDate echeancePolice;
    private Short     groupe;
    private String    statut;
    private LocalDateTime dateEnrole;
    private List<AyantDroitResponse> ayantsDroit;
}