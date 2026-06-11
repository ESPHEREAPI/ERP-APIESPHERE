package com.esphere.auth.repository;

import com.esphere.auth.entity.Profil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, Integer> {

    List<Profil> findByStatutAndSupprime(String statut, String supprime);

    Optional<Profil> findByCode(String code);
}
