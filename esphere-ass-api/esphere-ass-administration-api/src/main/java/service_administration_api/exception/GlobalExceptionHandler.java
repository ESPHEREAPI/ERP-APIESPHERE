package service_administration_api.exception;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import service_administration_api.DTO.ApiResponse;
import service_administration_api.exception.StockInsuffisantException;

/**
 * Gestionnaire global d'exceptions — toutes les erreurs backend sont
 * normalisées ici avant d'être remontées au frontend Angular.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ── Authentification & Autorisation ──────────────────────────────────────

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUserNotFound(UserNotFoundException ex) {
        log.warn("Utilisateur non trouvé : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ex.getMessage(), "USER_NOT_FOUND"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthentication(AuthenticationException ex) {
        log.warn("Échec d'authentification : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("Authentification échouée", "AUTHENTICATION_FAILED"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Accès refusé : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("Accès refusé", "ACCESS_DENIED"));
    }

    // ── Ressources & Doublons ─────────────────────────────────────────────────

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiResponse<Void>> handleDuplicate(DuplicateResourceException ex) {
        log.warn("Ressource dupliquée : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), "DUPLICATE_RESOURCE"));
    }

    @ExceptionHandler(CertificateNotFoundException.class)
    public ResponseEntity<ValidationErrorPayloadResponse> handleCertificateNotFound(
            CertificateNotFoundException ex) {
        log.warn("Certificat introuvable : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ValidationErrorPayloadResponse(
                        404, ex.getMessage(), Map.of(),
                        ValidationErrorPayloadResponse.ErrorType.UNKNOWN));
    }

    // ── Validation des données ────────────────────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorPayloadResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())));

        log.warn("Erreurs de validation : {}", errors);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ValidationErrorPayloadResponse(
                        422, "Erreurs de validation du formulaire", errors,
                        ValidationErrorPayloadResponse.ErrorType.VALIDATION));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableBody(
            HttpMessageNotReadableException ex) {
        log.warn("Corps de requête illisible : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Corps de la requête invalide ou mal formé", "INVALID_BODY"));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        log.warn("Type de paramètre incorrect — paramètre '{}' : {}", ex.getName(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Paramètre '%s' invalide".formatted(ex.getName()), "INVALID_PARAMETER"));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingHeader(
            MissingRequestHeaderException ex) {
        log.warn("Header manquant : {}", ex.getHeaderName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        "Header requis manquant : " + ex.getHeaderName(), "MISSING_HEADER"));
    }

    // ── Stock attestations ───────────────────────────────────────────────────

    @ExceptionHandler(StockInsuffisantException.class)
    public ResponseEntity<ValidationErrorPayloadResponse> handleStockInsuffisant(
            StockInsuffisantException ex) {
        log.warn("Stock insuffisant [{}] : demandé={}, disponible={}",
            ex.getOfficeCode(), ex.getDemande(), ex.getDisponible());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ValidationErrorPayloadResponse(
                        422, ex.getMessage(),
                        Map.of(
                            "officeCode",  List.of(ex.getOfficeCode()),
                            "demande",     List.of(String.valueOf(ex.getDemande())),
                            "disponible",  List.of(String.valueOf(ex.getDisponible()))
                        ),
                        ValidationErrorPayloadResponse.ErrorType.STOCK_INSUFFICIENT));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Argument invalide : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), "INVALID_ARGUMENT"));
    }

    // ── API Externe (ASAC, PoolTPV) ───────────────────────────────────────────

    @ExceptionHandler(ExternalApiPayloadException.class)
    public ResponseEntity<ValidationErrorPayloadResponse> handleExternalApi(
            ExternalApiPayloadException ex) {

        ApiErrorPayloadResponse apiError = ex.getApiErrorResponse();
        int status = ex.getHttpStatus();
        ValidationErrorPayloadResponse.ErrorType errorType = resolveErrorType(status, apiError);

        log.error("Erreur API externe [{}] : {}", status, apiError.message());

        ValidationErrorPayloadResponse response = new ValidationErrorPayloadResponse(
                status,
                apiError.message() != null ? apiError.message() : resolveDefaultMessage(status),
                apiError.errors() != null ? apiError.errors() : Map.of(),
                errorType);

        return ResponseEntity.status(status).body(response);
    }

    // ── Base de données ───────────────────────────────────────────────────────

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataAccess(DataAccessException ex) {
        log.error("Erreur d'accès à la base de données Oracle", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        "Erreur d'accès aux données. Veuillez réessayer.", "DATABASE_ERROR"));
    }

    // ── Filet de sécurité générique ───────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ValidationErrorPayloadResponse> handleGeneric(
            Exception ex, WebRequest request) {

        log.error("Erreur inattendue sur [{}]", request.getDescription(false), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ValidationErrorPayloadResponse(
                        500,
                        "Une erreur interne s'est produite. Veuillez contacter l'administrateur.",
                        Map.of("detail", List.of(
                                ex.getClass().getSimpleName() + ": " + sanitize(ex.getMessage()))),
                        ValidationErrorPayloadResponse.ErrorType.UNKNOWN));
    }

    // ── Utilitaires ───────────────────────────────────────────────────────────

    private ValidationErrorPayloadResponse.ErrorType resolveErrorType(
            int status, ApiErrorPayloadResponse apiError) {
        return switch (status) {
            case 401 -> ValidationErrorPayloadResponse.ErrorType.UNAUTHORIZED;
            case 403 -> ValidationErrorPayloadResponse.ErrorType.FORBIDDEN;
            case 422 -> {
                boolean isStock = apiError.message() != null
                        && apiError.message().toLowerCase().contains("stock");
                yield isStock
                        ? ValidationErrorPayloadResponse.ErrorType.STOCK_INSUFFICIENT
                        : ValidationErrorPayloadResponse.ErrorType.VALIDATION;
            }
            default -> ValidationErrorPayloadResponse.ErrorType.UNKNOWN;
        };
    }

    private String resolveDefaultMessage(int status) {
        return switch (status) {
            case 401 -> "Utilisateur non authentifié";
            case 403 -> "Action non autorisée";
            case 422 -> "Données invalides";
            default -> "Erreur inattendue";
        };
    }

    /** Évite d'exposer des détails techniques sensibles en production */
    private String sanitize(String message) {
        if (message == null) return "Aucun détail disponible";
        return message.length() > 300 ? message.substring(0, 300) + "..." : message;
    }
}
