package com.esphere.notification.repository;

import com.esphere.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    // Alertes in-app non lues d'un destinataire
    @Query("""
        SELECT n FROM Notification n
        WHERE n.destinataireId = :destinataireId
        AND n.canal = 'alerte'
        AND n.lu = false
        ORDER BY n.dateCreation DESC
    """)
    List<Notification> findAlerteNonLues(
            @Param("destinataireId") String destinataireId);

    // Toutes les alertes d'un destinataire
    @Query("""
        SELECT n FROM Notification n
        WHERE n.destinataireId = :destinataireId
        AND n.canal = 'alerte'
        ORDER BY n.dateCreation DESC
    """)
    List<Notification> findAlertes(
            @Param("destinataireId") String destinataireId);

    // Compter les non lues (pour badge clignotant)
    @Query("""
        SELECT COUNT(n) FROM Notification n
        WHERE n.destinataireId = :destinataireId
        AND n.canal = 'alerte'
        AND n.lu = false
    """)
    Long compterNonLues(@Param("destinataireId") String destinataireId);

    // Historique complet d'un destinataire
    @Query("""
        SELECT n FROM Notification n
        WHERE n.destinataireId = :destinataireId
        ORDER BY n.dateCreation DESC
    """)
    List<Notification> findByDestinataire(
            @Param("destinataireId") String destinataireId);

    // Marquer comme lu
    @Modifying
    @Query("UPDATE Notification n SET n.lu = true, n.dateLecture = CURRENT_TIMESTAMP WHERE n.id = :id")
    void marquerCommeLu(@Param("id") Integer id);

    // Marquer toutes comme lues
    @Modifying
    @Query("""
        UPDATE Notification n
        SET n.lu = true, n.dateLecture = CURRENT_TIMESTAMP
        WHERE n.destinataireId = :destinataireId
        AND n.canal = 'alerte'
        AND n.lu = false
    """)
    void marquerToutesCommeLues(@Param("destinataireId") String destinataireId);
}