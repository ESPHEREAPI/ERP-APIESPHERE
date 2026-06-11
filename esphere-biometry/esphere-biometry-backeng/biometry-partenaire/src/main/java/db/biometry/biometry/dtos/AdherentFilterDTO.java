/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO pour filtrer la liste des adhérents
 * Supporte les recherches multicritères
 * 
 * @author JIATOU FRANCK
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdherentFilterDTO {
    
    /**
     * Recherche par nom ou matricule
     */
    private String search;
    
    /**
     * Filtrage par souscripteur
     */
    private String souscripteur;
    
    /**
     * Filtrage par police
     */
    private String police;
    
    /**
     * Filtrage par groupe
     */
    private Short groupe;
    
    /**
     * Filtrage par statut
     */
    private String statut;
    
    /**
     * Filtrage par sexe
     */
    private String sexe;
    
    /**
     * Filtrage par statut d'enrôlement
     */
    private String enrole;
    
    /**
     * Date d'adhésion minimum
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAdhesionMin;
    
    /**
     * Date d'adhésion maximum
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateAdhesionMax;
    
    /**
     * Numéro de page (pour pagination)
     */
    private Integer page;
    
    /**
     * Taille de la page
     */
    private Integer size;
    
    /**
     * Champ de tri
     */
    private String sortBy;
    
    /**
     * Direction de tri (ASC, DESC)
     */
    private String sortDirection;
    
}
