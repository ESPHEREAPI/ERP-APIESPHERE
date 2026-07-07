package com.esphere.webservicecron.repository;

import com.esphere.webservicecron.entity.WebserviceSyncHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WebserviceSyncHistoryRepository extends JpaRepository<WebserviceSyncHistory, Long> {

    Page<WebserviceSyncHistory> findAllByOrderByDateDebutDesc(Pageable pageable);

    List<WebserviceSyncHistory> findTop10ByOrderByDateDebutDesc();

    Page<WebserviceSyncHistory> findByTypeSynchronisationOrderByDateDebutDesc(
            String typeSynchronisation, Pageable pageable);

    Optional<WebserviceSyncHistory> findTopByTypeSynchronisationOrderByDateDebutDesc(String typeSynchronisation);

    Page<WebserviceSyncHistory> findByDateDebutBetweenOrderByDateDebutDesc(
            LocalDateTime debut, LocalDateTime fin, Pageable pageable);

    Page<WebserviceSyncHistory> findByTypeSynchronisationAndDateDebutBetweenOrderByDateDebutDesc(
            String typeSynchronisation, LocalDateTime debut, LocalDateTime fin, Pageable pageable);

    long countByStatut(String statut);

    long countByDateDebutAfter(LocalDateTime depuis);

    long countByDeclencheurAndDateDebutAfter(String declencheur, LocalDateTime depuis);
}
