/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.repositories;

/**
 *
 * @author USER01
 */

import db.biometry.biometry.entite.ActivationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ActivationLogRepository extends JpaRepository<ActivationLog, Long> {

    Optional<ActivationLog> findByToken(String token);

    List<ActivationLog> findBySubscriberIdOrderBySentAtDesc(Long subscriberId);

    // Expire les tokens dont la date est dépassée (tâche planifiée)
    @Modifying
    @Query("""
        UPDATE ActivationLog al
        SET al.status = 'EXPIRED'
        WHERE al.status = 'PENDING'
          AND al.expiresAt < :now
        """)
    int expireOldTokens(@Param("now") Instant now);
    
}
