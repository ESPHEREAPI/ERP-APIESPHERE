package com.esphere.visite.controller;

import com.esphere.visite.dto.request.VisiteRequest;
import com.esphere.visite.dto.response.*;
import com.esphere.visite.service.VisiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visites")
@RequiredArgsConstructor
public class VisiteController {

    private final VisiteService visiteService;

    // POST /visites → créer une visite
    @PostMapping
    public ResponseEntity<VisiteResponse> creer(@Valid @RequestBody VisiteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(visiteService.creerVisite(request));
    }

    // GET /visites/{id}
    @GetMapping("/{id}")
    public ResponseEntity<VisiteResponse> getVisite(@PathVariable String id) {
        return ResponseEntity.ok(visiteService.getVisite(id));
    }

    // GET /visites/code/{codeCourt} → lookup mobile
    @GetMapping("/code/{codeCourt}")
    public ResponseEntity<VisiteResponse> getByCodeCourt(@PathVariable String codeCourt) {
        return ResponseEntity.ok(visiteService.getVisiteByCodeCourt(codeCourt));
    }

    // GET /visites/prestataire/{prestataireId}
    @GetMapping("/prestataire/{prestataireId}")
    public ResponseEntity<List<VisiteResponse>> getByPrestataire(@PathVariable String prestataireId) {
        return ResponseEntity.ok(visiteService.getVisitesByPrestataire(prestataireId));
    }

    // GET /visites/adherent/{codeAdherent}
    @GetMapping("/adherent/{codeAdherent}")
    public ResponseEntity<List<VisiteResponse>> getByAdherent(@PathVariable String codeAdherent) {
        return ResponseEntity.ok(visiteService.getVisitesByAdherent(codeAdherent));
    }

    // GET /visites/{id}/consultation
    @GetMapping("/{id}/consultation")
    public ResponseEntity<ConsultationResponse> getConsultation(@PathVariable String id) {
        return ResponseEntity.ok(visiteService.getConsultation(id));
    }

    // GET /visites/{id}/prestations
    @GetMapping("/{id}/prestations")
    public ResponseEntity<List<PrestationResponse>> getPrestations(@PathVariable String id) {
        return ResponseEntity.ok(visiteService.getPrestationsByVisite(id));
    }

    // GET /visites/consultations/en-attente
    @GetMapping("/consultations/en-attente")
    public ResponseEntity<List<ConsultationResponse>> getConsultationsEnAttente() {
        return ResponseEntity.ok(visiteService.getConsultationsEnAttente());
    }

    // GET /visites/prestations/en-attente
    @GetMapping("/prestations/en-attente")
    public ResponseEntity<List<PrestationResponse>> getPrestationsEnAttente() {
        return ResponseEntity.ok(visiteService.getPrestationsEnAttente());
    }
}