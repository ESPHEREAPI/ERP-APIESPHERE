/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.dtos.PrestationPrestataireDTO;
import db.biometry.biometry.dtos.StatistiqueTypePrestationDTO;
import db.biometry.biometry.dtos.StatistiquesGlobalesDTO;
import db.biometry.biometry.dtos.StatistiquesParPeriodeDTO;
import db.biometry.biometry.dtos.TopPrestationDTO;
import db.biometry.biometry.entite.Dbx45tyLignePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour la gestion des lignes de prestation Requêtes pour le détail
 * des prestations médicales
 *
 * @author JIATOU FRANCK
 * @version 1.0
 */
@Repository

/**
 *
 * @author USER01
 */
public interface LignePrestationRepository extends JpaRepository<Dbx45tyLignePrestation, Integer> {

//    /*
//    lister les beneficiaires pour type de prestation examen
//     */
//    List<LignePrestation> findByCodeBeneficiaireIsNotNullAndCodeTypePrestationIsNotNull();
//
//    /*
//    lister les beneficiaires pour type de prestation Ordonance
//     */
//    List<LignePrestation> findByCodeBeneficiaireIsNotNullAndCodeTypePrestationIsNull();
//
//    /*
//    lister les adherents pour type de prestation Examen,
//     */
//    List<LignePrestation> findByCodeAssurePrincipalIsNotNullAndCodeTypePrestationIsNotNull();
//
//    /*
//    lister les adherents pour type de prestation Ordonance/paharmacie
//     */
//    List<LignePrestation> findByCodeAssurePrincipalIsNotNullAndCodeTypePrestationIsNull();
//
//    /*
//       on liste les ligne de prestation par prestataire, assure principal et date de facon distincte
//     */
//    @Query("SELECT DISTINCT l.codePrestataire,l.codeAssurePrincipal,DATE_FORMAT ( l.date , '%Y-%m-01' )  as date,l.codeTypePrestation FROM LignePrestation l ")
//    List<Object>
//            listePrestataireDistinct();
//
//    List<LignePrestation> findByCodePrestataire(String codePrestataire);
//
//    @Query("SELECT SUM (l.montant) FROM LignePrestation l WHERE l.codeAssurePrincipal= :ap and l.codePrestataire= :cp and DATE_FORMAT ( l.date , '%Y-%m-01' )= DATE_FORMAT ( :date , '%Y-%m-01' ) and l.codeTypePrestation= :tp")
//    public Double sommeLignePrestation(@Param(value = "ap") String ap, @Param(value = "cp") String cp, @Param(value = "date") Date date, @Param(value = "tp") String tp);
//
//    @Query("SELECT DISTINCT (l.idPrestation) FROM LignePrestation l")
//    public List<Integer> listesPrestionId();
//    public List<LignePrestation> findByIdPrestation(Integer idPrestation);
    /**
     * Liste des prestations par consultation
     */
    @Query("SELECT lp FROM Dbx45tyLignePrestation lp "
            + "WHERE lp.prestationId.id = :prestationId "
            + "AND lp.supprime = 'N' "
            + "ORDER BY lp.date DESC")
    List<Dbx45tyLignePrestation> findByPrestationId(@Param("prestationId") Integer prestationId);

    /**
     * Statistiques des prestations par type
     */
    @Query("SELECT "
            + "COALESCE(lp.typeExamen.id, lp.descriptionSoins.id, lp.medicamentId.nom), "
            + "COALESCE(lp.typeExamen.nom, lp.descriptionSoins.nom, lp.medicamentId.nom), "
            + "COUNT(lp), "
            + "COALESCE(SUM(lp.valeur * lp.nbre), 0), "
            + "COALESCE(SUM(CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END * CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END), 0) "
            + "FROM Dbx45tyLignePrestation lp "
            + "JOIN lp.prestationId p "
            + "JOIN p.visiteId v "
            + "JOIN v.codeAdherent benef "
            + "WHERE benef.souscripteur = :souscripteur "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY COALESCE(lp.typeExamen.id, lp.descriptionSoins.id, lp.medicamentId.nom), "
            + "COALESCE(lp.typeExamen.nom, lp.descriptionSoins.nom, lp.medicamentId.nom) "
            + "ORDER BY SUM(lp.valeur * lp.nbre) DESC")
    List<Object[]> getStatistiquesParType(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    @Query("SELECT new db.biometry.biometry.dtos.StatistiqueTypePrestationDTO("
            + "lp.id, "
            + "pr.naturePrestation, "
            + "pr.naturePrestation, "
            + "COUNT(lp), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN lp.taux = 100 THEN 0 "
            + "    WHEN lp.taux < 100 THEN ((100 - lp.taux) * (lp.valeurModif * lp.nbreModif) / 100) "
            + "    ELSE 0 "
            + "  END"
            + "), 0), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN lp.taux = 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeurModif * lp.nbreModif) END "
            + "    WHEN lp.taux < 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.taux * (lp.valeurModif * lp.nbreModif) / 100) ELSE (lp.taux * (lp.valeurModif * lp.nbreModif) / 100) END "
            + "    ELSE "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeurModif * lp.nbreModif) END "
            + "  END"
            + "), 0)) "
            + "FROM Dbx45tyLignePrestation lp "
            + " INNER JOIN lp. prestationId pr "
            + " INNER JOIN pr.visiteId vi "
            + " INNER JOIN vi.codeAdherent ad "
            + "WHERE ad.souscripteur = :souscripteur "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin "
            + "AND lp.etat = :encaisse "
            + "GROUP BY lp.id, pr.naturePrestation "
            + "ORDER BY COALESCE(SUM("
            + "  CASE "
            + "    WHEN lp.taux = 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeurModif * lp.nbreModif) END "
            + "    WHEN lp.taux < 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.taux * (lp.valeurModif * lp.nbreModif) / 100) ELSE (lp.taux * (lp.valeurModif * lp.nbreModif) / 100) END "
            + "    ELSE "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeurModif * lp.nbreModif) END "
            + "  END"
            + "), 0) DESC")
    List<StatistiqueTypePrestationDTO> getStatistiquesParTypePrestation(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("encaisse") String etatPrestation);
//@Query(" SELECT pr  WHERE pr.visiteId.codeAdherent.souscripteur= :souscripteur")

    /**
     * Top prestations par fréquence d'utilisation
     */
    @Query("SELECT "
            + "COALESCE(lp.typeExamen.nom, lp.descriptionSoins.nom, lp.medicamentId.nom), "
            + "COUNT(lp), "
            + "COALESCE(SUM(lp.valeur * lp.nbre), 0) "
            + "FROM Dbx45tyLignePrestation lp "
            + "JOIN lp.prestationId p "
            + "JOIN p.visiteId v "
            + "JOIN v.codeAdherent benef "
            + "WHERE benef.souscripteur = :souscripteur "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY COALESCE(lp.typeExamen.nom, lp.descriptionSoins.nom, lp.medicamentId.nom) "
            + "ORDER BY COUNT(lp) DESC")
    List<Object[]> getTopPrestationsParUtilisation(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Montant total des prestations pour un adhérent
     */
//    @Query("SELECT COALESCE(SUM(CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END * "
//            + "CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END * (lp.taux / 100)), 0) "
//            + "FROM Dbx45tyLignePrestation lp "
//            + "JOIN lp.prestationId p "
//            + "JOIN p.visiteId v "
//            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
//            + "AND lp.etat = 'encaisse' "
//            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
//    BigDecimal getMontantRemboursePourAdherent(
//            @Param("codeAdherent") String codeAdherent,
//            @Param("dateDebut") LocalDateTime dateDebut,
//            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Nombre total de prestations pour un souscripteur
     */
    @Query("SELECT COUNT(pr) FROM  Dbx45tyPrestation pr inner join  Dbx45tyLignePrestation lp on pr.id=lp.prestationId.id  where pr.visiteId.codeAdherent.souscripteur= :souscripteur AND lp.date BETWEEN :dateDebut AND :dateFin and lp.etat= :encaisse ")

    Long countPrestationsBySouscripteur(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("encaisse") String etatPrestation);

    /**
     * Prestations par prestataire
     */
    @Query("SELECT new db.biometry.biometry.dtos.PrestationPrestataireDTO("
            + "lp.prestataireId.id, "
            + "lp.prestataireId.nom, "
            + "lp.prestataireId.categorieId.nom, "
            + "COUNT(lp.prestationId.visiteId), "
            + "COALESCE(SUM(lp.valeur * lp.nbre), 0)) "
            + "FROM Dbx45tyLignePrestation lp "
            + "WHERE lp.prestationId.visiteId.codeAdherent.souscripteur = :souscripteur "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY lp.prestataireId.id,lp.prestataireId.nom, lp.prestataireId.categorieId.nom "
            + "ORDER BY SUM(lp.valeur * lp.nbre) DESC")
    List<PrestationPrestataireDTO> getPrestationsParPrestataire(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    @Query("select lp FROM Dbx45tyLignePrestation lp WHERE lp.prestationId.visiteId.codeAdherent.codeAdherent= :codeAdherent and lp.date BETWEEN :dateDebut and  :dateFin")
    List<Dbx45tyLignePrestation> listeDeatailPrestation(@Param("codeAdherent") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    //statistique global des prestation par periode 
    @Query("SELECT new db.biometry.biometry.dtos.StatistiquesGlobalesDTO("
            + "COUNT(lp.prestationId.visiteId), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN lp.taux = 100 THEN 0 "
            + "    WHEN lp.taux < 100 THEN ((100 - lp.taux) * (lp.valeurModif * lp.nbreModif) / 100) "
            + "    ELSE 0 "
            + "  END"
            + "), 0), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN lp.taux = 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeurModif * lp.nbreModif) END "
            + "    WHEN lp.taux < 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.taux *(lp.valeurModif * lp.nbreModif)/ 100) ELSE (lp.taux * (lp.valeurModif * lp.nbreModif) / 100) END "
            + "    ELSE "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeurModif * lp.nbreModif) END "
            + "  END"
            + "), 0), "
            + "COALESCE(AVG(lp.valeurModif), 0)) "
            + "FROM Dbx45tyLignePrestation lp WHERE lp.prestationId.visiteId.codeAdherent.souscripteur= :souscripteur and lp.date BETWEEN :dateDebut and  :dateFin"
            + " AND lp.etat = :encaisse")
    StatistiquesGlobalesDTO getStatistiquesGlobales(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("encaisse") String etatprestation);

    @Query("SELECT new db.biometry.biometry.dtos.StatistiquesParPeriodeDTO("
            + "lp.date, "
            + "COUNT(pr.visiteId), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN lp.taux = 100 THEN 0 "
            + "    WHEN lp.taux < 100 THEN ((100 - lp.taux) * (lp.valeurModif * lp.nbreModif) / 100) "
            + "    ELSE 0 "
            + "  END"
            + "), 0), "
            + "COALESCE(SUM("
            + "  CASE "
            + "    WHEN lp.taux = 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeur * lp.nbre) END "
            + "    WHEN lp.taux < 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.taux * (lp.valeurModif * lp.nbreModif) / 100) ELSE (lp.taux * (lp.valeur * lp.nbre) / 100) END "
            + "    ELSE "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeur * lp.nbre) END "
            + "  END"
            + "), 0)) "
            + "FROM Dbx45tyLignePrestation lp "
            + "INNER JOIN lp.prestationId pr "
            + "INNER JOIN pr.visiteId vi "
            + "INNER JOIN vi.codeAdherent ad "
            + "WHERE ad.souscripteur = :souscripteur "
            + "AND lp.etat = :encaisse "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY DATE(lp.date) "
            + "ORDER BY DATE(lp.date)")
    List<StatistiquesParPeriodeDTO> getStatistiquesParPeriode(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("format") String format,
            @Param("encaisse") String etatPrestation);
    
    

    @Query("SELECT new db.biometry.biometry.dtos.TopPrestationDTO("
            + "lp.id, "
            + "ad.assurePrincipal , "
            + "ad.assurePrincipal, "
            + "COUNT(pr.visiteId), "
            + "COALESCE(SUM("
            + "CASE"
            + "    WHEN lp.taux = 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeur * lp.nbre) END "
            + "    WHEN lp.taux < 100 THEN "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.taux * (lp.valeurModif * lp.nbreModif) / 100) ELSE (lp.taux * (lp.valeur * lp.nbre) / 100) END "
            + "    ELSE "
            + "      CASE WHEN lp.valeurModif IS NOT NULL THEN (lp.valeurModif * lp.nbreModif) ELSE (lp.valeur * lp.nbre) END "
            + "  END"
            + "), 0)) "
            + "FROM Dbx45tyLignePrestation lp "
            + "INNER JOIN lp.prestationId pr "
            + "INNER JOIN pr.visiteId vi "
            + "INNER JOIN vi.codeAdherent ad "
            + "WHERE ad.souscripteur = :souscripteur "
            + "AND lp.etat = :encaisse "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin "
            + "GROUP BY lp.id, pr.naturePrestation   ORDER BY COUNT(DISTINCT vi.id) DESC "
           
    )
    List<TopPrestationDTO> getTopPrestations(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            @Param("encaisse") String encaisse);

    //nombre total de consultationm
//    @Query("SELECT COUNT(pr.visiteId) FROM Dbx45tyLignePrestation lp "
//            + "INNER JOIN lp.prestationId pr "
//            + "INNER JOIN pr.visiteId vi"
//            + "INNER JOIN vi.codeAdherent ad "
//            + "WHERE ad.souscripteur = :souscripteur "
//            + "AND lp.etat = :encaisse "
//            + "AND c.date BETWEEN :dateDebut AND :dateFin "
//    )
//     Long nombrePrestationForSouscripteur(
//            @Param("souscripteur") String souscripteur,
//            @Param("dateDebut") LocalDateTime dateDebut,
//            @Param("dateFin") LocalDateTime dateFin,
//            @Param("encaisse") String encaisse);

 // ── Nouvelles requêtes pour le dashboard adhérent ─────────────────────────
 
    /**
     * PEC prestations (examens + ordonnances) pour un adhérent
     * Formule : valeurModif * nbreModif * (taux / 100)
     */
    @Query("SELECT COALESCE(SUM("
            + "CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END "
            + "* CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END "
            + "* (lp.taux / 100)), 0) "
            + "FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND lp.etat = 'encaisse' "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getMontantRemboursePourAdherent(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * Ticket modérateur prestations pour un adhérent
     * Formule : valeurModif * nbreModif * ((100 - taux) / 100)
     */
    @Query("SELECT COALESCE(SUM("
            + "CASE WHEN lp.taux = 100 THEN 0 ELSE "
            + "CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END "
            + "* CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END "
            + "* ((100 - lp.taux) / 100) END), 0) "
            + "FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND lp.etat = 'encaisse' "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getTicketModerateurPourAdherent(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * PEC prestations par type (examen / ordonnance) pour un adhérent
     */
    @Query("SELECT COALESCE(SUM("
            + "CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END "
            + "* CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END "
            + "* (lp.taux / 100)), 0) "
            + "FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND lp.etat = 'encaisse' "
            + "AND p.naturePrestation = :naturePrestation "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getMontantRembourseParNature(
            @Param("codeAdherent") String codeAdherent,
            @Param("naturePrestation") String naturePrestation,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * Nombre de prestations pour un adhérent sur une période
     */
    @Query("SELECT COUNT(lp) FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND lp.etat = 'encaisse' "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
    int countByAdherentAndDateBetween(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * PEC prestations des ayants droit d'un adhérent
     */
    @Query("SELECT COALESCE(SUM("
            + "CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END "
            + "* CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END "
            + "* (lp.taux / 100)), 0) "
            + "FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND v.codeAyantDroit IS NOT NULL "
            + "AND lp.etat = 'encaisse' "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getMontantRembourseAyantsDroitsPourAdherent(
            @Param("codeAdherent") String codeAdherent,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);
 
    /**
     * PEC prestations d'un ayant droit spécifique
     */
    @Query("SELECT COALESCE(SUM("
            + "CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END "
            + "* CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END "
            + "* (lp.taux / 100)), 0) "
            + "FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAyantDroit = :codeAyantDroit AND lp.etat = 'encaisse' "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getMontantRembourseParAyantDroit(
            @Param("codeAyantDroit") String codeAyantDroit,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    // ── Nouvelles requêtes dashboard adhérent — détail par type ──────────────

    /**
     * Ticket modérateur pour un adhérent filtré par nature de prestation
     * Formule : valeurModif * nbreModif * ((100-taux)/100) si taux < 100, sinon 0
     */
    @Query("SELECT COALESCE(SUM(CASE WHEN lp.taux = 100 THEN 0 ELSE "
            + "CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END "
            + "* CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END "
            + "* ((100 - lp.taux) / 100) END), 0) "
            + "FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND lp.etat = 'encaisse' "
            + "AND p.naturePrestation = :naturePrestation "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
    BigDecimal getTMParNature(
            @Param("codeAdherent") String codeAdherent,
            @Param("naturePrestation") String naturePrestation,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Nombre de lignes de prestation pour un adhérent par nature et période
     */
    @Query("SELECT COUNT(lp) FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND lp.etat = 'encaisse' "
            + "AND p.naturePrestation = :naturePrestation "
            + "AND lp.date BETWEEN :dateDebut AND :dateFin")
    int countByAdherentNatureAndDate(
            @Param("codeAdherent") String codeAdherent,
            @Param("naturePrestation") String naturePrestation,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin);

    /**
     * Dernières lignes de prestation encaissées d'un adhérent (examen + ordonnance) — historique paginé.
     * Règle métier : seules les lignes avec etat = 'encaisse' sont affichées.
     * Inclut les visites des ayants droit appartenant à cet adhérent.
     */
    @Query("SELECT lp FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND lp.etat = 'encaisse' "
            + "ORDER BY lp.date DESC")
    List<Dbx45tyLignePrestation> findDernieresLignesParAdherent(
            @Param("codeAdherent") String codeAdherent,
            org.springframework.data.domain.Pageable pageable);

    /**
     * Nombre total de lignes encaissées pour un adhérent (pour pagination)
     */
    @Query("SELECT COUNT(lp) FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent "
            + "AND lp.etat = 'encaisse'")
    long countTotalLignesParAdherent(@Param("codeAdherent") String codeAdherent);

    // ── Requêtes filtrées pour la recherche dans l'historique des visites ─────

    @Query("SELECT lp FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND lp.etat = 'encaisse' "
            + "AND (:prestataireId IS NULL OR lp.prestataireId.id = :prestataireId) "
            + "AND (:dateDebut IS NULL OR lp.date >= :dateDebut) "
            + "AND (:dateFin IS NULL OR lp.date <= :dateFin) "
            + "AND (:codeAyantDroit IS NULL OR v.codeAyantDroit = :codeAyantDroit) "
            + "AND (:typePrestation IS NULL OR p.naturePrestation = :typePrestation) "
            + "ORDER BY lp.date DESC")
    List<Dbx45tyLignePrestation> findDernieresLignesFiltrees(
            @Param("codeAdherent") String codeAdherent,
            @Param("prestataireId") String prestataireId,
            @Param("dateDebut") java.time.LocalDateTime dateDebut,
            @Param("dateFin") java.time.LocalDateTime dateFin,
            @Param("codeAyantDroit") String codeAyantDroit,
            @Param("typePrestation") String typePrestation,
            org.springframework.data.domain.Pageable pageable);

    @Query("SELECT COUNT(lp) FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND lp.etat = 'encaisse' "
            + "AND (:prestataireId IS NULL OR lp.prestataireId.id = :prestataireId) "
            + "AND (:dateDebut IS NULL OR lp.date >= :dateDebut) "
            + "AND (:dateFin IS NULL OR lp.date <= :dateFin) "
            + "AND (:codeAyantDroit IS NULL OR v.codeAyantDroit = :codeAyantDroit) "
            + "AND (:typePrestation IS NULL OR p.naturePrestation = :typePrestation)")
    long countLignesFiltrees(
            @Param("codeAdherent") String codeAdherent,
            @Param("prestataireId") String prestataireId,
            @Param("dateDebut") java.time.LocalDateTime dateDebut,
            @Param("dateFin") java.time.LocalDateTime dateFin,
            @Param("codeAyantDroit") String codeAyantDroit,
            @Param("typePrestation") String typePrestation);

    /** Somme montant base des lignes filtrées */
    @Query("SELECT COALESCE(SUM("
            + "CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END "
            + "* CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END), 0) "
            + "FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND lp.etat = 'encaisse' "
            + "AND (:prestataireId IS NULL OR lp.prestataireId.id = :prestataireId) "
            + "AND (:dateDebut IS NULL OR lp.date >= :dateDebut) "
            + "AND (:dateFin IS NULL OR lp.date <= :dateFin) "
            + "AND (:codeAyantDroit IS NULL OR v.codeAyantDroit = :codeAyantDroit) "
            + "AND (:typePrestation IS NULL OR p.naturePrestation = :typePrestation)")
    BigDecimal sumMontantBaseLignesFiltrees(
            @Param("codeAdherent") String codeAdherent,
            @Param("prestataireId") String prestataireId,
            @Param("dateDebut") java.time.LocalDateTime dateDebut,
            @Param("dateFin") java.time.LocalDateTime dateFin,
            @Param("codeAyantDroit") String codeAyantDroit,
            @Param("typePrestation") String typePrestation);

    /** Somme PEC des lignes filtrées */
    @Query("SELECT COALESCE(SUM("
            + "CASE WHEN lp.valeurModif IS NOT NULL THEN lp.valeurModif ELSE lp.valeur END "
            + "* CASE WHEN lp.nbreModif IS NOT NULL THEN lp.nbreModif ELSE lp.nbre END "
            + "* (lp.taux / 100)), 0) "
            + "FROM Dbx45tyLignePrestation lp JOIN lp.prestationId p JOIN p.visiteId v "
            + "WHERE v.codeAdherent.codeAdherent = :codeAdherent AND lp.etat = 'encaisse' "
            + "AND (:prestataireId IS NULL OR lp.prestataireId.id = :prestataireId) "
            + "AND (:dateDebut IS NULL OR lp.date >= :dateDebut) "
            + "AND (:dateFin IS NULL OR lp.date <= :dateFin) "
            + "AND (:codeAyantDroit IS NULL OR v.codeAyantDroit = :codeAyantDroit) "
            + "AND (:typePrestation IS NULL OR p.naturePrestation = :typePrestation)")
    BigDecimal sumPECLignesFiltrees(
            @Param("codeAdherent") String codeAdherent,
            @Param("prestataireId") String prestataireId,
            @Param("dateDebut") java.time.LocalDateTime dateDebut,
            @Param("dateFin") java.time.LocalDateTime dateFin,
            @Param("codeAyantDroit") String codeAyantDroit,
            @Param("typePrestation") String typePrestation);

}
