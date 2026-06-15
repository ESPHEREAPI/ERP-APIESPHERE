package service_administration_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.*;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.ProductionPayloadResponse;
import service_administration_api.entite.pooltpv.CertificatePlayLoad;
import service_administration_api.entite.pooltpv.ProductionPayload;
import service_administration_api.entite.ZenAttdigAsac;
import service_administration_api.exception.*;
import service_administration_api.mapper.*;
import service_administration_api.repository.*;
import service_administration_api.repository.poolTPV.*;
import service_administration_api.entite.*;
import service_administration_api.entite.pooltpv.Infos_AdministrateurAgencePayLoad;
import org.springframework.context.annotation.Lazy;

@Service
@Slf4j
public class CertificateService {  // ← @Transactional retiré du niveau classe

    private final RestTemplate restTemplate;
    private final ProductionPayloadRepository productionRepository;
    private final CertificateRepository certificateRepository;
    private final ZenAttdigAsacRepository zenAttdigAsacRepository;
    private final AttestationRisqueRepository attestationRisqueRepository;
    private final NatureDocumentRepository natureDocumentRepository;
    private final Infos_AdministrateurAgencePayLoadRepository administrateurAgencePayLoadRepository;
    private final ProductionMapper mapper;
    private final ObjectMapper objectMapper;
    private final CategorieVehiculeRepository categorieVehiculeRepository;
    private final EnergieVehiculeRepository energieVehiculeRepository;
    private final GenreVehiculeRepository genreVehiculeRepository;
    private final ProfessionAssureRepository  professionAssureRepository;
    private final TypeAssureRepository typeAssureRepository;
    private final TypeVehiculeRepository typeVehiculeRepository;
    private final UsageVehiculeRepository usageVehiculeRepository;
    private final ZoneCirculationRepository zoneCirculationRepository;
    private final StockAttestationService stockService;

    @Value("${api.external.url.production}")
    private String externalApiUrl;

    @Value("${api.external.token}")
    private String apiTokenUrl;
    @Value("${api.X-App-Id}")
    private String xAppId;

    // Volatile pour la visibilité en environnement multi-thread
    private volatile String token_final = null;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public CertificateService(RestTemplate restTemplate,
            ProductionPayloadRepository productionRepository,
            CertificateRepository certificateRepository,
            ProductionMapper mapper,
            ObjectMapper objectMapper,
            ZenAttdigAsacRepository zenAttdigAsacRepository,
            AttestationRisqueRepository attestationRisqueRepository,
            NatureDocumentRepository natureDocumentRepository,
            Infos_AdministrateurAgencePayLoadRepository administrateurAgencePayLoadRepository,
            CategorieVehiculeRepository categorieVehiculeRepository,
            EnergieVehiculeRepository energieVehiculeRepository,
            TypeAssureRepository typeAssureRepository,
            TypeVehiculeRepository typeVehiculeRepository,
            UsageVehiculeRepository usageVehiculeRepository,
            ZoneCirculationRepository zoneCirculationRepository,
            ProfessionAssureRepository professionAssureRepository,
            GenreVehiculeRepository genreVehiculeRepository,
            @Lazy StockAttestationService stockService) {
        this.restTemplate = restTemplate;
        this.productionRepository = productionRepository;
        this.certificateRepository = certificateRepository;
        this.mapper = mapper;
        this.objectMapper = objectMapper;
        this.zenAttdigAsacRepository = zenAttdigAsacRepository;
        this.attestationRisqueRepository = attestationRisqueRepository;
        this.natureDocumentRepository = natureDocumentRepository;
        this.administrateurAgencePayLoadRepository = administrateurAgencePayLoadRepository;
        this.energieVehiculeRepository = energieVehiculeRepository;
        this.genreVehiculeRepository = genreVehiculeRepository;
        this.professionAssureRepository = professionAssureRepository;
        this.typeAssureRepository = typeAssureRepository;
        this.typeVehiculeRepository = typeVehiculeRepository;
        this.usageVehiculeRepository = usageVehiculeRepository;
        this.zoneCirculationRepository = zoneCirculationRepository;
        this.categorieVehiculeRepository = categorieVehiculeRepository;
        this.stockService = stockService;
    }

    // ════════════════════════════════════════════════════════════════
    // LOGIN
    // ════════════════════════════════════════════════════════════════
    public String JwtLoginCertificate(String username) throws Exception {
        Infos_AdministrateurAgencePayLoad infos = administrateurAgencePayLoadRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                "Utilisateur introuvable, veuillez contacter votre administrateur"));

        LoginCertificatRequest loginRequest = new LoginCertificatRequest(infos.getUsername());

        String requestBody = objectMapper.writeValueAsString(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiTokenUrl))
                .header("Content-Type", "application/json;charset=UTF-8")
                .header("X-App-Id", xAppId)
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            log.error("Échec du login PoolTPV [{}] : {}", response.statusCode(), response.body());
            throw new ExternalApiPayloadException(
                    response.statusCode(),
                    new ApiErrorPayloadResponse(
                            "Échec d'authentification auprès de l'API externe",
                            java.util.Map.of("login", List.of(response.body()))
                    )
            );
        }

        log.info("Login PoolTPV réussi pour [{}]", infos.getUsername());
        this.token_final = objectMapper.readTree(response.body()).get("token").asText();
        return this.token_final;
    }

    // ════════════════════════════════════════════════════════════════
    // MÉTHODE PRINCIPALE — POST /api/v1/certificates
    // ════════════════════════════════════════════════════════════════
    @Transactional  // ← Transactionnel uniquement ici (opérations BDD)
    public ProductionPayloadResponse sendAndSave(InsuranceCertificateRequest request,String username) {

//        // Résolution de l'agence
//        Infos_AdministrateurAgencePayLoad infos = administrateurAgencePayLoadRepository
//                .findByLogin(request.login())
//                .orElseThrow(() -> new RuntimeException(
//                "Login introuvable, veuillez contacter votre administrateur"));

        // ── ÉTAPE 1 : Appel API externe (hors transaction) ───────────
        ProductionPayloadResponse apiResponse;
//        if (infos.getOffice_code() != null && !"".equals(infos.getOffice_code())) {
//            InsuranceCertificateRequest updatedRequest = request.withOfficeCode(infos.getOffice_code(),"117","cima");
//            apiResponse = callExternalApi(request, infos.getUsername());
//        } else {
            apiResponse = callExternalApi(request, username);
//        }

        // ── ÉTAPE 2 : Sauvegarde en Oracle ───────────────────────────
        ProductionPayload productionEntity = mapper.apiResponseToEntity(apiResponse.data());
        ProductionPayload savedProduction = productionRepository.save(productionEntity);

        // ── ÉTAPE 2b : Déduction automatique du stock ─────────────────
        // production.quantity = nombre d'attestations consommées par ce bureau
        deduireStockProduction(savedProduction, username);

        // ── ÉTAPE 3 : Téléchargement des PDFs ────────────────────────
        savedProduction.getCertificates().forEach(cert -> {
            try {
                byte[] pdfBytes = downloadPdf(cert.getDownloadLink());
                cert.setPdfBytes(pdfBytes);
                certificateRepository.save(cert);
            } catch (Exception e) {
                // On logue l'erreur mais on ne bloque pas la réponse
                log.warn("Erreur téléchargement PDF pour [{}] : {}", cert.getReference(), e.getMessage());
            }
        });

        return apiResponse;
    }

    // ════════════════════════════════════════════════════════════════
    // GET ALL
    // ════════════════════════════════════════════════════════════════
    @Transactional(readOnly = true)
    public List<ProductionPayloadResponse> getAllProductions(String codeagence) {
        return productionRepository.findByOfficeCode(codeagence)
                .stream()
                .map(mapper::entityToResponse)
                .toList();
    }

    // ════════════════════════════════════════════════════════════════
    // GET BY ID
    // ════════════════════════════════════════════════════════════════
    @Transactional(readOnly = true)
    public ProductionPayloadResponse getById(Long id) {
        ProductionPayload entity = productionRepository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException(id));
        return mapper.entityToResponse(entity);
    }

    // ════════════════════════════════════════════════════════════════
    // DOWNLOAD PDF
    // ════════════════════════════════════════════════════════════════
    @Transactional
    public byte[] getPdfBytes(String reference) {
        CertificatePlayLoad cert = certificateRepository.findByReference(reference)
                .orElseThrow(() -> new CertificateNotFoundException(reference));

        if (cert.getPdfBytes() == null) {
            byte[] pdfBytes = downloadPdf(cert.getDownloadLink());
            cert.setPdfBytes(pdfBytes);
            certificateRepository.save(cert);
            return pdfBytes;
        }
        return cert.getPdfBytes();
    }

    
      @Transactional
    public byte[] getPdfBytes(Long id) {
        CertificatePlayLoad cert = certificateRepository.findById(id)
                .orElseThrow(() -> new CertificateNotFoundException(id));

        if (cert.getPdfBytes() == null) {
            byte[] pdfBytes = downloadPdf(cert.getDownloadLink());
            cert.setPdfBytes(pdfBytes);
            certificateRepository.save(cert);
            return pdfBytes;
        }
        return cert.getPdfBytes();
    }

    // ════════════════════════════════════════════════════════════════
    // MÉTHODES PRIVÉES
    // ════════════════════════════════════════════════════════════════
    /**
     * Appelle l'API externe avec gestion complète des erreurs et renouvellement
     * automatique du token en cas de 401. Toutes les erreurs sont propagées —
     * aucune n'est avalée silencieusement.
     */
    private ProductionPayloadResponse callExternalApi(InsuranceCertificateRequest request, String username) {

        // ── Authentification initiale si pas de token ─────────────────
        if (token_final == null) {
            try {
                this.JwtLoginCertificate(username);
            } catch (ExternalApiPayloadException e) {
                throw e;  // Déjà bien formatée → on laisse remonter
            } catch (Exception e) {
                log.error("Impossible d'obtenir le token pour [{}] : {}", username, e.getMessage());
                throw new ExternalApiPayloadException(
                        503,
                        new ApiErrorPayloadResponse(
                                "Impossible de s'authentifier auprès de l'API externe",
                                java.util.Map.of("auth", List.of(e.getMessage()))
                        )
                );
            }
        }

        // ── Premier essai ─────────────────────────────────────────────
        try {
            return doPost(request, username, false);

        } catch (HttpClientErrorException ex) {

            // 401 → token expiré : on renouvelle et on réessaie une fois
            if (ex.getStatusCode().value() == 401) {
                log.warn("Token expiré pour [{}], renouvellement en cours...", username);
                try {
                    this.JwtLoginCertificate(username);
                } catch (Exception renewEx) {
                    log.error("Renouvellement du token échoué : {}", renewEx.getMessage());
                    throw new ExternalApiPayloadException(
                            401,
                            new ApiErrorPayloadResponse(
                                    "Session expirée et renouvellement impossible",
                                    java.util.Map.of("auth", List.of(renewEx.getMessage()))
                            )
                    );
                }

                // Second essai après renouvellement
                try {
                    return doPost(request, username, true);
                } catch (HttpClientErrorException retryEx) {
                    log.error("Échec après renouvellement du token [{}] : {}", retryEx.getStatusCode(), retryEx.getResponseBodyAsString());
                    throw new ExternalApiPayloadException(
                            retryEx.getStatusCode().value(),
                            parseErrorBody(retryEx.getResponseBodyAsString())
                    );
                }
            }

            // Autres erreurs 4xx (422, 400, 403...)
            log.error("Erreur client API externe [{}] : {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ExternalApiPayloadException(
                    ex.getStatusCode().value(),
                    parseErrorBody(ex.getResponseBodyAsString())
            );

        } catch (HttpServerErrorException ex) {
            // Erreurs 5xx de l'API externe
            log.error("Erreur serveur API externe [{}] : {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw new ExternalApiPayloadException(
                    ex.getStatusCode().value(),
                    parseErrorBody(ex.getResponseBodyAsString())
            );
        }
        // Toute autre exception (réseau, timeout...) remonte naturellement
    }

    /**
     * Exécute le POST HTTP vers l'API externe. Lance une
     * HttpClientErrorException / HttpServerErrorException en cas d'erreur HTTP.
     */
//    private ProductionPayloadResponse doPost(InsuranceCertificateRequest request,
//            String email,
//            boolean isRetry) {
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Content-Type", "application/json;charset=UTF-8");
//        headers.set("Authorization", "Bearer " + token_final);
//        ///mapin cle externe et interne
//        ///
//        ///
//        ///
//        List<ProductionPayloadRequest> productions = request.productions().stream()
//                .map(ZenAttdigAsacMapper::toProductionPayloadRequestFinal)
//                .toList();
//
//        // Résolution de l'agence
//     
//   InsuranceCertificateRequest  request_new=    new InsuranceCertificateRequest(
//                request.office_code(),
//                "117",
//                "cima",
//                //"api",
//                productions
//        );
//   
//        System.out.println("new requette :"+request_new);
//
//        HttpEntity<InsuranceCertificateRequest> httpEntity = new HttpEntity<>(request_new, headers);
//
//        log.debug("[{}] POST {} | payload : {}", isRetry ? "RETRY" : "ESSAI", externalApiUrl, request_new);
//
//        ResponseEntity<ProductionPayloadResponse> response = restTemplate.exchange(
//                externalApiUrl,
//                HttpMethod.POST,
//                httpEntity,
//                ProductionPayloadResponse.class
//        );
//
//        // Réponse 2xx mais body null → erreur métier
//        if (response.getBody() == null || response.getBody().data() == null) {
//            log.error("Réponse 2xx reçue mais body vide de l'API externe");
//            throw new ExternalApiPayloadException(
//                    502,
//                    new ApiErrorPayloadResponse(
//                            "L'API externe a retourné une réponse vide",
//                            java.util.Map.of("response", List.of("body null"))
//                    )
//            );
//        }
//
//        log.info("Réponse API externe reçue avec succès pour [{}]", email);
//        return response.getBody();
//    }
    
   private ProductionPayloadResponse doPost(InsuranceCertificateRequest request,
        String email,
        boolean isRetry) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json;charset=UTF-8");
    headers.set("Authorization", "Bearer " + token_final);

    List<ProductionPayloadRequest> productions = request.productions().stream()
            .map(ZenAttdigAsacMapper::toProductionPayloadRequestFinal)
            .toList();

    InsuranceCertificateRequest request_new = new InsuranceCertificateRequest(
            request.office_code(),
            "117",
            "cima",
            productions
    );

    System.out.println("new requette :" + request_new);

    HttpEntity<InsuranceCertificateRequest> httpEntity = new HttpEntity<>(request_new, headers);

    log.debug("[{}] POST {} | payload : {}", isRetry ? "RETRY" : "ESSAI", externalApiUrl, request_new);

    // ── ÉTAPE 1 : Récupérer la réponse brute en String ──────────
    ResponseEntity<String> rawResponse = restTemplate.exchange(
            externalApiUrl,
            HttpMethod.POST,
            httpEntity,
            String.class   // ← String pour voir le JSON brut
    );

    // ── ÉTAPE 2 : Logger le JSON brut ───────────────────────────
    log.info("=======================================================");
    log.info("RÉPONSE BRUTE API EXTERNE");
    log.info("Status : {}", rawResponse.getStatusCode());
    log.info("Body   : {}", rawResponse.getBody());
    log.info("=======================================================");

    // ── ÉTAPE 3 : Désérialiser manuellement ─────────────────────
    try {
        ProductionPayloadResponse parsed = objectMapper.readValue(
                rawResponse.getBody(),
                ProductionPayloadResponse.class
        );

        if (parsed == null || parsed.data() == null) {
            log.error("Réponse 2xx reçue mais body vide de l'API externe");
            throw new ExternalApiPayloadException(
                    502,
                    new ApiErrorPayloadResponse(
                            "L'API externe a retourné une réponse vide",
                            java.util.Map.of("response", List.of("body null"))
                    )
            );
        }

        log.info("Désérialisation réussie pour [{}]", email);
        return parsed;

    } catch (ExternalApiPayloadException e) {
        throw e;  // déjà formatée, on laisse remonter
    } catch (Exception e) {
        log.error("Erreur désérialisation réponse API externe : {}", e.getMessage());
        log.error("JSON brut reçu : {}", rawResponse.getBody());
        throw new ExternalApiPayloadException(
                502,
                new ApiErrorPayloadResponse(
                        "Impossible de parser la réponse de l'API externe",
                        java.util.Map.of("parse", List.of(e.getMessage()))
                )
        );
    }
}

    /**
     * Télécharge un PDF depuis l'URL distante.
     */
    private byte[] downloadPdf(String downloadUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token_final);
        headers.setAccept(List.of(MediaType.APPLICATION_PDF));

        ResponseEntity<byte[]> response = restTemplate.exchange(
                downloadUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class
        );

        if (response.getBody() == null) {
            throw new RuntimeException("PDF vide reçu depuis : " + downloadUrl);
        }

        return response.getBody();
    }

    /**
     * Parse le corps JSON d'une erreur de l'API externe.
     */
    private ApiErrorPayloadResponse parseErrorBody(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, ApiErrorPayloadResponse.class);
        } catch (Exception e) {
            log.warn("Impossible de parser le corps de l'erreur : {}", responseBody);
            return new ApiErrorPayloadResponse(
                    "Erreur inattendue de l'API externe",
                    java.util.Map.of("general", List.of(
                            responseBody != null ? responseBody : "Corps de réponse vide"
                    ))
            );
        }
    }

    // ════════════════════════════════════════════════════════════════
    // UTILITAIRES
    // ════════════════════════════════════════════════════════════════
    @Transactional
    public List<Infos_AdministrateurAgencePayLoad> create(String username) {
        return administrateurAgencePayLoadRepository.findByUsername(username)
                .map(List::of)
                .orElseGet(() -> {
                    Infos_AdministrateurAgencePayLoad infos = new Infos_AdministrateurAgencePayLoad();
                    infos.setCodeAgence(1000);
                    infos.setOffice_code("BD1000");
                    infos.setLibelleAgence("bureau direct office");
                    infos.setClientName("bds nyoue");
                    infos.setUsername(username);
                    infos.setLogin("AdminBio2");
                 
                    return List.of(administrateurAgencePayLoadRepository.save(infos));
                });
    }

    @Transactional(readOnly = true)
    public InsuranceCertificateRequest checkPolice(String policeNumber,String username) {
//        String[] parts = policeNumber.split("-");
//        int codeinte = Integer.parseInt(parts[0]);
//        long numeroPolice = Long.parseLong(parts[1]);
//        short avenant = (parts.length == 3) ? Short.parseShort(parts[2]) : 0;
//
//        log.debug("checkPolice → codeAgence={} | police={} | avenant={}", codeinte, numeroPolice, avenant);
//
//        List<ZenAttdigAsac> all = zenAttdigAsacRepository.findByNumeroPoliceNative(policeNumber);
 // ✅ Décomposer le numéro de police
    String[] parts = policeNumber.split("-");
    if (parts.length < 2) {
        throw new IllegalArgumentException("Format police invalide: " + policeNumber);
    }
    
    Long    codeinte = Long.parseLong(parts[0].trim());   // ✅ NUMBER Oracle
    Long    numepoli = Long.parseLong(parts[1].trim());   // ✅ NUMBER Oracle
    Integer numeaven = parts.length >= 3 
                       ? Integer.parseInt(parts[2].trim()) 
                       : null;                            // ✅ null si pas d'avenant

    log.debug("checkPolice → codeinte={} | numepoli={} | numeaven={}", 
              codeinte, numepoli, numeaven);

    // ✅ Appel avec types NUMBER corrects → index Oracle utilisés
    List<ZenAttdigAsac> all  = zenAttdigAsacRepository
                              .findByPoliceDecompose(codeinte, numepoli, numeaven);

        List<ProductionPayloadRequest> productions = all.stream()
                .map(ZenAttdigAsacMapper::toProductionPayloadRequest)
                .toList();

        // Résolution de l'agence
        Infos_AdministrateurAgencePayLoad infos = administrateurAgencePayLoadRepository
                .findByUsername(username)
                .orElseThrow(() -> new RuntimeException(
                "Login introuvable, veuillez contacter votre administrateur"));
        return new InsuranceCertificateRequest(
                infos.getOffice_code(),
                "117",
                "cima",
                //"api",
                productions
        );
    }

    // ════════════════════════════════════════════════════════════════
    // DÉDUCTION STOCK — appelé automatiquement après chaque production
    // ════════════════════════════════════════════════════════════════

    /**
     * Déduit du stock le nombre d'attestations produites.
     *
     * Stratégie :
     *  1. Cherche un stock spécifique (certTypeCode + certVariantCode) pour ce bureau.
     *  2. Si absent, remonte au stock global (certTypeCode IS NULL).
     *  3. Si stock insuffisant : log un avertissement — la production est déjà confirmée
     *     côté API externe, on ne bloque pas mais le statut passe à RUPTURE.
     */
    private void deduireStockProduction(ProductionPayload production, String username) {
        if (production == null || production.getOfficeCode() == null) return;

        int quantite = production.getQuantity() != null ? production.getQuantity() : 0;
        if (quantite <= 0) return;

        // Déterminer type/variante depuis le premier certificat (homogènes dans une prod)
        String certTypeCode    = null;
        String certVariantCode = null;
        if (production.getCertificates() != null && !production.getCertificates().isEmpty()) {
            CertificatePlayLoad premier = production.getCertificates().get(0);
            certTypeCode    = premier.getCertTypeCode();
            certVariantCode = premier.getCertVariantCode();
        }

        try {
            stockService.deduireStock(
                production.getOfficeCode(),
                certTypeCode,
                certVariantCode,
                quantite,
                production.getReference(),
                username
            );
            log.info("Stock déduit : bureau={} | type={} | quantité={} | réf={}",
                production.getOfficeCode(), certTypeCode, quantite, production.getReference());

        } catch (StockInsuffisantException e) {
            // La production est déjà créée côté API — on logue sans bloquer
            log.warn("STOCK INSUFFISANT : bureau={} | type={} | demandé={} | disponible={} | réf={}",
                e.getOfficeCode(), e.getCertTypeCode(), e.getDemande(), e.getDisponible(),
                production.getReference());

        } catch (Exception e) {
            // Pas de stock configuré pour ce bureau — log info, pas d'erreur bloquante
            log.info("Aucun stock configuré pour bureau={} | type={} — production enregistrée sans déduction.",
                production.getOfficeCode(), certTypeCode);
        }
    }

    public String getNatureDocument(String code) {
        return switch (code) {
            case "J" ->
                "JAUNE";
            case "R" ->
                "ROSE";
            case "B" ->
                "BLEU";
            case "V" ->
                "VERT";
            default ->
                throw new IllegalArgumentException("Code nature document inconnu : " + code);
        };
    }
}//package service_administration_api.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
//import java.time.Duration;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import lombok.extern.slf4j.Slf4j;
//import oracle.security.crypto.cert.PKIX;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.RestTemplate;
//
//import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.InsuranceCertificateRequest;
//import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.LoginRequest;
//import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.LoginCertificatRequest;
//import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.ProductionPayloadRequest;
//import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.ProductionPayloadResponse;
//import service_administration_api.entite.AttestationRisque;
//import service_administration_api.entite.NatureDocument;
//import service_administration_api.entite.pooltpv.CertificatePlayLoad;
//import service_administration_api.entite.Police;
//import service_administration_api.entite.ZenAttdigAsac;
//import service_administration_api.entite.pooltpv.ProductionPayload;
//import service_administration_api.exception.ApiErrorPayloadResponse;
//import service_administration_api.exception.CertificateNotFoundException;
//import service_administration_api.exception.ExternalApiPayloadException;
//import service_administration_api.exception.UserNotFoundException;
//
//import service_administration_api.mapper.ProductionMapper;
//import service_administration_api.repository.AttestationRisqueRepository;
//import service_administration_api.repository.NatureDocumentRepository;
//import service_administration_api.mapper.ZenAttdigAsacMapper;
//import service_administration_api.repository.poolTPV.CertificateRepository;
//import service_administration_api.repository.poolTPV.ProductionPayloadRepository;
//import service_administration_api.entite.pooltpv.Infos_AdministrateurAgencePayLoad;
//import service_administration_api.repository.ZenAttdigAsacRepository;
//import service_administration_api.repository.poolTPV.Infos_AdministrateurAgencePayLoadRepository;
//
//@Service
//@Transactional
//@Slf4j
//public class CertificateService {
//
//    private final RestTemplate restTemplate;
//    private final ProductionPayloadRepository productionRepository;
//    private final CertificateRepository certificateRepository;
//    private final ZenAttdigAsacRepository zenAttdigAsacRepository;
//    private final AttestationRisqueRepository attestationRisqueRepository;
//    private final NatureDocumentRepository natureDocumentRepository;
//    private final Infos_AdministrateurAgencePayLoadRepository administrateurAgencePayLoadRepository;
//
//    private final ProductionMapper mapper;
//    private final ObjectMapper objectMapper;   // ← pour parser les erreurs JSON
//
//    @Value("${api.external.url.production}")
//    private String externalApiUrl;
//
//    @Value("${api.external.token}")
//    private String apiTokenUrl;
//
//    private String token_final = null;
//// HttpClient unique et réutilisable (avec timeout)
//    private final HttpClient httpClient = HttpClient.newBuilder()
//            .connectTimeout(Duration.ofSeconds(10))
//            .build();
//
//    // ── Constructeur (injection par constructeur) ─────────────────
//    public CertificateService(RestTemplate restTemplate,
//            ProductionPayloadRepository productionRepository,
//            CertificateRepository certificateRepository,
//            ProductionMapper mapper,
//            ObjectMapper objectMapper, ZenAttdigAsacRepository zenAttdigAsacRepository, AttestationRisqueRepository attestationRisqueRepository, NatureDocumentRepository natureDocumentRepository, Infos_AdministrateurAgencePayLoadRepository administrateurAgencePayLoadRepository) {
//        this.restTemplate = restTemplate;
//        this.productionRepository = productionRepository;
//        this.certificateRepository = certificateRepository;
//        this.mapper = mapper;
//        this.objectMapper = objectMapper;
//        this.zenAttdigAsacRepository = zenAttdigAsacRepository;
//        this.attestationRisqueRepository = attestationRisqueRepository;
//        this.natureDocumentRepository = natureDocumentRepository;
//        this.administrateurAgencePayLoadRepository = administrateurAgencePayLoadRepository;
//    }
//
//    public List<Infos_AdministrateurAgencePayLoad> create(String email) {
//        Optional<Infos_AdministrateurAgencePayLoad> infos = administrateurAgencePayLoadRepository.findByEmail(email);
//        if (infos.isPresent()) {
//            return List.of(infos.get());
//        }
//        Infos_AdministrateurAgencePayLoad infosagence = new Infos_AdministrateurAgencePayLoad();
//
//        infosagence.setCodeAgence(1000);
//        infosagence.setLibelleAgence("bureau direct office");
//        infosagence.setClientName("bds nyoue");
//        infosagence.setEmail(email);
//        infosagence.setPassword("alice18121993@ASAC");
//        return List.of(this.administrateurAgencePayLoadRepository.save(infosagence));
//    }
//
//    public String JwtLoginCertificate(String email) throws Exception {
//        Infos_AdministrateurAgencePayLoad infos = administrateurAgencePayLoadRepository.findByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException("USER EXIST ... PLEASE CONTACT YOUR ADMINISTRATEUR"));
//        LoginCertificatRequest loginCertificatRequest = new LoginCertificatRequest(email, infos.getPassword(), infos.getClientName(), infos.getExpiresAt());
//
//        String requestBody = objectMapper.writeValueAsString(loginCertificatRequest);
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(apiTokenUrl))
//                .header("Content-Type", "application/json;charset=UTF-8")
//                .timeout(Duration.ofSeconds(10))
//                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
//                .build();
//
//        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//
//        if (response.statusCode() != 200) {
//            throw new RuntimeException("Login échoué [" + response.statusCode() + "] : " + response.body());
//        }
//
//        log.info("Login PoolTPV réussi");
//        this.token_final = objectMapper.readTree(response.body()).get("token").asText();
//        return this.token_final;
//    }
//
//    // ════════════════════════════════════════════════════════════════
//    // MÉTHODE 1 — Appelée par POST /api/v1/certificates
//    // ════════════════════════════════════════════════════════════════
//    /**
//     * ÉTAPE 1 : Envoie la demande à l'API externe ÉTAPE 2 : Sauvegarde la
//     * production en Oracle ÉTAPE 3 : Télécharge chaque PDF et le stocke en
//     * Oracle (BLOB)
//     */
//    public ProductionPayloadResponse sendAndSave(InsuranceCertificateRequest request) {
//        //recuperons l adress mail par le code 
//          Infos_AdministrateurAgencePayLoad infos = administrateurAgencePayLoadRepository.findByCodeAgence(Integer.parseInt(request.officeCode()))
//                .orElseThrow(() -> new RuntimeException("Code Agence Not Found ... PLEASE CONTACT YOUR ADMINISTRATEUR"));
//          //String email=infos.getEmail();
//     
//        // ── ÉTAPE 1 : Appel API externe ──────────────────────────────
//        ProductionPayloadResponse apiResponse = callExternalApi(request,infos.getEmail());
//
//        if (apiResponse == null || apiResponse.data() == null) {
//            throw new RuntimeException("Réponse vide de l'API externe");
//        }
//
//        // ── ÉTAPE 2 : Sauvegarde en Oracle ───────────────────────────
//        // Convertit le record ProductionData → entité JPA (avec ses certificats)
//        ProductionPayload productionEntity
//                = mapper.apiResponseToEntity(apiResponse.data());
//
//        // INSERT INTO PRODUCTIONS + INSERT INTO CERTIFICATES (cascade)
//        ProductionPayload savedProduction
//                = productionRepository.save(productionEntity);
//
//        // ── ÉTAPE 3 : Téléchargement des PDFs ────────────────────────
//        savedProduction.getCertificates().forEach(cert -> {
//            try {
//                byte[] pdfBytes = downloadPdf(cert.getDownloadLink());
//                cert.setPdfBytes(pdfBytes);        // Stocke le PDF en BLOB Oracle
//                certificateRepository.save(cert);  // UPDATE avec le PDF
//            } catch (Exception e) {
//                System.err.println("Erreur téléchargement PDF pour "
//                        + cert.getReference() + " : " + e.getMessage());
//            }
//        });
//
//        return apiResponse;
//    }
//
//    // ════════════════════════════════════════════════════════════════
//    // MÉTHODE 2 — Appelée par GET /api/v1/certificates
//    // ════════════════════════════════════════════════════════════════
//    /**
//     * Récupère toutes les productions sauvegardées en Oracle et les convertit
//     * en records ProductionPayloadResponse
//     */
//    @Transactional(readOnly = true)  // readOnly = pas d'écriture → optimisation Oracle
//    public List<ProductionPayloadResponse> getAllProductions() {
//
//        return productionRepository.findAll() // SELECT * FROM PRODUCTIONS
//                .stream()
//                .map(entity -> {
//                    // Convertit chaque entité Oracle → record réponse
//                    return mapper.entityToResponse(entity);
//                })
//                .toList();  // Java 16+ (sinon .collect(Collectors.toList()))
//    }
//
//    // ════════════════════════════════════════════════════════════════
//    // MÉTHODE 3 — Appelée par GET /api/v1/certificates/{id}
//    // ════════════════════════════════════════════════════════════════
//    /**
//     * Récupère une production par son ID Oracle Lance
//     * CertificateNotFoundException si introuvable → interceptée par
//     * GlobalExceptionHandler → 404
//     */
//    @Transactional(readOnly = true)
//    public ProductionPayloadResponse getById(Long id) {
//
//        ProductionPayload entity = productionRepository.findById(id)
//                .orElseThrow(() -> new CertificateNotFoundException(id));
//
//        return mapper.entityToResponse(entity);
//    }
//
//    // ════════════════════════════════════════════════════════════════
//    // MÉTHODE 4 — Appelée par GET /api/v1/certificates/{id}/download
//    // ════════════════════════════════════════════════════════════════
//    /**
//     * Récupère un PDF depuis Oracle (BLOB) et le renvoie en bytes Si le PDF
//     * n'existe pas encore → le télécharge à la demande
//     */
//    public byte[] getPdfBytes(Long certificateId) {
//
//        CertificatePlayLoad cert = certificateRepository.findById(certificateId)
//                .orElseThrow(() -> new CertificateNotFoundException(certificateId));
//
//        if (cert.getPdfBytes() == null) {
//            // PDF absent en base → téléchargement à la demande (lazy download)
//            byte[] pdfBytes = downloadPdf(cert.getDownloadLink());
//            cert.setPdfBytes(pdfBytes);
//            certificateRepository.save(cert);
//            return pdfBytes;
//        }
//
//        // PDF déjà en base → on retourne directement depuis Oracle BLOB
//        return cert.getPdfBytes();
//    }
//
//    // ════════════════════════════════════════════════════════════════
//    // MÉTHODES PRIVÉES
//    // ════════════════════════════════════════════════════════════════
//    /**
//     * Appelle l'API externe POST /productions Intercepte les erreurs HTTP 422 /
//     * 401 / 403 et les transforme en ExternalApiException
//     */
//    private ProductionPayloadResponse callExternalApi(InsuranceCertificateRequest request, String email) {
//
//        if(token_final==null){
//            try {
//               this.JwtLoginCertificate(email); 
//            } catch (Exception e) {
//                 ApiErrorPayloadResponse errorResponse = parseErrorBody(
//                    e.getLocalizedMessage()
//            );
//            }
//            
//        }
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setBearerAuth(token_final);
//
//        HttpEntity<InsuranceCertificateRequest> httpEntity
//                = new HttpEntity<>(request, headers);
//
//        try {
//            ResponseEntity<ProductionPayloadResponse> response = restTemplate.exchange(
//                    externalApiUrl,
//                    HttpMethod.POST,
//                    httpEntity,
//                    ProductionPayloadResponse.class
//            );
//            // 3. Token expiré → relogin + réessai
//            if (response.getStatusCode().value() == 401) {
//                log.warn("Token expiré pour [{}], renouvellement...", externalApiUrl);
//                this.token_final = this.JwtLoginCertificate(email);
//                headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                headers.setBearerAuth(token_final);
//                httpEntity= new HttpEntity<>(request, headers);
//               response = restTemplate.exchange(
//                    externalApiUrl,
//                    HttpMethod.POST,
//                    httpEntity,
//                    ProductionPayloadResponse.class
//            );
//            }
//
//            // 4. Toujours 401 → identifiants invalides
//            if (response.getStatusCode().value() == 401) {
//                throw new RuntimeException("Authentification échouée : identifiants invalides.");
//            }
//
//            // 5. Autres erreurs HTTP
//            if (response.getStatusCode().value()  != 200) {
//                throw new RuntimeException("Erreur HTTP " + response.getStatusCode().value() + " : " + response.getBody());
//            }
//
//          
//
//        } catch (HttpClientErrorException ex) {
//            // L'API externe a retourné 422 / 401 / 403
//            // On parse le corps JSON de l'erreur
//            ApiErrorPayloadResponse errorResponse = parseErrorBody(
//                    ex.getResponseBodyAsString()
//            );
//            // On lance notre exception métier → interceptée par GlobalExceptionHandler
//            throw new ExternalApiPayloadException(
//                    ex.getStatusCode().value(),
//                    errorResponse
//            );
//        } catch (Exception ex) {
//            System.getLogger(CertificateService.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
//        }
//        
//          return new ProductionPayloadResponse(0, email, null);
//    }
//
//    /**
//     * Télécharge un PDF depuis une URL distante Retourne les bytes bruts du
//     * fichier PDF
//     */
//    private byte[] downloadPdf(String downloadUrl) {
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(token_final);
//        headers.setAccept(List.of(MediaType.APPLICATION_PDF));
//
//        HttpEntity<Void> httpEntity = new HttpEntity<>(headers);
//
//        ResponseEntity<byte[]> response = restTemplate.exchange(
//                downloadUrl,
//                HttpMethod.GET,
//                httpEntity,
//                byte[].class
//        );
//
//        return response.getBody();
//    }
//
//    /**
//     * Parse le corps JSON d'une erreur de l'API externe en record
//     * ApiErrorResponse Si le parsing échoue → retourne une erreur générique
//     */
//    private ApiErrorPayloadResponse parseErrorBody(String responseBody) {
//        try {
//            return objectMapper.readValue(responseBody, ApiErrorPayloadResponse.class);
//        } catch (Exception e) {
//            return new ApiErrorPayloadResponse(
//                    "Erreur inattendue de l'API externe",
//                    java.util.Map.of("general", List.of(responseBody))
//            );
//        }
//    }
//
//    /**
//     * Vérifie si un numéro de police existe en base Oracle Si oui → retourne
//     * toutes les infos pour pré-remplir le formulaire Si non → retourne
//     * exists=false
//     *
//     * @param policeNumber Le numéro de police saisi par l'utilisateur
//     * @return PoliceCheckResponse avec exists=true/false + données
//     */
//    public InsuranceCertificateRequest checkPolice(String policeNumber) {
//
//        String[] parts = policeNumber.split("-");
//        int codeinte = Integer.parseInt(parts[0]);  // 1017
//        long numeroPolice = Long.parseLong(parts[1]);    // 2130000100
//        short avenant = (parts.length == 3)
//                ? Short.parseShort(parts[2])
//                : 0;                         // 4 ou 0 par défaut
//
//        System.out.println("codeint      = " + codeinte);
//        System.out.println("numeroPolice = " + numeroPolice);
//        System.out.println("avenant      = " + avenant);
////      Police police=policeRepository.findByPoliceAndCodeAgent(codeint, numeroPolice, avenant)
////                .orElseThrow(() ->  new ApiErrorPayloadResponse( "Erreur inattendue de l'API externe",java.util.Map.of("general", List.of(responseBody))));
//        //  Police police = policeRepository.findByPoliceAndCodeAgent(codeinte, numeroPolice, avenant)
//        //  .orElseThrow(() -> new UserNotFoundException("Soucis avec le numero de police ..."));
//        List<ZenAttdigAsac> all_zenAttdigAsac = zenAttdigAsacRepository.findByNumeroPolice(policeNumber);
//        // Police introuvable → retourne exists=false
//
//        List<ProductionPayloadRequest> allProductionPayloadRequests = all_zenAttdigAsac.stream()
//                .map(ZenAttdigAsacMapper::toProductionPayloadRequest)
//                .toList();
////                
////        AttestationRisque attestationRisque = attestationRisqueRepository.findByPolice(codeinte, numeroPolice, avenant)
////                .orElseThrow(() -> new UserNotFoundException("Soucis avec l attestion risque non trouver ..."));
////        NatureDocument nd = natureDocumentRepository.findByNatudocu(attestationRisque.getNatudocu())
////                .orElseThrow(() -> new UserNotFoundException("Soucis avec l attestion risque non trouver ..."));
////        ProductionPayloadRequest productions = new ProductionPayloadRequest(nd.getLibnatdo().toUpperCase(),
////                63784,
////                "POL-2026-00123",
////                police.getDateeffe(),
////                police.getDateeche(),
////                // Customer
////                police.getCodeassu().getRaissoci(),
////                police.getCodeassu().getTeleassu(),
////                police.getCodeassu().getMailassu(),
////                police.getCodeassu().getAdreassu(),
////                "TSPM",//customer
////                // Insured
////                police.getCodeassu().getRaissoci(),
////                police.getCodeassu().getTeleassu(),
////                police.getCodeassu().getMailassu(),
////                police.getCodeassu().getAdreassu(),
////                // Vehicle ils s agit du risque
////                "LT-1234-AB",
////                "WVWZZZ3CZWE123456",
////                "TOYOTA",
////                "COROLLA",
////                "01",
////                "GV04",
////                "TV10",
////                "UV01",
////                "SEES",
////                5,
////                5,
////                "A",
////                // Driver
////                "Paul Kamga",
////                LocalDate.of(1990, 5, 20),
////                LocalDate.of(2018, 6, 1),
////                false, "");
//
//        InsuranceCertificateRequest request = new InsuranceCertificateRequest(
//              ""+codeinte, // office_code
//                "ZENITHE", // organization_code
//                "cima", // certificate_type
//                "api", // channel
//                allProductionPayloadRequests);
//
//        return request;
//
//    }
//
//    public String getNatureDocument(String code) {
//        return switch (code) {
//            case "J" ->
//                "JAUNE";
//            case "R" ->
//                "ROSE";
//            case "B" ->
//                "BLEU";
//            case "V" ->
//                "VERT";
//            default ->
//                throw new IllegalArgumentException("Code inconnu : " + code);
//        };
//    }
//
//}
