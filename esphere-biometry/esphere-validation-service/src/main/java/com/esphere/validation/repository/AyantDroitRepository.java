package com.esphere.validation.repository;

import com.esphere.validation.entity.AyantDroit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AyantDroitRepository extends JpaRepository<AyantDroit, String> {

    Optional<AyantDroit> findByCodeAyantDroit(String codeAyantDroit);
}
