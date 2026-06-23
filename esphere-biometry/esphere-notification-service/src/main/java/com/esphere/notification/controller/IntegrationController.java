package com.esphere.notification.controller;

import com.esphere.notification.dto.request.ConsoAlertRequest;
import com.esphere.notification.dto.request.FraudeCheckRequest;
import com.esphere.notification.dto.request.PrestationNotifRequest;
import com.esphere.notification.dto.request.SoumissionNotifRequest;
import com.esphere.notification.service.PrestationNotifService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notifications/integration")
@RequiredArgsConstructor
public class IntegrationController {

    private final PrestationNotifService prestationNotifService;

    @PostMapping("/prestation-traitee")
    public ResponseEntity<Map<String, String>> prestationTraitee(@RequestBody PrestationNotifRequest req) {
        prestationNotifService.notifierTraitementPrestation(req);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/prestation-soumise")
    public ResponseEntity<Map<String, String>> prestationSoumise(@RequestBody SoumissionNotifRequest req) {
        prestationNotifService.notifierNouvelleSoumission(req);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/check-fraude")
    public ResponseEntity<Map<String, String>> checkFraude(@RequestBody FraudeCheckRequest req) {
        prestationNotifService.verifierFraude(req);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }

    @PostMapping("/check-conso")
    public ResponseEntity<Map<String, String>> checkConso(@RequestBody ConsoAlertRequest req) {
        prestationNotifService.verifierConsoAbusive(req);
        return ResponseEntity.ok(Map.of("status", "ok"));
    }
}
