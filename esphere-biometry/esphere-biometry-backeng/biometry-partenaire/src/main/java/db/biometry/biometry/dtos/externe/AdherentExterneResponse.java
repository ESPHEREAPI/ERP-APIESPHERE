package db.biometry.biometry.dtos.externe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * Wrapper pour la réponse racine :
 * {
 *   "error": "",
 *   "tabAdherent": [ {...}, ... ]
 * }
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdherentExterneResponse {

    @JsonProperty("error")
    private String error;

    @JsonProperty("tabAdherent")
    private List<AdherentExterneDTO> tabAdherent;
}
