package service_administration_api.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service_administration_api.DTO.ApiResponse;
import service_administration_api.DTO.stock.*;
import service_administration_api.service.StockAttestationService;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * API REST — Gestion du stock d'attestations.
 *
 * Préfixe : /stock  (gateway StripPrefix=3 enlève /gateway-proxy/api/esphere-ass-microservice-admin)
 *
 * Endpoints :
 *   GET    /stock/{officeCode}                     → stocks du bureau
 *   GET    /stock/{officeCode}/historique           → historique complet
 *   GET    /stock/{officeCode}/historique/periode   → historique par période
 *   GET    /stock/{officeCode}/historique/type      → historique par type
 *   GET    /stock/alertes                           → stocks en alerte
 *   GET    /stock/alertes/org/{orgCode}             → alertes par organisation
 *   POST   /stock/initier                           → init stock bureau
 *   POST   /stock/{officeCode}/approvisionner       → approvisionnement
 *   POST   /stock/{officeCode}/ajuster              → ajustement manuel
 *   DELETE /stock/{officeCode}/annuler/{refProd}    → annulation production
 */
@RestController
@RequestMapping("/stock")
public class StockAttestationController {

    private final StockAttestationService stockService;

    public StockAttestationController(StockAttestationService stockService) {
        this.stockService = stockService;
    }

    // ── Stocks d'un bureau ───────────────────────────────────────

    @GetMapping("/{officeCode}")
    public ResponseEntity<ApiResponse<List<StockAttestationDTO>>> getStocksParBureau(
        @PathVariable String officeCode
    ) {
        List<StockAttestationDTO> data = stockService.getStocksParBureau(officeCode);
        return ok("Stocks récupérés", data);
    }

    // ── Alertes ──────────────────────────────────────────────────

    @GetMapping("/alertes")
    public ResponseEntity<ApiResponse<List<StockAttestationDTO>>> getAlertes() {
        return ok("Stocks en alerte", stockService.getStocksEnAlerte());
    }

    @GetMapping("/alertes/org/{orgCode}")
    public ResponseEntity<ApiResponse<List<StockAttestationDTO>>> getAlertesParOrg(
        @PathVariable String orgCode
    ) {
        return ok("Stocks en alerte", stockService.getStocksEnAlerteParOrg(orgCode));
    }

    // ── Historique ───────────────────────────────────────────────

    @GetMapping("/{officeCode}/historique")
    public ResponseEntity<ApiResponse<List<MouvementStockDTO>>> getHistorique(
        @PathVariable String officeCode
    ) {
        return ok("Historique récupéré", stockService.getHistorique(officeCode));
    }

    @GetMapping("/{officeCode}/historique/periode")
    public ResponseEntity<ApiResponse<List<MouvementStockDTO>>> getHistoriqueParPeriode(
        @PathVariable String officeCode,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime debut,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fin
    ) {
        return ok("Historique période", stockService.getHistoriqueParPeriode(officeCode, debut, fin));
    }

    @GetMapping("/{officeCode}/historique/type")
    public ResponseEntity<ApiResponse<List<MouvementStockDTO>>> getHistoriqueParType(
        @PathVariable String officeCode,
        @RequestParam String type
    ) {
        return ok("Historique type", stockService.getHistoriqueParType(officeCode, type));
    }

    // ── Initialisation ───────────────────────────────────────────

    @PostMapping("/initier")
    public ResponseEntity<ApiResponse<StockAttestationDTO>> initierStock(
        @RequestBody InitierStockRequest request,
        Principal principal
    ) {
        String user = principal != null ? principal.getName() : "SYSTEM";
        return ok("Stock initialisé", stockService.initierStock(request, user));
    }

    // ── Approvisionnement ────────────────────────────────────────

    @PostMapping("/{officeCode}/approvisionner")
    public ResponseEntity<ApiResponse<StockAttestationDTO>> approvisionner(
        @PathVariable String officeCode,
        @RequestBody ApprovisionnerRequest request,
        Principal principal
    ) {
        String user = principal != null ? principal.getName() : "SYSTEM";
        return ok("Stock approvisionné", stockService.approvisionner(officeCode, request, user));
    }

    // ── Ajustement ───────────────────────────────────────────────

    @PostMapping("/{officeCode}/ajuster")
    public ResponseEntity<ApiResponse<StockAttestationDTO>> ajuster(
        @PathVariable String officeCode,
        @RequestBody AjustementStockRequest request,
        Principal principal
    ) {
        String user = principal != null ? principal.getName() : "SYSTEM";
        return ok("Stock ajusté", stockService.ajuster(officeCode, request, user));
    }

    // ── Annulation production ────────────────────────────────────

    @DeleteMapping("/{officeCode}/annuler/{refProduction}")
    public ResponseEntity<ApiResponse<StockAttestationDTO>> annuler(
        @PathVariable String officeCode,
        @PathVariable String refProduction,
        Principal principal
    ) {
        String user = principal != null ? principal.getName() : "SYSTEM";
        return ok("Production annulée — stock restitué",
            stockService.annulerProduction(officeCode, refProduction, user));
    }

    // ── Helper ──────────────────────────────────────────────────

    private <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(ApiResponse.success(message, data));
    }
}
