package com.esphere.bonmanuel.repository;

import com.esphere.bonmanuel.entity.BonManuelLigne;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BonManuelLigneRepository extends JpaRepository<BonManuelLigne, Integer> {

    @Query("SELECT l FROM BonManuelLigne l WHERE l.bonManuel.id = :bonManuelId")
    List<BonManuelLigne> findByBonManuel(@Param("bonManuelId") Integer bonManuelId);
}