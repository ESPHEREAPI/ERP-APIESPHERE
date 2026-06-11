package com.esphere.auth.controller;

import com.esphere.auth.dto.response.UserInfoResponse;
import com.esphere.auth.entity.Employe;
import com.esphere.auth.entity.Utilisateur;
import com.esphere.auth.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller de gestion des utilisateurs.
 * Accessible via : GET /auth/users, POST /auth/users, etc.
 *
 * Sécurité : seul SUP_ADMIN peut gérer les utilisateurs.
 */
@Slf4j
@RestController
@RequestMapping("/auth/users")
@RequiredArgsConstructor
public class UserController {

    private final UtilisateurRepository utilisateurRepository;

    // ── Liste paginée ─────────────────────────────────────────
    /**
     * GET /auth/users?page=0&size=10&search=dupont
     * Retourne la liste paginée des utilisateurs actifs.
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)    String search) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Utilisateur> utilisateurs;
        if (search != null && !search.isBlank()) {
            utilisateurs = utilisateurRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrLoginContainingIgnoreCase(
                    search, search, search, pageable);
        } else {
            utilisateurs = utilisateurRepository.findAll(pageable);
        }

        Page<UserInfoResponse> dtos = utilisateurs.map(this::toResponse);

        return ResponseEntity.ok(Map.of(
            "data",         dtos.getContent(),
            "total",        dtos.getTotalElements(),
            "totalPages",   dtos.getTotalPages(),
            "currentPage",  dtos.getNumber()
        ));
    }

    // ── Détail utilisateur ────────────────────────────────────
    /**
     * GET /auth/users/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<UserInfoResponse> getUserById(@PathVariable Integer id) {
        return utilisateurRepository.findById(id)
            .map(u -> ResponseEntity.ok(toResponse(u)))
            .orElse(ResponseEntity.notFound().build());
    }

    // ── Activation ────────────────────────────────────────────
    /**
     * PATCH /auth/users/{id}/activate
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<UserInfoResponse> activate(@PathVariable Integer id) {
        return utilisateurRepository.findById(id).map(u -> {
            u.setStatut("1");
            utilisateurRepository.save(u);
            log.info("Utilisateur {} activé", u.getLogin());
            return ResponseEntity.ok(toResponse(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Désactivation ─────────────────────────────────────────
    /**
     * PATCH /auth/users/{id}/deactivate
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<UserInfoResponse> deactivate(@PathVariable Integer id) {
        return utilisateurRepository.findById(id).map(u -> {
            u.setStatut("0");
            utilisateurRepository.save(u);
            log.info("Utilisateur {} désactivé", u.getLogin());
            return ResponseEntity.ok(toResponse(u));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Suppression logique ───────────────────────────────────
    /**
     * DELETE /auth/users/{id}
     * Suppression logique (supprime = '1', statut = '0')
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return utilisateurRepository.findById(id).map(u -> {
            u.setSupprime("1");
            u.setStatut("0");
            utilisateurRepository.save(u);
            log.info("Utilisateur {} supprimé (logique)", u.getLogin());
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Réinitialisation mot de passe ─────────────────────────
    /**
     * POST /auth/users/{id}/reset-password
     * Génère un mot de passe temporaire et le retourne.
     * À améliorer : envoyer par email via notification-service.
     */
    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Map<String, String>> resetPassword(@PathVariable Integer id) {
        return utilisateurRepository.findById(id).map(u -> {
            String tempPassword = "Esphere@" + (1000 + (int)(Math.random() * 9000));
            log.info("Mot de passe réinitialisé pour : {}", u.getLogin());
            // TODO : hasher avec CryptoLegacy et sauvegarder
            return ResponseEntity.ok(Map.of("temporaryPassword", tempPassword));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Mapping entité → DTO ──────────────────────────────────
    private UserInfoResponse toResponse(Utilisateur u) {
        Employe employe = u.getEmploye();
        return UserInfoResponse.builder()
            .id(u.getId())
            .login(u.getLogin())
            .nom(u.getNom())
            .prenom(u.getPrenom())
            .email(u.getEmail())
            .genre(u.getGenre())
            .telephone(u.getTelephone())
            .statut(u.getStatut())
            .profilCode(employe != null ? employe.getProfil().getCode() : null)
            .profilLibelle(employe != null ? employe.getProfil().getTypeProfil() : null)
            .connexionAppli(employe != null ? employe.getConnexionAppli() : null)
            .prestataireId(employe != null ? employe.getPrestataireId() : null)
            .build();
    }
}
