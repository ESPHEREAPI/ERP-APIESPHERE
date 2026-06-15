package service_administration_api.service;

import service_administration_api.DTO.stock.*;
import service_administration_api.entite.pooltpv.StockAttestation;

import java.time.OffsetDateTime;
import java.util.List;

public interface StockAttestationService {

    /**
     * Initialise un nouveau stock pour un bureau.
     * Lance une exception si le stock (officeCode + type + variante) existe déjà.
     */
    StockAttestationDTO initierStock(InitierStockRequest request, String createdBy);

    /**
     * Ajoute des attestations au stock (approvisionnement).
     * Crée le stock automatiquement si absent (mode global).
     */
    StockAttestationDTO approvisionner(String officeCode, ApprovisionnerRequest request, String createdBy);

    /**
     * Déduit les attestations consommées par une production.
     * Stratégie : cherche d'abord le stock spécifique (type+variante),
     * remonte au stock global si absent.
     * Lance StockInsuffisantException si stock < quantite.
     *
     * @param officeCode       bureau
     * @param certTypeCode     type d'attestation (peut être null)
     * @param certVariantCode  variante (peut être null)
     * @param quantite         nombre à déduire
     * @param referenceSource  référence de la production
     * @param createdBy        utilisateur déclencheur
     */
    StockAttestationDTO deduireStock(
        String officeCode,
        String certTypeCode,
        String certVariantCode,
        int    quantite,
        String referenceSource,
        String createdBy
    );

    /**
     * Annule une production : restitue les attestations au stock.
     * Cherche le mouvement DESTOCKAGE par referenceSource.
     */
    StockAttestationDTO annulerProduction(String officeCode, String referenceProduction, String createdBy);

    /**
     * Ajustement manuel (correction d'écart, inventaire).
     * delta > 0 → AJUSTEMENT_PLUS, delta < 0 → AJUSTEMENT_MOINS.
     */
    StockAttestationDTO ajuster(String officeCode, AjustementStockRequest request, String createdBy);

    /** Retourne tous les stocks d'un bureau. */
    List<StockAttestationDTO> getStocksParBureau(String officeCode);

    /** Retourne tous les stocks en alerte/critique/rupture. */
    List<StockAttestationDTO> getStocksEnAlerte();

    /** Retourne tous les stocks en alerte pour une organisation. */
    List<StockAttestationDTO> getStocksEnAlerteParOrg(String orgCode);

    /** Historique complet d'un bureau. */
    List<MouvementStockDTO> getHistorique(String officeCode);

    /** Historique filtré par période. */
    List<MouvementStockDTO> getHistoriqueParPeriode(
        String officeCode, OffsetDateTime debut, OffsetDateTime fin
    );

    /** Historique filtré par type de mouvement. */
    List<MouvementStockDTO> getHistoriqueParType(String officeCode, String typeMouvement);

    /** Résumé stock courant (stock brut — sans conversion DTO). */
    StockAttestation getStockBrut(String officeCode, String certTypeCode, String certVariantCode);
}
