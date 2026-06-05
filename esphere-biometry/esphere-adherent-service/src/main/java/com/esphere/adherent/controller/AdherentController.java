package com.esphere.adherent.controller;

import com.esphere.adherent.dto.response.AdherentResponse;
import com.esphere.adherent.dto.response.AyantDroitResponse;
import com.esphere.adherent.service.AdherentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adherents")
@RequiredArgsConstructor
public class AdherentController {

    private final AdherentService adherentService;

    // GET /adherents/{code}
    @GetMapping("/{code}")
    public ResponseEntity<AdherentResponse> getAdherent(@PathVariable String code) {
        return ResponseEntity.ok(adherentService.getAdherent(code));
    }

    // GET /adherents/{code}/ayants-droit
    @GetMapping("/{code}/ayants-droit")
    public ResponseEntity<List<AyantDroitResponse>> getAyantsDroit(@PathVariable String code) {
        return ResponseEntity.ok(adherentService.getAyantsDroit(code));
    }

    // GET /adherents/search?nom=dupont
    @GetMapping("/search")
    public ResponseEntity<List<AdherentResponse>> search(@RequestParam String nom) {
        return ResponseEntity.ok(adherentService.searchByNom(nom));
    }

    // GET /adherents/police/{police}
    @GetMapping("/police/{police}")
    public ResponseEntity<List<AdherentResponse>> getByPolice(@PathVariable String police) {
        return ResponseEntity.ok(adherentService.getByPolice(police));
    }

    // GET /ayants-droit/{code}
    @GetMapping("/ayants-droit/{code}")
    public ResponseEntity<AyantDroitResponse> getAyantDroit(@PathVariable String code) {
        return ResponseEntity.ok(adherentService.getAyantDroit(code));
    }
}