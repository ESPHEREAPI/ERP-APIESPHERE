package com.esphere.auth.repository;

import com.esphere.auth.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeRepository extends JpaRepository<Employe, Integer> {

    Optional<Employe> findByUtilisateurId(Integer utilisateurId);

    @Query("SELECT e FROM Employe e JOIN FETCH e.utilisateur u WHERE e.profil.id = :profilId AND u.statut = '1' AND u.supprime = '-1'")
    List<Employe> findByProfilIdAndStatut(@Param("profilId") Integer profilId);

    @Query("SELECT e FROM Employe e JOIN FETCH e.utilisateur u WHERE e.prestataireId = :prestataireId AND u.statut = '1' AND u.supprime = '-1'")
    List<Employe> findByPrestataireId(@Param("prestataireId") String prestataireId);
}
