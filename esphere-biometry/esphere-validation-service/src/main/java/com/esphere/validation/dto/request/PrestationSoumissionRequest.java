/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.dto.request;

/**
 *
 * @author USER01
 */
import lombok.Data;
import java.util.List;

@Data
public class PrestationSoumissionRequest {

    private String  visiteId;
    private String  prestataireId;
    private String  naturePrestation;       // ordonnance | examen
    private String  natureAffection;
    private Integer employeId;

    private List<LigneRequest> lignes;
    
     @Data
    public static class LigneRequest {
        private Integer medicamentId;  // pour ordonnance
        private Integer examenId;      // pour examen
        private String  typeExamen;    // pour examen
        private String  nom;
        private Double  valeur;
        private Double  nbre;
        private Double  actePrelevement;
        private String  posologie;     // pour ordonnance
        private String  observations;
        
        
        
    }
}
