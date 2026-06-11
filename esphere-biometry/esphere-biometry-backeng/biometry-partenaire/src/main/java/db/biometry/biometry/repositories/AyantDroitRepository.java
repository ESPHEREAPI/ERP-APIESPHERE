/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.entite.Dbx45tyAyantDroit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour la gestion des ayants droit
 * 
 * @author JIATOU FRANCK
 * @version 1.0
 */
/**
 *
 * @author USER01
 */
@Repository
public interface AyantDroitRepository extends JpaRepository<Dbx45tyAyantDroit, String>{
    //public Dbx45tyAyantDroit findByCodeAyantDroit(String codeAyantDroit);
            
     
    /**
     * Recherche d'un ayant droit par son code
     */
    Optional<Dbx45tyAyantDroit> findByCodeAyantDroit(String codeAyantDroit);
    
    /**
     * Liste des ayants droit pour un adhérent
     */
    @Query("SELECT ad FROM Dbx45tyAyantDroit ad " +
           "WHERE ad.codeAdherent.codeAdherent = :codeAdherent " +
           "ORDER BY ad.nom")
    List<Dbx45tyAyantDroit> findByAdherent(@Param("codeAdherent") String codeAdherent);
    
    /**
     * Liste des ayants droit actifs pour un adhérent
     */
    @Query("SELECT ad FROM Dbx45tyAyantDroit ad " +
           "WHERE ad.codeAdherent.codeAdherent = :codeAdherent " +
           "AND ad.statut = 'ACTIF' " +
           "ORDER BY ad.nom")
    List<Dbx45tyAyantDroit> findActiveByAdherent(@Param("codeAdherent") String codeAdherent);
    
    /**
     * Comptage des ayants droit pour un adhérent
     */
    @Query("SELECT COUNT(ad) FROM Dbx45tyAyantDroit ad " +
           "WHERE ad.codeAdherent.codeAdherent = :codeAdherent " +
           "AND ad.statut = 'ACTIF'")
    Long countActiveByAdherent(@Param("codeAdherent") String codeAdherent);
    
    /**
     * Comptage total des ayants droit actifs pour un souscripteur
     */
    @Query("SELECT COUNT(ad) FROM Dbx45tyAyantDroit ad " +
           "WHERE ad.codeAdherent.souscripteur = :souscripteur " +
           "AND ad.statut = '1'")
    Long countActiveBySouscripteur(@Param("souscripteur") String souscripteur);
    
    /**
     * Liste des ayants droit sans enrôlement biométrique
     */
    @Query("SELECT ad FROM Dbx45tyAyantDroit ad " +
           "WHERE ad.codeAdherent.souscripteur = :souscripteur " +
           "AND ad.enrole = 'N' " +
           "AND ad.statut = 'ACTIF'")
    List<Dbx45tyAyantDroit> findNonEnroledBySouscripteur(@Param("souscripteur") String souscripteur);
    
    /**
     * Recherche par nom et lien de parenté
     */
    @Query("SELECT ad FROM Dbx45tyAyantDroit ad " +
           "WHERE ad.codeAdherent.codeAdherent = :codeAdherent " +
           "AND LOWER(ad.nom) LIKE LOWER(CONCAT('%', :nom, '%')) " +
           "AND (:lienpare IS NULL OR ad.lienpare = :lienpare)")
    List<Dbx45tyAyantDroit> searchAyantsDroits(
            @Param("codeAdherent") String codeAdherent,
            @Param("nom") String nom,
            @Param("lienpare") String lienpare);
}
