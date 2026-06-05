package com.esphere.media.controller;

import com.esphere.media.dto.response.MediaResponse;
import com.esphere.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    // ── URL MOBILE PRESTATAIRE ────────────────────────────────────
    // POST /capture/{code_court} → upload via code_court (mobile)
    @PostMapping("/capture/{codeCourt}")
    public ResponseEntity<MediaResponse> captureParCodeCourt(
            @PathVariable String codeCourt,
            @RequestParam("fichier") MultipartFile fichier,
            @RequestParam(value = "employeId", required = false) Integer employeId,
            @RequestParam(value = "demandeParSs", defaultValue = "false")
                boolean demandeParSs) {
        return ResponseEntity.ok(mediaService.uploadParCodeCourt(
                codeCourt, fichier, employeId, demandeParSs));
    }

    // ── URL INTERNE SERVICE SANTÉ ─────────────────────────────────
    // POST /medias/visite/{visiteId} → upload via visiteId
    @PostMapping("/medias/visite/{visiteId}")
    public ResponseEntity<MediaResponse> uploadParVisiteId(
            @PathVariable String visiteId,
            @RequestParam("fichier") MultipartFile fichier,
            @RequestParam(value = "employeId", required = false) Integer employeId,
            @RequestParam(value = "demandeParSs", defaultValue = "false")
                boolean demandeParSs) {
        return ResponseEntity.ok(mediaService.uploadParVisiteId(
                visiteId, fichier, employeId, demandeParSs));
    }

    // ── LECTURES ─────────────────────────────────────────────────

    // GET /medias/visite/{visiteId}
    @GetMapping("/medias/visite/{visiteId}")
    public ResponseEntity<List<MediaResponse>> getParVisite(
            @PathVariable String visiteId) {
        return ResponseEntity.ok(mediaService.getParVisite(visiteId));
    }

    // GET /medias/adherent/{codeAdherent}
    @GetMapping("/medias/adherent/{codeAdherent}")
    public ResponseEntity<List<MediaResponse>> getParAdherent(
            @PathVariable String codeAdherent) {
        return ResponseEntity.ok(mediaService.getParAdherent(codeAdherent));
    }

    // GET /medias/prestataire/{prestataireId}
    @GetMapping("/medias/prestataire/{prestataireId}")
    public ResponseEntity<List<MediaResponse>> getParPrestataire(
            @PathVariable String prestataireId) {
        return ResponseEntity.ok(mediaService.getParPrestataire(prestataireId));
    }

    // GET /medias/demandes-ss
    @GetMapping("/medias/demandes-ss")
    public ResponseEntity<List<MediaResponse>> getDemandesParSs() {
        return ResponseEntity.ok(mediaService.getDemandesParSs());
    }

    // ── TÉLÉCHARGEMENT ───────────────────────────────────────────

    // GET /medias/{id}/telecharger
    @GetMapping("/medias/{id}/telecharger")
    public ResponseEntity<byte[]> telecharger(@PathVariable Integer id) {
        byte[] contenu     = mediaService.telecharger(id);
        String contentType = mediaService.getContentType(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"media_" + id + "\"")
                .body(contenu);
    }
}