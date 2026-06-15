/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.InsuranceCertificateRequest;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.ProductionPayloadResponse;
import service_administration_api.entite.pooltpv.Infos_AdministrateurAgencePayLoad;
import service_administration_api.entite.pooltpv.StockAttestation;
import service_administration_api.repository.poolTPV.Infos_AdministrateurAgencePayLoadRepository;
import service_administration_api.repository.poolTPV.StockAttestationRepository;
import service_administration_api.service.CertificateService;

/**
 *
 * @author USER01
 */
@RestController
@RequestMapping("/certificates")
// Autorise Angular (localhost:4200) à appeler ce backend
//@CrossOrigin(origins = "http://localhost:4200")
public class CertificateController {

    private final CertificateService service;
    private final Infos_AdministrateurAgencePayLoadRepository agentRepository;
    private final StockAttestationRepository stockRepository;

    public CertificateController(CertificateService service,
            Infos_AdministrateurAgencePayLoadRepository agentRepository,
            StockAttestationRepository stockRepository) {
        this.service = service;
        this.agentRepository = agentRepository;
        this.stockRepository = stockRepository;
    }

    // ════════════════════════════════════════════════════════════════
    // ENDPOINT 1 : POST /api/v1/certificates/{username}
    // ════════════════════════════════════════════════════════════════
    @PostMapping("/{username}")
    public ResponseEntity<ProductionPayloadResponse> createCertificate(
            @PathVariable String username,
            @RequestBody @Valid InsuranceCertificateRequest request) {

        // ── 1. Identification du bureau de l'utilisateur connecté ────
        Infos_AdministrateurAgencePayLoad agent = agentRepository.findByUsername(username)
                .orElse(null);
        if (agent == null) {
            return stockError(HttpStatus.UNAUTHORIZED, "STOCK.UNKNOWN_USER");
        }
        String officeCode = agent.getOffice_code();

        // ── 2. Vérification de l'existence d'un stock pour ce bureau ─
        List<StockAttestation> stocks = stockRepository.findByOfficeCode(officeCode);
        if (stocks.isEmpty()) {
            return stockError(HttpStatus.UNPROCESSABLE_ENTITY, "STOCK.NOT_INIT");
        }

        // ── 3. Quantité demandée ─────────────────────────────────────
        int quantiteDemandee = request.productions().size();

        // ── 4. Calcul du stock disponible total pour ce bureau ───────
        int stockDisponible = stocks.stream()
                .mapToInt(s -> s.getQuantiteDisponible() != null ? s.getQuantiteDisponible() : 0)
                .sum();

        // ── 5. Comparaison stock vs quantité demandée ────────────────
        if (stockDisponible < quantiteDemandee) {
            return stockError(HttpStatus.UNPROCESSABLE_ENTITY, "STOCK.INSUFFICIENT");
        }

        // ── Production (tous les contrôles sont passés) ──────────────
        System.out.println(request);
        ProductionPayloadResponse response = service.sendAndSave(request, username);

        return ResponseEntity
                .status(response.status())
                .body(response);
    }

    /**
     * Réponse d'erreur stock — même structure que la réponse succès.
     * Le message contient la clé i18n ; data est null.
     * Exemple : {"status": 422, "message": "STOCK.NOT_INIT", "data": null}
     */
    private ResponseEntity<ProductionPayloadResponse> stockError(HttpStatus status, String messageKey) {
        return ResponseEntity.status(status)
                .body(new ProductionPayloadResponse(status.value(), messageKey, null));
    }

    // ════════════════════════════════════════════════════════════════
    // ENDPOINT 2 : GET /api/v1/certificates
    // ════════════════════════════════════════════════════════════════
    /**
     * Récupère toutes les productions sauvegardées en Oracle
     *
     * Utilisé par Angular pour afficher la liste
     *
     * @return Liste de toutes les ApiResponse en base
     */
    @GetMapping("/all/{codeagence}")
    public ResponseEntity<List<ProductionPayloadResponse>> getAllProductions(@PathVariable String codeagence) {

        List<ProductionPayloadResponse> productions = service.getAllProductions(codeagence);

        // 200 OK + liste JSON
        return ResponseEntity.ok(productions);
    }

    // ════════════════════════════════════════════════════════════════
    // ENDPOINT 3 : GET /api/v1/certificates/{id}
    // ════════════════════════════════════════════════════════════════
    /**
     * Récupère une production par son ID Oracle
     *
     * @param id L'ID Oracle de la production (Long)
     * @return La production correspondante
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductionPayloadResponse> getById(
            // @PathVariable extrait {id} depuis l'URL
            @PathVariable Long id) {

        ProductionPayloadResponse response = service.getById(id);
        return ResponseEntity.ok(response);
    }

    // ════════════════════════════════════════════════════════════════
    // ENDPOINT 4 : GET /api/v1/certificates/{id}/download
    // ════════════════════════════════════════════════════════════════
    /**
     * Streame le PDF d'un certificat stocké en Oracle (BLOB) vers Angular ou le
     * navigateur directement
     *
     * @param id L'ID Oracle du certificat
     * @return Les bytes du PDF avec Content-Type application/pdf
     */
    @GetMapping("/ref/{reference}/download")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable String reference) {

        // Récupère les bytes du PDF depuis Oracle BLOB
         
        byte[] pdfBytes = service.getPdfBytes(reference);

        HttpHeaders headers = new HttpHeaders();

        // Indique au navigateur que c'est un fichier PDF
        headers.setContentType(MediaType.APPLICATION_PDF);

        // "inline"     → affiche dans le navigateur
        // "attachment" → force le téléchargement
        headers.setContentDispositionFormData("inline",
                "certificat- reference" + reference + ".pdf");

        headers.setContentLength(pdfBytes.length);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfBytes);
    }
    
     @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable Long id) {

        // Récupère les bytes du PDF depuis Oracle BLOB
         
        byte[] pdfBytes = service.getPdfBytes(id);

        HttpHeaders headers = new HttpHeaders();

        // Indique au navigateur que c'est un fichier PDF
        headers.setContentType(MediaType.APPLICATION_PDF);

        // "inline"     → affiche dans le navigateur
        // "attachment" → force le téléchargement
        headers.setContentDispositionFormData("inline",
                "certificat- id" + id + ".pdf");

        headers.setContentLength(pdfBytes.length);

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * GET /api/v1/police/check?number=POL-2026-00123
     *
     * Appelé automatiquement par Angular quand l'utilisateur termine la saisie
     * du numéro de police (debounce 600ms)
     *
     * Réponse si trouvée : { exists: true, ...tous les champs } Réponse si
     * absente : { exists: false, policeNumber: "..." }
     */
    @GetMapping("/check")
    public ResponseEntity<InsuranceCertificateRequest> checkPolice(
            @RequestParam("police") String policeNumber, @RequestParam("username") String username) {

        if (policeNumber == null || policeNumber.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        InsuranceCertificateRequest response
                = service.checkPolice(policeNumber.trim(),username.trim());
        System.out.println(""+response);

        return ResponseEntity.ok(response);
    }
}
