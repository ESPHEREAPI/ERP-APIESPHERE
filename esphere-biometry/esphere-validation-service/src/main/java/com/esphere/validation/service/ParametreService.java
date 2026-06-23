package com.esphere.validation.service;

import com.esphere.validation.entity.Parametre;
import com.esphere.validation.repository.ParametreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParametreService {

    private final ParametreRepository parametreRepository;

    public String getValeur(String cle) {
        return parametreRepository.findByCle(cle)
                .map(Parametre::getValeur)
                .orElse(null);
    }

    public boolean getBooleanValeur(String cle, boolean defaut) {
        String v = getValeur(cle);
        if (v == null) return defaut;
        return "true".equalsIgnoreCase(v.trim());
    }

    public List<Parametre> getAll() {
        return parametreRepository.findAll();
    }

    public Parametre set(String cle, String valeur) {
        Parametre p = parametreRepository.findByCle(cle)
                .orElseGet(() -> Parametre.builder().cle(cle).build());
        p.setValeur(valeur);
        p.setDateModification(LocalDateTime.now());
        log.info("Paramètre mis à jour : {} = {}", cle, valeur);
        return parametreRepository.save(p);
    }
}
