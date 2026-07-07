package com.esphere.webservicecron.repository;

import com.esphere.webservicecron.entity.TauxPrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TauxPrestationRepository extends JpaRepository<TauxPrestation, Integer> {

    Optional<TauxPrestation> findByTypePrestationIdAndPoliceAndGroupe(
            String typePrestationId, String police, short groupe);
}
