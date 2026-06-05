/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.esphere.validation.repository;

import com.esphere.validation.entity.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;

/**
 *
 * @author USER01
 */
@Repository
public interface ExamenRepository
        extends JpaRepository<Examen, Integer> {

    /** Actifs uniquement */
    List<Examen> findByStatutAndSupprime(
            String statut, String supprime);

    /** Recherche insensible casse — pour rechercher-ou-créer */
    Optional<Examen> findByNomIgnoreCase(String nom);

    /** Par code */
    Optional<Examen> findByCode(String code);
}
    

