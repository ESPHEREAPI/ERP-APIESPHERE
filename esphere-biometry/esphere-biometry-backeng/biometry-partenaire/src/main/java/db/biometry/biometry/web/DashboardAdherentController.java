package db.biometry.biometry.web;

import db.biometry.biometry.dtos.DashboardAdherentDTO;
import db.biometry.biometry.services.DashboardAdherentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST — Tableau de bord Adhérent.
 *
 * Expose un seul endpoint : GET /dashboard/adherent/me
 *
 * Le codeAdherent est résolu depuis le header X-User-Login propagé
 * par la Gateway — l'adhérent ne peut voir que ses propres données.
 *
 * Un DII ou Service Santé peut passer un codeAdherent explicite
 * en query param pour la supervision.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Dashboard Adhérent", description = "Tableau de bord personnel de l'adhérent")
public class DashboardAdherentController {

    private final DashboardAdherentService dashboardAdherentService;

    /**
     * Tableau de bord personnel de l'adhérent connecté.
     *
     * Résolution du codeAdherent :
     *   1. Paramètre codeAdherent (supervision DII/Service Santé)
     *   2. Header X-User-Login (adhérent connecté)
     */
    @GetMapping("/dashboard/adherent/me")
    @Operation(
        summary = "Dashboard adhérent",
        description = "Retourne le tableau de bord complet : plafonds, consommation, " +
                      "ayants droit, dernières visites, alerte échéance"
    )
    public ResponseEntity<DashboardAdherentDTO> getDashboard(
            @RequestParam(required = false) String codeAdherent,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            // Filtres optionnels pour l'historique des visites
            @RequestParam(required = false) String prestataireId,
            @RequestParam(required = false) String dateDebut,
            @RequestParam(required = false) String dateFin,
            @RequestParam(required = false) String typePrestation,
            @RequestParam(required = false) String codeAyantDroit,
            @RequestHeader(value = "X-User-Login",  required = false) String xUserLogin,
            @RequestHeader(value = "X-Profil-Code", required = false) String xProfilCode) {

        String code = resolveCode(codeAdherent, xUserLogin, xProfilCode);

        if (code == null || code.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        int safePage = Math.max(0, page);
        int safeSize = Math.min(Math.max(1, size), 50);

        log.info("[DashboardAdherent] code={} profil={} page={} size={} type={} presta={}",
                code, xProfilCode, safePage, safeSize, typePrestation, prestataireId);
        return ResponseEntity.ok(dashboardAdherentService.getDashboard(
                code, safePage, safeSize,
                prestataireId, dateDebut, dateFin, typePrestation, codeAyantDroit));
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private String resolveCode(String param, String xUserLogin, String xProfilCode) {
        // DII et Service Santé peuvent passer un code explicite
        boolean isSuperviseur = "DII".equalsIgnoreCase(xProfilCode)
                             || "SERVICE_SANTE".equalsIgnoreCase(xProfilCode);

        if (isSuperviseur && param != null && !param.isBlank()) {
            return param;
        }

        // Priorité 1 : header Gateway (X-User-Login)
        if (xUserLogin != null && !xUserLogin.isBlank()) {
            return xUserLogin;
        }

        // Priorité 2 : query param codeAdherent (accès direct sans gateway)
        if (param != null && !param.isBlank()) {
            return param;
        }

        return null;
    }
}