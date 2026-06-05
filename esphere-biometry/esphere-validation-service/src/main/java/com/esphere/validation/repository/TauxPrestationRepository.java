/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.esphere.validation.repository;

import com.esphere.validation.entity.TauxPrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

/**
 *
 * @author USER01
 */
@Repository
public interface TauxPrestationRepository
        extends JpaRepository<TauxPrestation, Integer> {

    /** Par type de prestation */
    List<TauxPrestation> findByTypePrestationId(
            String typePrestationId);

    /** Par police */
    List<TauxPrestation> findByPolice(String police);

    /** Par police + groupe (pour trouver le taux exact) */
    Optional<TauxPrestation> findByPoliceAndGroupeAndTypePrestationId(
            String police, short groupe,
            String typePrestationId);
    
     
}
    

