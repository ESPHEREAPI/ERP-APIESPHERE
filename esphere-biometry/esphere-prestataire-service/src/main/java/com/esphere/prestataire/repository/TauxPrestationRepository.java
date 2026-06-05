package com.esphere.prestataire.repository;

import com.esphere.prestataire.entity.TauxPrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TauxPrestationRepository extends JpaRepository<TauxPrestation, Integer> {

    // Taux applicable selon type de prestation, police et groupe de l'assuré
    @Query("""
        SELECT t FROM TauxPrestation t
        WHERE t.typePrestation.id = :typePrestationId
        AND t.police = :police
        AND t.groupe = :groupe
    """)
    Optional<TauxPrestation> findTaux(
            @Param("typePrestationId") String typePrestationId,
            @Param("police") String police,
            @Param("groupe") Short groupe);

    // Tous les taux d'une police
    @Query("SELECT t FROM TauxPrestation t WHERE t.police = :police")
    List<TauxPrestation> findByPolice(@Param("police") String police);
}