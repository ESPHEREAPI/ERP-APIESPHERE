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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les statistiques du tableau de bord souscripteur
 * Contient tous les indicateurs clés de performance
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
public class DashboardStatisticsDTO {
    /**
     * Statistiques globales de consommation
     */
    private ConsommationGlobaleDTO consommationGlobale;
    
    /**
     * Statistiques par période
     */
    private List<StatistiquePeriodeDTO> statistiquesParPeriode;
    
    /**
     * Statistiques par type de prestation
     */
    private List<StatistiqueTypePrestationDTO> statistiquesParTypePrestation;
    
    /**
     * Top 5 des prestations les plus utilisées
     */
    private List<TopPrestationDTO> topPrestations;
    
    /**
     * Top 5 des prestataires les plus sollicités
     */
    private List<TopPrestataireDTO> topPrestataires;
    
    /**
     * Taux d'utilisation des plafonds
     */
    private TauxUtilisationPlafondDTO tauxUtilisationPlafond;
    
    /**
     * Liste des alertes actives
     */
    private List<AlerteDTO> alertes;
    
    /**
     * Date de génération du rapport
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateGeneration;
    
    /**
     * Code souscripteur
     */
    private String codeSouscripteur;
    
    /**
     * Nom du souscripteur
     */
    private String nomSouscripteur;
    
    /**
     * Période de référence
     */
    private PeriodeDTO periode;
    
    /**
     * DTO pour la consommation globale
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsommationGlobaleDTO {
        private BigDecimal totalDepenses;
        private BigDecimal totalRembourse;
        private BigDecimal totalACharge;
        private Integer nombreConsultations;
        private Integer nombrePrestations;
        private Integer nombreAssuresActifs;
        private Integer nombreAyantsDroitsActifs;
        private BigDecimal moyenneDepenseParAssure;
        private BigDecimal tauxRemboursementMoyen;
    }
    
    /**
     * DTO pour les statistiques par période
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatistiquePeriodeDTO {
        private String periode; // JOUR, SEMAINE, MOIS, ANNEE
        private String libelle; // Ex: "Janvier 2024", "Semaine 15"
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime dateDebut;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime dateFin;
         private BigDecimal montantRembourse;
        private BigDecimal montantTotal;
        private Integer nombreConsultations;
        private Integer nombrePrestations;
    }
    
    /**
     * DTO pour les statistiques par type de prestation
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatistiqueTypePrestationDTO {
        private String typeId;
        private String typeNom;
        private String categorie;
        private BigDecimal montantTotal;
        private BigDecimal montantRembourse;
        private Integer nombreUtilisations;
        private BigDecimal pourcentageTotal;
        private BigDecimal tauxRemboursementMoyen;
    }
    
    /**
     * DTO pour le top des prestations
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopPrestationDTO {
        private Integer rang;
        private String typeId;
        private String typenom;
        private String categorie;
        private Integer nombreUtilisations;
        private BigDecimal montantTotal;
        private BigDecimal pourcentageUtilisation;
    }
    
    /**
     * DTO pour le top des prestataires
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TopPrestataireDTO {
        private Integer rang;
        private String prestataireId;
        private String nomPrestataire;
        private String categoriePrestataire;
        private String ville;
        private Integer nombreVisites;
        private BigDecimal montantTotal;
        private BigDecimal pourcentageUtilisation;
    }
    
    /**
     * DTO pour le taux d'utilisation des plafonds
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TauxUtilisationPlafondDTO {
        private BigDecimal plafondGlobal;
        private BigDecimal montantUtilise;
        private BigDecimal montantRestant;
        private BigDecimal pourcentageUtilisation;
        private List<PlafondParTypeDTO> detailsParType;
    }
    
    /**
     * DTO pour le détail des plafonds par type
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlafondParTypeDTO {
        private String typeId;
        private String typeNom;
        private BigDecimal plafond;
        private BigDecimal montantUtilise;
        private BigDecimal montantRestant;
        private BigDecimal pourcentageUtilisation;
        private String statut; // NORMAL, ATTENTION, CRITIQUE
    }
    
    /**
     * DTO pour les alertes
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlerteDTO {
        private String type; // DEPASSEMENT_PLAFOND, CONSOMMATION_ANORMALE, VISITE_REPETEE
        private String niveau; // INFO, WARNING, CRITICAL
        private String message;
        private String codeAssure;
        private String nomAssure;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime dateDetection;
        private String details;
    }
    
    /**
     * DTO pour la période
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeriodeDTO {
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime dateDebut;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDateTime dateFin;
        private String type; // JOUR, SEMAINE, MOIS, ANNEE, PERSONNALISE
    }
}
