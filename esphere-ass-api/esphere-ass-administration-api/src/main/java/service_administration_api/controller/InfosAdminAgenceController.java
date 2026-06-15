/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.controller;

/**
 *
 * @author USER01
 */
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service_administration_api.DTO.ApiResponse;
import service_administration_api.DTO.InfosAdminAgenceDTO;
import service_administration_api.service.InfosAdminAgenceService;


@RestController
@RequestMapping("/admin-agences")
//@CrossOrigin(origins = "*")
public class InfosAdminAgenceController {

    private final InfosAdminAgenceService service;

    public InfosAdminAgenceController(InfosAdminAgenceService service) {
        this.service = service;
    }

    /**
     * POST /api/v1/admin-agences
     * Créer un nouvel administrateur agence
     */
    @PostMapping
    public ResponseEntity<ApiResponse<InfosAdminAgenceDTO>> create(
            @Valid @RequestBody InfosAdminAgenceDTO dto) {
        InfosAdminAgenceDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Administrateur agence créé avec succès", created));
    }

    /**
     * GET /api/v1/admin-agences?search=&page=0&size=10&sortBy=id&direction=desc
     * Lister avec pagination et recherche
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<InfosAdminAgenceDTO>>> findAll(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Page<InfosAdminAgenceDTO> result = service.findAll(search, page, size, sortBy, direction);
        return ResponseEntity.ok(ApiResponse.success("Liste récupérée", result));
    }

    /**
     * GET /api/v1/admin-agences/{id}
     * Obtenir un administrateur agence par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InfosAdminAgenceDTO>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Trouvé", service.findById(id)));
    }

    /**
     * PUT /api/v1/admin-agences/{id}
     * Mettre à jour un administrateur agence
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InfosAdminAgenceDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody InfosAdminAgenceDTO dto) {
        InfosAdminAgenceDTO updated = service.update(id, dto);
        return ResponseEntity.ok(
                ApiResponse.success("Administrateur agence mis à jour avec succès", updated));
    }

    /**
     * DELETE /api/v1/admin-agences/{id}
     * Supprimer un administrateur agence
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(
                ApiResponse.success("Administrateur agence supprimé avec succès"));
    }

    /**
     * GET /admin-agences/by-username/{username}
     * Récupérer le profil agent d'un utilisateur par son username.
     * Appelé par l'application Angular après login pour enrichir la session.
     */
    @GetMapping("/by-username/{username}")
    public ResponseEntity<ApiResponse<InfosAdminAgenceDTO>> findByUsername(
            @PathVariable String username) {
        return ResponseEntity.ok(
                ApiResponse.success("Profil agent récupéré",
                        service.findByUsername(username)));
    }

}
