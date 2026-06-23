package com.esphere.validation.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PrestationBonResponse {

    private Integer       id;
    private String        visiteId;
    private String        codeCourt;          // dernière partie de visiteId
    private String        naturePrestation;   // ordonnance / examen
    private LocalDateTime date;

    private String        prestataireNom;

    private String        nomAdherent;
    private String        nomAyantDroit;
    private String        malade;             // ayant droit si présent, sinon adhérent
    private String        souscripteur;

    private List<LigneBon> lignes;

    private double        montantTotal;
    private double        partZenithe;
    private double        partAssure;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class LigneBon {
        private String nom;
        private String typeExamen;       // utilisé pour examens/actes
        private double taux;
        private double montantValide;    // valeur unitaire validée
        private double quantite;         // nbre validé
        private double total;            // montantValide * quantite
    }
}
