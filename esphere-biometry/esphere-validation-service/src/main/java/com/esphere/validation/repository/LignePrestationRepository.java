package com.esphere.validation.repository;

import com.esphere.validation.dto.request.ExamenDTO;
import com.esphere.validation.entity.LignePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface LignePrestationRepository extends JpaRepository<LignePrestation, Integer> {

    @Query("""
        SELECT l FROM LignePrestation l
        WHERE l.etat = 'attente_validation'
        AND l.supprime = '-1'
        ORDER BY l.date ASC
    """)
    List<LignePrestation> findEnAttente();

    @Query("""
        SELECT l FROM LignePrestation l
        WHERE l.prestationId = :prestationId
        AND l.supprime = '-1'
        ORDER BY l.date ASC
    """)
    List<LignePrestation> findByPrestation(@Param("prestationId") Integer prestationId);

    @Query("""
        SELECT l FROM LignePrestation l
        WHERE l.etat = 'attente_validation'
        AND l.supprime = '-1'
        AND l.prestataireId = :prestataireId
        ORDER BY l.date ASC
    """)
    List<LignePrestation> findEnAttenteByPrestataire(
            @Param("prestataireId") String prestataireId);
    

@Query("SELECT new com.esphere.validation.dto.request.ExamenDTO(l.nom, l.valeur) " +
       "FROM LignePrestation l " +
       "WHERE YEAR(l.date) = :annee " +
       "GROUP BY l.nom, l.valeur " +
       "HAVING l.valeur = MIN(l.valeur)")
List<ExamenDTO> listeExamen(@Param("annee") int currentyear);



    // Ordonnances validées — médicaments uniquement
    @Query(value = """
        SELECT COUNT(*) 
        FROM dbx45ty_ligne_prestation l
        INNER JOIN dbx45ty_prestation p 
            ON l.prestation_id = p.id
        INNER JOIN dbx45ty_visite vi 
            ON p.visite_id = vi.id
        WHERE vi.prestataire_id = :prestataireId
          AND l.etat             = :etat
          AND p.nature_prestation='ordonnance'
        """, nativeQuery = true)
    long countOrdonnancesValidesByPrestataire(
        @Param("prestataireId") String prestataireId,
        @Param("etat")          String etat);

    // Examens validés uniquement
    @Query(value = """
        SELECT COUNT(*) 
        FROM dbx45ty_ligne_prestation l
        INNER JOIN dbx45ty_prestation p 
            ON l.prestation_id = p.id
        INNER JOIN dbx45ty_visite vi 
            ON p.visite_id = vi.id
        WHERE vi.prestataire_id = :prestataireId
          AND l.etat            = :etat
          AND p.nature_prestation='examen'
        """, nativeQuery = true)
    long countExamensValidesByPrestataire(
        @Param("prestataireId") String prestataireId,
        @Param("etat")          String etat);
    
    
    
    // ✅ Requête 1 : Dernière prestation de l'ADHÉRENT PRINCIPAL
    @Query(value = """
        SELECT
            a.police,
            v.code_adherent,
            NULL              AS code_ayant_droit,
            lp.taux,
            lp.etat,
            lp.date,
            lp.id             AS ligne_prestation_id,
            p.id              AS prestation_id
        FROM dbx45ty_ligne_prestation  lp
        JOIN dbx45ty_prestation         p  ON p.id           = lp.prestation_id
        JOIN dbx45ty_visite             v  ON v.id            = p.visite_id
        JOIN dbx45ty_adherent           a  ON a.code_adherent = v.code_adherent
        WHERE
            a.police            = :police
            AND v.code_adherent = :codeAdherent
            AND v.code_ayant_droit IS NULL         
            AND lp.etat IN ('valide', 'encaisse')
            AND lp.taux IS NOT NULL
            AND lp.taux > 0
            AND lp.date >= DATE_SUB(CURDATE(), INTERVAL 2 YEAR)
        ORDER BY lp.date DESC, lp.id DESC
        LIMIT 1
        """, nativeQuery = true)
    Optional<DernierePrestationProjection> findDernierePrestationAdherent(
        @Param("police")        String police,
        @Param("codeAdherent")  String codeAdherent
    );


    // ✅ Requête 2 : Dernière prestation d'un AYANT DROIT
    @Query(value = """
        SELECT
            a.police,
            v.code_adherent,
            v.code_ayant_droit,
            lp.taux,
            lp.etat,
            lp.date,
            lp.id              AS ligne_prestation_id,
            p.id               AS prestation_id
        FROM dbx45ty_ligne_prestation  lp
        JOIN dbx45ty_prestation         p  ON p.id                = lp.prestation_id
        JOIN dbx45ty_visite             v  ON v.id                 = p.visite_id
        JOIN dbx45ty_adherent           a  ON a.code_adherent      = v.code_adherent
        JOIN dbx45ty_ayant_droit        ad ON ad.code_ayant_droit  = v.code_ayant_droit
        WHERE
            a.police               = :police
            AND v.code_ayant_droit = :codeAyantDroit
            AND lp.etat IN ('valide', 'encaisse')
            AND lp.taux IS NOT NULL
            AND lp.taux > 0
            AND lp.date >= DATE_SUB(CURDATE(), INTERVAL 2 YEAR)
        ORDER BY lp.date DESC, lp.id DESC
        LIMIT 1
        """, nativeQuery = true)
    Optional<DernierePrestationProjection> findDernierePrestationAyantDroit(
        @Param("police")          String police,
        @Param("codeAyantDroit")  String codeAyantDroit
    );
}
