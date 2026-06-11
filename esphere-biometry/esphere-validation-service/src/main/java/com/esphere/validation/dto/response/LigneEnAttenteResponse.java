package com.esphere.validation.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneEnAttenteResponse {

    private Integer id;
    private Integer prestationId;
    private String prestataireId;
    private String visiteId;
    private String codeAdherent;
    private String codeAyantDroit;
    private String nomAssure;
    private String nomAyantDroit;
    private String nom;
    private String codification;
    private String typeExamen;
    private String descriptionSoins;
    private String dentsConcernees;
    private Double valeur;
    private Double nbre;
    private Double actePrelevement;
    private Double taux;
    private String posologie;
    private String observations;
    private String etat;
    private LocalDateTime date;
    private String nomPrestataire; // ← ajoutez ce champ

    private String prestataireNom;
    private short groupe;
    private String souscripteur;
    private String natureAffection;
    private Double valeurModif;
    private Double nbreModif;
    private Double actePrelevementModif;
    private String statutAdherent;
}
