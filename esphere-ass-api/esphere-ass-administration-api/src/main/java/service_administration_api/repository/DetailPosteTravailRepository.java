/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.DetailPosteTravail;
import service_administration_api.entite.DetailPosteTravailId;

/**
 *
 * @author USER01
 */
public interface DetailPosteTravailRepository extends JpaRepository<DetailPosteTravail, DetailPosteTravailId> {
    
   // Méthodes de base héritées de JpaRepository :
    // - findById(DetailPosteTravailId id)
    // - save(DetailPosteTravail entity)
    // - delete(DetailPosteTravail entity)
    // - findAll()
    // - count()
    // etc.
    
    // === MÉTHODES DE RECHERCHE PAR CHAMPS INDIVIDUELS ===
    
    // Recherche par nom utilisateur
    List<DetailPosteTravail> findByIdNomUtil(String nomUtil);
    
    // Recherche par code branche
    List<DetailPosteTravail> findByIdCodeBran(Integer codeBran);
    
    // Recherche par code catégorie
    List<DetailPosteTravail> findByIdCodecate(Integer codecate);
    
    // Recherche par code intégration
    List<DetailPosteTravail> findByIdCodeInte(Integer codeInte);
    
    // === MÉTHODES DE RECHERCHE COMBINÉES ===
    
    // Recherche par nom utilisateur et code branche
    List<DetailPosteTravail> findByIdNomUtilAndIdCodeBran(String nomUtil, Integer codeBran);
    
    // Recherche par nom utilisateur et code catégorie
    List<DetailPosteTravail> findByIdNomUtilAndIdCodecate(String nomUtil, Integer codecate);
    
    // Recherche par code branche et catégorie
    List<DetailPosteTravail> findByIdCodeBranAndIdCodecate(Integer codeBran, Integer codecate);
    
    // === MÉTHODES AVEC CRITÈRES MÉTIER ===
    
    // Recherche par statut de validation
    List<DetailPosteTravail> findByValiAn(String valiAn);
    List<DetailPosteTravail> findByValiAv(String valiAv);
    
    // Recherche par statut de stockage
    List<DetailPosteTravail> findByStocAtte(String stocAtte);
    
    // Recherche combinée validation et utilisateur
    List<DetailPosteTravail> findByIdNomUtilAndValiAnAndValiAv(String nomUtil, String valiAn, String valiAv);
    
    // === MÉTHODES AVEC @Query PERSONNALISÉES ===
    
    @Query("SELECT d FROM DetailPosteTravail d WHERE d.id.nomUtil = :nomUtil")
    List<DetailPosteTravail> findDetailsByNomUtil(@Param("nomUtil") String nomUtil);
    
    @Query("SELECT d FROM DetailPosteTravail d WHERE d.id.codeBran = :codeBran AND d.valiAn = 'O'")
    List<DetailPosteTravail> findValidDetailsByBranche(@Param("codeBran") Integer codeBran);
    
    @Query("SELECT d FROM DetailPosteTravail d WHERE d.id.codecate = :codecate AND d.stocAtte IS NOT NULL")
    List<DetailPosteTravail> findDetailsWithStocAtteByCategorie(@Param("codecate") Integer codecate);
    
    // Requête native SQL
    @Query(value = "SELECT * FROM ORASSADM.DETAIL_POSTE_TRAVAIL WHERE NOM_UTIL = ?1 AND CODEBRAN = ?2", nativeQuery = true)
    List<DetailPosteTravail> findByNomUtilAndCodeBranNative(String nomUtil, Integer codeBran);
    
    // === MÉTHODES DE COMPTAGE ===
    
    // Compter par nom utilisateur
    long countByIdNomUtil(String nomUtil);
    
    // Compter par code branche
    long countByIdCodeBran(Integer codeBran);
    
    // Compter les enregistrements validés
    long countByValiAnAndValiAv(String valiAn, String valiAv);
    
    // === MÉTHODES D'EXISTENCE ===
    
    // Vérifier l'existence avec clé partielle
    boolean existsByIdNomUtilAndIdCodeBran(String nomUtil, Integer codeBran);
    
    // Vérifier l'existence avec validation
    boolean existsByIdNomUtilAndValiAn(String nomUtil, String valiAn);
    
    // === MÉTHODES DE SUPPRESSION ===
    
    // Supprimer par nom utilisateur
    void deleteByIdNomUtil(String nomUtil);
    
    // Supprimer par code branche
    void deleteByIdCodeBran(Integer codeBran);
    
    // Supprimer avec conditions
    void deleteByIdNomUtilAndValiAn(String nomUtil, String valiAn); 
}
