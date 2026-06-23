package com.esphere.auth.controller;

import com.esphere.auth.entity.Menu;
import com.esphere.auth.entity.Permission;
import com.esphere.auth.entity.Profil;
import com.esphere.auth.exception.AuthException;
import com.esphere.auth.repository.PermissionRepository;
import com.esphere.auth.repository.ProfilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionRepository permissionRepository;
    private final ProfilRepository profilRepository;

    @GetMapping("/menus")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllMenus() {
        List<Menu> menus = permissionRepository.findAllActiveMenus();
        return ResponseEntity.ok(menus.stream().map(m -> Map.<String, Object>of(
                "id", m.getId(),
                "pereId", m.getPereId() != null ? m.getPereId() : 0,
                "nomModule", m.getNomModule(),
                "nomAction", m.getNomAction() != null ? m.getNomAction() : "",
                "classImage", m.getClassImage() != null ? m.getClassImage() : "",
                "numeroOrdre", m.getNumeroOrdre(),
                "type", m.getType()
        )).toList());
    }

    @GetMapping("/profil/{profilId}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    public ResponseEntity<List<Integer>> getPermissionsByProfil(@PathVariable Integer profilId) {
        List<Permission> perms = permissionRepository.findByProfilId(profilId);
        List<Integer> menuIds = perms.stream().map(p -> p.getMenu().getId()).toList();
        return ResponseEntity.ok(menuIds);
    }

    @PutMapping("/profil/{profilId}")
    @PreAuthorize("hasAuthority('SUP_ADMIN')")
    @Transactional
    public ResponseEntity<Map<String, Object>> savePermissions(
            @PathVariable Integer profilId,
            @RequestBody List<Integer> menuIds) {

        Profil profil = profilRepository.findById(profilId)
                .orElseThrow(() -> new AuthException("Profil introuvable"));

        List<Permission> existing = permissionRepository.findByProfilId(profilId);
        Set<Integer> existingMenuIds = existing.stream()
                .map(p -> p.getMenu().getId()).collect(Collectors.toSet());
        Set<Integer> newMenuIds = Set.copyOf(menuIds);

        // Supprimer les permissions retirées
        existing.stream()
                .filter(p -> !newMenuIds.contains(p.getMenu().getId()))
                .forEach(permissionRepository::delete);

        // Ajouter les nouvelles permissions
        newMenuIds.stream()
                .filter(mid -> !existingMenuIds.contains(mid))
                .forEach(mid -> {
                    Menu m = new Menu();
                    m.setId(mid);
                    Permission p = Permission.builder()
                            .profil(profil)
                            .menu(m)
                            .build();
                    permissionRepository.save(p);
                });

        log.info("Permissions mises à jour pour profil {} : {} menus", profil.getCode(), menuIds.size());
        return ResponseEntity.ok(Map.of("saved", menuIds.size()));
    }
}
