package com.esphere.bonmanuel.repository;

import com.esphere.bonmanuel.entity.BonManuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BonManuelRepository extends JpaRepository<BonManuel, Integer> {

    Optional<BonManuel> findByReference(String reference);

    Optional<BonManuel> findByNumeroProforma(String numeroProforma);

    @Query("""
        SELECT b FROM BonManuel b
        WHERE b.statut = :statut
        AND b.supprime = '-1'
        ORDER BY b.dateCreation DESC
    """)
    List<BonManuel> findByStatut(@Param("statut") String statut);

    @Query("""
        SELECT b FROM BonManuel b
        WHERE b.prestataireId = :prestataireId
        AND b.supprime = '-1'
        ORDER BY b.dateCreation DESC
    """)
    List<BonManuel> findByPrestataire(@Param("prestataireId") String prestataireId);

    @Query("""
        SELECT b FROM BonManuel b
        WHERE b.prestataireId = :prestataireId
        AND b.statut = 'confirme'
        AND b.supprime = '-1'
        ORDER BY b.dateCreation DESC
    """)
    List<BonManuel> findConfirmesParPrestataire(
            @Param("prestataireId") String prestataireId);

    @Query("""
        SELECT b FROM BonManuel b
        WHERE b.codeAdherent = :codeAdherent
        AND b.supprime = '-1'
        ORDER BY b.dateCreation DESC
    """)
    List<BonManuel> findByAdherent(@Param("codeAdherent") String codeAdherent);

    // Séquence pour générer la référence
    @Query("SELECT COUNT(b) FROM BonManuel b WHERE b.prestataireId = :prestataireId")
    Long countByPrestataire(@Param("prestataireId") String prestataireId);
}