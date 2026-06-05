package com.esphere.visite.repository;

import com.esphere.visite.entity.Visite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisiteRepository extends JpaRepository<Visite, String> {

    // Lookup par code_court (utilisé pour capture média mobile)
    Optional<Visite> findByCodeCourt(String codeCourt);

    // Historique d'un prestataire
    @Query("SELECT v FROM Visite v WHERE v.prestataireId = :prestataireId ORDER BY v.date DESC")
    List<Visite> findByPrestataire(@Param("prestataireId") String prestataireId);

    // Historique d'un assuré
    @Query("""
        SELECT v FROM Visite v
        WHERE v.codeAdherent = :codeAdherent
        ORDER BY v.date DESC
    """)
    List<Visite> findByAdherent(@Param("codeAdherent") String codeAdherent);
}