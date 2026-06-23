package com.esphere.validation.dto.response;

import jakarta.persistence.Column;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationEnAttenteResponse {

    private Integer       id;
    private String        visiteId;
    private String        codeAdherent;
    private String        codeAyantDroit;
    private String        prestataireId;
  //     @Column(name = "prestataire_nom")
    private String prestataireNom;
    private String        typeConsultation;
    private String        natureConsultation;
    private String        natureAffection;
    private Double        montant;
    private Double        taux;
    private String        observations;
    private String        etatConsultation;
    private LocalDateTime date;
     private short groupe;
    private String souscripteur;
    private Double montantValide;
    private Double partZenithe;
    private Double partAssure;
    private String statutAdherent;
    private String nomAdherent;
    private String nomAyantDroit;
  
}