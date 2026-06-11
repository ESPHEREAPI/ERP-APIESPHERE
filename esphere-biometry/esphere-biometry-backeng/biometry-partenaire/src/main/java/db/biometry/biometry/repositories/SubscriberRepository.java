/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

/**
 *
 * @author USER01
 */

import db.biometry.biometry.entite.Subscribers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscribers, Long> {

    // ── Vérifications d'unicité ──────────────────────────────────────────────
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByPolicyNumber(String policyNumber);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByUsernameAndIdNot(String username, Long id);

    // ── Recherche par token d'activation ────────────────────────────────────
    Optional<Subscribers> findByActivationToken(String token);
     Optional<Subscribers> findByPolicyNumber(String PolicyNumber);
          Optional<Subscribers> findByEmail(String email);

    // ── Recherche full-text paginée (search + filtre isActive) ───────────────
    @Query("""
        SELECT s FROM Subscribers s
        WHERE (:search IS NULL OR :search = '' OR
               LOWER(s.fullName)    LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(s.email)       LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(s.username)    LIKE LOWER(CONCAT('%', :search, '%')) OR
               LOWER(s.policyNumber) LIKE LOWER(CONCAT('%', :search, '%')))
          AND (:isActive IS NULL OR s.active = :isActive)
        """)
    Page<Subscribers> findAllWithFilter(
            @Param("search") String search,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    // ── Mise à jour du statut en masse (toggle) ──────────────────────────────
    @Modifying
    @Query("UPDATE Subscribers s SET s.active = :status, s.updatedAt = :now WHERE s.id = :id")
    int updateStatus(@Param("id") Long id,
                     @Param("status") boolean status,
                     @Param("now") Instant now);

    // ── Disponibilité username ───────────────────────────────────────────────
    @Query("SELECT COUNT(s) = 0 FROM Subscribers s WHERE s.username = :username AND (:excludeId IS NULL OR s.id <> :excludeId)")
    boolean isUsernameAvailable(@Param("username") String username,
                                @Param("excludeId") Long excludeId);
    
}
