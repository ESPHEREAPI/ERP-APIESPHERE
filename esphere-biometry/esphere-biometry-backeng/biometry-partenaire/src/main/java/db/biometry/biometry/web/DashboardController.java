package db.biometry.biometry.web;

import db.biometry.biometry.dtos.DashboardStatisticsDTO;
import db.biometry.biometry.repositories.AdherentRepository;
import db.biometry.biometry.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Controller REST — Tableau de bord souscripteur
 *
 * Le codeSouscripteur est résolu dans cet ordre :
 *   1. Header X-User-Login propagé par la Gateway (utilisateur connecté)
 *   2. Paramètre codeSouscripteur en query string (DII uniquement, supervision)
 *
 * Un SOUSCRIPTEUR ne peut consulter que ses propres données.
 * Un DII ou SERVICE_SANTE peut passer n'importe quel codeSouscripteur.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Tableau de Bord", description = "APIs pour le tableau de bord souscripteur")
public class DashboardController {

    private final DashboardService dashboardService;
    private final AdherentRepository adherentRepository;

    // ── Utilitaire : résoudre le code souscripteur ───────────────────────────

    /**
     * Résout le code souscripteur à utiliser pour la requête :
     * - Si X-Profil-Code est SOUSCRIPTEUR → force l'utilisation de son propre code (X-User-Login)
     * - Sinon (DII, SERVICE_SANTE) → utilise le paramètre passé, ou X-User-Login par défaut
     */
    private String resolveCodeSouscripteur(
            String xUserLogin,
            String xProfilCode,
            String queryCodeSouscripteur) {

        boolean isSouscripteur = "SOUSCRIPTEUR".equalsIgnoreCase(xProfilCode);

        if (isSouscripteur) {
            // Un souscripteur ne peut voir que ses propres données
            if (xUserLogin == null || xUserLogin.isBlank()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Identité souscripteur non déterminable (header X-User-Login manquant).");
            }
            // xUserLogin = numéro de police (ex: "1017-2130000100")
            // Les requêtes filtrent par ad.souscripteur (ex: "CAMTEL") → conversion nécessaire
            String nomSouscripteur = adherentRepository.findSouscripteurByPolice(xUserLogin);
            if (nomSouscripteur == null || nomSouscripteur.isBlank()) {
                log.warn("[Dashboard] Impossible de résoudre le souscripteur pour la police {}", xUserLogin);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Aucun souscripteur trouvé pour la police : " + xUserLogin);
            }
            log.debug("[Dashboard] Souscripteur {} (police {}) — données propres", nomSouscripteur, xUserLogin);
            return nomSouscripteur;
        }

        // DII / SERVICE_SANTE : peut passer un code en paramètre pour supervision
        if (queryCodeSouscripteur != null && !queryCodeSouscripteur.isBlank()) {
            log.debug("[Dashboard] Supervision par {} — souscripteur cible : {}", xProfilCode, queryCodeSouscripteur);
            return queryCodeSouscripteur;
        }

        // Fallback : son propre code (utile si DII consulte son propre dashboard)
        if (xUserLogin != null && !xUserLogin.isBlank()) {
            return xUserLogin;
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Le paramètre codeSouscripteur est requis.");
    }

    // ── GET /dashboard/statistics ────────────────────────────────────────────

    @GetMapping("/dashboard/statistics")
    @Operation(summary = "Statistiques tableau de bord",
               description = "Statistiques complètes pour une période donnée")
    public ResponseEntity<DashboardStatisticsDTO> getStatistics(
            @RequestParam(required = false) String codeSouscripteur,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestHeader(value = "X-User-Login",  required = false) String xUserLogin,
            @RequestHeader(value = "X-Profil-Code", required = false) String xProfilCode) {

        String codeResolu = resolveCodeSouscripteur(xUserLogin, xProfilCode, codeSouscripteur);

        LocalDateTime start = dateDebut != null
                ? dateDebut.atStartOfDay()
                : LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = dateFin != null
                ? dateFin.atTime(23, 59, 59)
                : LocalDateTime.now();

        log.info("[Dashboard] statistics — souscripteur={} période={} → {}", codeResolu, start, end);
        return ResponseEntity.ok(dashboardService.generateDashboardStatistics(codeResolu, start, end));
    }

    // ── GET /dashboard/statistics/current-month ──────────────────────────────

    @GetMapping("/dashboard/statistics/current-month")
    @Operation(summary = "Statistiques du mois en cours")
    public ResponseEntity<DashboardStatisticsDTO> getCurrentMonthStatistics(
            @RequestParam(required = false) String codeSouscripteur,
            @RequestHeader(value = "X-User-Login",  required = false) String xUserLogin,
            @RequestHeader(value = "X-Profil-Code", required = false) String xProfilCode) {

        String codeResolu = resolveCodeSouscripteur(xUserLogin, xProfilCode, codeSouscripteur);
        LocalDateTime start = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end   = LocalDateTime.now();

        log.info("[Dashboard] current-month — souscripteur={}", codeResolu);
        return ResponseEntity.ok(dashboardService.generateDashboardStatistics(codeResolu, start, end));
    }

    // ── GET /dashboard/statistics/current-year ───────────────────────────────

    @GetMapping("/dashboard/statistics/current-year")
    @Operation(summary = "Statistiques de l'année en cours")
    public ResponseEntity<DashboardStatisticsDTO> getCurrentYearStatistics(
            @RequestParam(required = false) String codeSouscripteur,
            @RequestHeader(value = "X-User-Login",  required = false) String xUserLogin,
            @RequestHeader(value = "X-Profil-Code", required = false) String xProfilCode) {

        String codeResolu = resolveCodeSouscripteur(xUserLogin, xProfilCode, codeSouscripteur);
        LocalDateTime start = LocalDateTime.now().withDayOfYear(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end   = LocalDateTime.now();

        log.info("[Dashboard] current-year — souscripteur={}", codeResolu);
        return ResponseEntity.ok(dashboardService.generateDashboardStatistics(codeResolu, start, end));
    }

    // ── GET /dashboard/statistics/last-week ──────────────────────────────────

    @GetMapping("/dashboard/statistics/last-week")
    @Operation(summary = "Statistiques des 7 derniers jours")
    public ResponseEntity<DashboardStatisticsDTO> getLastWeekStatistics(
            @RequestParam(required = false) String codeSouscripteur,
            @RequestHeader(value = "X-User-Login",  required = false) String xUserLogin,
            @RequestHeader(value = "X-Profil-Code", required = false) String xProfilCode) {

        String codeResolu = resolveCodeSouscripteur(xUserLogin, xProfilCode, codeSouscripteur);
        LocalDateTime start = LocalDateTime.now().minusDays(7).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end   = LocalDateTime.now();

        log.info("[Dashboard] last-week — souscripteur={}", codeResolu);
        return ResponseEntity.ok(dashboardService.generateDashboardStatistics(codeResolu, start, end));
    }
}