package com.esphere.validation.repository;

import com.esphere.validation.entity.BonManuel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BonManuelConsommationRepository
        extends JpaRepository<BonManuel, Integer> {

    // ════════════════════════════════════════════════════════════════
    // BONS MANUELS ENCAISSÉS
    // statut = 'encaisse'
    // Part Zenithe = montant_confirme si renseigné sinon montant_proforma
    // ════════════════════════════════════════════════════════════════

    @Query(value = """
        SELECT COALESCE(SUM(
            CASE WHEN b.montant_confirme IS NOT NULL AND b.montant_confirme > 0
                 THEN b.montant_confirme
                 ELSE b.montant_proforma END
        ), 0)
        FROM dbx45ty_bon_manuel b
        WHERE b.code_adherent   = :codeAdherent
          AND b.statut          = 'encaisse'
          AND b.supprime        = '-1'
          AND YEAR(b.date_creation) = :annee
    """, nativeQuery = true)
    Double sumBonsManuelsEncaisses(
        @Param("codeAdherent") String codeAdherent,
        @Param("annee") int annee);

    @Query(value = """
        SELECT COUNT(b.id)
        FROM dbx45ty_bon_manuel b
        WHERE b.code_adherent   = :codeAdherent
          AND b.statut          = 'encaisse'
          AND b.supprime        = '-1'
          AND YEAR(b.date_creation) = :annee
    """, nativeQuery = true)
    Long countBonsManuelsEncaisses(
        @Param("codeAdherent") String codeAdherent,
        @Param("annee") int annee);

    // ════════════════════════════════════════════════════════════════
    // BONS MANUELS EN COURS
    // statut IN ('en_attente', 'confirme')
    // ════════════════════════════════════════════════════════════════

    @Query(value = """
        SELECT COALESCE(SUM(
            CASE WHEN b.montant_confirme IS NOT NULL AND b.montant_confirme > 0
                 THEN b.montant_confirme
                 ELSE b.montant_proforma END
        ), 0)
        FROM dbx45ty_bon_manuel b
        WHERE b.code_adherent   = :codeAdherent
          AND b.statut          IN ('en_attente', 'confirme')
          AND b.supprime        = '-1'
          AND YEAR(b.date_creation) = :annee
    """, nativeQuery = true)
    Double sumBonsManuelsEnCours(
        @Param("codeAdherent") String codeAdherent,
        @Param("annee") int annee);
}