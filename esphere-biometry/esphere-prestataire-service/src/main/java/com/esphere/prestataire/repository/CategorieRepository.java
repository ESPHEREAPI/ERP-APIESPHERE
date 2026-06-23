package com.esphere.prestataire.repository;

import com.esphere.prestataire.entity.CategoriePrestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategorieRepository extends JpaRepository<CategoriePrestataire, String> {

    @Query("SELECT c FROM CategoriePrestataire c WHERE c.statut = '1' ORDER BY c.nom ASC")
    List<CategoriePrestataire> findAllActives();
}
