package com.esphere.bonmanuel.controller;

import com.esphere.bonmanuel.dto.request.*;
import com.esphere.bonmanuel.dto.response.BonManuelResponse;
import com.esphere.bonmanuel.service.BonManuelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bons-manuels")
@RequiredArgsConstructor
public class BonManuelController {

    private final BonManuelService bonManuelService;

    // POST /bons-manuels → créer un bon manuel
    @PostMapping
    public ResponseEntity<BonManuelResponse> creer(
            @Valid @RequestBody BonManuelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bonManuelService.creer(request));
    }

    // GET /bons-manuels/{id}
    @GetMapping("/{id}")
    public ResponseEntity<BonManuelResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(bonManuelService.getById(id));
    }

    // GET /bons-manuels/reference/{reference}
    @GetMapping("/reference/{reference}")
    public ResponseEntity<BonManuelResponse> getByReference(
            @PathVariable String reference) {
        return ResponseEntity.ok(bonManuelService.getByReference(reference));
    }

    // GET /bons-manuels/en-attente
    @GetMapping("/en-attente")
    public ResponseEntity<List<BonManuelResponse>> getEnAttente() {
        return ResponseEntity.ok(bonManuelService.getEnAttente());
    }

    // GET /bons-manuels/prestataire/{id}
    @GetMapping("/prestataire/{prestataireId}")
    public ResponseEntity<List<BonManuelResponse>> getByPrestataire(
            @PathVariable String prestataireId) {
        return ResponseEntity.ok(bonManuelService.getByPrestataire(prestataireId));
    }

    // GET /bons-manuels/prestataire/{id}/confirmes
    @GetMapping("/prestataire/{prestataireId}/confirmes")
    public ResponseEntity<List<BonManuelResponse>> getConfirmesParPrestataire(
            @PathVariable String prestataireId) {
        return ResponseEntity.ok(
                bonManuelService.getConfirmesParPrestataire(prestataireId));
    }

    // GET /bons-manuels/adherent/{codeAdherent}
    @GetMapping("/adherent/{codeAdherent}")
    public ResponseEntity<List<BonManuelResponse>> getByAdherent(
            @PathVariable String codeAdherent) {
        return ResponseEntity.ok(bonManuelService.getByAdherent(codeAdherent));
    }

    // PUT /bons-manuels/{id}/confirmer/global
    @PutMapping("/{id}/confirmer/global")
    public ResponseEntity<BonManuelResponse> confirmerGlobal(
            @PathVariable Integer id,
            @Valid @RequestBody ConfirmationGlobaleRequest request) {
        return ResponseEntity.ok(bonManuelService.confirmerGlobal(id, request));
    }

    // PUT /bons-manuels/{id}/confirmer/detail
    @PutMapping("/{id}/confirmer/detail")
    public ResponseEntity<BonManuelResponse> confirmerDetail(
            @PathVariable Integer id,
            @Valid @RequestBody ConfirmationDetailRequest request) {
        return ResponseEntity.ok(bonManuelService.confirmerDetail(id, request));
    }

    // PUT /bons-manuels/{id}/rejeter
    @PutMapping("/{id}/rejeter")
    public ResponseEntity<BonManuelResponse> rejeter(
            @PathVariable Integer id,
            @RequestBody RejeterRequest request) {
        return ResponseEntity.ok(bonManuelService.rejeter(
                id, request.getObservations(), request.getEmployeId()));
    }

    // PUT /bons-manuels/{id}/encaisser
    @PutMapping("/{id}/encaisser")
    public ResponseEntity<BonManuelResponse> encaisser(
            @PathVariable Integer id,
            @RequestBody EncaisserRequest request) {
        return ResponseEntity.ok(
                bonManuelService.encaisser(id, request.getEmployeEncaisseId()));
    }
}