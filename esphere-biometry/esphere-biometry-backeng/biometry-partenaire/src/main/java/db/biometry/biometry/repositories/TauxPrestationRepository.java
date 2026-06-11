/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.entite.Dbx45tyTauxPrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des taux de prestation
 * Permet de récupérer les taux et plafonds de couverture
 * 
 * @author JIATOU FRANCK
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@Repository
public interface TauxPrestationRepository extends JpaRepository<Dbx45tyTauxPrestation, Integer> {
    
    /**
     * Recherche du taux pour un type de prestation, police et groupe spécifiques
     */
    @Query("SELECT tp FROM Dbx45tyTauxPrestation tp " +
           "WHERE tp.typePrestationId = :typePrestationId " +
           "AND tp.police = :police " +
           "AND tp.groupe = :groupe")
    Optional<Dbx45tyTauxPrestation> findByTypePrestationAndPoliceAndGroupe(
            @Param("typePrestationId") String typePrestationId,
            @Param("police") String police,
            @Param("groupe") Short groupe);
    
    /**
     * Liste des taux pour une police et un groupe
     */
    @Query("SELECT tp FROM Dbx45tyTauxPrestation tp " +
           "WHERE tp.police = :police " +
           "AND tp.groupe = :groupe")
    List<Dbx45tyTauxPrestation> findByPoliceAndGroupe(
            @Param("police") String police,
            @Param("groupe") Short groupe);
    
    /**
     * Liste des taux pour une police
     */
    List<Dbx45tyTauxPrestation> findByPolice(String police);
    
    /**
     * Récupération des plafonds par catégorie pour une police et un groupe
     */
    @Query("SELECT " +
           "tp.typePrestationId, " +
           "tpr.nom, " +
           "tpr.categorie, " +
           "tp.taux, " +
           "tp.plafond " +
           "FROM Dbx45tyTauxPrestation tp " +
           "JOIN Dbx45tyTypePrestation tpr ON tpr.id = tp.typePrestationId " +
           "WHERE tp.police = :police " +
           "AND tp.groupe = :groupe " +
           "ORDER BY tpr.categorie, tpr.nom")
    List<Object[]> getPlafondsByPoliceAndGroupe(
            @Param("police") String police,
            @Param("groupe") Short groupe);
    
    /**
     * Récupération du plafond global pour une police et un groupe
     * (Somme de tous les plafonds par catégorie)
     */
    @Query("SELECT COALESCE(SUM(tp.plafond), 0) " +
           "FROM Dbx45tyTauxPrestation tp " +
           "WHERE tp.police = :police " +
           "AND tp.groupe = :groupe")
    Float getPlafondGlobal(
            @Param("police") String police,
            @Param("groupe") Short groupe);
    
}
