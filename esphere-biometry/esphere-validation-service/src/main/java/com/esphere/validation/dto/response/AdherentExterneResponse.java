package com.esphere.validation.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Wrapper pour la réponse racine de l'endpoint :
 * {
 *   "error": "",
 *   "tabAdherent": [...]
 * }
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdherentExterneResponse {

    @JsonProperty("error")
    private String error;

    @JsonProperty("tabAdherent")
    private List<AdherentExterneDTO> tabAdherent;
}