/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

import db.biometry.biometry.entite.Dbx45tyAdherent;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER01
 */
@Repository

public interface AdherentRepository  extends JpaRepository<Dbx45tyAdherent, String>{
   // public Dbx45tyAdherent findByCodeAdherent(String codeAdherent);
    //public List<Dbx45tyAdherent>findBySouscripteur(String souscripteur);
    /*
    liste les adherents pour une police precis pendant une periode
    */
    public List<Dbx45tyAdherent>findByPolice(String police);
    
    /**
     * Recherche un adhérent par son code
     */
    Optional<Dbx45tyAdherent> findByCodeAdherent(String codeAdherent);
    Optional<Dbx45tyAdherent> findByTelephone(String telephone);
//     @Query("SELECT COUNT(ad) > 0 FROM Dbx45tyAdherent ad WHERE ad.telephone= :telephone and ad.enrole!= -1")
//    boolean existsByCode(@Param("telephone") String code);
    
    /**
     * Recherche un adhérent par matricule
     */
    Optional<Dbx45tyAdherent> findByMatricule(String matricule);
    
    /**
     * Liste des adhérents par souscripteur
     */
    List<Dbx45tyAdherent> findBySouscripteur(String souscripteur);
    
    /**
     * Liste des adhérents actifs par souscripteur
     */
    @Query("SELECT a FROM Dbx45tyAdherent a WHERE a.souscripteur = :souscripteur AND a.statut = '1'")
    List<Dbx45tyAdherent> findActiveAdherentsBySouscripteur(@Param("souscripteur") String souscripteur);
    
    /**
     * Comptage des adhérents actifs par souscripteur
     */
    @Query("SELECT COUNT(a) FROM Dbx45tyAdherent a WHERE a.souscripteur = :souscripteur AND a.statut = '1'")
    Long countActiveAdherentsBySouscripteur(@Param("souscripteur") String souscripteur);
    
    /**
     * Recherche multicritères avec pagination
     */
    @Query("SELECT a FROM Dbx45tyAdherent a WHERE " +
           "(:search IS NULL OR LOWER(a.assurePrincipal) LIKE LOWER(CONCAT('%', :search, '%')) OR a.matricule LIKE CONCAT('%', :search, '%') OR a.codeAdherent LIKE CONCAT('%', :search, '%')) AND " +
           "(:souscripteur IS NULL OR a.souscripteur = :souscripteur) AND " +
           "(:police IS NULL OR a.police = :police) AND " +
           "(:groupe IS NULL OR a.groupe = :groupe) AND " +
           "(:statut IS NULL OR a.statut = :statut) AND " +
           "(:sexe IS NULL OR a.sexe = :sexe) AND " +
           "(:enrole IS NULL OR a.enrole = :enrole) AND " +
           "(:dateMin IS NULL OR a.effetPolice >= :dateMin) AND " +
           "(:dateMax IS NULL OR a.effetPolice <= :dateMax)")
    Page<Dbx45tyAdherent> searchAdherents(
            @Param("search") String search,
            @Param("souscripteur") String souscripteur,
            @Param("police") String police,
            @Param("groupe") Short groupe,
            @Param("statut") String statut,
            @Param("sexe") String sexe,
            @Param("enrole") String enrole,
            @Param("dateMin") LocalDate dateMin,
            @Param("dateMax") LocalDate dateMax,
            Pageable pageable);
    
    /**
     * Liste des adhérents par police et groupe
     */
    @Query("SELECT a FROM Dbx45tyAdherent a WHERE a.police = :police AND a.groupe = :groupe")
    List<Dbx45tyAdherent> findByPoliceAndGroupe(@Param("police") String police, @Param("groupe") Short groupe);
    
    /**
     * Liste des adhérents dont le plafond approche l'épuisement
     * (Cette requête nécessite une jointure avec les consommations)
     */
    @Query("SELECT a FROM Dbx45tyAdherent a WHERE a.souscripteur = :souscripteur AND a.statut = '1' " +
           "ORDER BY a.assurePrincipal")
    List<Dbx45tyAdherent> findAdherentsForPlafondAnalysis(@Param("souscripteur") String souscripteur);
    
    /**
     * Statistiques par souscripteur
     */
    @Query("SELECT COUNT(a), a.statut FROM Dbx45tyAdherent a WHERE a.souscripteur = :souscripteur GROUP BY a.statut")
    List<Object[]> getStatistiquesBySouscripteur(@Param("souscripteur") String souscripteur);
    
    /**
     * Liste des adhérents sans enrôlement biométrique
     */
    @Query("SELECT a FROM Dbx45tyAdherent a WHERE a.souscripteur = :souscripteur AND a.enrole = 'N' AND a.statut = '1'")
    List<Dbx45tyAdherent> findNonEnroledAdherents(@Param("souscripteur") String souscripteur);
    
    /**
     * Vérification d'existence par matricule et souscripteur
     */
    boolean existsByMatriculeAndSouscripteur(String matricule, String souscripteur);
    
    /**
     * Liste des adhérents avec police expirant bientôt
     */
    @Query("SELECT a FROM Dbx45tyAdherent a WHERE a.souscripteur = :souscripteur AND a.echeancePolice BETWEEN :dateDebut AND :dateFin AND a.statut = '1'")
    List<Dbx45tyAdherent> findAdherentsWithExpiringPolice(
            @Param("souscripteur") String souscripteur,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin);

    /**
     * Liste distincte des souscripteurs actifs :
     * effetPolice <= aujourd'hui ET echeancePolice > aujourd'hui
     * Requête native SQL pour compatibilité MySQL (CURDATE()).
     * Retourne [souscripteur, police] pour affichage "NOM (police)"
     */
    @Query(value = "SELECT DISTINCT souscripteur, police FROM dbx45ty_adherent " +
                   "WHERE effet_police <= CURDATE() AND echeance_police > CURDATE() " +
                   "ORDER BY souscripteur",
           nativeQuery = true)
    List<Object[]> findDistinctSouscripteursActifs();

    /**
     * Liste des adhérents d'une police active (requête native SQL)
     * pour le sélecteur dans la vue adhérent
     */
    @Query(value = "SELECT code_adherent, assure_principal, matricule, statut " +
                   "FROM dbx45ty_adherent WHERE police = :police " +
                   "AND effet_police <= CURDATE() AND echeance_police > CURDATE() " +
                   "ORDER BY assure_principal",
           nativeQuery = true)
    List<Object[]> findAdherentsByPoliceActif(@Param("police") String police);

    /**
     * Met à jour le plafond d'un adhérent (appelé après récupération depuis l'API externe).
     * @Modifying + @Transactional requis pour les requêtes UPDATE natives.
     */
    @Modifying
    @Transactional
    @Query(value = "UPDATE dbx45ty_adherent SET plafond_assurep = :plafond " +
                   "WHERE code_adherent = :codeAdherent",
           nativeQuery = true)
    int updatePlafond(@Param("codeAdherent") String codeAdherent,
                      @Param("plafond")      Double plafond);

    /**
     * Somme des plafonds (plafond_assurep) de tous les adhérents actifs d'un souscripteur.
     * Exclut les valeurs null/0 — elles seront enrichies depuis l'API externe avant appel.
     */
    @Query(value = "SELECT COALESCE(SUM(plafond_assurep), 0) FROM dbx45ty_adherent " +
                   "WHERE souscripteur = :souscripteur AND statut = '1' AND plafond_assurep > 0",
           nativeQuery = true)
    Double sumPlafondBySouscripteur(@Param("souscripteur") String souscripteur);

    /**
     * Adhérents actifs d'un souscripteur dont le plafond est null ou 0.
     * Utilisé pour l'enrichissement depuis l'API externe.
     */
    @Query("SELECT a FROM Dbx45tyAdherent a WHERE a.souscripteur = :souscripteur "
         + "AND a.statut = '1' AND (a.plafondAssurep IS NULL OR a.plafondAssurep = 0)")
    List<Dbx45tyAdherent> findAdherentsWithoutPlafond(@Param("souscripteur") String souscripteur);

    /**
     * Résout le nom du souscripteur depuis le numéro de police.
     * Utilisé pour mapper le login souscripteur (police) vers le champ souscripteur en base.
     */
    @Query(value = "SELECT souscripteur FROM dbx45ty_adherent WHERE police = :police LIMIT 1",
           nativeQuery = true)
    String findSouscripteurByPolice(@Param("police") String police);
}
