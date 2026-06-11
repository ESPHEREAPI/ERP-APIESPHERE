package db.biometry.biometry.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import db.biometry.biometry.dtos.externe.AdherentExterneDTO;
import db.biometry.biometry.dtos.externe.AdherentExterneResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service de récupération du plafond depuis l'API externe Esphere.
 *
 * Logique :
 *   1. Appel unique à l'endpoint /get-liste-adherent → liste de tous les adhérents
 *   2. Indexation en Map<codeAdherent, DTO>  (codeAdherent = CODE_ASSURE + "_" + POLICE)
 *   3. Cache en mémoire avec TTL de 30 minutes pour éviter les appels répétés
 *
 * Utilisation dans DashboardAdherentService :
 *   Si plafondAssurep == null || 0 → appel ici → sauvegarde en base
 */
@Slf4j
@Service
public class PlafondExterneService {

    private static final String EXTERNAL_URL =
            "http://35.204.126.17/web_service/public/biometry/get-liste-adherent";

    /** TTL du cache en mémoire : 30 minutes */
    private static final long CACHE_TTL_MS = 30L * 60 * 1000;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    // Cache simple en mémoire (volatile pour thread-safety lecture)
    private volatile Map<String, AdherentExterneDTO> cache = Collections.emptyMap();
    private volatile long cacheLoadedAt = 0L;

    public PlafondExterneService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = buildRestTemplate();
    }

    // ── API publique ─────────────────────────────────────────────────────────

    /**
     * Retourne le plafond effectif d'un adhérent depuis l'API externe.
     * Règle : prendre le premier champ renseigné (> 0) parmi :
     *   PLAFOND_ASSUREP → PLAFOND_FAMILLE → PLAFOND_PERSONNE → PLAFOND_MEMBRE
     *
     * @param codeAdherent  format "CODEASSURE_POLICE"  ex: "4346_1017-2130000100"
     * @return Double plafond, ou null si introuvable / tous à 0
     */
    public Double getPlafond(String codeAdherent) {
        if (codeAdherent == null || codeAdherent.isBlank()) return null;
        ensureCacheLoaded();
        AdherentExterneDTO dto = cache.get(codeAdherent);
        if (dto == null) {
            log.debug("[PlafondExterne] Adhérent non trouvé dans le cache : {}", codeAdherent);
            return null;
        }
        Double plafond = resoudrePlafond(dto);
        log.info("[PlafondExterne] Plafond pour {} : {}", codeAdherent, plafond);
        return plafond;
    }

    /**
     * Somme les plafonds effectifs de tous les adhérents d'une police.
     * Plus fiable que la recherche par souscripteur car la police est l'identifiant unique du contrat.
     * Règle : pour chaque adhérent, prendre le premier champ renseigné parmi les 4 champs plafond.
     */
    public BigDecimal sumPlafondsByPolice(String police) {
        if (police == null || police.isBlank()) return BigDecimal.ZERO;
        ensureCacheLoaded();

        List<AdherentExterneDTO> matched = cache.values().stream()
                .filter(a -> police.equalsIgnoreCase(a.getPolice() != null ? a.getPolice().trim() : ""))
                .collect(Collectors.toList());

        log.info("[PlafondExterne] police='{}' → {} entrées dans cache ({} total)",
                police, matched.size(), cache.size());

        if (!matched.isEmpty()) {
            AdherentExterneDTO sample = matched.get(0);
            log.info("[PlafondExterne] sample police={} SOUSCRIPTEUR='{}' ASSUREP='{}' FAMILLE='{}' PERSONNE='{}' MEMBRE='{}'",
                    sample.getPolice(), sample.getSouscripteur(),
                    sample.getPlafondAssurep(), sample.getPlafondFamille(),
                    sample.getPlafondPersonne(), sample.getPlafondMembre());
        }

        double total = matched.stream()
                .mapToDouble(a -> {
                    Double v = resoudrePlafond(a);
                    return v != null ? v : 0.0;
                })
                .sum();

        log.info("[PlafondExterne] sumPlafondsByPolice({}) = {}", police, total);
        return BigDecimal.valueOf(total);
    }

    /**
     * Somme les plafonds effectifs de tous les adhérents d'un souscripteur.
     * Règle : pour chaque adhérent, prendre le premier champ renseigné parmi les 4 champs plafond.
     */
    public BigDecimal sumPlafondsBySouscripteur(String souscripteur) {
        if (souscripteur == null || souscripteur.isBlank()) return BigDecimal.ZERO;
        ensureCacheLoaded();

        List<AdherentExterneDTO> matched = cache.values().stream()
                .filter(a -> souscripteur.equalsIgnoreCase(a.getSouscripteur()))
                .collect(Collectors.toList());

        log.info("[PlafondExterne] souscripteur='{}' → {} entrées dans cache ({} total)",
                souscripteur, matched.size(), cache.size());

        if (!matched.isEmpty()) {
            AdherentExterneDTO sample = matched.get(0);
            log.info("[PlafondExterne] sample: ASSUREP='{}' FAMILLE='{}' PERSONNE='{}' MEMBRE='{}'",
                    sample.getPlafondAssurep(), sample.getPlafondFamille(),
                    sample.getPlafondPersonne(), sample.getPlafondMembre());
        }

        double total = matched.stream()
                .mapToDouble(a -> {
                    Double v = resoudrePlafond(a);
                    return v != null ? v : 0.0;
                })
                .sum();

        log.info("[PlafondExterne] sumPlafondsBySouscripteur({}) = {}", souscripteur, total);
        return BigDecimal.valueOf(total);
    }

    /** Force le rechargement du cache externe (utile pour les tests ou un endpoint admin) */
    public synchronized void invaliderCache() {
        cache = Collections.emptyMap();
        cacheLoadedAt = 0L;
        log.info("[PlafondExterne] Cache invalidé manuellement.");
    }

    // ── Cache + chargement ───────────────────────────────────────────────────

    private synchronized void ensureCacheLoaded() {
        long now = System.currentTimeMillis();
        if (cache.isEmpty() || (now - cacheLoadedAt) > CACHE_TTL_MS) {
            log.info("[PlafondExterne] Rechargement du cache depuis l'API externe...");
            cache = chargerDepuisAPI();
            cacheLoadedAt = System.currentTimeMillis();
            log.info("[PlafondExterne] {} adhérents chargés dans le cache.", cache.size());
        }
    }

    private Map<String, AdherentExterneDTO> chargerDepuisAPI() {
        try {
            // L'API peut retourner text/html même si le contenu est JSON → lire en String
            String rawBody = restTemplate.getForObject(EXTERNAL_URL, String.class);

            if (rawBody == null || rawBody.isBlank()) {
                log.warn("[PlafondExterne] Réponse vide de l'API externe.");
                return Collections.emptyMap();
            }

            AdherentExterneResponse response =
                    objectMapper.readValue(rawBody, AdherentExterneResponse.class);

            if (response.getTabAdherent() == null) {
                log.warn("[PlafondExterne] tabAdherent null dans la réponse externe.");
                return Collections.emptyMap();
            }

            return response.getTabAdherent().stream()
                    .filter(a -> a.getCodeAssure() != null && a.getPolice() != null)
                    .collect(Collectors.toMap(
                            a -> a.getCodeAssure().trim() + "_" + a.getPolice().trim(),
                            a -> a,
                            (a1, a2) -> a1  // en cas de doublon, conserver le premier
                    ));

        } catch (Exception e) {
            log.error("[PlafondExterne] Erreur lors du chargement de l'API externe : {}", e.getMessage());
            return Collections.emptyMap();
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Retourne le premier champ plafond renseigné (> 0) parmi les 4 champs du contrat.
     * Ordre de priorité : PLAFOND_ASSUREP → PLAFOND_FAMILLE → PLAFOND_PERSONNE → PLAFOND_MEMBRE
     */
    private Double resoudrePlafond(AdherentExterneDTO dto) {
        Double v;
        v = parseDouble(dto.getPlafondAssurep());
        if (v != null && v > 0) return v;
        v = parseDouble(dto.getPlafondFamille());
        if (v != null && v > 0) return v;
        v = parseDouble(dto.getPlafondPersonne());
        if (v != null && v > 0) return v;
        v = parseDouble(dto.getPlafondMembre());
        if (v != null && v > 0) return v;
        return null;
    }

    private Double parseDouble(String s) {
        if (s == null || s.isBlank()) return null;
        try {
            return Double.parseDouble(s.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            log.warn("[PlafondExterne] Impossible de parser le plafond '{}' en Double.", s);
            return null;
        }
    }

    private RestTemplate buildRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);   // 10 secondes connexion
        factory.setReadTimeout(60_000);      // 60 secondes lecture (liste complète)
        return new RestTemplate(factory);
    }
}
