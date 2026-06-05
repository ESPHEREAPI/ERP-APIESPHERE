package com.esphere.visite.repository;

import com.esphere.visite.entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Integer> {

    // Consultation d'une visite
    @Query("SELECT c FROM Consultation c WHERE c.visiteId = :visiteId AND c.supprime = '-1'")
    Optional<Consultation> findByVisite(@Param("visiteId") String visiteId);

    // Consultations en attente de validation (pour le service santé)
    @Query("""
        SELECT c FROM Consultation c
        WHERE c.etatConsultation = 'attente_validation'
        AND c.supprime = '-1'
        ORDER BY c.date ASC
    """)
    List<Consultation> findEnAttente();

    // Consultations en attente pour un prestataire
    @Query("""
        SELECT c FROM Consultation c
        JOIN Visite v ON v.id = c.visiteId
        WHERE v.prestataireId = :prestataireId
        AND c.etatConsultation = 'attente_validation'
        AND c.supprime = '-1'
    """)
    List<Consultation> findEnAttenteByPrestataire(@Param("prestataireId") String prestataireId);
}