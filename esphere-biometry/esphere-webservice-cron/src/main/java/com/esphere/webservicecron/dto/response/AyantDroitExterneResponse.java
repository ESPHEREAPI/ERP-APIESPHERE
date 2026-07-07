package com.esphere.webservicecron.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Réponse racine de l'endpoint externe /get-liste-ayant-droit :
 * { "error": "", "tabAyantDroit": [...] }
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AyantDroitExterneResponse {

    @JsonProperty("error")
    private String error;

    @JsonProperty("tabAyantDroit")
    private List<AyantDroitExterneDTO> tabAyantDroit;
}
