package service_administration_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import service_administration_api.DTO.stock.*;
import service_administration_api.entite.pooltpv.StockAttestation;
import service_administration_api.repository.poolTPV.MouvementStockRepository;
import service_administration_api.repository.poolTPV.StockAttestationRepository;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockAuditService {

    private final StockAttestationRepository stockRepo;
    private final MouvementStockRepository   mouvementRepo;
    private final CertificateService         certificateService;
    private final RestTemplate               restTemplate;
    private final ObjectMapper               objectMapper;

    @Value("${api.external.url.statistics}")
    private String statisticsUrl;

    public StockAuditService(
            StockAttestationRepository stockRepo,
            MouvementStockRepository mouvementRepo,
            @Lazy CertificateService certificateService,
            RestTemplate restTemplate,
            ObjectMapper objectMapper
    ) {
        this.stockRepo          = stockRepo;
        this.mouvementRepo      = mouvementRepo;
        this.certificateService = certificateService;
        this.restTemplate       = restTemplate;
        this.objectMapper       = objectMapper;
    }

    // ════════════════════════════════════════════════════════════════
    // POINT D'ENTRÉE PRINCIPAL
    // ════════════════════════════════════════════════════════════════

    @Transactional(readOnly = true)
    public StockAuditResponse getAudit(String officeCode, String username) {

        // 1. Données locales (Oracle)
        List<StockAttestation> localStocks = stockRepo.findByOfficeCode(officeCode);

        List<MouvementStockDTO> mouvements = mouvementRepo
                .findByOfficeCodeOrderByCreatedAtDesc(officeCode)
                .stream().limit(20)
                .map(MouvementStockDTO::from)
                .collect(Collectors.toList());

        // 2. Données externes (ppeatt-api)
        ExternalUsageResponse.ExternalOfficeUsage externalOffice = null;
        String  externalError             = null;
        Integer externalTotalAttributed   = null;
        Integer externalTotalUsed         = null;
        Integer externalTotalAvailable    = null;

        try {
            String token = certificateService.ensureTokenAndGet(username);
            if (token == null) {
                externalError = "Token indisponible — reconnectez-vous pour actualiser";
            } else {
                ExternalUsageResponse.ExternalUsageData externalData = fetchExternalStats(token, username);
                if (externalData != null) {
                    externalTotalAttributed = externalData.totalAttributed();
                    externalTotalUsed       = externalData.totalUsed();
                    externalTotalAvailable  = externalData.totalAvailable();
                    externalOffice = externalData.items() == null ? null
                            : externalData.items().stream()
                            .filter(i -> officeCode.equals(i.officeCode()))
                            .findFirst().orElse(null);
                }
            }
        } catch (Exception e) {
            log.warn("Erreur lors de l'appel aux statistiques externes [{}] : {}", officeCode, e.getMessage());
            externalError = e.getMessage();
        }

        // 3. Construction des items de comparaison par type
        List<StockAuditItemDTO> items = buildAuditItems(localStocks, externalOffice);

        // 4. Totaux locaux
        int localTotalDisponible   = sum(localStocks, StockAttestation::getQuantiteDisponible);
        int localTotalConsommation = sum(localStocks, StockAttestation::getQuantiteTotalConsommee);
        int localTotalAppro        = sum(localStocks, StockAttestation::getQuantiteTotaleApprovisionnee);

        // 5. Deltas globaux
        Integer deltaDisponible   = externalTotalAvailable != null ? localTotalDisponible   - externalTotalAvailable : null;
        Integer deltaConsommation = externalTotalUsed      != null ? localTotalConsommation - externalTotalUsed      : null;

        // 6. Statut global
        String statutGlobal = computeStatutGlobal(items, externalOffice, externalError);

        // 7. Nom du bureau
        String officeName = localStocks.isEmpty() ? officeCode
                : localStocks.stream()
                .filter(s -> s.getOfficeName() != null)
                .map(StockAttestation::getOfficeName)
                .findFirst().orElse(officeCode);

        return new StockAuditResponse(
                officeCode, officeName, OffsetDateTime.now(),
                statutGlobal,
                localTotalDisponible, localTotalConsommation, localTotalAppro,
                externalTotalAttributed, externalTotalUsed, externalTotalAvailable,
                deltaDisponible, deltaConsommation,
                items, mouvements, externalError
        );
    }

    // ════════════════════════════════════════════════════════════════
    // APPEL API EXTERNE — avec retry sur 401
    // ════════════════════════════════════════════════════════════════

    private ExternalUsageResponse.ExternalUsageData fetchExternalStats(String token, String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<String> response = restTemplate.exchange(
                statisticsUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class
        );

        // Token expiré → renouvellement et second essai
        if (response.getStatusCode().value() == 401) {
            log.warn("Token expiré pour les statistiques [{}], renouvellement...", username);
            try {
                String newToken = certificateService.ensureTokenAndGet(username);
                if (newToken != null) {
                    headers.set("Authorization", "Bearer " + newToken);
                    response = restTemplate.exchange(
                            statisticsUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class
                    );
                }
            } catch (Exception e) {
                log.warn("Renouvellement token impossible : {}", e.getMessage());
                return null;
            }
        }

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            try {
                ExternalUsageResponse parsed = objectMapper.readValue(response.getBody(), ExternalUsageResponse.class);
                return parsed != null ? parsed.data() : null;
            } catch (Exception e) {
                log.warn("Impossible de parser la réponse des statistiques externes : {}", e.getMessage());
            }
        }
        return null;
    }

    // ════════════════════════════════════════════════════════════════
    // CONSTRUCTION DES ITEMS DE COMPARAISON
    // ════════════════════════════════════════════════════════════════

    private List<StockAuditItemDTO> buildAuditItems(
            List<StockAttestation> localStocks,
            ExternalUsageResponse.ExternalOfficeUsage externalOffice
    ) {
        // Index locaux par type (null → "__GLOBAL__")
        Map<String, StockAttestation> localByType = new LinkedHashMap<>();
        for (StockAttestation s : localStocks) {
            localByType.put(s.getCertTypeCode() != null ? s.getCertTypeCode() : "__GLOBAL__", s);
        }

        // Index externes par type
        Map<String, ExternalUsageResponse.ExternalCertTypeUsage> externalByType = new LinkedHashMap<>();
        if (externalOffice != null && externalOffice.values() != null) {
            for (ExternalUsageResponse.ExternalCertTypeUsage v : externalOffice.values()) {
                externalByType.put(v.certificateType(), v);
            }
        }

        // Union des clés — locaux d'abord, puis externes uniquement
        Set<String> allTypes = new LinkedHashSet<>();
        allTypes.addAll(localByType.keySet());
        allTypes.addAll(externalByType.keySet());

        List<StockAuditItemDTO> items = new ArrayList<>();
        for (String type : allTypes) {
            StockAttestation local = localByType.get(type);
            ExternalUsageResponse.ExternalCertTypeUsage ext = externalByType.get(type);

            int localDispo = local != null && local.getQuantiteDisponible()            != null ? local.getQuantiteDisponible()            : 0;
            int localConso = local != null && local.getQuantiteTotalConsommee()         != null ? local.getQuantiteTotalConsommee()         : 0;
            int localAppro = local != null && local.getQuantiteTotaleApprovisionnee()   != null ? local.getQuantiteTotaleApprovisionnee()   : 0;

            Integer extAttributed = ext != null ? ext.details().attributed() : null;
            Integer extAvailable  = ext != null ? ext.details().available()  : null;
            Integer extUsed       = ext != null ? ext.details().used()       : null;

            Integer deltaDispo = extAvailable != null ? localDispo - extAvailable : null;
            Integer deltaConso = extUsed      != null ? localConso - extUsed      : null;

            String statut;
            if (local == null) {
                statut = "EXTERNE_ONLY";
            } else if (ext == null) {
                statut = "LOCAL_ONLY";
            } else if (deltaDispo == 0 && deltaConso == 0) {
                statut = "SYNCHRONISE";
            } else if (Math.abs(deltaDispo) <= 5 && Math.abs(deltaConso) <= 5) {
                statut = "ATTENTION";
            } else {
                statut = "DIVERGENCE";
            }

            items.add(new StockAuditItemDTO(
                    "__GLOBAL__".equals(type) ? null : type,
                    localAppro, localDispo, localConso,
                    extAttributed, extAvailable, extUsed,
                    deltaDispo, deltaConso,
                    statut
            ));
        }
        return items;
    }

    // ════════════════════════════════════════════════════════════════
    // HELPERS
    // ════════════════════════════════════════════════════════════════

    private String computeStatutGlobal(
            List<StockAuditItemDTO> items,
            ExternalUsageResponse.ExternalOfficeUsage externalOffice,
            String externalError
    ) {
        if (externalError != null || externalOffice == null) return "SANS_DONNEE_EXTERNE";
        if (items.stream().anyMatch(i -> "DIVERGENCE".equals(i.statut())))  return "DIVERGENCE";
        if (items.stream().anyMatch(i -> "ATTENTION".equals(i.statut())
                || "LOCAL_ONLY".equals(i.statut())
                || "EXTERNE_ONLY".equals(i.statut())))                       return "ATTENTION";
        return "SYNCHRONISE";
    }

    @FunctionalInterface
    private interface IntExtractor { Integer get(StockAttestation s); }

    private int sum(List<StockAttestation> stocks, IntExtractor fn) {
        return stocks.stream().mapToInt(s -> fn.get(s) != null ? fn.get(s) : 0).sum();
    }
}
