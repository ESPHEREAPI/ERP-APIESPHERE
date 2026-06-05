/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.esphere.validation.repository;

import com.esphere.validation.entity.TypePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TypePrestationRepository
        extends JpaRepository<TypePrestation, String> {

    /** Types affichés (affiche=1) */
    List<TypePrestation> findByAffiche(int affiche);

    /** Par catégorie */
    List<TypePrestation> findByCategorieAndAffiche(
            String categorie, int affiche);
}
    

