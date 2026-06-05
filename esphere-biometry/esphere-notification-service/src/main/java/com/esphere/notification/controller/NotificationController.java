package com.esphere.notification.controller;

import com.esphere.notification.dto.request.NotificationRequest;
import com.esphere.notification.dto.request.SmsManuelRequest;
import com.esphere.notification.dto.response.NotificationResponse;
import com.esphere.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // ── ENVOI ─────────────────────────────────────────────────────

    // POST /notifications/envoyer → envoi général
    @PostMapping("/envoyer")
    public ResponseEntity<NotificationResponse> envoyer(
            @Valid @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(notificationService.envoyer(request));
    }

    // POST /notifications/sms-manuel → SMS manuel par agent SS
    @PostMapping("/sms-manuel")
    public ResponseEntity<NotificationResponse> envoyerSmsManuel(
            @Valid @RequestBody SmsManuelRequest request) {
        return ResponseEntity.ok(notificationService.envoyerSmsManuel(request));
    }

    // ── ALERTES IN-APP ────────────────────────────────────────────

    // GET /notifications/alertes/{destinataireId}
    @GetMapping("/alertes/{destinataireId}")
    public ResponseEntity<List<NotificationResponse>> getAlertes(
            @PathVariable String destinataireId) {
        return ResponseEntity.ok(notificationService.getAlertes(destinataireId));
    }

    // GET /notifications/alertes/{destinataireId}/non-lues
    @GetMapping("/alertes/{destinataireId}/non-lues")
    public ResponseEntity<List<NotificationResponse>> getNonLues(
            @PathVariable String destinataireId) {
        return ResponseEntity.ok(notificationService.getAlerteNonLues(destinataireId));
    }

    // GET /notifications/alertes/{destinataireId}/compteur
    // → Pour le badge clignotant sur le menu
    @GetMapping("/alertes/{destinataireId}/compteur")
    public ResponseEntity<Map<String, Long>> getCompteur(
            @PathVariable String destinataireId) {
        Long count = notificationService.compterNonLues(destinataireId);
        return ResponseEntity.ok(Map.of("nonLues", count));
    }

    // PUT /notifications/{id}/lire
    @PutMapping("/{id}/lire")
    public ResponseEntity<Void> marquerCommeLu(@PathVariable Integer id) {
        notificationService.marquerCommeLu(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /notifications/alertes/{destinataireId}/lire-tout
    @PutMapping("/alertes/{destinataireId}/lire-tout")
    public ResponseEntity<Void> marquerToutesCommeLues(
            @PathVariable String destinataireId) {
        notificationService.marquerToutesCommeLues(destinataireId);
        return ResponseEntity.noContent().build();
    }

    // GET /notifications/historique/{destinataireId}
    @GetMapping("/historique/{destinataireId}")
    public ResponseEntity<List<NotificationResponse>> getHistorique(
            @PathVariable String destinataireId) {
        return ResponseEntity.ok(notificationService.getHistorique(destinataireId));
    }
}