package com.esphere.prestataire.repository;

import com.esphere.prestataire.entity.TypePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypePrestationRepository extends JpaRepository<TypePrestation, String> {

    @Query("SELECT t FROM TypePrestation t WHERE t.affiche = 1 ORDER BY t.nom ASC")
    List<TypePrestation> findAllVisible();

    @Query("SELECT t FROM TypePrestation t WHERE t.categorie = :categorie AND t.affiche = 1")
    List<TypePrestation> findByCategorie(String categorie);
}