package com.esphere.prestataire.controller;

import com.esphere.prestataire.dto.request.PrestataireRequest;
import com.esphere.prestataire.dto.response.PrestataireResponse;
import com.esphere.prestataire.service.AdminPrestataireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/prestataires")
@RequiredArgsConstructor
public class AdminPrestataireController {

    private final AdminPrestataireService adminService;

    // GET /admin/prestataires?page=0&size=10&statut=&categorieId=&villeId=&search=
    @GetMapping
    public ResponseEntity<Map<String, Object>> lister(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "")   String statut,
            @RequestParam(defaultValue = "")   String categorieId,
            @RequestParam(required = false)    Integer villeId,
            @RequestParam(defaultValue = "")   String search) {
        return ResponseEntity.ok(adminService.lister(statut, categorieId, villeId, search, page, size));
    }

    // GET /admin/prestataires/categories
    @GetMapping("/categories")
    public ResponseEntity<List<Map<String, String>>> categories() {
        return ResponseEntity.ok(adminService.listerCategories());
    }

    // GET /admin/prestataires/villes
    @GetMapping("/villes")
    public ResponseEntity<List<Map<String, Object>>> villes() {
        return ResponseEntity.ok(adminService.listerVilles());
    }

    // POST /admin/prestataires
    @PostMapping
    public ResponseEntity<PrestataireResponse> creer(@RequestBody PrestataireRequest req) {
        return ResponseEntity.ok(adminService.creer(req));
    }

    // PUT /admin/prestataires/{id}
    @PutMapping("/{id}")
    public ResponseEntity<PrestataireResponse> modifier(
            @PathVariable String id,
            @RequestBody PrestataireRequest req) {
        return ResponseEntity.ok(adminService.modifier(id, req));
    }

    // PATCH /admin/prestataires/{id}/activer
    @PatchMapping("/{id}/activer")
    public ResponseEntity<PrestataireResponse> activer(@PathVariable String id) {
        return ResponseEntity.ok(adminService.changerStatut(id, "1"));
    }

    // PATCH /admin/prestataires/{id}/desactiver
    @PatchMapping("/{id}/desactiver")
    public ResponseEntity<PrestataireResponse> desactiver(@PathVariable String id) {
        return ResponseEntity.ok(adminService.changerStatut(id, "-1"));
    }

    // DELETE /admin/prestataires/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(@PathVariable String id) {
        adminService.supprimer(id);
        return ResponseEntity.noContent().build();
    }

    // POST /admin/prestataires/{id}/logo
    @PostMapping("/{id}/logo")
    public ResponseEntity<PrestataireResponse> uploadLogo(
            @PathVariable String id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(adminService.uploadLogo(id, file));
    }
}
