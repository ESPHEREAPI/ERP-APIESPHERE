package com.esphere.validation.service;


import com.esphere.validation.dto.response.AdherentExterneDTO;
import com.esphere.validation.dto.response.AdherentExterneResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdherentExterneService {

    private final WebClient adherentWebClient;
    @Autowired
private ObjectMapper objectMapper;

    // ----------------------------------------------------------------
    // 1. Chargement + mise en cache de TOUTE la liste sous forme de Map
    //    Clé : "CODEASSURE_POLICE"  →  Valeur : AdherentExterneDTO
    // ----------------------------------------------------------------
    @Cacheable(value = "adherents", key = "'all'")
    
    public Map<String, AdherentExterneDTO> chargerTousLesAdherents() {
    log.info("Chargement de la liste complète des adhérents...");
    try {
        // Lire le body brut en String (accepte n'importe quel Content-Type)
        String rawBody = adherentWebClient.get()
                .uri("/get-liste-adherent")
                .accept(MediaType.TEXT_HTML, MediaType.APPLICATION_JSON, MediaType.ALL)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        if (rawBody == null || rawBody.isBlank()) {
            log.warn("Réponse vide de l'endpoint externe");
            return Collections.emptyMap();
        }

        // Désérialiser manuellement
        AdherentExterneResponse response = objectMapper.readValue(rawBody, AdherentExterneResponse.class);

        if (response.getTabAdherent() == null) {
            log.warn("tabAdherent null dans la réponse");
            return Collections.emptyMap();
        }

        Map<String, AdherentExterneDTO> index = response.getTabAdherent().stream()
                .filter(a -> a.getCodeAssure() != null && a.getPolice() != null)
                .collect(Collectors.toMap(
                        a -> a.getCodeAssure() + "_" + a.getPolice().trim(),
                        a -> a,
                        (a1, a2) -> a1
                ));

        log.info("{} adhérents chargés et indexés.", index.size());
        return index;

    } catch (JsonProcessingException e) {
        log.error("Réponse non-JSON reçue du serveur. Body reçu probablement en HTML.", e);
        return Collections.emptyMap();
    } catch (Exception e) {
        log.error("Erreur lors du chargement des adhérents : {}", e.getMessage());
        return Collections.emptyMap();
    }
    }
//    public Map<String, AdherentExterneDTO> chargerTousLesAdherents() {
//        log.info("Chargement de la liste complète des adhérents depuis l'endpoint externe...");
//        try {
//            AdherentExterneResponse response = adherentWebClient.get()
//                    .uri("/get-liste-adherent")
//                    .retrieve()
//                    .bodyToMono(AdherentExterneResponse.class)
//                    .block();
//
//            if (response == null || response.getTabAdherent() == null) {
//                log.warn("Réponse vide ou nulle de l'endpoint externe");
//                return Collections.emptyMap();
//            }
//
//            // Indexation en Map pour O(1) à la recherche
//            Map<String, AdherentExterneDTO> index = response.getTabAdherent().stream()
//                    .filter(a -> a.getCodeAssure() != null && a.getPolice() != null)
//                    .collect(Collectors.toMap(
//                            a -> a.getCodeAssure() + "_" + a.getPolice().trim(),
//                            a -> a,
//                            (a1, a2) -> a1   // en cas de doublon, garder le premier
//                    ));
//
//            log.info("{} adhérents chargés et indexés.", index.size());
//            return index;
//
//        } catch (Exception e) {
//            log.error("Erreur lors du chargement des adhérents externes : {}", e.getMessage());
//            return Collections.emptyMap();
//        }
//    }
//@Cacheable(value = "adherents", key = "'all'")
//public Map<String, AdherentExterneDTO> chargerTousLesAdherents() {
//
//    log.info("Chargement de la liste complète des adhérents depuis l'endpoint externe...");
//
//    try {
//
//        String json = adherentWebClient.get()
//                .uri("/get-liste-adherent")
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        log.info("Réponse brute API : {}", json);
//
//        ObjectMapper mapper = new ObjectMapper();
//
//        AdherentExterneResponse response =
//                mapper.readValue(json, AdherentExterneResponse.class);
//
//        if (response == null || response.getTabAdherent() == null) {
//            log.warn("Réponse vide ou nulle de l'endpoint externe");
//            return Collections.emptyMap();
//        }
//
//        Map<String, AdherentExterneDTO> index =
//                response.getTabAdherent().stream()
//                        .filter(a ->
//                                a.getCodeAssure() != null &&
//                                a.getPolice() != null
//                        )
//                        .collect(Collectors.toMap(
//                                a -> a.getCodeAssure() + "_" + a.getPolice().trim(),
//                                a -> a,
//                                (a1, a2) -> a1
//                        ));
//
//        log.info("{} adhérents chargés et indexés.", index.size());
//
//        return index;
//
//    } catch (Exception e) {
//
//        log.error("Erreur lors du chargement : ", e);
//
//        return Collections.emptyMap();
//    }
//}
    // ----------------------------------------------------------------
    // 2. Recherche en O(1) grâce à la Map
    // ----------------------------------------------------------------
    public Optional<AdherentExterneDTO> findByCodeAdherent(String codeAdherent) {
        if (codeAdherent == null || !codeAdherent.contains("_")) {
            log.warn("Format code adhérent invalide : {}", codeAdherent);
            return Optional.empty();
        }
        return Optional.ofNullable(chargerTousLesAdherents().get(codeAdherent));
    }

    // ----------------------------------------------------------------
    // 3. Invalidation automatique du cache toutes les 30 min
    //    (synchronisé avec l'expiration Caffeine)
    // ----------------------------------------------------------------
    @Scheduled(fixedDelay = 30 * 60 * 1000)
    @CacheEvict(value = "adherents", allEntries = true)
    public void rafraichirCache() {
        log.info("Cache adhérents invalidé — prochain appel rechargera la liste.");
    }
}