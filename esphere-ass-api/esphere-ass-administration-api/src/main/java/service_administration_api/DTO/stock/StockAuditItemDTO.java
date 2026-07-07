package service_administration_api.DTO.stock;

/**
 * Ligne de comparaison par type de certificat dans l'audit de stock.
 * Regroupe les données locales (base Oracle) et les données externes (ppeatt-api).
 */
public record StockAuditItemDTO(

        /** Type de certificat (ex: "cima", "carte-rose") — null si stock global */
        String certTypeCode,

        // ── Stock local (base Oracle) ──────────────────────────────
        int localApprovisionnement,
        int localDisponible,
        int localConsommation,

        // ── Stock externe (ppeatt-api) — null si API indisponible ──
        Integer externalAttributed,
        Integer externalAvailable,
        Integer externalUsed,

        // ── Deltas (local − externe) — null si externe indisponible ─
        Integer deltaDisponible,    // localDisponible  − externalAvailable
        Integer deltaConsommation,  // localConsommation − externalUsed

        // ── Résultat de comparaison ─────────────────────────────────
        /** SYNCHRONISE | ATTENTION | DIVERGENCE | LOCAL_ONLY | EXTERNE_ONLY */
        String statut
) {}
