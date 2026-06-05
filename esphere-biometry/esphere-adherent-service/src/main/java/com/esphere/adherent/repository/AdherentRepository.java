package com.esphere.adherent.repository;

import com.esphere.adherent.entity.Adherent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdherentRepository extends JpaRepository<Adherent, String> {

    // Cherche un adhérent actif par son code
    @Query("SELECT a FROM Adherent a WHERE a.codeAdherent = :code AND a.statut = '1'")
    Optional<Adherent> findActiveByCode(@Param("code") String code);

    // Recherche par nom (insensible à la casse)
    @Query("SELECT a FROM Adherent a WHERE LOWER(a.assurePrincipal) LIKE LOWER(CONCAT('%', :nom, '%')) AND a.statut = '1'")
    List<Adherent> searchByNom(@Param("nom") String nom);

    // Recherche par police/contrat
    @Query("SELECT a FROM Adherent a WHERE a.police = :police AND a.statut = '1'")
    List<Adherent> findByPolice(@Param("police") String police);
}