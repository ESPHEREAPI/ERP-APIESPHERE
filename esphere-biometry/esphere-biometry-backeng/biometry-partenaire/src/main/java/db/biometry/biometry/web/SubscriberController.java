/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.web;

/**
 *
 * @author USER01
 */
import db.biometry.biometry.dtos.ActivationResponse;
import db.biometry.biometry.dtos.CreateSubscriberRequest;
import db.biometry.biometry.dtos.PagedResponse;
import db.biometry.biometry.dtos.PolicyCheckResponse;
import db.biometry.biometry.dtos.SendActivationRequest;
import db.biometry.biometry.dtos.SetPasswordRequest;
import db.biometry.biometry.dtos.SubscriberResponse;
import db.biometry.biometry.dtos.UpdateSubscriberRequest;
import db.biometry.biometry.services.SubscriberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller REST — Souscripteurs
 *
 * Endpoints mappés sur le SubscriberService Angular (frontend) :
 * ──────────────────────────────────────────────────────────────
 * GET    /subscribers                   → getAll()
 * GET    /subscribers/{id}              → getById()
 * POST   /subscribers                   → create()
 * PUT    /subscribers/{id}              → update()
 * DELETE /subscribers/{id}             → delete()
 * PATCH  /subscribers/{id}/status      → toggleStatus()
 * GET    /policies/check/{number}      → checkPolicy()
 * POST   /subscribers/{id}/send-activation → sendActivationLink()
 * POST   /subscribers/activate         → setPasswordViaToken()
 * POST   /subscribers/{id}/reset-password → resetPassword()
 * GET    /subscribers/username-check   → checkUsernameAvailability()
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/subscriber")
public class SubscriberController {

    private final SubscriberService subscriberService;

    // ── GET /subscribers?search=&isActive=&page=&limit= ───────────────────────
    @GetMapping("/all")
    public ResponseEntity<PagedResponse<SubscriberResponse>> getAll(
            @RequestParam(required = false)              String  search,
            @RequestParam(required = false)              Boolean isActive,
            @RequestParam(defaultValue = "1")            int     page,
            @RequestParam(defaultValue = "10")           int     limit) {

        return ResponseEntity.ok(
                subscriberService.findAll(search, isActive, page, limit));
    }

    // ── GET /subscribers/{id} ─────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<SubscriberResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subscriberService.findById(id));
    }

    // ── POST /subscribers ─────────────────────────────────────────────────────
    @PostMapping("/create")
    public ResponseEntity<SubscriberResponse> create(
            @Valid @RequestBody CreateSubscriberRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subscriberService.create(request));
    }

    // ── PUT /subscribers/{id} ─────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<SubscriberResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSubscriberRequest request) {
        return ResponseEntity.ok(subscriberService.update(id, request));
    }

    // ── DELETE /subscribers/{id} ──────────────────────────────────────────────
    @DeleteMapping("/subscribers/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subscriberService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── PATCH /subscribers/{id}/status ───────────────────────────────────────
    @PatchMapping("/{id}/status")
    public ResponseEntity<SubscriberResponse> toggleStatus(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        Boolean isActive = body.get("isActive");
        if (isActive == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(subscriberService.toggleStatus(id, isActive));
    }

    // ── GET /policies/check/{policyNumber} ───────────────────────────────────
    @GetMapping("/policies/check/{policyNumber}")
    public ResponseEntity<PolicyCheckResponse> checkPolicy(
            @PathVariable String policyNumber) {
        return ResponseEntity.ok(subscriberService.checkPolicy(policyNumber));
    }

    // ── POST /subscribers/{id}/send-activation ───────────────────────────────
    @PostMapping("/{id}/send-activation")
    public ResponseEntity<ActivationResponse> sendActivationLink(
            @PathVariable Long id,
            @Valid @RequestBody SendActivationRequest request) {
        return ResponseEntity.ok(subscriberService.sendActivationLink(id, request));
    }

    // ── POST /subscribers/activate  (page publique, pas de JWT requis) ────────
    @PostMapping("/activate")
    public ResponseEntity<SubscriberResponse> activate(
            @Valid @RequestBody SetPasswordRequest request) {
        return ResponseEntity.ok(subscriberService.setPasswordViaToken(request));
    }

    // ── POST /subscribers/{id}/reset-password ────────────────────────────────
    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.isBlank())
            return ResponseEntity.badRequest().build();
        subscriberService.resetPassword(id, newPassword);
        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
    }
    
    @PostMapping("/email/{email}/reset-password")
    public ResponseEntity<Map<String, String>> resetPasswordByEmail(
            @PathVariable String email,
            @RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");
        if (newPassword == null || newPassword.isBlank())
            return ResponseEntity.badRequest().build();
        subscriberService.resetPassword(email, newPassword);
        return ResponseEntity.ok(Map.of("message", "Mot de passe réinitialisé avec succès"));
    }

    // ── GET /subscribers/username-check?username=&excludeId= ─────────────────
    @GetMapping("/username-check")
    public ResponseEntity<Map<String, Boolean>> checkUsername(
            @RequestParam String username,
            @RequestParam(required = false) Long excludeId) {
        boolean available = subscriberService.isUsernameAvailable(username, excludeId);
        return ResponseEntity.ok(Map.of("available", available));
    }
    
}
