package com.esphere.webservicecron.repository;

import com.esphere.webservicecron.entity.AyantDroit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AyantDroitRepository extends JpaRepository<AyantDroit, String> {

    @Query("SELECT a FROM AyantDroit a WHERE a.statut = :statut")
    List<AyantDroit> findByStatut(@Param("statut") String statut);

    long countByPoliceAndStatut(String police, String statut);

    /** Comptage groupé (police, statut) en une seule requête pour toutes les polices. */
    @Query("SELECT a.police, a.statut, COUNT(a) FROM AyantDroit a GROUP BY a.police, a.statut")
    List<Object[]> countGroupedByPoliceAndStatut();
}
