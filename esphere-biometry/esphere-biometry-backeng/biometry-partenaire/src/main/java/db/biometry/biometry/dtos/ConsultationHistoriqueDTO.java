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
 * DTO pour l'historique des consultations et prestations d'un adhérent
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
public class ConsultationHistoriqueDTO {
    
    /**
     * ID de la consultation
     */
    private Integer id;
    
    /**
     * Code de l'adhérent ou ayant droit
     */
    private String codeBeneficiaire;
    
    /**
     * Nom du bénéficiaire
     */
    private String nomBeneficiaire;
    
    /**
     * Type de bénéficiaire (ADHERENT, AYANT_DROIT)
     */
    private String typeBeneficiaire;
    
    /**
     * Date de la consultation
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateConsultation;
    
    /**
     * Nature de la consultation
     */
    private String natureConsultation;
    
    /**
     * Type de consultation
     */
    private TypeConsultationDTO typeConsultation;
    
    /**
     * Nature de l'affection
     */
    private String natureAffection;
    
    /**
     * Montant initial
     */
    private BigDecimal montant;
    
    /**
     * Montant modifié (après validation/rejet)
     */
    private BigDecimal montantModifie;
    
    /**
     * Taux de remboursement appliqué
     */
    private Double taux;
    
    /**
     * Montant remboursé
     */
    private BigDecimal montantRembourse;
    
    /**
     * Montant à charge de l'assuré
     */
    private BigDecimal montantACharge;
    
    /**
     * État de la consultation (EN_ATTENTE, VALIDEE, REJETEE, PAYEE)
     */
    private String etatConsultation;
    
    /**
     * Date de validation/rejet
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateValidationRejet;
    
    /**
     * Employé ayant validé/rejeté
     */
    private String employeValidateur;
    
    /**
     * Observations
     */
    private String observations;
    
    /**
     * Informations sur le prestataire
     */
    private PrestataireSimpleDTO prestataire;
    
    /**
     * Liste des prestations associées
     */
    private List<LignePrestationDTO> prestations;
    
    /**
     * Supprimé (O/N)
     */
    private String supprime;
    
    /**
     * DTO pour le type de consultation
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TypeConsultationDTO {
        private String id;
        private String nom;
        private String categorie;
    }
    
    /**
     * DTO pour les informations du prestataire
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PrestataireSimpleDTO {
        private String id;
        private String nom;
        private String categorie;
        private String adresse;
        private String telephone;
        private String ville;
    }
    
    /**
     * DTO pour une ligne de prestation
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LignePrestationDTO {
        private Integer id;
        private String typePrestation;
        private String nomPrestation;
        private String codification;
        private Double quantite;
        private BigDecimal prixUnitaire;
        private BigDecimal montantTotal;
        private BigDecimal montantRembourse;
        private Double tauxRemboursement;
        private String etat;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime date;
        private String observations;
    }
    
}
