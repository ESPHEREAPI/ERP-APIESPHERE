/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.dto.response;

/**
 *
 * @author USER01
 */
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsommationResponse {
    
     private String codeAdherent;
    private String nomAssure;
    private String souscripteur;
    private Short groupe;
    private Integer annee;
    private Double plafondGlobal;

    // Encaissés
    private Double montantConsultationsEncaissees;
    private Long nbreConsultationsEncaissees;

    private Double montantOrdonnancesEncaissees;
    private Long nbreOrdonnancesEncaissees;

    private Double montantExamensEncaisses;
    private Long nbreExamensEncaisses;

    private Double montantBonsManuelsEncaisses;
    private Long nbreBonsManuelsEncaisses;

    private Double totalEncaisse;

    // En cours
    private Double montantConsultationsEnCours;
    private Double montantOrdonnancesEnCours;
    private Double montantExamensEnCours;
    private Double montantBonsManuelsEnCours;

    private Double totalEnCours;

    // Projection
    private Double totalProjecte;
    private Double soldeApresEncaisse;
    private Double soldeApresProjection;

    private Double pourcentageEncaisse;
    private Double pourcentageProjecte;

    // Alerte
    private String niveauAlerte;
    private String messageAlerte;

   
    
}
