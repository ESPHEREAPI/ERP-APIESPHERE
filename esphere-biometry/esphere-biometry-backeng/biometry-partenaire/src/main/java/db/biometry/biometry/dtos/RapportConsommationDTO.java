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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO pour les rapports de consommation
 * Supporte différents types de rapports avec filtres personnalisables
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
public class RapportConsommationDTO {
    
    /**
     * Informations générales du rapport
     */
    private InformationsRapportDTO informations;
    
    /**
     * Résumé exécutif
     */
    private ResumeExecutifDTO resume;
    
    /**
     * Détails de consommation par adhérent
     */
    private List<ConsommationParAdherentDTO> consommationParAdherent;
    
    /**
     * Détails de consommation par type de prestation
     */
    private List<ConsommationParTypePrestationDTO> consommationParTypePrestation;
    
    /**
     * Détails de consommation par prestataire
     */
    private List<ConsommationParPrestataireDTO> consommationParPrestataire;
    
    /**
     * Analyse des plafonds
     */
    private AnalysePlafondDTO analysePlafond;
    
    /**
     * Tendances et prévisions
     */
    private TendancesDTO tendances;
    
    /**
     * DTO pour les informations générales du rapport
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InformationsRapportDTO {
        private String titre;
        private String typeRapport; // CONSOMMATION_GLOBALE, PAR_PERIODE, PAR_TYPE_PRESTATION, VS_PLAFOND
        private String souscripteur;
        private String nomSouscripteur;
        private String police;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateDebut;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateFin;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime dateGeneration;
        private String generateurPar;
    }
    
    /**
     * DTO pour le résumé exécutif
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResumeExecutifDTO {
        private BigDecimal montantTotalDepense;
        private BigDecimal montantTotalRembourse;
        private BigDecimal montantTotalACharge;
        private BigDecimal tauxRemboursementMoyen;
        private Integer nombreTotalConsultations;
        private Integer nombreTotalPrestations;
        private Integer nombreAssuresConcernes;
        private Integer nombreAyantsDroitsConcernes;
        private BigDecimal depenseMoyenneParAssure;
        private BigDecimal depenseMoyenneParConsultation;
        private List<String> pointsCles;
        private List<String> recommandations;
    }
    
    /**
     * DTO pour la consommation par adhérent
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsommationParAdherentDTO {
        private String codeAdherent;
        private String nomAdherent;
        private String matricule;
        private String groupe;
        private BigDecimal montantDepense;
        private BigDecimal montantRembourse;
        private BigDecimal montantACharge;
        private Integer nombreConsultations;
        private Integer nombrePrestations;
        private BigDecimal plafondAnnuel;
        private BigDecimal montantUtilise;
        private BigDecimal pourcentageUtilisation;
        private Integer nombreAyantsDroits;
        private BigDecimal consommationAyantsDroits;
    }
    
    /**
     * DTO pour la consommation par type de prestation
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsommationParTypePrestationDTO {
        private String typeId;
        private String typeNom;
        private String categorie;
        private BigDecimal montantTotal;
        private BigDecimal montantRembourse;
        private Integer nombreUtilisations;
        private BigDecimal pourcentageTotalDepenses;
        private BigDecimal tauxRemboursementMoyen;
        private BigDecimal plafondCategorie;
        private BigDecimal montantUtilise;
        private BigDecimal pourcentageUtilisationPlafond;
    }
    
    /**
     * DTO pour la consommation par prestataire
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsommationParPrestataireDTO {
        private String prestataireId;
        private String nomPrestataire;
        private String categoriePrestataire;
        private String ville;
        private BigDecimal montantTotal;
        private BigDecimal montantRembourse;
        private Integer nombreVisites;
        private Integer nombreAsssuresDifferents;
        private BigDecimal montantMoyenParVisite;
        private BigDecimal pourcentageTotalDepenses;
    }
    
    /**
     * DTO pour l'analyse des plafonds
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalysePlafondDTO {
        private BigDecimal plafondGlobalAnnuel;
        private BigDecimal montantTotalUtilise;
        private BigDecimal montantRestant;
        private BigDecimal pourcentageUtilisation;
        private String statut; // NORMAL, ATTENTION, CRITIQUE
        private List<PlafondParCategorieDTO> detailsParCategorie;
        private List<AdherentRisqueDTO> adherentsARisque;
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
        private String statut;
    }
    
    /**
     * DTO pour les adhérents à risque
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AdherentRisqueDTO {
        private String codeAdherent;
        private String nomAdherent;
        private BigDecimal plafond;
        private BigDecimal montantUtilise;
        private BigDecimal pourcentageUtilisation;
        private String niveauRisque; // FAIBLE, MOYEN, ELEVE, CRITIQUE
        private String raison;
    }
    
    /**
     * DTO pour les tendances
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TendancesDTO {
        private String tendanceGlobale; // HAUSSE, BAISSE, STABLE
        private BigDecimal variationPourcentage;
        private List<TendanceMensuelleDTO> tendancesMensuelles;
        private List<String> observations;
        private List<String> previsions;
    }
    
    /**
     * DTO pour la tendance mensuelle
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TendanceMensuelleDTO {
        private String mois;
        private BigDecimal montant;
        private BigDecimal variation;
        private String tendance;
    }    
}
