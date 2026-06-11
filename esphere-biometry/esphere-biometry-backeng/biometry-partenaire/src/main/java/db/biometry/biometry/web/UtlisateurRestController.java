package db.biometry.biometry.web;

import db.biometry.biometry.dtos.UserDTO;
import db.biometry.biometry.dtos.UserLogin;
import db.biometry.biometry.dtos.UserSessionDTO;
import db.biometry.biometry.enums.ProfilType;
import db.biometry.biometry.exceptions.UtilisateurException;
import db.biometry.biometry.mappers.BiometrieMapperImpl;
import db.biometry.biometry.services.UtilisateurService;

import java.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST — Utilisateurs / Authentification
 *
 * Trois points d'entrée de login selon le type d'utilisateur :
 *   POST /users/login              → agents internes (DII, Service Santé…)
 *   POST /users/compagnie/login    → souscripteurs (police + mot de passe)
 *   POST /users/adherent/login     → adhérents (téléphone + code adhérent)
 *
 * Le profilType est déterminé ainsi (par ordre de priorité) :
 *   1. Header X-Profil-Code propagé par la Gateway (source de vérité)
 *   2. Champ profilType dans le corps de la requête (fallback dev/test)
 *   3. Valeur par défaut selon le point d'entrée utilisé
 */
@RestController
@AllArgsConstructor
@Slf4j
public class UtlisateurRestController {

    private UtilisateurService utilisateurService;
    private BiometrieMapperImpl mappers;

    // ── Utilitaire : résoudre le ProfilType depuis le header Gateway ─────────

    /**
     * Résout le ProfilType dans cet ordre de priorité :
     *   1. Header X-Profil-Code (Gateway) — source de vérité en production
     *   2. profilType dans le body — utile en dev/test direct sans Gateway
     *   3. defaultProfil — valeur de repli selon le point d'entrée
     */
    private ProfilType resolveProfilType(
            String xProfilCode,
            ProfilType bodyProfil,
            ProfilType defaultProfil) {

        // Priorité 1 : header Gateway
        if (xProfilCode != null && !xProfilCode.isBlank()) {
            try {
                ProfilType fromHeader = ProfilType.valueOf(xProfilCode.toUpperCase().trim());
                log.debug("[Auth] ProfilType résolu depuis header Gateway : {}", fromHeader);
                return fromHeader;
            } catch (IllegalArgumentException e) {
                log.warn("[Auth] Header X-Profil-Code inconnu '{}' — ignoré", xProfilCode);
            }
        }

        // Priorité 2 : body de la requête
        if (bodyProfil != null) {
            log.debug("[Auth] ProfilType résolu depuis le body : {}", bodyProfil);
            return bodyProfil;
        }

        // Priorité 3 : valeur par défaut du point d'entrée
        log.debug("[Auth] ProfilType par défaut appliqué : {}", defaultProfil);
        return defaultProfil;
    }

    // ── GET /users/all-users ─────────────────────────────────────────────────

    @GetMapping("users/all-users")
    public List<UserDTO> listeusers() {
        return utilisateurService.listeUtilisateur();
    }

    // ── GET /users/{id} ──────────────────────────────────────────────────────

    @GetMapping("users/{id}")
    public UserDTO getUser(@PathVariable(name = "id") int userId) {
        try {
            return utilisateurService.getUser(userId);
        } catch (UtilisateurException u) {
            UserDTO erreur = new UserDTO();
            erreur.setEcheck_connection(true);
            erreur.setMessageEcheck(u.getMessage());
            return erreur;
        }
    }

    // ── GET /users/search ────────────────────────────────────────────────────

    @GetMapping("users/search")
    public List<UserDTO> searchUsers(
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return utilisateurService.seacrhUsers(keyword);
    }

    // ── POST /users/login  (agents internes : DII, Service Santé…) ──────────

    @PostMapping("users/login")
    public ResponseEntity<?> connect(
            @RequestBody UserLogin userLogin,
            @RequestHeader(value = "X-Profil-Code", required = false) String xProfilCode) {
        try {
            log.info("[Auth] Tentative login interne : {}", userLogin.getUserName());

            ResponseEntity<?> validation = validateLoginFields(userLogin);
            if (validation != null) return validation;

            UserDTO user = utilisateurService.findUserByLogin(userLogin);

            if (Boolean.TRUE.equals(user.getEcheck_connection())) {
                log.warn("[Auth] Échec login interne {} : {}", userLogin.getUserName(), user.getMessageEcheck());
                return errorResponse(HttpStatus.UNAUTHORIZED, user.getMessageEcheck());
            }

            // Résoudre le profil — DII par défaut pour ce point d'entrée
            ProfilType profil = resolveProfilType(xProfilCode, userLogin.getProfilType(), ProfilType.DII);
            user.setProfilType(profil);

            UserSessionDTO session = mappers.mapUserSessionDTOByuserDTO(user);
            if (session == null || session.getToken() == null) {
                log.error("[Auth] Impossible de créer la session pour {}", userLogin.getUserName());
                return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la création de la session.");
            }

            log.info("[Auth] Login interne réussi : {} (profil={})", userLogin.getUserName(), profil);
            return ResponseEntity.ok(session);

        } catch (Exception e) {
            log.error("[Auth] Erreur inattendue login interne {} : {}", userLogin.getUserName(), e.getMessage(), e);
            return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Une erreur interne est survenue.");
        }
    }

    // ── POST /users/compagnie/login  (souscripteurs) ─────────────────────────

    @PostMapping("users/compagnie/login")
    public ResponseEntity<?> connectCompagnie(
            @RequestBody UserLogin userLogin,
            @RequestHeader(value = "X-Profil-Code", required = false) String xProfilCode) {
        try {
            log.info("[Auth] Tentative login souscripteur (police) : {}", userLogin.getUserName());

            ResponseEntity<?> validation = validateLoginFields(userLogin);
            if (validation != null) return validation;

            UserDTO user = utilisateurService.findSouscripteurByLogin(userLogin);

            if (user == null || Boolean.TRUE.equals(user.getEcheck_connection())) {
                String msg = user != null ? user.getMessageEcheck() : "Login ou mot de passe incorrect";
                log.warn("[Auth] Échec login souscripteur {} : {}", userLogin.getUserName(), msg);
                return errorResponse(HttpStatus.UNAUTHORIZED, msg);
            }

            // Résoudre le profil — SOUSCRIPTEUR par défaut pour ce point d'entrée
            ProfilType profil = resolveProfilType(xProfilCode, userLogin.getProfilType(), ProfilType.SOUSCRIPTEUR);
            user.setProfilType(profil);

            UserSessionDTO session = mappers.mapUserSessionDTOByuserDTO(user);
            if (session == null || session.getToken() == null) {
                return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la création de la session.");
            }

            log.info("[Auth] Login souscripteur réussi : {} (profil={})", userLogin.getUserName(), profil);
            return ResponseEntity.ok(session);

        } catch (Exception e) {
            log.error("[Auth] Erreur login souscripteur {} : {}", userLogin.getUserName(), e.getMessage(), e);
            return errorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    // ── POST /users/adherent/login  (adhérents) ──────────────────────────────

    @PostMapping("users/adherent/login")
    public ResponseEntity<?> connectAdherent(
            @RequestBody UserLogin userLogin,
            @RequestHeader(value = "X-Profil-Code", required = false) String xProfilCode) {
        try {
            log.info("[Auth] Tentative login adhérent (tél) : {}", userLogin.getUserName());

            ResponseEntity<?> validation = validateLoginFields(userLogin);
            if (validation != null) return validation;

            UserDTO user = utilisateurService.findAdherentByLogin(userLogin);

            if (user == null || Boolean.TRUE.equals(user.getEcheck_connection())) {
                String msg = user != null ? user.getMessageEcheck() : "Login ou mot de passe incorrect";
                log.warn("[Auth] Échec login adhérent {} : {}", userLogin.getUserName(), msg);
                return errorResponse(HttpStatus.UNAUTHORIZED, msg);
            }

            // Résoudre le profil — ADHERENT par défaut pour ce point d'entrée
            ProfilType profil = resolveProfilType(xProfilCode, userLogin.getProfilType(), ProfilType.ADHERENT);
            user.setProfilType(profil);

            UserSessionDTO session = mappers.mapUserSessionDTOByuserDTO(user);
            if (session == null || session.getToken() == null) {
                return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur lors de la création de la session.");
            }

            log.info("[Auth] Login adhérent réussi : {} (profil={})", userLogin.getUserName(), profil);
            return ResponseEntity.ok(session);

        } catch (Exception e) {
            log.error("[Auth] Erreur login adhérent {} : {}", userLogin.getUserName(), e.getMessage(), e);
            return errorResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    // ── POST /users/logout ───────────────────────────────────────────────────

    @PostMapping("users/logout")
    public ResponseEntity<?> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        // Le JWT est stateless — l'invalidation côté client suffit.
        // Si une blacklist serveur est ajoutée plus tard, c'est ici qu'elle s'intègre.
        log.info("[Auth] Déconnexion");
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Déconnexion réussie");
        response.put("timestamp", new Date());
        return ResponseEntity.ok(response);
    }

    // ── Helpers privés ───────────────────────────────────────────────────────

    private ResponseEntity<?> validateLoginFields(UserLogin userLogin) {
        if (userLogin.getUserName() == null || userLogin.getUserName().isBlank()) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Le nom d'utilisateur est requis.");
        }
        if (userLogin.getPassWord() == null || userLogin.getPassWord().isBlank()) {
            return errorResponse(HttpStatus.BAD_REQUEST, "Le mot de passe est requis.");
        }
        return null;
    }

    private ResponseEntity<Map<String, String>> errorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("message", message));
    }
}