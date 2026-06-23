package com.esphere.media.controller;

import com.esphere.media.dto.response.MediaResponse;
import com.esphere.media.service.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    // ── UPLOAD MOBILE via code_court ──────────────────────────────
    @PostMapping("/capture/{codeCourt}")
    public ResponseEntity<MediaResponse> captureParCodeCourt(
            @PathVariable String codeCourt,
            @RequestParam("fichier") MultipartFile fichier,
            @RequestParam(value = "employeId",        required = false) Integer employeId,
            @RequestParam(value = "demandeParSs",     defaultValue = "false") boolean demandeParSs,
            @RequestParam(value = "prestationId",     required = false) Integer prestationId,
            @RequestParam(value = "naturePrestation", required = false) String  naturePrestation) {
        return ResponseEntity.ok(mediaService.uploadParCodeCourt(
                codeCourt, fichier, employeId, demandeParSs, prestationId, naturePrestation));
    }

    // ── UPLOAD interne via visiteId ───────────────────────────────
    @PostMapping("/medias/visite/{visiteId}")
    public ResponseEntity<MediaResponse> uploadParVisiteId(
            @PathVariable String visiteId,
            @RequestParam("fichier") MultipartFile fichier,
            @RequestParam(value = "employeId",        required = false) Integer employeId,
            @RequestParam(value = "demandeParSs",     defaultValue = "false") boolean demandeParSs,
            @RequestParam(value = "prestationId",     required = false) Integer prestationId,
            @RequestParam(value = "naturePrestation", required = false) String  naturePrestation) {
        return ResponseEntity.ok(mediaService.uploadParVisiteId(
                visiteId, fichier, employeId, demandeParSs, prestationId, naturePrestation));
    }

    // ── REVUE DOCUMENT SS ─────────────────────────────────────────

    @PutMapping("/medias/{id}/approuver")
    public ResponseEntity<MediaResponse> approuver(
            @PathVariable Integer id,
            @RequestParam(value = "employeId", required = false) Integer employeId) {
        return ResponseEntity.ok(mediaService.approuver(id, employeId));
    }

    @PutMapping("/medias/{id}/rejeter")
    public ResponseEntity<MediaResponse> rejeter(
            @PathVariable Integer id,
            @RequestBody Map<String, String> body,
            @RequestParam(value = "employeId", required = false) Integer employeId) {
        return ResponseEntity.ok(
                mediaService.rejeter(id, body.get("commentaire"), employeId));
    }

    // ── LECTURES ─────────────────────────────────────────────────

    @GetMapping("/medias/visite/{visiteId}")
    public ResponseEntity<List<MediaResponse>> getParVisite(
            @PathVariable String visiteId) {
        return ResponseEntity.ok(mediaService.getParVisite(visiteId));
    }

    @GetMapping("/medias/prestation/{prestationId}")
    public ResponseEntity<List<MediaResponse>> getParPrestation(
            @PathVariable Integer prestationId) {
        return ResponseEntity.ok(mediaService.getParPrestation(prestationId));
    }

    @GetMapping("/medias/adherent/{codeAdherent}")
    public ResponseEntity<List<MediaResponse>> getParAdherent(
            @PathVariable String codeAdherent) {
        return ResponseEntity.ok(mediaService.getParAdherent(codeAdherent));
    }

    @GetMapping("/medias/prestataire/{prestataireId}")
    public ResponseEntity<List<MediaResponse>> getParPrestataire(
            @PathVariable String prestataireId) {
        return ResponseEntity.ok(mediaService.getParPrestataire(prestataireId));
    }

    @GetMapping("/medias/demandes-ss")
    public ResponseEntity<List<MediaResponse>> getDemandesParSs() {
        return ResponseEntity.ok(mediaService.getDemandesParSs());
    }

    // ── TÉLÉCHARGEMENT ───────────────────────────────────────────

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
