package com.esphere.visite.repository;

import com.esphere.visite.entity.Prestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PrestationRepository extends JpaRepository<Prestation, Integer> {

    @Query("SELECT p FROM Prestation p WHERE p.visiteId = :visiteId AND p.supprime = '-1'")
    List<Prestation> findByVisite(@Param("visiteId") String visiteId);

    @Query("""
        SELECT p FROM Prestation p
        JOIN LignePrestation l ON l.prestation.id = p.id
        WHERE l.etat = 'EN_ATTENTE'
        AND p.supprime = '-1'
        ORDER BY p.date ASC
    """)
    List<Prestation> findEnAttente();
    
     @Modifying
    @Transactional
    @Query(value = "UPDATE dbx45ty_prestation p SET p.supprime = '1' " +
                   "WHERE NOT EXISTS (" +
                   "SELECT 1 FROM dbx45ty_ligne_prestation lp " +
                   "WHERE lp.prestation_id = p.id)",
           nativeQuery = true)
    int updateSupprimeForPrestationsSansLignes();
}