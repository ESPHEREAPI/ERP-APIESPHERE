/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.dtos.DashboardStatisticsDTO;
import db.biometry.biometry.dtos.StatistiqueTypePrestationDTO;
import db.biometry.biometry.dtos.StatistiquesGlobalesDTO;
import db.biometry.biometry.dtos.StatistiquesParPeriodeDTO;
import db.biometry.biometry.dtos.TopPrestationDTO;
import db.biometry.biometry.entite.Dbx45tyConsultation;
import db.biometry.biometry.entite.Dbx45tyVisite;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour la gestion des consultations Requêtes optimisées pour le
 * tableau de bord et les statistiques
 *
 * @author JIATOU FRANCK
 * @version 1.0 y /**
 *
 * @author USER01
 */
@Repository
public interface ConsultationRepository extends JpaRepository<Dbx45tyConsultation, Integer> {

    public Dbx45tyConsultation findByVisiteId(Dbx45tyVisite visiteid);

    //lister les consultation pour une periode precis
    @Query(" SELECT c FROM Dbx45tyConsultation c WHERE c.date BETWEEN :debut and :fin  and c.taux IS NOT NULL")
    public List<Dbx45tyConsultation> listeeBonsConsultation(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2);

    // nombre de prestation pour 
    @Query("SELECT  COUNT(c) FROM Dbx45tyConsultation c WHERE c.date BETWEEN :debut and :fin   and c.visiteId.prestataireId= :prestataireId and c.taux IS NOT NULL ")
    public Long nombreBonsbyPrestataire(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2, @Param(value = "prestataireId") String prestataireId);

    // liste les consultation d un dherent pour une periode precis
    //lister les consultation pour une periode precis
    @Query(" SELECT c FROM Dbx45tyConsultation c WHERE c.date BETWEEN :debut and :fin and c.visiteId.codeAdherent= :codeAdherent and c.taux IS NOT NULL")
    public List<Dbx45tyConsultation> listeeBonsConsultationByAdherent(@Param(value = "debut") Date date1, @Param(value = "fin") Date date2, @Param(value = "codeAdherent") String codeAdherent);

    /**
     * Statistiques de consommation globale pour un souscripteur
     */
    /**
     * Statistiques de consommation globale pour un souscripteur
     */
    //@Query("SELECT c  FROM Dbx45tyConsultation c inner join Dbx45tyVisite vi on c.visiteId=vi.id inner join Dbx45tyAdherent ad on vi.codeAdherent.codeAdherent=ad.codeAdherent where ad.souscripteur= :souscripteur")
    @Query("SELECT new db.biometry.biometry.dtos.StatistiquesGlobalesDTO("
            + "COUNT(c), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN c.taux = 100 THEN 0 "
            + "    WHEN c.taux < 100 THEN ((100 - c.taux) * c.montantModif / 100) "
            + "    ELSE 0 "
            + "  END"
            + "), 0), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN c.taux = 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montantModif END "
            + "    WHEN c.taux < 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN (c.taux * c.montantModif / 100) ELSE (c.taux * c.montant / 100) END "
            + "    ELSE "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END "
            + "  END"
            + "), 0), "
            + "COALESCE(AVG(c.montant), 0)) "
            + "FROM Dbx45tyConsultation c "
            + "INNER JOIN c.visiteId vi "
            + "INNER JOIN vi.codeAdherent ad "
            + "WHERE ad.souscripteur = :souscripteur "
            + "AND c.date BETWEEN :dateDebut AND :dateFin "
            + "AND c.etatConsultation = :encaisse")
    StatistiquesGlobalesDTO getStatistiquesGlobales(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("encaisse") String etatprestation);

    /**
     * Statistiques par type de prestation
     */
    @Query("SELECT new db.biometry.biometry.dtos.StatistiqueTypePrestationDTO("
            + "c.id, "
            + "c.typeConsultation.categorie, "
            + "c.typeConsultation.categorie, "
            + "COUNT(c), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN c.taux = 100 THEN 0 "
            + "    WHEN c.taux < 100 THEN ((100 - c.taux) * c.montantModif / 100) "
            + "    ELSE 0 "
            + "  END"
            + "), 0), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN c.taux = 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END "
            + "    WHEN c.taux < 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN (c.taux * c.montantModif / 100) ELSE (c.taux * c.montant / 100) END "
            + "    ELSE "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END "
            + "  END"
            + "), 0)) "
            + "FROM Dbx45tyConsultation c "
            + "INNER JOIN c.typeConsultation tp "
            + " INNER JOIN c.visiteId vi "
            + " INNER JOIN vi.codeAdherent ad "
            + "WHERE ad.souscripteur = :souscripteur "
            + "AND c.date BETWEEN :dateDebut AND :dateFin "
            + "AND c.etatConsultation = :encaisse "
            + "GROUP BY c.id, tp.nom, tp.categorie "
            + "ORDER BY COALESCE(SUM("
            + "  CASE "
            + "    WHEN c.taux = 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END "
            + "    WHEN c.taux < 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN (c.taux * c.montantModif / 100) ELSE (c.taux * c.montant / 100) END "
            + "    ELSE "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END "
            + "  END"
            + "), 0) DESC")
    List<StatistiqueTypePrestationDTO> getStatistiquesParTypePrestation(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("encaisse") String etatPrestation);

    /**
     * Top 5 des types de prestation les plus utilisés
     */
    @Query("SELECT new db.biometry.biometry.dtos.TopPrestationDTO("
            + "c.id, "
            + "adh.assurePrincipal, "
            + "adh.assurePrincipal, "
            + "COUNT(c), "
            + "COALESCE(SUM("
            + "CASE"
            + "    WHEN c.taux = 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END "
            + "    WHEN c.taux < 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN (c.taux * c.montantModif / 100) ELSE (c.taux * c.montant / 100) END "
            + "    ELSE "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END "
            + "  END"
            + "), 0)) "
            + "FROM Dbx45tyConsultation c "
            + "INNER JOIN c.typeConsultation tp "
            + "INNER JOIN c.visiteId v "
            + "INNER JOIN v.codeAdherent adh "
            + "WHERE adh.souscripteur = :souscripteur "
             + "AND c.etatConsultation = :encaisse "
            + "AND c.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY c.id, tp.nom, tp.categorie "
            + "ORDER BY COUNT(c) DESC")
    List<TopPrestationDTO> getTopPrestations(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
             @Param("encaisse") String etatPrestation);

    /**
     * Statistiques par période (jour, semaine, mois, année)
     */
    @Query("SELECT new db.biometry.biometry.dtos.StatistiquesParPeriodeDTO("
            + "c.date, "
            + "COUNT(c), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN c.taux = 100 THEN 0 "
            + "    WHEN c.taux < 100 THEN ((100 - c.taux) * c.montantModif / 100) "
            + "    ELSE 0 "
            + "  END"
            + "), 0), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN c.taux = 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END "
            + "    WHEN c.taux < 100 THEN "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN (c.taux * c.montantModif / 100) ELSE (c.taux * c.montant / 100) END "
            + "    ELSE "
            + "      CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END "
            + "  END"
            + "), 0)) "
            + "FROM Dbx45tyConsultation c "
            + "INNER JOIN c.visiteId vi "
            + "INNER JOIN vi.codeAdherent ad "
            + "WHERE ad.souscripteur = :souscripteur "
            + "AND c.etatConsultation = :encaisse "
            + "AND c.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY DATE(c.date) "
            + "ORDER BY DATE(c.date)")
    List<StatistiquesParPeriodeDTO> getStatistiquesParPeriode(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("format") String format,
            @Param("encaisse") String etatPrestation);

    /**
     * Consommation par adhérent
     */
    @Query("SELECT "
            + "a.codeAdherent, "
            + "a.assurePrincipal, "
            + "a.matricule, "
            + "COUNT(c), "
            + "COALESCE(SUM(c.montant), 0), "
            + "COALESCE(SUM(CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END), 0) "
            + "FROM Dbx45tyConsultation c "
            + "JOIN c.visiteId v "
            + "JOIN v.codeAdherent benef "
            + "JOIN Dbx45tyAdherent a ON a.codeAdherent = benef.codeAdherent "
            + "WHERE a.souscripteur = :souscripteur "
            + "AND c.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY a.codeAdherent, a.assurePrincipal, a.matricule "
            + "ORDER BY SUM(c.montant) DESC")
    List<Object[]> getConsommationParAdherent(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Historique des consultations pour un adhérent ou ayant droit
     */
    @Query("SELECT c FROM Dbx45tyConsultation c "
            + "JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeBeneficiaire "
            + "ORDER BY c.date DESC")
    List<Dbx45tyConsultation> findConsultationsByBeneficiaire(@Param("codeBeneficiaire") String codeBeneficiaire);

    /**
     * Détection d'anomalies - Consultations répétées
     */
    @Query("SELECT "
            + "v.codeAdherent.codeAdherent, "
            + "COUNT(c) "
            + "FROM Dbx45tyConsultation c "
            + "JOIN c.visiteId v "
            + "JOIN v.codeAdherent benef "
            + "WHERE benef.souscripteur = :souscripteur "
            + "AND c.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY v.codeAdherent.codeAdherent "
            + "HAVING COUNT(c) >= :seuil "
            + "ORDER BY COUNT(c) DESC")
    List<Object[]> detectConsultationsRepetees(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("seuil") Integer seuil);

    /**
     * Montant total des consultations validées pour un adhérent
     */
//    @Query("SELECT COALESCE(SUM(CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END * (c.taux / 100)), 0) "
//            + "FROM Dbx45tyConsultation c "
//            + "JOIN c.visiteId v "
//            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
//            + "AND c.etatConsultation = 'encaisse' "
//            + "AND c.date BETWEEN :dateDebut AND :dateFin")
//    BigDecimal getMontantRemboursePourAdherent(
//            @Param("codeAdherent") String codeAdherent,
//            @Param("dateDebut") LocalDateTime dateDebut,
//            @Param("dateFin") LocalDateTime dateFin);

//    @Query("SELECT c FROM Dbx45tyConsultation c WHERE c.visiteId.codeAdherent.codeAdherent= :codeAdherent and c.date BETWEEN :dateDebut and :dateFin")
//    List<Dbx45tyConsultation> listeConsulTationDetailAdherent(@Param("codeAdherent") String codeAdherent,
//            @Param("dateDebut") LocalDateTime dateDebut,
//            @Param("dateFin") LocalDateTime dateFin);
    
    /**
     * PEC consultation pour un adhérent (taux * montantModif / 100)
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END * (c.taux / 100)), 0) "
            + "FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND c.etatConsultation = 'encaisse' "
            + "AND c.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getMontantRemboursePourAdherent(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * Ticket modérateur consultation pour un adhérent ((100-taux) * montantModif / 100)
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN c.taux = 100 THEN 0 "
            + "ELSE (CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END) * ((100 - c.taux) / 100) END), 0) "
            + "FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND c.etatConsultation = 'encaisse' "
            + "AND c.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getTicketModerateurPourAdherent(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * Nombre de consultations pour un adhérent sur une période
     */
    @Query("SELECT COUNT(c) FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND c.etatConsultation = 'encaisse' "
            + "AND c.date BETWEEN :dateDebut AND :dateFin")
    int countByAdherentAndDateBetween(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * PEC consultations des ayants droit d'un adhérent
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END * (c.taux / 100)), 0) "
            + "FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND v.codeAyantDroit IS NOT NULL "
            + "AND c.etatConsultation = 'encaisse' "
            + "AND c.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getMontantRembourseAyantsDroitsPourAdherent(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * PEC consultations d'un ayant droit spécifique
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END * (c.taux / 100)), 0) "
            + "FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAyantDroit = :codeAyantDroit "
            + "AND c.etatConsultation = 'encaisse' "
            + "AND c.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getMontantRembourseParAyantDroit(
            @Param("codeAyantDroit") String codeAyantDroit,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * Dernières visites (consultations) d'un adhérent — adhérent + ayants droit
     */
    /**
     * Dernières consultations encaissées d'un adhérent — historique paginé.
     * Règle métier : etat = 'encaisse' (cohérent avec examen/ordonnance).
     */
    @Query("SELECT c FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND c.etatConsultation = 'encaisse' "
            + "ORDER BY c.date DESC")
    List<Dbx45tyConsultation> findDernieresVisitesByAdherent(
            @Param("codeAdherent") String codeAdherent,
            org.springframework.data.domain.Pageable pageable);
 
    @Query("SELECT c FROM Dbx45tyConsultation c WHERE c.visiteId.codeAdherent.codeAdherent = :codeAdherent "
            + "AND c.date BETWEEN :dateDebut AND :dateFin")
    List<Dbx45tyConsultation> listeConsulTationDetailAdherent(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Nombre total de consultations encaissées pour un adhérent (pour pagination).
     * Cohérent avec findDernieresVisitesByAdherent qui filtre aussi sur etat = 'encaisse'.
     */
    @Query("SELECT COUNT(c) FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND c.etatConsultation = 'encaisse'")
    long countTotalConsultationsParAdherent(@Param("codeAdherent") String codeAdherent);

    // ── Requêtes filtrées pour la recherche dans l'historique des visites ─────

    @Query("SELECT c FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND c.etatConsultation = 'encaisse' "
            + "AND (:prestataireId IS NULL OR v.prestataireId = :prestataireId) "
            + "AND (:dateDebut IS NULL OR c.date >= :dateDebut) "
            + "AND (:dateFin IS NULL OR c.date <= :dateFin) "
            + "AND (:codeAyantDroit IS NULL OR v.codeAyantDroit = :codeAyantDroit) "
            + "ORDER BY c.date DESC")
    List<Dbx45tyConsultation> findDernieresVisitesFiltrees(
            @Param("codeAdherent") String codeAdherent,
            @Param("prestataireId") String prestataireId,
            @Param("dateDebut") java.time.LocalDateTime dateDebut,
            @Param("dateFin") java.time.LocalDateTime dateFin,
            @Param("codeAyantDroit") String codeAyantDroit,
            org.springframework.data.domain.Pageable pageable);

    @Query("SELECT COUNT(c) FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND c.etatConsultation = 'encaisse' "
            + "AND (:prestataireId IS NULL OR v.prestataireId = :prestataireId) "
            + "AND (:dateDebut IS NULL OR c.date >= :dateDebut) "
            + "AND (:dateFin IS NULL OR c.date <= :dateFin) "
            + "AND (:codeAyantDroit IS NULL OR v.codeAyantDroit = :codeAyantDroit)")
    long countConsultationsFiltrees(
            @Param("codeAdherent") String codeAdherent,
            @Param("prestataireId") String prestataireId,
            @Param("dateDebut") java.time.LocalDateTime dateDebut,
            @Param("dateFin") java.time.LocalDateTime dateFin,
            @Param("codeAyantDroit") String codeAyantDroit);

    /** Somme montant base des consultations filtrées (toutes pages — pour affichage total) */
    @Query("SELECT COALESCE(SUM(CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END), 0) "
            + "FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND c.etatConsultation = 'encaisse' "
            + "AND (:prestataireId IS NULL OR v.prestataireId = :prestataireId) "
            + "AND (:dateDebut IS NULL OR c.date >= :dateDebut) "
            + "AND (:dateFin IS NULL OR c.date <= :dateFin) "
            + "AND (:codeAyantDroit IS NULL OR v.codeAyantDroit = :codeAyantDroit)")
    BigDecimal sumMontantBaseConsultationsFiltrees(
            @Param("codeAdherent") String codeAdherent,
            @Param("prestataireId") String prestataireId,
            @Param("dateDebut") java.time.LocalDateTime dateDebut,
            @Param("dateFin") java.time.LocalDateTime dateFin,
            @Param("codeAyantDroit") String codeAyantDroit);

    /** Somme PEC des consultations filtrées (toutes pages — pour affichage total) */
    @Query("SELECT COALESCE(SUM(CASE WHEN c.montantModif IS NOT NULL THEN c.montantModif ELSE c.montant END * (c.taux / 100)), 0) "
            + "FROM Dbx45tyConsultation c JOIN c.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND c.etatConsultation = 'encaisse' "
            + "AND (:prestataireId IS NULL OR v.prestataireId = :prestataireId) "
            + "AND (:dateDebut IS NULL OR c.date >= :dateDebut) "
            + "AND (:dateFin IS NULL OR c.date <= :dateFin) "
            + "AND (:codeAyantDroit IS NULL OR v.codeAyantDroit = :codeAyantDroit)")
    BigDecimal sumPECConsultationsFiltrees(
            @Param("codeAdherent") String codeAdherent,
            @Param("prestataireId") String prestataireId,
            @Param("dateDebut") java.time.LocalDateTime dateDebut,
            @Param("dateFin") java.time.LocalDateTime dateFin,
            @Param("codeAyantDroit") String codeAyantDroit);
}
