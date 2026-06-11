/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * DTO pour la gestion complète d'un adhérent
 * Utilisé pour les opérations CRUD et l'affichage du profil détaillé
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
public class AdherentDTO {
    
    /**
     * Identifiant unique de l'adhérent
     */
    private String codeAdherent;
    
    /**
     * Numéro séquentiel
     */
    private Integer numero;
    
    /**
     * Nom complet de l'assuré principal
     */
    @NotBlank(message = "Le nom de l'assuré principal est obligatoire")
    private String assurePrincipal;
    
    /**
     * Date de naissance
     */
    @NotNull(message = "La date de naissance est obligatoire")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date naissance;
    
    /**
     * Sexe (M/F)
     */
    @Pattern(regexp = "^[MF]$", message = "Le sexe doit être M ou F")
    private String sexe;
    
    /**
     * Matricule de l'employé
     */
    private String matricule;
    
    /**
     * Numéro de téléphone
     */
    @Pattern(regexp = "^[+]?[0-9]{8,15}$", message = "Format de téléphone invalide")
    private String telephone;
    
    /**
     * Email de l'adhérent
     */
    @Email(message = "Format d'email invalide")
    private String email;
    
    /**
     * Taux de couverture personnalisé
     */
    private Double taux;
    
    /**
     * Code du souscripteur
     */
    @NotBlank(message = "Le code souscripteur est obligatoire")
    private String souscripteur;
    
    /**
     * Numéro de police
     */
    @NotBlank(message = "Le numéro de police est obligatoire")
    private String police;
    
    /**
     * Date d'effet de la police
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effetPolice;
    
    /**
     * Date d'échéance de la police
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date echeancePolice;
    
    /**
     * Groupe de couverture
     */
    private Short groupe;
    
    /**
     * Statut d'enrôlement biométrique (O/N)
     */
    private String enrole;
    
    /**
     * Date d'enrôlement biométrique
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateEnrole;
    
    /**
     * Statut d'impression de la carte (O/N)
     */
    private String imprime;
    
    /**
     * Statut de l'adhérent (ACTIF, SUSPENDU, RESILIE)
     */
    @NotBlank(message = "Le statut est obligatoire")
    private String statut;
    
    /**
     * Photo de l'adhérent (Base64 ou URL)
     */
    private String photo;
    
    /**
     * Adresse complète
     */
    private String adresse;
    
    /**
     * Ville
     */
    private String ville;
    
    /**
     * Liste des ayants droit
     */
    private List<AyantDroitSimpleDTO> ayantsDroits;
    
    /**
     * Statistiques de consommation
     */
    private ConsommationAdherentDTO consommation;
    
    /**
     * Informations sur les plafonds
     */
    private PlafondAdherentDTO plafonds;
    
    /**
     * Date de création du dossier
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreation;
    
    /**
     * Date de dernière modification
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateModification;
    
    /**
     * DTO simplifié pour les ayants droit
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AyantDroitSimpleDTO {
        private String codeAyantDroit;
        private String nom;
        private String sexe;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date naissance;
        private String lienpare;
        private String telephone;
        private String statut;
        private String photo;
    }
    
    /**
     * DTO pour la consommation de l'adhérent
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsommationAdherentDTO {
        private BigDecimal montantTotalDepense;
        private BigDecimal montantTotalRembourse;
        private BigDecimal montantTotalACharge;
        private Integer nombreConsultations;
        private Integer nombrePrestations;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateDerniereConsultation;
        private BigDecimal moyenneParConsultation;
        private String tendance; // HAUSSE, BAISSE, STABLE
    }
    
    /**
     * DTO pour les plafonds de l'adhérent
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlafondAdherentDTO {
        private BigDecimal plafondGlobal;
        private BigDecimal montantUtilise;
        private BigDecimal montantRestant;
        private BigDecimal pourcentageUtilisation;
        private List<PlafondParCategorieDTO> detailsParCategorie;
    }
    
    /**
     * DTO pour les plafonds par catégorie
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlafondParCategorieDTO {
        private String categorieId;
        private String categorieNom;
        private BigDecimal plafond;
        private BigDecimal montantUtilise;
        private BigDecimal montantRestant;
        private BigDecimal pourcentageUtilisation;
        private String statut; // DISPONIBLE, ATTENTION, EPUISE
    }
}
