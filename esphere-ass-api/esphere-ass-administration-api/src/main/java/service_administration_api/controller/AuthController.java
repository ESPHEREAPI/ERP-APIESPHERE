/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import service_administration_api.DTO.ApiResponse;
import service_administration_api.DTO.LoginResponseDTO;
import service_administration_api.DTO.UserSessionDTO;
import service_administration_api.entite.PosteTravail;
import service_administration_api.entite.pooltpv.Infos_AdministrateurAgencePayLoad;
import service_administration_api.enums.ProfilType;
import service_administration_api.mapper.MapperDtoImpl;
import service_administration_api.repository.InfosAdminAgenceRepository;
import service_administration_api.repository.PosteTravailRepository;
import service_administration_api.service.OracleAuthService;

/**
 * Contrôleur d'authentification ✅ Compatible avec le frontend Angular ✅ Tous
 * les endpoints nécessaires : login, logout, refresh, verify
 */
/**
 *
 * @author USER01
 */
@RestController
//@RequestMapping("/esphere-ass-microservice-admin/auth/users")
@RequestMapping("/auth")

public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private OracleAuthService authService;
    @Autowired
    MapperDtoImpl mapper;
    @Autowired
    private PosteTravailRepository posteTravailRepository;
    @Autowired
    private InfosAdminAgenceRepository agenceRepository;

    /**
     * 🔐 Endpoint de connexion POST /auth/users/login
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UserSessionDTO>> login(@RequestBody LoginResponseDTO request) {
        logger.info("Login request received for user: {}", request.getUsername());
        ProfilType profilType = request.getProfilType();
        // Valider les entrées
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Username requis", "MISSING_USERNAME"));
        }
        
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Password requis", "MISSING_PASSWORD"));
        }

        // Authentifier
        ApiResponse<UserSessionDTO> response = authService.authenticate(
                request.getUsername(),
                request.getPassword()
        );
        
        // Retourner la réponse avec le bon code HTTP
        if (response.isSuccess()) {
            logger.info("Login successful for user: {}", request.getUsername());
            UserSessionDTO session = response.getData();
            session.setProfilType(profilType);

            // Enrichir avec le profilAgent depuis ZEN_INFOS_ADMIN_AGENCE
            String userapiasac = session.getUserapiasac();
            if (userapiasac != null && !userapiasac.isBlank()) {
                agenceRepository.findByUsername(userapiasac).ifPresent(agent -> {
                    session.setProfilAgent(agent.getProfilAgent());
                    session.setCanEdit(agent.isCanEdit());
                    logger.info("ProfilAgent enrichi : {} → {}", userapiasac, agent.getProfilAgent());
                });
            }

            return ResponseEntity.ok(response);
        } else {
            logger.warn("Login failed for user: {} - {}", request.getUsername(), response.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * 🚪 Endpoint de déconnexion POST /auth/users/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        logger.info("Logout request received");

        // Extraire le token du header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Token manquant", "MISSING_TOKEN"));
        }
        
        String token = authHeader.substring(7); // Retirer "Bearer "

        // Déconnecter
        ApiResponse<Void> response = authService.logout(token);
        
        if (response.isSuccess()) {
            logger.info("Logout successful");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Logout failed: {}", response.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * 🔄 Endpoint de rafraîchissement du token POST /auth/users/refresh
     */
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<UserSessionDTO>> refreshToken(
            @RequestBody Map<String, String> request) {
        
        logger.info("Token refresh request received");
        
        String refreshToken = request.get("refreshToken");
        
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Refresh token manquant", "MISSING_REFRESH_TOKEN"));
        }

        // Rafraîchir le token
        ApiResponse<UserSessionDTO> response = authService.refreshToken(refreshToken);
        
        if (response.isSuccess()) {
            logger.info("Token refresh successful");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Token refresh failed: {}", response.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * ✅ Endpoint de vérification du token GET /auth/users/verify
     */
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyToken(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        logger.info("Token verification request received");

        // Extraire le token du header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Token manquant", "MISSING_TOKEN"));
        }
        
        String token = authHeader.substring(7); // Retirer "Bearer "

        // Vérifier le token
        ApiResponse<Map<String, Object>> response = authService.verifyToken(token);
        
        if (response.isSuccess()) {
            logger.info("Token verification successful");
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Token verification failed: {}", response.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * 📋 Endpoint pour récupérer tous les postes de travail GET /auth/all-poste
     * (conservé de votre code original)
     */
    @GetMapping("/all-poste")
    public ResponseEntity<List<PosteTravail>> allPoste() {
        logger.info("Request for all work positions");
        
        List<PosteTravail> postes = posteTravailRepository.findAll();
        
        if (postes.isEmpty()) {
            logger.info("No work positions found");
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        
        logger.info("Returning {} work positions", postes.size());
        return ResponseEntity.ok(postes); // 200 OK
    }

    /**
     * 📊 Endpoint de santé / statut GET /auth/users/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = Map.of(
                "status", "UP",
                "service", "Authentication Service",
                "timestamp", System.currentTimeMillis(),
                "activeUsers", authService.getActiveUsersCount()
        );
        
        return ResponseEntity.ok(health);
    }
    
}
