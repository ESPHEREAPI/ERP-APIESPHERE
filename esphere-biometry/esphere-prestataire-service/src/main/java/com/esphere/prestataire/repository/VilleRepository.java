package com.esphere.prestataire.repository;

import com.esphere.prestataire.entity.VilleLangue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VilleRepository extends JpaRepository<VilleLangue, Integer> {

    @Query("SELECT vl FROM VilleLangue vl WHERE vl.langueId = 2 ORDER BY vl.nom ASC")
    List<VilleLangue> findAllFrench();

    @Query("SELECT vl.nom FROM VilleLangue vl WHERE vl.villeId = :villeId AND vl.langueId = 2")
    Optional<String> findNomByVilleId(@Param("villeId") Integer villeId);
}
