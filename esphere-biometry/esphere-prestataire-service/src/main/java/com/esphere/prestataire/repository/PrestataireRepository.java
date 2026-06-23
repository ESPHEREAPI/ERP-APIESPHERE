package com.esphere.prestataire.repository;

import com.esphere.prestataire.entity.Prestataire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // ── Admin : liste paginée avec filtres optionnels ──
    @Query("""
        SELECT p FROM Prestataire p
        WHERE p.supprime = '-1'
          AND (:statut     IS NULL OR p.statut = :statut)
          AND (:categorieId IS NULL OR p.categorie.id = :categorieId)
          AND (:villeId    IS NULL OR p.villeId = :villeId)
          AND (:search     IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :search, '%')))
        ORDER BY p.nom ASC
        """)
    Page<Prestataire> findAllAdmin(
            @Param("statut")      String statut,
            @Param("categorieId") String categorieId,
            @Param("villeId")     Integer villeId,
            @Param("search")      String search,
            Pageable pageable);

    boolean existsById(String id);
}