/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package service_administration_api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import service_administration_api.entite.Intermediaire;

/**
 * Repository JPA pour l'entité Intermediaire.
 * Base : Oracle 11g / Schéma ORASSADM
 *
 * Hérite de :
 *  - JpaRepository         → CRUD de base + pagination
 *  - JpaSpecificationExecutor → recherches dynamiques (filtres combinés)
 */
@Repository
public interface IntermediaireRepository extends JpaRepository<Intermediaire, Integer>,
                JpaSpecificationExecutor<Intermediaire> {
// ══════════════════════════════════════════════════════
    //  CHAMPS TERMINANT PAR "In" → @Query OBLIGATOIRE
    //  Spring Data interprète "In" comme mot-clé SQL IN(...)
    // ══════════════════════════════════════════════════════

    @Query("SELECT i FROM Intermediaire i WHERE UPPER(i.raiSocIn) = UPPER(:raiSocIn)")
    List<Intermediaire> findByRaiSocIn(@Param("raiSocIn") String raiSocIn);

    @Query("SELECT i FROM Intermediaire i WHERE UPPER(i.raiSocIn) LIKE UPPER(CONCAT('%', :keyword, '%'))")
    List<Intermediaire> findByRaiSocInContaining(@Param("keyword") String keyword);

    @Query("SELECT i FROM Intermediaire i WHERE i.codTypIn = :codTypIn")
    List<Intermediaire> findByCodTypIn(@Param("codTypIn") String codTypIn);

    @Query("SELECT i FROM Intermediaire i WHERE i.codTypIn = :codTypIn AND i.datFinAc IS NULL AND i.flagProd = 'O' ORDER BY i.raiSocIn")
    List<Intermediaire> findActifsByType(@Param("codTypIn") String codTypIn);

    // ── Existence
    @Query("SELECT COUNT(i) > 0 FROM Intermediaire i WHERE UPPER(i.raiSocIn) = UPPER(:raiSocIn)")
    boolean existsByRaiSocIn(@Param("raiSocIn") String raiSocIn);

    // ══════════════════════════════════════════════════════
    //  RECHERCHES SIMPLES (pas de suffixe "In" → méthodes dérivées OK)
    // ══════════════════════════════════════════════════════

    Optional<Intermediaire> findByNumeAgre(String numeAgre);
    Optional<Intermediaire> findByNumeImpo(String numeImpo);
    Optional<Intermediaire> findByNumeTva(String numeTva);
    Optional<Intermediaire> findByAdreMail(String adreMail);

    List<Intermediaire> findByCodeVill(Integer codeVill);
    List<Intermediaire> findByFlagProd(String flagProd);
    List<Intermediaire> findByFlagTest(String flagTest);
    List<Intermediaire> findByFlagPool(String flagPool);
    List<Intermediaire> findByFlagLien(String flagLien);
    List<Intermediaire> findByLieIntCo(Integer lieIntCo);
    List<Intermediaire> findByLieIntRe(Integer lieIntRe);
    List<Intermediaire> findByCodBanBa(Integer codBanBa);
    List<Intermediaire> findByCodAgeBa(Long codAgeBa);
    List<Intermediaire> findByCodBanBaAndCodAgeBa(Integer codBanBa, Long codAgeBa);
    List<Intermediaire> findByLienInte_CodeInte(Integer codeIntParent);
    List<Intermediaire> findByDateNomiBetween(Date debut, Date fin);
    List<Intermediaire> findByDatFinAcBefore(Date date);
    List<Intermediaire> findByDatFinAcIsNull();
    List<Intermediaire> findByDatFinAcIsNotNull();

    // ── Existence (champs sans "In" → OK en méthode dérivée)
    boolean existsByNumeAgre(String numeAgre);
    boolean existsByNumeImpo(String numeImpo);
    boolean existsByNumeTva(String numeTva);

    // ══════════════════════════════════════════════════════
    //  REQUÊTES JPQL PERSONNALISÉES
    // ══════════════════════════════════════════════════════

    @Query("""
        SELECT i FROM Intermediaire i
        WHERE (:codeVill IS NULL OR i.codeVill = :codeVill)
          AND (:flagProd IS NULL OR i.flagProd = :flagProd)
        ORDER BY i.raiSocIn
        """)
    List<Intermediaire> findByFiltres(
            @Param("codeVill") Integer codeVill,
            @Param("flagProd") String flagProd
    );

    @Query("""
        SELECT i FROM Intermediaire i
        LEFT JOIN FETCH i.lienInte parent
        WHERE i.codeInte = :codeInte
        """)
    Optional<Intermediaire> findByIdWithParent(@Param("codeInte") Integer codeInte);

    @Query("""
        SELECT i FROM Intermediaire i
        WHERE UPPER(i.raiSocIn) LIKE UPPER(CONCAT('%', :keyword, '%'))
           OR UPPER(i.abreInte) LIKE UPPER(CONCAT('%', :keyword, '%'))
           OR UPPER(i.adreMail) LIKE UPPER(CONCAT('%', :keyword, '%'))
        ORDER BY i.raiSocIn
        """)
    List<Intermediaire> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT i FROM Intermediaire i WHERE i.delaEnca > :seuilJours ORDER BY i.delaEnca DESC")
    List<Intermediaire> findByDelaEncaGreaterThan(@Param("seuilJours") Integer seuilJours);

    @Query("SELECT i FROM Intermediaire i WHERE i.numeLot IS NOT NULL")
    List<Intermediaire> findDiffuses();

    @Query("SELECT i.codTypIn, COUNT(i) FROM Intermediaire i GROUP BY i.codTypIn")
    List<Object[]> countByType();

    // ══════════════════════════════════════════════════════
    //  REQUÊTES NATIVES ORACLE
    // ══════════════════════════════════════════════════════

    @Query(value = "SELECT COUNT(*) FROM ORASSADM.INTERMEDIAIRE WHERE NUMEAGRE = :numeAgre", nativeQuery = true)
    int countByNumeAgreNative(@Param("numeAgre") String numeAgre);

    @Query(
        value = """
                SELECT CODEINTE, RAISOCIN, ABREINTE, CODTYPIN
                FROM ORASSADM.INTERMEDIAIRE
                WHERE DATFINAC IS NULL AND FLAGPROD = 'O'
                ORDER BY RAISOCIN
                """,
        nativeQuery = true
    )
    List<Object[]> findAllActifsForSelect();

    // ══════════════════════════════════════════════════════
    //  MISES À JOUR (@Modifying)
    // ══════════════════════════════════════════════════════

    @Modifying
    @Transactional
    @Query("UPDATE Intermediaire i SET i.datFinAc = :datFinAc WHERE i.codeInte = :codeInte")
    int desactiver(@Param("codeInte") Integer codeInte, @Param("datFinAc") Date datFinAc);

    @Modifying
    @Transactional
    @Query("UPDATE Intermediaire i SET i.numeLot = NULL WHERE i.codeInte = :codeInte")
    int resetNumeLot(@Param("codeInte") Integer codeInte);

    @Modifying
    @Transactional
    @Query("UPDATE Intermediaire i SET i.flagProd = :flagProd WHERE i.codTypIn = :codTypIn")
    int updateFlagProdByType(@Param("codTypIn") String codTypIn, @Param("flagProd") String flagProd);
    
     Optional<Intermediaire>findByCodeInte(Integer codeInte);
}
