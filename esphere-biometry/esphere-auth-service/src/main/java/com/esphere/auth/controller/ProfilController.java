package com.esphere.auth.controller;

import com.esphere.auth.entity.Profil;
import com.esphere.auth.repository.ProfilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller de gestion des profils (= rôles dans le frontend Angular).
 * Accessible via : /auth/roles/**
 *
 * La table dbx45ty_profil contient les profils :
 * SUP_ADMIN, SERVICE_SANTE, PHARMACIE, LABORATOIRE, etc.
 */
@Slf4j
@RestController
@RequestMapping("/auth/roles")
@RequiredArgsConstructor
public class ProfilController {

    private final ProfilRepository profilRepository;

    // ── Liste complète ────────────────────────────────────────
    /**
     * GET /auth/roles/all
     * Tous les profils actifs, sans pagination.
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<List<Profil>> getAll() {
        return ResponseEntity.ok(
            profilRepository.findByStatutAndSupprime("1", "-1"));
    }

    // ── Liste paginée ─────────────────────────────────────────
    /**
     * GET /auth/roles?page=0&size=10&search=admin
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Map<String, Object>> getPaginated(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false)    String search) {

        List<Profil> all = profilRepository.findByStatutAndSupprime("1", "-1");

        // Filtrage simple en mémoire (volume faible)
        List<Profil> filtered = (search == null || search.isBlank()) ? all :
            all.stream().filter(p ->
                p.getCode().toLowerCase().contains(search.toLowerCase()) ||
                p.getTypeProfil().toLowerCase().contains(search.toLowerCase())
            ).toList();

        int start = page * size;
        int end   = Math.min(start + size, filtered.size());
        List<Profil> page_ = (start >= filtered.size())
            ? List.of() : filtered.subList(start, end);

        return ResponseEntity.ok(Map.of(
            "data",       page_,
            "total",      filtered.size(),
            "totalPages", (int) Math.ceil((double) filtered.size() / size),
            "page",       page
        ));
    }

    // ── Détail ────────────────────────────────────────────────
    /**
     * GET /auth/roles/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Profil> getById(@PathVariable Integer id) {
        return profilRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // ── Création ──────────────────────────────────────────────
    /**
     * POST /auth/roles
     */
    @PostMapping
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Profil> create(@RequestBody Profil profil) {
        profil.setStatut("1");
        profil.setSupprime("-1");
        Profil saved = profilRepository.save(profil);
        log.info("Profil créé : {}", saved.getCode());
        return ResponseEntity.ok(saved);
    }

    // ── Mise à jour ───────────────────────────────────────────
    /**
     * PUT /auth/roles/{id}
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Profil> update(@PathVariable Integer id,
                                          @RequestBody Profil body) {
        return profilRepository.findById(id).map(profil -> {
            profil.setTypeProfil(body.getTypeProfil());
            profil.setTypeSousProfil(body.getTypeSousProfil());
            profil.setCode(body.getCode());
            Profil saved = profilRepository.save(profil);
            log.info("Profil mis à jour : {}", saved.getCode());
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ── Suppression logique ───────────────────────────────────
    /**
     * DELETE /auth/roles/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return profilRepository.findById(id).map(profil -> {
            profil.setSupprime("1");
            profil.setStatut("0");
            profilRepository.save(profil);
            log.info("Profil supprimé (logique) : {}", profil.getCode());
            return ResponseEntity.ok().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
