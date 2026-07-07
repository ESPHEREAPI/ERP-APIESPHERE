package com.esphere.webservicecron.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

/**
 * Réponse racine de l'endpoint externe /get-liste-taux-prestation :
 * { "error": "", "tabTauxPrestation": [...] }
 *
 * Contrairement aux adhérents/ayants droit, le PHP legacy lit ces lignes
 * directement par leurs clés brutes (CODEPRES, CODEINTE, NUMEPOLI,
 * NUMEGROU, TYPCONSU, TAUXCOUV, VALEPLAF) sans conversion camelCase :
 * on conserve donc une Map brute par ligne.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TauxPrestationExterneResponse {

    @JsonProperty("error")
    private String error;

    @JsonProperty("tabTauxPrestation")
    private List<Map<String, Object>> tabTauxPrestation;
}
