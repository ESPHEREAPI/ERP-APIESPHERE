package com.esphere.visite.repository;

import com.esphere.visite.entity.LignePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LignePrestationRepository extends JpaRepository<LignePrestation, Integer> {

    @Query("SELECT l FROM LignePrestation l WHERE l.prestation.id = :prestationId AND l.supprime = '-1'")
    List<LignePrestation> findByPrestation(@Param("prestationId") Integer prestationId);

    @Query("SELECT l FROM LignePrestation l WHERE l.prestation.id = :prestationId AND l.etat = :etat AND l.supprime = '-1'")
    List<LignePrestation> findByPrestationAndEtat(
            @Param("prestationId") Integer prestationId,
            @Param("etat") String etat);
}