/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApiPoolTPV;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USER01
 */
// dto/response/CodeName.java
// Record générique pour les objets { "code": "...", "name": "..." }
// Réutilisé pour certificate_type ET certificate_variant
@JsonIgnoreProperties(ignoreUnknown = true)
public record CodeName(

    @JsonProperty("code")
    String code,            // "TERR" ou "JAUNE"

    @JsonProperty("name")
    String name             // "Attestation Terrestre" ou "Jaune"

) {}