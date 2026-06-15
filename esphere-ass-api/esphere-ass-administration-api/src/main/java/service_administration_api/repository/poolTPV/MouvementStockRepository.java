package service_administration_api.repository.poolTPV;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import service_administration_api.entite.pooltpv.MouvementStock;
import service_administration_api.enums.TypeMouvement;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {

    /** Historique complet d'un bureau, du plus récent au plus ancien. */
    List<MouvementStock> findByOfficeCodeOrderByCreatedAtDesc(String officeCode);

    /** Historique filtré par type de mouvement. */
    List<MouvementStock> findByOfficeCodeAndTypeMouvementOrderByCreatedAtDesc(
        String officeCode, TypeMouvement typeMouvement
    );

    /** Historique entre deux dates. */
    @Query("""
        SELECT m FROM MouvementStock m
        WHERE m.officeCode = :officeCode
          AND m.createdAt BETWEEN :debut AND :fin
        ORDER BY m.createdAt DESC
        """)
    List<MouvementStock> findByOfficeCodeAndPeriode(
        @Param("officeCode") String officeCode,
        @Param("debut")      OffsetDateTime debut,
        @Param("fin")        OffsetDateTime fin
    );

    /** Historique filtré par type + période. */
    @Query("""
        SELECT m FROM MouvementStock m
        WHERE m.officeCode    = :officeCode
          AND m.typeMouvement = :type
          AND m.createdAt BETWEEN :debut AND :fin
        ORDER BY m.createdAt DESC
        """)
    List<MouvementStock> findByOfficeCodeTypeAndPeriode(
        @Param("officeCode") String officeCode,
        @Param("type")       TypeMouvement type,
        @Param("debut")      OffsetDateTime debut,
        @Param("fin")        OffsetDateTime fin
    );

    /** Retrouver le mouvement lié à une production (pour annulation). */
    Optional<MouvementStock> findByOfficeCodeAndReferenceSourceAndTypeMouvement(
        String officeCode, String referenceSource, TypeMouvement typeMouvement
    );

    /** Somme des approvisionnements d'un bureau sur une période. */
    @Query("""
        SELECT COALESCE(SUM(m.quantite), 0) FROM MouvementStock m
        WHERE m.officeCode = :officeCode
          AND m.typeMouvement = 'APPROVISIONNEMENT'
          AND m.createdAt BETWEEN :debut AND :fin
        """)
    Integer sumApprovisionnements(
        @Param("officeCode") String officeCode,
        @Param("debut")      OffsetDateTime debut,
        @Param("fin")        OffsetDateTime fin
    );

    /** Somme des déstockages d'un bureau sur une période. */
    @Query("""
        SELECT COALESCE(SUM(m.quantite), 0) FROM MouvementStock m
        WHERE m.officeCode = :officeCode
          AND m.typeMouvement = 'DESTOCKAGE'
          AND m.createdAt BETWEEN :debut AND :fin
        """)
    Integer sumDestockages(
        @Param("officeCode") String officeCode,
        @Param("debut")      OffsetDateTime debut,
        @Param("fin")        OffsetDateTime fin
    );
}
