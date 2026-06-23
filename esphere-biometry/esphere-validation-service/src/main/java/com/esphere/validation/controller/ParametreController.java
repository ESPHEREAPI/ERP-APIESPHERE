package com.esphere.validation.controller;

import com.esphere.validation.entity.Parametre;
import com.esphere.validation.service.ParametreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parametres")
@RequiredArgsConstructor
public class ParametreController {

    private final ParametreService parametreService;

    /** GET /parametres — liste tous les paramètres (admin) */
    @GetMapping
    public ResponseEntity<List<Parametre>> getAll() {
        return ResponseEntity.ok(parametreService.getAll());
    }

    /** GET /parametres/{cle} — valeur d'un paramètre */
    @GetMapping("/{cle}")
    public ResponseEntity<Map<String, String>> get(@PathVariable String cle) {
        String valeur = parametreService.getValeur(cle);
        if (valeur == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("cle", cle, "valeur", valeur));
    }

    /** PUT /parametres/{cle} — modifier un paramètre (admin SS) */
    @PutMapping("/{cle}")
    public ResponseEntity<Parametre> set(
            @PathVariable String cle,
            @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(parametreService.set(cle, body.get("valeur")));
    }
}
