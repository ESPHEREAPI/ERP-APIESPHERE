package com.esphere.validation.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrestationResponse {
    private Integer       id;
    private String        visiteId;
    private String        prestataireId;
    private String        naturePrestation;
    private LocalDateTime date;
    // Infos visite
    private String        codeAdherent;
    private String        codeAyantDroit;
    // Infos adhérent enrichies
    private String        nomAssure;
    private String        nomAyantDroit;
    private String        souscripteur;
    private Short         groupe;
    private String        natureAffection;
    // Stats lignes
    private long          nbreLignes;
    private long          nbreLignesEnAttente;
    private String        etatGlobal; // attente_validation | partiellement_valide | valide | encaisse
    private String nomPrestataire; // ← ajoutez ce champ
    private String statutAdherent;
}