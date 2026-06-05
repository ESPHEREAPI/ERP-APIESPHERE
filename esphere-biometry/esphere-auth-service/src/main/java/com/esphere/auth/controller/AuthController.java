package com.esphere.auth.controller;

import com.esphere.auth.dto.request.LoginRequest;
import com.esphere.auth.dto.request.OtpRequest;
import com.esphere.auth.dto.request.RefreshTokenRequest;
import com.esphere.auth.dto.request.ValidateOtpRequest;
import com.esphere.auth.dto.response.LoginResponse;
import com.esphere.auth.dto.response.OtpResponse;
import com.esphere.auth.dto.response.UserInfoResponse;
import com.esphere.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller Auth Service.
 *
 * Endpoints :
 *   POST /auth/login   → Authentification, retourne JWT + menus
 *   GET  /auth/me      → Infos de l'utilisateur connecté
 *   POST /auth/logout  → Invalidation côté client (stateless)
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /auth/login
     *
     * Body : { "login": "frank", "password": "monMotDePasse" }
     *
     * Réponse 200 : token JWT + infos utilisateur + menus du profil
     * Réponse 401 : login ou mot de passe incorrect
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /auth/me
     *
     * Header requis : Authorization: Bearer <token>
     *
     * Réponse 200 : infos de l'utilisateur actuellement connecté
     */
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> me(Authentication authentication) {
        // authentication.getName() = login extrait du JWT par JwtAuthFilter
        UserInfoResponse response = authService.getMe(authentication.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /auth/logout
     *
     * Le JWT est stateless : l'invalidation se fait côté client
     * (supprimer le token du localStorage / cookie).
     *
     * Pour une invalidation côté serveur (blacklist), on ajoutera
     * Redis dans une prochaine itération (AM-04 architecture extensible).
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Côté serveur : rien à faire pour l'instant (stateless)
        // Le client doit supprimer le token
        return ResponseEntity.noContent().build();
    }
    /**
 * POST /auth/refresh
 *
 * Body : { "token": "<ancien_jwt>" }
 *
 * Réponse 200 : nouveau JWT + infos utilisateur + menus
 * Réponse 401 : token invalide ou altéré
 *
 * Accessible sans authentification (le token peut être expiré)
 */
 /**
     * POST /auth/refresh-token
     *
     * Body : { "token": "<ancien_jwt>" }
     * Accessible sans authentification (token peut être expiré).
     * Réponse 200 : nouveau JWT + infos utilisateur + menus
     * Réponse 401 : token altéré ou utilisateur inactif
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request.getToken()));
    }
    
    /**
 * POST /auth/otp
 * Appelé par l'app biométrie locale pour générer un token one-time
 * Body : { "prestataireId": "DLA_BINGO", "codeVisite": "ME6ED5", "annee": "2026" }
 * Pas de protection JWT — sécurisé par signature HMAC
 */
@PostMapping("/otp")
public ResponseEntity<OtpResponse> generateOtp(
        @Valid @RequestBody OtpRequest request) {
    return ResponseEntity.ok(authService.generateOtp(request));
}

/**
 * POST /auth/validate-otp
 * Appelé par Angular pour valider le token OTP et obtenir un JWT normal
 * Body : { "otp": "xxxxx" }
 */
@PostMapping("/validate-otp")
public ResponseEntity<LoginResponse> validateOtp(
        @Valid @RequestBody ValidateOtpRequest request) {
    return ResponseEntity.ok(authService.validateOtp(request.getOtp()));
}

}
