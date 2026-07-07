package service_administration_api.DTO.stock;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Réponse complète de l'audit de stock pour un bureau.
 * Combine les données locales (Oracle) et l'état de l'API externe (ppeatt-api).
 */
public record StockAuditResponse(

        String officeCode,
        String officeName,
        OffsetDateTime auditDate,

        /**
         * Statut global de synchronisation :
         * SYNCHRONISE | ATTENTION | DIVERGENCE | SANS_DONNEE_EXTERNE
         */
        String statutGlobal,

        // ── Totaux locaux ──────────────────────────────────────────
        int localTotalDisponible,
        int localTotalConsommation,
        int localTotalAppro,

        // ── Totaux externes (null si API indisponible) ─────────────
        Integer externalTotalAttributed,
        Integer externalTotalUsed,
        Integer externalTotalAvailable,

        // ── Deltas globaux ─────────────────────────────────────────
        Integer deltaDisponible,
        Integer deltaConsommation,

        // ── Détail par type de certificat ──────────────────────────
        List<StockAuditItemDTO> items,

        // ── 20 derniers mouvements locaux ──────────────────────────
        List<MouvementStockDTO> mouvementsRecents,

        /** Message d'erreur si l'API externe était injoignable */
        String externalApiError
) {}
