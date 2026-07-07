package com.esphere.webservicecron.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Mappe un élément du tableau "tabAdherent" retourné par le serveur
 * biométrie externe legacy. Les valeurs arrivent en chaîne de caractères
 * (comme en PHP) ; le service applique le même nettoyage (trim, parsing).
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdherentExterneDTO {

    @JsonProperty("POLICE")
    private String police;

    @JsonProperty("SOUSCRIPTEUR")
    private String souscripteur;

    @JsonProperty("CODE_ASSURE")
    private String codeAssure;

    @JsonProperty("ASSURE_PRINCIPAL")
    private String assurePrincipal;

    @JsonProperty("SEXE")
    private String sexe;

    @JsonProperty("NAISSANCE")
    private String naissance;

    @JsonProperty("MATRICULE")
    private String matricule;

    @JsonProperty("TELEPHONE")
    private String telephone;

    @JsonProperty("TAUX")
    private String taux;

    @JsonProperty("EFFET_POLICE")
    private String effetPolice;

    @JsonProperty("ECHEANCE_POLICE")
    private String echeancePolice;

    @JsonProperty("GROUPE")
    private String groupe;

    @JsonProperty("VILLE")
    private String ville;

    @JsonProperty("PLAFOND_ASSUREP")
    private String plafondAssurep;

    @JsonProperty("CONS_AP")
    private String consAp;
}
