package service_administration_api.repository.poolTPV;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.pooltpv.StockAttestation;

import java.util.List;
import java.util.Optional;

public interface StockAttestationRepository extends JpaRepository<StockAttestation, Long> {

    /** Stock spécifique (officeCode + type + variante). */
    @Query("""
        SELECT s FROM StockAttestation s
        WHERE s.officeCode = :officeCode
          AND (:certTypeCode    IS NULL OR s.certTypeCode    = :certTypeCode)
          AND (:certVariantCode IS NULL OR s.certVariantCode = :certVariantCode)
          AND (:certTypeCode    IS NOT NULL OR s.certTypeCode    IS NULL)
          AND (:certVariantCode IS NOT NULL OR s.certVariantCode IS NULL)
        """)
    Optional<StockAttestation> findByOfficeAndType(
        @Param("officeCode")      String officeCode,
        @Param("certTypeCode")    String certTypeCode,
        @Param("certVariantCode") String certVariantCode
    );

    /** Tous les stocks d'un bureau (global + tous types/variantes). */
    List<StockAttestation> findByOfficeCode(String officeCode);

    /** Stocks en alerte ou critique — pour le tableau de bord. */
    @Query("SELECT s FROM StockAttestation s WHERE s.statut IN ('ALERTE','CRITIQUE','RUPTURE')")
    List<StockAttestation> findStocksEnAlerte();

    /** Stocks en alerte d'une organisation. */
    @Query("SELECT s FROM StockAttestation s WHERE s.orgCode = :orgCode AND s.statut IN ('ALERTE','CRITIQUE','RUPTURE')")
    List<StockAttestation> findStocksEnAlerteByOrg(@Param("orgCode") String orgCode);

    /** Stock global d'un bureau (certTypeCode IS NULL). */
    Optional<StockAttestation> findByOfficeCodeAndCertTypeCodeIsNull(String officeCode);

    /**
     * Remplace existsBy... (génère FETCH FIRST ? ROWS ONLY, rejeté par Oracle).
     * COUNT ne produit pas de clause LIMIT — compatible toutes versions Oracle.
     */
    @Query("""
        SELECT COUNT(s) FROM StockAttestation s
        WHERE s.officeCode = :officeCode
          AND ((:certTypeCode IS NULL AND s.certTypeCode IS NULL) OR s.certTypeCode = :certTypeCode)
          AND ((:certVariantCode IS NULL AND s.certVariantCode IS NULL) OR s.certVariantCode = :certVariantCode)
        """)
    long countByOfficeAndTypes(
        @Param("officeCode")      String officeCode,
        @Param("certTypeCode")    String certTypeCode,
        @Param("certVariantCode") String certVariantCode
    );
}
