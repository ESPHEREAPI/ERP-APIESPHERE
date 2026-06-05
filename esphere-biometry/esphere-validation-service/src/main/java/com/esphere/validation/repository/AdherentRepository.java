package com.esphere.validation.repository;

import com.esphere.validation.entity.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface AdherentRepository
        extends JpaRepository<Adherent, String> {

    @Query("SELECT a FROM Adherent a " +
           "WHERE a.codeAdherent = :code AND a.statut = '1'")
    Optional<Adherent> findActiveByCode(@Param("code") String code);
    
    // ── AdherentRepository.java  (ajouter cette méthode) ──────────────────
@Modifying
@Transactional
@Query(value = """
    UPDATE dbx45ty_adherent
    SET plafond_assurep = :plafond
    WHERE code_adherent = :codeAdherent
""", nativeQuery = true)
int updatePlafond(
    @Param("codeAdherent") String codeAdherent,
    @Param("plafond")      Double plafond
);
}