package com.esphere.prestataire.controller;

import com.esphere.prestataire.dto.response.*;
import com.esphere.prestataire.service.PrestataireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestataires")
@RequiredArgsConstructor
public class PrestataireController {

    private final PrestataireService prestataireService;

    // GET /prestataires/{id}
    @GetMapping("/{id}")
    public ResponseEntity<PrestataireResponse> getPrestataire(@PathVariable String id) {
        return ResponseEntity.ok(prestataireService.getPrestataire(id));
    }

    // GET /prestataires/categorie/{categorieId}
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<PrestataireResponse>> getByCategorie(@PathVariable String categorieId) {
        return ResponseEntity.ok(prestataireService.getByCategorie(categorieId));
    }

    // GET /prestataires/search?nom=clinique
    @GetMapping("/search")
    public ResponseEntity<List<PrestataireResponse>> search(@RequestParam String nom) {
        return ResponseEntity.ok(prestataireService.search(nom));
    }

    // GET /prestataires/types-prestation
    @GetMapping("/types-prestation")
    public ResponseEntity<List<TypePrestationResponse>> getTypes() {
        return ResponseEntity.ok(prestataireService.getTypesPrestation());
    }

    // GET /prestataires/types-prestation/categorie/{categorie}
    @GetMapping("/types-prestation/categorie/{categorie}")
    public ResponseEntity<List<TypePrestationResponse>> getTypesByCategorie(@PathVariable String categorie) {
        return ResponseEntity.ok(prestataireService.getTypesByCategorie(categorie));
    }

    // GET /prestataires/taux/{police}
    @GetMapping("/taux/{police}")
    public ResponseEntity<List<TauxPrestationResponse>> getTauxByPolice(@PathVariable String police) {
        return ResponseEntity.ok(prestataireService.getTauxByPolice(police));
    }

    // GET /prestataires/taux?typePrestationId=&police=&groupe=
    @GetMapping("/taux")
    public ResponseEntity<TauxPrestationResponse> getTaux(
            @RequestParam String typePrestationId,
            @RequestParam String police,
            @RequestParam Short groupe) {
        return ResponseEntity.ok(prestataireService.getTaux(typePrestationId, police, groupe));
    }
}