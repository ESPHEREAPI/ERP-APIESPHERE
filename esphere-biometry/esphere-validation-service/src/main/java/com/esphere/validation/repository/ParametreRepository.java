package com.esphere.validation.repository;

import com.esphere.validation.entity.Parametre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Integer> {
    Optional<Parametre> findByCle(String cle);
}
