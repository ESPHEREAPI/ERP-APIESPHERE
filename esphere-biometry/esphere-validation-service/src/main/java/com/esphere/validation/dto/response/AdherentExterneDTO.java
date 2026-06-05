package com.esphere.validation.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO mappant un élément du tableau "tabAdherent"
 * retourné par l'endpoint externe.
 * Les clés numériques ("0","1",...) sont ignorées car redondantes.
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)  // ignore les clés "0","1","2"...
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
    private LocalDate naissance;

    @JsonProperty("MATRICULE")
    private String matricule;

    @JsonProperty("TAUX")
    private String taux;

    @JsonProperty("EFFET_POLICE")
    private LocalDate effetPolice;

    @JsonProperty("ECHEANCE_POLICE")
    private LocalDate echeancePolice;

    @JsonProperty("GROUPE")
    private String groupe;

    @JsonProperty("VILLE")
    private String ville;

    @JsonProperty("PLAFOND_FAMILLE")
    private String plafondFamille;

    @JsonProperty("PLAFOND_PERSONNE")
    private String plafondPersonne;

    @JsonProperty("PLAFOND_ASSUREP")
    private String plafondAssurep;

    @JsonProperty("PLAFOND_MEMBRE")
    private String plafondMembre;

    @JsonProperty("CONS_AP")
    private String consAp;

    @JsonProperty("CONS_MB")
    private String consMb;
}