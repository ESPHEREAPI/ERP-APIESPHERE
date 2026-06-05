package com.esphere.validation.repository;

import com.esphere.validation.entity.Visite;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisiteRepository extends JpaRepository<Visite, String> {
    Optional<Visite> findByCodeCourt(String codeCourt);
}