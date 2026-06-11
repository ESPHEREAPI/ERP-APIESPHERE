package db.biometry.biometry.dtos.externe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * DTO mappant un élément du tableau "tabAdherent"
 * retourné par l'endpoint externe :
 *   http://35.204.126.17/web_service/public/biometry/get-liste-adherent
 *
 * Les champs numériques "0","1","2"... sont ignorés (clés redondantes dans la réponse).
 * La clé de lookup est : CODE_ASSURE + "_" + POLICE  (= codeAdherent en base locale)
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

    @JsonProperty("MATRICULE")
    private String matricule;

    @JsonProperty("TAUX")
    private String taux;

    @JsonProperty("GROUPE")
    private String groupe;

    // ── Plafonds (retournés en String dans le JSON externe) ───────────────────

    /** Plafond de l'assuré principal — c'est la valeur qu'on utilise. */
    @JsonProperty("PLAFOND_ASSUREP")
    private String plafondAssurep;

    @JsonProperty("PLAFOND_FAMILLE")
    private String plafondFamille;

    @JsonProperty("PLAFOND_PERSONNE")
    private String plafondPersonne;

    @JsonProperty("PLAFOND_MEMBRE")
    private String plafondMembre;

    /** Consommation assuré principal (information) */
    @JsonProperty("CONS_AP")
    private String consAp;

    /** Consommation membres (information) */
    @JsonProperty("CONS_MB")
    private String consMb;
}
