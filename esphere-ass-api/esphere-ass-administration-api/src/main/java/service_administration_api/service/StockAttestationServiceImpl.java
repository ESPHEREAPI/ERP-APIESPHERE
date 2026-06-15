package service_administration_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service_administration_api.DTO.stock.*;
import service_administration_api.entite.pooltpv.MouvementStock;
import service_administration_api.entite.pooltpv.StockAttestation;
import service_administration_api.enums.TypeMouvement;
import service_administration_api.exception.StockInsuffisantException;
import service_administration_api.repository.poolTPV.MouvementStockRepository;
import service_administration_api.repository.poolTPV.StockAttestationRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StockAttestationServiceImpl implements StockAttestationService {

    private final StockAttestationRepository stockRepo;
    private final MouvementStockRepository   mouvementRepo;

    public StockAttestationServiceImpl(
        StockAttestationRepository stockRepo,
        MouvementStockRepository   mouvementRepo
    ) {
        this.stockRepo    = stockRepo;
        this.mouvementRepo = mouvementRepo;
    }

    // ────────────────────────────────────────────────────────────
    // INIT
    // ────────────────────────────────────────────────────────────

    @Override
    public StockAttestationDTO initierStock(InitierStockRequest req, String createdBy) {
        if (stockRepo.countByOfficeAndTypes(
                req.getOfficeCode(), req.getCertTypeCode(), req.getCertVariantCode()) > 0) {
            throw new IllegalArgumentException(
                "Un stock existe déjà pour ce bureau / type / variante : "
                + req.getOfficeCode() + " / " + req.getCertTypeCode() + " / " + req.getCertVariantCode()
            );
        }

        StockAttestation stock = new StockAttestation();
        stock.setOfficeCode(req.getOfficeCode());
        stock.setOfficeName(req.getOfficeName());
        stock.setOrgCode(req.getOrgCode());
        stock.setCertTypeCode(req.getCertTypeCode());
        stock.setCertTypeName(req.getCertTypeName());
        stock.setCertVariantCode(req.getCertVariantCode());
        stock.setCertVariantName(req.getCertVariantName());
        stock.setSeuilAlerte(req.getSeuilAlerte()   != null ? req.getSeuilAlerte()   : 50);
        stock.setSeuilCritique(req.getSeuilCritique() != null ? req.getSeuilCritique() : 10);
        stock.setQuantiteDisponible(0);
        stock.setQuantiteReservee(0);
        stock.setQuantiteTotaleApprovisionnee(0);
        stock.setQuantiteTotalConsommee(0);
        stock.setCreatedBy(createdBy);
        stock.setCreatedAt(OffsetDateTime.now());
        stock.setUpdatedAt(OffsetDateTime.now());
        stock.recalculerStatut();
        stock = stockRepo.save(stock);

        // Si une quantité initiale est fournie, l'enregistrer comme approvisionnement
        if (req.getQuantiteInitiale() != null && req.getQuantiteInitiale() > 0) {
            appliquerMouvement(stock, TypeMouvement.APPROVISIONNEMENT,
                req.getQuantiteInitiale(), "INIT", req.getMotif(), createdBy);
        }

        return StockAttestationDTO.from(stock);
    }

    // ────────────────────────────────────────────────────────────
    // APPROVISIONNEMENT
    // ────────────────────────────────────────────────────────────

    @Override
    public StockAttestationDTO approvisionner(String officeCode, ApprovisionnerRequest req, String createdBy) {
        if (req.getQuantite() == null || req.getQuantite() <= 0) {
            throw new IllegalArgumentException("La quantité d'approvisionnement doit être positive.");
        }

        StockAttestation stock = trouverOuCreerStockGlobal(
            officeCode, req.getCertTypeCode(), req.getCertVariantCode()
        );

        appliquerMouvement(stock, TypeMouvement.APPROVISIONNEMENT,
            req.getQuantite(), req.getReferenceSource(), req.getMotif(), createdBy);

        return StockAttestationDTO.from(stock);
    }

    // ────────────────────────────────────────────────────────────
    // DEDUCTION (appelé automatiquement après chaque production)
    // ────────────────────────────────────────────────────────────

    @Override
    public StockAttestationDTO deduireStock(
        String officeCode, String certTypeCode, String certVariantCode,
        int quantite, String referenceSource, String createdBy
    ) {
        // 1. Cherche stock spécifique (type + variante)
        StockAttestation stock = stockRepo.findByOfficeAndType(officeCode, certTypeCode, certVariantCode)
            // 2. Remonte au stock global si absent
            .orElseGet(() -> stockRepo.findByOfficeCodeAndCertTypeCodeIsNull(officeCode)
                .orElseThrow(() -> new StockInsuffisantException(officeCode, certTypeCode, quantite, 0))
            );

        if (!stock.peutDeduire(quantite)) {
            throw new StockInsuffisantException(
                officeCode, certTypeCode, quantite, stock.getQuantiteDisponible()
            );
        }

        appliquerMouvement(stock, TypeMouvement.DESTOCKAGE, quantite, referenceSource, null, createdBy);
        return StockAttestationDTO.from(stock);
    }

    // ────────────────────────────────────────────────────────────
    // ANNULATION PRODUCTION
    // ────────────────────────────────────────────────────────────

    @Override
    public StockAttestationDTO annulerProduction(String officeCode, String referenceProduction, String createdBy) {
        MouvementStock destockage = mouvementRepo
            .findByOfficeCodeAndReferenceSourceAndTypeMouvement(
                officeCode, referenceProduction, TypeMouvement.DESTOCKAGE)
            .orElseThrow(() -> new IllegalArgumentException(
                "Aucun déstockage trouvé pour la production : " + referenceProduction));

        StockAttestation stock = destockage.getStockAttestation();
        appliquerMouvement(stock, TypeMouvement.ANNULATION,
            destockage.getQuantite(), referenceProduction,
            "Annulation production " + referenceProduction, createdBy);

        return StockAttestationDTO.from(stock);
    }

    // ────────────────────────────────────────────────────────────
    // AJUSTEMENT MANUEL
    // ────────────────────────────────────────────────────────────

    @Override
    public StockAttestationDTO ajuster(String officeCode, AjustementStockRequest req, String createdBy) {
        if (req.getDelta() == null || req.getDelta() == 0) {
            throw new IllegalArgumentException("Le delta d'ajustement ne peut pas être zéro.");
        }

        StockAttestation stock = stockRepo.findByOfficeAndType(
                officeCode, req.getCertTypeCode(), req.getCertVariantCode())
            .orElseThrow(() -> new IllegalArgumentException(
                "Stock introuvable pour ce bureau / type / variante."));

        TypeMouvement type = req.getDelta() > 0 ? TypeMouvement.AJUSTEMENT_PLUS : TypeMouvement.AJUSTEMENT_MOINS;
        appliquerMouvement(stock, type, Math.abs(req.getDelta()), null, req.getMotif(), createdBy);
        return StockAttestationDTO.from(stock);
    }

    // ────────────────────────────────────────────────────────────
    // LECTURES
    // ────────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<StockAttestationDTO> getStocksParBureau(String officeCode) {
        return stockRepo.findByOfficeCode(officeCode).stream()
            .map(StockAttestationDTO::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockAttestationDTO> getStocksEnAlerte() {
        return stockRepo.findStocksEnAlerte().stream()
            .map(StockAttestationDTO::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockAttestationDTO> getStocksEnAlerteParOrg(String orgCode) {
        return stockRepo.findStocksEnAlerteByOrg(orgCode).stream()
            .map(StockAttestationDTO::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockDTO> getHistorique(String officeCode) {
        return mouvementRepo.findByOfficeCodeOrderByCreatedAtDesc(officeCode).stream()
            .map(MouvementStockDTO::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockDTO> getHistoriqueParPeriode(
        String officeCode, OffsetDateTime debut, OffsetDateTime fin
    ) {
        return mouvementRepo.findByOfficeCodeAndPeriode(officeCode, debut, fin).stream()
            .map(MouvementStockDTO::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MouvementStockDTO> getHistoriqueParType(String officeCode, String typeMouvement) {
        TypeMouvement type = TypeMouvement.valueOf(typeMouvement.toUpperCase());
        return mouvementRepo.findByOfficeCodeAndTypeMouvementOrderByCreatedAtDesc(officeCode, type).stream()
            .map(MouvementStockDTO::from)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public StockAttestation getStockBrut(String officeCode, String certTypeCode, String certVariantCode) {
        return stockRepo.findByOfficeAndType(officeCode, certTypeCode, certVariantCode)
            .orElseGet(() -> stockRepo.findByOfficeCodeAndCertTypeCodeIsNull(officeCode).orElse(null));
    }

    // ────────────────────────────────────────────────────────────
    // MÉTHODE PRIVÉE — applique un mouvement et met à jour le stock
    // ────────────────────────────────────────────────────────────

    private void appliquerMouvement(
        StockAttestation stock,
        TypeMouvement    type,
        int              quantite,
        String           referenceSource,
        String           motif,
        String           createdBy
    ) {
        int avant = stock.getQuantiteDisponible() != null ? stock.getQuantiteDisponible() : 0;
        int apres;

        switch (type) {
            case APPROVISIONNEMENT:
            case AJUSTEMENT_PLUS:
            case ANNULATION:
                apres = avant + quantite;
                stock.setQuantiteTotaleApprovisionnee(
                    (stock.getQuantiteTotaleApprovisionnee() != null ? stock.getQuantiteTotaleApprovisionnee() : 0) + quantite
                );
                break;
            case DESTOCKAGE:
            case AJUSTEMENT_MOINS:
                apres = avant - quantite;
                stock.setQuantiteTotalConsommee(
                    (stock.getQuantiteTotalConsommee() != null ? stock.getQuantiteTotalConsommee() : 0) + quantite
                );
                break;
            default:
                apres = avant;
        }

        stock.setQuantiteDisponible(Math.max(0, apres));
        stock.setUpdatedAt(OffsetDateTime.now());
        stock.recalculerStatut();
        stockRepo.save(stock);

        // Enregistrer le mouvement
        MouvementStock mouv = new MouvementStock();
        mouv.setStockAttestation(stock);
        mouv.setOfficeCode(stock.getOfficeCode());
        mouv.setCertTypeCode(stock.getCertTypeCode());
        mouv.setCertVariantCode(stock.getCertVariantCode());
        mouv.setTypeMouvement(type);
        mouv.setQuantite(quantite);
        mouv.setQuantiteAvant(avant);
        mouv.setQuantiteApres(Math.max(0, apres));
        mouv.setReferenceSource(referenceSource);
        mouv.setMotif(motif);
        mouv.setCreatedAt(OffsetDateTime.now());
        mouv.setCreatedBy(createdBy);
        mouvementRepo.save(mouv);
    }

    // ────────────────────────────────────────────────────────────
    // Créer un stock global à la volée si absent
    // ────────────────────────────────────────────────────────────

    private StockAttestation trouverOuCreerStockGlobal(
        String officeCode, String certTypeCode, String certVariantCode
    ) {
        Optional<StockAttestation> existing =
            stockRepo.findByOfficeAndType(officeCode, certTypeCode, certVariantCode);
        if (existing.isPresent()) return existing.get();

        // Si type spécifique non trouvé, essaie le stock global
        if (certTypeCode != null) {
            Optional<StockAttestation> global =
                stockRepo.findByOfficeCodeAndCertTypeCodeIsNull(officeCode);
            if (global.isPresent()) return global.get();
        }

        // Crée un stock global pour ce bureau
        StockAttestation nouveau = new StockAttestation();
        nouveau.setOfficeCode(officeCode);
        nouveau.setCertTypeCode(certTypeCode);
        nouveau.setCertVariantCode(certVariantCode);
        nouveau.setSeuilAlerte(50);
        nouveau.setSeuilCritique(10);
        nouveau.setQuantiteDisponible(0);
        nouveau.setQuantiteReservee(0);
        nouveau.setQuantiteTotaleApprovisionnee(0);
        nouveau.setQuantiteTotalConsommee(0);
        nouveau.setCreatedAt(OffsetDateTime.now());
        nouveau.setUpdatedAt(OffsetDateTime.now());
        nouveau.recalculerStatut();
        return stockRepo.save(nouveau);
    }
}
