package com.esphere.adherent.repository;

import com.esphere.adherent.entity.AyantDroit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AyantDroitRepository extends JpaRepository<AyantDroit, String> {

    // Tous les ayants droit actifs d'un assuré
    @Query("""
        SELECT a FROM AyantDroit a
        WHERE a.adherent.codeAdherent = :codeAdherent
        AND a.statut = '1'
    """)
    List<AyantDroit> findActiveByAdherent(@Param("codeAdherent") String codeAdherent);

    // Cherche un ayant droit actif par son code
    @Query("SELECT a FROM AyantDroit a WHERE a.codeAyantDroit = :code AND a.statut = '1'")
    Optional<AyantDroit> findActiveByCode(@Param("code") String code);
}