package com.esphere.webservicecron.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Réponse racine de l'endpoint externe /get-liste-adherent :
 * { "error": "", "tabAdherent": [...] }
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
