/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.esphere.validation.repository;

import com.esphere.validation.entity.Medicament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentRepository
        extends JpaRepository<Medicament, Integer> {

    /** Recherche insensible à la casse — utilisé par rechercherOuCreer() */
    Optional<Medicament> findByNomIgnoreCase(String nom);

    /** Liste les médicaments actifs (statut=1, supprime=-1) */
    List<Medicament> findByStatutAndSupprime(
            String statut, String supprime);

    /** Recherche par prestataire (si besoin de filtrer par clinique) */
    List<Medicament> findByPrestataireIdAndStatutAndSupprime(
            String prestataireId,
            String statut,
            String supprime);
}
