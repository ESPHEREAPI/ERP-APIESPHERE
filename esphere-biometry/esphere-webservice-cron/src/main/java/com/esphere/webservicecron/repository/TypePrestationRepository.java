package com.esphere.webservicecron.repository;

import com.esphere.webservicecron.entity.TypePrestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypePrestationRepository extends JpaRepository<TypePrestation, String> {
}
