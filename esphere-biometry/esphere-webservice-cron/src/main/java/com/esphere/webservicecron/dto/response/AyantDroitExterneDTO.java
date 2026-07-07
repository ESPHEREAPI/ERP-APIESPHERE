package com.esphere.webservicecron.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Mappe un élément du tableau "tabAyantDroit" retourné par le serveur
 * biométrie externe legacy.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AyantDroitExterneDTO {

    @JsonProperty("POLICE")
    private String police;

    @JsonProperty("CODE_ASSURE")
    private String codeAssure;

    @JsonProperty("CODE_AYANT_D")
    private String codeAyantD;

    @JsonProperty("AYANTS_DROITS")
    private String ayantsDroits;

    @JsonProperty("SEXE")
    private String sexe;

    @JsonProperty("NAISSANCE")
    private String naissance;

    @JsonProperty("LIENPARE")
    private String lienpare;

    @JsonProperty("TELEPHONE")
    private String telephone;
}
