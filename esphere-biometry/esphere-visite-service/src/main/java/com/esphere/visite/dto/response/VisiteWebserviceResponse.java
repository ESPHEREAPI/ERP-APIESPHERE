package com.esphere.visite.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VisiteWebserviceResponse {

    /** Code court : ME6ED5 — utilisé par VB.NET */
    @JsonProperty("id")
    private String id;

    /** ID long : 2026_DLA_BINGO_ME6ED5 */
    @JsonProperty("idVisite")
    private String idVisite;

    @JsonProperty("idVisiteCrypte")
    private String idVisiteCrypte;

    @JsonProperty("codeAdherent")
    private String codeAdherent;

    @JsonProperty("codeAyantDroit")
    private String codeAyantDroit;

    @JsonProperty("prestataire")
    private String prestataire;
}