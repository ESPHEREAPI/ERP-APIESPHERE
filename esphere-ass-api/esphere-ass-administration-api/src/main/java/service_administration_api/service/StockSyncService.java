package service_administration_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import service_administration_api.DTO.stock.AjustementStockRequest;
import service_administration_api.DTO.stock.ExternalUsageResponse;
import service_administration_api.DTO.stock.InitierStockRequest;
import service_administration_api.entite.pooltpv.Infos_AdministrateurAgencePayLoad;
import service_administration_api.repository.poolTPV.Infos_AdministrateurAgencePayLoadRepository;
import service_administration_api.repository.poolTPV.StockAttestationRepository;

import java.util.List;
import org.springframework.context.annotation.Lazy;

/**
 * Synchronise le stock local avec ppeatt-api, bureau par bureau.
 *
 * Algorithme :
 *  1. Récupère un représentant (username) pour chaque office_code distinct
 *     dans ZEN_INFOS_ADMIN_AGENCE.
 *  2. Pour chaque bureau : génère un token PoolTPV avec ce username.
 *  3. Appelle GET /statistics/usage avec ce token.
 *  4. Filtre la réponse sur l'office_code du bureau.
 *  5. Pour chaque cert_type :
 *     - stock absent → initierStock (quantité = available externe)
 *     - stock présent et delta ≠ 0 → ajuster
 */
@Service
@Slf4j
public class StockSyncService {

    private final Infos_AdministrateurAgencePayLoadRepository agenceRepo;
    private final StockAttestationRepository                  stockRepo;
    private final StockAttestationService                     stockService;
    private final CertificateService                          certificateService;
    private final RestTemplate                                restTemplate;
    private final ObjectMapper                                objectMapper;

    @Value("${api.external.url.statistics}")
    private String statisticsUrl;

    private volatile StockSyncResult lastResult = null;


    public StockSyncService(
            Infos_AdministrateurAgencePayLoadRepository agenceRepo,
            StockAttestationRepository stockRepo,
            StockAttestationService stockService,
            @Lazy CertificateService certificateService,
            RestTemplate restTemplate,
            ObjectMapper objectMapper
    ) {
        this.agenceRepo         = agenceRepo;
        this.stockRepo          = stockRepo;
        this.stockService       = stockService;
        this.certificateService = certificateService;
        this.restTemplate       = restTemplate;
        this.objectMapper       = objectMapper;
    }

    // ════════════════════════════════════════════════════════════════
    // CRON — toutes les heures (configurable)
    // ════════════════════════════════════════════════════════════════

    @Scheduled(cron = "${stock.sync.cron:0 0 * * * *}")
    public void syncScheduled() {
        log.info("[STOCK-SYNC] Synchronisation planifiée démarrée");
        syncNow();
    }

    // ════════════════════════════════════════════════════════════════
    // SYNCHRONISATION PRINCIPALE
    // ════════════════════════════════════════════════════════════════

    public StockSyncResult syncNow() {
        int created = 0, updated = 0, skipped = 0, errors = 0;
        String lastError = null;

        // 1. Un représentant par bureau
        List<Infos_AdministrateurAgencePayLoad> bureaux = agenceRepo.findOneRepresentantParBureau();

        if (bureaux.isEmpty()) {
            log.warn("[STOCK-SYNC] Aucun bureau trouvé dans ZEN_INFOS_ADMIN_AGENCE — sync annulée");
            lastResult = new StockSyncResult(0, 0, 0, 1,
                    "Aucun bureau enregistré dans la base de données");
            return lastResult;
        }

        log.info("[STOCK-SYNC] {} bureau(x) à synchroniser", bureaux.size());

        // 2. Traitement bureau par bureau
        for (Infos_AdministrateurAgencePayLoad agence : bureaux) {

            String officeCode = agence.getOffice_code();
            String officeName = agence.getLibelleAgence() != null ? agence.getLibelleAgence() : officeCode;
            String username   = agence.getUsername();

            log.info("[STOCK-SYNC] Bureau [{} — {}] via utilisateur [{}]",
                    officeCode, officeName, username);

            try {
                // 3. Token PoolTPV pour ce bureau
                String token = obtenirToken(username, officeCode);
                if (token == null) {
                    log.warn("[STOCK-SYNC] Token indisponible pour [{}/{}] — bureau ignoré",
                            officeCode, username);
                    errors++;
                    lastError = "Token indisponible pour le bureau " + officeCode;
                    continue;
                }

                // 4. Appel API externe
                ExternalUsageResponse.ExternalUsageData data = fetchExternalStats(token);
                if (data == null || data.items() == null) {
                    log.warn("[STOCK-SYNC] Aucune donnée reçue pour le bureau [{}]", officeCode);
                    errors++;
                    lastError = "Aucune donnée ppeatt-api pour " + officeCode;
                    continue;
                }

                // 5. Filtrer sur ce bureau dans la réponse
                ExternalUsageResponse.ExternalOfficeUsage officeData = data.items().stream()
                        .filter(i -> officeCode.equals(i.officeCode()))
                        .findFirst()
                        .orElse(null);

                if (officeData == null || officeData.values() == null || officeData.values().isEmpty()) {
                    log.info("[STOCK-SYNC] Bureau [{}] absent de la réponse ppeatt-api — ignoré", officeCode);
                    skipped++;
                    continue;
                }

                // 6. Syncer chaque type de certificat
                for (ExternalUsageResponse.ExternalCertTypeUsage certUsage : officeData.values()) {
                    String certTypeCode = certUsage.certificateType();
                    int    extAvailable = certUsage.details().available();

                    try {
                        long count = stockRepo.countByOfficeAndTypes(officeCode, certTypeCode, null);

                        if (count == 0) {
                            // ── CRÉATION ──────────────────────────────────
                            InitierStockRequest req = new InitierStockRequest();
                            req.setOfficeCode(officeCode);
                            req.setOfficeName(officeName);
                            req.setCertTypeCode(certTypeCode);
                            req.setSeuilAlerte(50);
                            req.setSeuilCritique(10);
                            req.setQuantiteInitiale(extAvailable);
                            req.setMotif("Initialisation automatique depuis ppeatt-api");
                            stockService.initierStock(req, "SYSTEM-SYNC");
                            log.info("[STOCK-SYNC] ✓ Créé [{}/{}] = {}", officeCode, certTypeCode, extAvailable);
                            created++;

                        } else {
                            // ── MISE À JOUR ────────────────────────────────
                            var stockOpt = stockRepo.findByOfficeAndType(officeCode, certTypeCode, null);
                            if (stockOpt.isEmpty()) { skipped++; continue; }

                            int localDispo = stockOpt.get().getQuantiteDisponible() != null
                                    ? stockOpt.get().getQuantiteDisponible() : 0;
                            int delta = extAvailable - localDispo;

                            if (delta == 0) { skipped++; continue; }

                            AjustementStockRequest ajReq = new AjustementStockRequest();
                            ajReq.setCertTypeCode(certTypeCode);
                            ajReq.setDelta(delta);
                            ajReq.setMotif("Ajustement auto ppeatt-api : externe=" + extAvailable
                                    + " local=" + localDispo);
                            stockService.ajuster(officeCode, ajReq, "SYSTEM-SYNC");
                            log.info("[STOCK-SYNC] ✓ Ajusté [{}/{}] {} → {} (Δ={})",
                                    officeCode, certTypeCode, localDispo, extAvailable, delta);
                            updated++;
                        }

                    } catch (Exception e) {
                        log.error("[STOCK-SYNC] Erreur sur [{}/{}] : {}", officeCode, certTypeCode, e.getMessage());
                        errors++;
                        lastError = e.getMessage();
                    }
                }

            } catch (Exception e) {
                log.error("[STOCK-SYNC] Erreur bureau [{}] : {}", officeCode, e.getMessage());
                errors++;
                lastError = e.getMessage();
            }
        }

        log.info("[STOCK-SYNC] Terminé — créés={} mis-à-jour={} ignorés={} erreurs={}",
                created, updated, skipped, errors);

        lastResult = new StockSyncResult(created, updated, skipped, errors, lastError);
        return lastResult;
    }

    public StockSyncResult getLastResult() {
        return lastResult;
    }

    // ════════════════════════════════════════════════════════════════
    // SYNC À LA CONNEXION — déclenché si le bureau n'est pas initialisé
    // ════════════════════════════════════════════════════════════════

    /**
     * Appelé juste après un login réussi.
     * Si aucune ligne de stock n'existe pour ce bureau, déclenche une sync
     * immédiate en arrière-plan (non bloquant pour la réponse de login).
     */
    @org.springframework.scheduling.annotation.Async
    public void syncBureauSiNonInitialise(String officeCode, String username) {
        long count = stockRepo.findByOfficeCode(officeCode).size();
        if (count > 0) {
            log.debug("[STOCK-SYNC] Bureau [{}] déjà initialisé ({} ligne(s)) — sync ignorée", officeCode, count);
            return;
        }

        log.info("[STOCK-SYNC] Bureau [{}] non initialisé — sync déclenchée à la connexion de [{}]",
                officeCode, username);
        try {
            String token = obtenirToken(username, officeCode);
            if (token == null) {
                log.warn("[STOCK-SYNC] Token indisponible pour [{}/{}] à la connexion", officeCode, username);
                return;
            }

            ExternalUsageResponse.ExternalUsageData data = fetchExternalStats(token);
            if (data == null || data.items() == null) return;

            ExternalUsageResponse.ExternalOfficeUsage officeData = data.items().stream()
                    .filter(i -> officeCode.equals(i.officeCode()))
                    .findFirst().orElse(null);

            if (officeData == null || officeData.values() == null) {
                log.warn("[STOCK-SYNC] Bureau [{}] absent de la réponse ppeatt-api", officeCode);
                return;
            }

            String officeName = officeData.name() != null ? officeData.name() : officeCode;
            int created = 0;

            for (ExternalUsageResponse.ExternalCertTypeUsage certUsage : officeData.values()) {
                try {
                    InitierStockRequest req = new InitierStockRequest();
                    req.setOfficeCode(officeCode);
                    req.setOfficeName(officeName);
                    req.setCertTypeCode(certUsage.certificateType());
                    req.setSeuilAlerte(50);
                    req.setSeuilCritique(10);
                    req.setQuantiteInitiale(certUsage.details().available());
                    req.setMotif("Initialisation automatique à la connexion de " + username);
                    stockService.initierStock(req, username);
                    created++;
                } catch (Exception e) {
                    log.error("[STOCK-SYNC] Erreur init [{}/{}] : {}",
                            officeCode, certUsage.certificateType(), e.getMessage());
                }
            }

            log.info("[STOCK-SYNC] Bureau [{}] initialisé : {} type(s) créé(s)", officeCode, created);

        } catch (Exception e) {
            log.error("[STOCK-SYNC] Erreur sync à la connexion [{}] : {}", officeCode, e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════════
    // TOKEN PAR BUREAU — force un nouveau login si absent
    // ════════════════════════════════════════════════════════════════

    private String obtenirToken(String username, String officeCode) {
        try {
            // Forcer un login frais pour ce bureau (chaque bureau a son propre contexte)
            certificateService.JwtLoginCertificate(username);
            return certificateService.ensureTokenAndGet(username);
        } catch (Exception e) {
            log.warn("[STOCK-SYNC] Login PoolTPV échoué pour [{}/{}] : {}",
                    officeCode, username, e.getMessage());
            return null;
        }
    }

    // ════════════════════════════════════════════════════════════════
    // APPEL API EXTERNE
    // ════════════════════════════════════════════════════════════════

    private ExternalUsageResponse.ExternalUsageData fetchExternalStats(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange(
                statisticsUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                ExternalUsageResponse parsed = objectMapper.readValue(
                        response.getBody(), ExternalUsageResponse.class);
                return parsed != null ? parsed.data() : null;
            } catch (Exception e) {
                log.error("[STOCK-SYNC] Parse erreur réponse : {}", e.getMessage());
            }
        }
        return null;
    }

    // ════════════════════════════════════════════════════════════════
    // DTO résultat exposé via endpoint
    // ════════════════════════════════════════════════════════════════

    public record StockSyncResult(
            int    created,
            int    updated,
            int    skipped,
            int    errors,
            String errorMessage
    ) {}
}
