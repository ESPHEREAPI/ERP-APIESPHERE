package com.esphere.validation.repository;

import com.esphere.validation.dto.request.ExamenDTO;
import com.esphere.validation.entity.LignePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
