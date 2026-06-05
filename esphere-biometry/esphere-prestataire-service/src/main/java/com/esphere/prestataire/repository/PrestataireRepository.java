package com.esphere.prestataire.repository;

import com.esphere.prestataire.entity.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrestataireRepository extends JpaRepository<Prestataire, String> {

    @Query("SELECT p FROM Prestataire p WHERE p.id = :id AND p.statut = '1' AND p.supprime = '-1'")
    Optional<Prestataire> findActiveById(@Param("id") String id);

    @Query("SELECT p FROM Prestataire p WHERE p.categorie.id = :categorieId AND p.statut = '1' AND p.supprime = '-1'")
    List<Prestataire> findActiveByCategorie(@Param("categorieId") String categorieId);

    @Query("SELECT p FROM Prestataire p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%')) AND p.statut = '1' AND p.supprime = '-1'")
    List<Prestataire> searchByNom(@Param("nom") String nom);
}