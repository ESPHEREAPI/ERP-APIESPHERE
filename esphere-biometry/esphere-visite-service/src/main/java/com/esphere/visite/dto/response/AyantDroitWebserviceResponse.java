package com.esphere.visite.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AyantDroitWebserviceResponse {

    @JsonProperty("codeAyantDroit")
    private String codeAyantDroit;

    @JsonProperty("codeAdherent")
    private String codeAdherent;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("sexe")
    private String sexe;

    @JsonProperty("naissance")
    private String naissance;

    @JsonProperty("police")
    private String police;

    @JsonProperty("telephone")
    private String telephone;
}