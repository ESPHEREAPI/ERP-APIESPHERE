package db.biometry.biometry.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * DTO du tableau de bord adhérent.
 *
 * Regroupe toutes les informations nécessaires à l'affichage
 * du dashboard personnel d'un adhérent connecté :
 * informations identité, plafonds, consommation, ayants droit,
 * dernières visites et jours restants avant échéance.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardAdherentDTO {

    // ── Identité ──────────────────────────────────────────────────────────────
    private String codeAdherent;
    private String nom;
    private String sexe;
    private String telephone;
    private String matricule;
    private String statut;
    private String police;
    private String souscripteur;
    /** Groupe de l'adhérent (ex : 1, 2, 3…) */
    private Short groupe;
    /** Taux de couverture contractuel (%) */
    private Double taux;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date naissance;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date effetPolice;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date echeancePolice;

    /** Nombre de jours restants avant échéance du contrat (négatif = expiré) */
    private long joursAvantEcheance;

    /** Niveau d'alerte échéance : NORMAL / WARNING (≤30j) / DANGER (≤5j) / EXPIRE */
    private String niveauAlertEcheance;

    // ── Plafonds ──────────────────────────────────────────────────────────────
    private PlafondDTO plafond;

    // ── Consommation année en cours ───────────────────────────────────────────
    private ConsommationDTO consommation;

    // ── Ayants droit ──────────────────────────────────────────────────────────
    private List<AyantDroitDTO> ayantsDroits;
    private int nombreAyantsDroits;

    // ── Dernières visites (paginées) ─────────────────────────────────────────
    private List<VisiteRecenteDTO> dernieresVisites;
    /** Nombre total de visites (toutes pages) — pour la pagination */
    private long totalVisites;
    /** Page courante (0-based) */
    private int pageVisites;
    /** Somme montant base des visites filtrées (toutes pages) */
    private BigDecimal filteredTotalMontantBase;
    /** Somme PEC des visites filtrées (toutes pages) */
    private BigDecimal filteredTotalMontantPEC;

    // ── Sous-objets ───────────────────────────────────────────────────────────

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class PlafondDTO {
        private BigDecimal plafondGlobal;
        private BigDecimal montantConsomme;
        private BigDecimal montantRestant;
        private double pourcentageConsomme;
        /** NORMAL / WARNING / DANGER */
        private String niveau;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ConsommationDTO {
        // ── Totaux ────────────────────────────────────────────────────────────
        private BigDecimal montantTotalPriseEnCharge;
        private BigDecimal montantTotalTicketModerateur;
        private BigDecimal montantTotalDepense;
        /** Part de consommation des ayants droit */
        private BigDecimal montantAyantsDroits;

        // ── Consultation ─────────────────────────────────────────────────────
        private BigDecimal pecConsultation;
        private BigDecimal tmConsultation;
        private int nombreConsultations;

        // ── Examen ───────────────────────────────────────────────────────────
        private BigDecimal pecExamen;
        private BigDecimal tmExamen;
        private int nombreExamens;

        // ── Ordonnance ───────────────────────────────────────────────────────
        private BigDecimal pecOrdonnance;
        private BigDecimal tmOrdonnance;
        private int nombreOrdonnances;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class AyantDroitDTO {
        private String codeAyantDroit;
        private String nom;
        private String sexe;
        private String lienpare;
        private String statut;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private Date naissance;
        private BigDecimal montantConsomme;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class VisiteRecenteDTO {
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private Date date;
        /** Code / identifiant du prestataire */
        private String prestataireId;
        /** Nom lisible du prestataire */
        private String nomPrestataire;
        /** consultation | examen | ordonnance */
        private String typePrestation;
        private BigDecimal montant;
        private BigDecimal montantPriseEnCharge;
        private BigDecimal montantTicketModerateur;
        private String etat;
        /** true = visite d'un ayant droit */
        private boolean ayantDroit;
        private String nomBeneficiaire;
        /** Taux de couverture (%) de cette prestation */
        private Double taux;
    }
}