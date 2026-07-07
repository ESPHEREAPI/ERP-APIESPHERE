package com.esphere.webservicecron.repository;

import com.esphere.webservicecron.entity.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent, String> {

    @Query("SELECT a FROM Adherent a WHERE a.statut = '1'")
    List<Adherent> findAllActifs();

    @Query("SELECT a FROM Adherent a WHERE a.police = :police")
    List<Adherent> findByPolice(@Param("police") String police);

    long countByPoliceAndStatut(String police, String statut);

    Optional<Adherent> findFirstByPolice(String police);

    @Query("SELECT DISTINCT a.police FROM Adherent a WHERE a.police IS NOT NULL ORDER BY a.police")
    List<String> findDistinctPolices();

    /**
     * Une ligne représentative par police (souscripteur + dates), choisie
     * via le plus petit numéro. Évite une requête par police.
     */
    @Query("""
        SELECT a.police, a.souscripteur, a.effetPolice, a.echeancePolice
        FROM Adherent a
        WHERE a.numero = (SELECT MIN(a2.numero) FROM Adherent a2 WHERE a2.police = a.police)
        """)
    List<Object[]> findInfoRepresentativeParPolice();

    /** Comptage groupé (police, statut) en une seule requête pour toutes les polices. */
    @Query("SELECT a.police, a.statut, COUNT(a) FROM Adherent a GROUP BY a.police, a.statut")
    List<Object[]> countGroupedByPoliceAndStatut();
}
