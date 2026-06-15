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
// dto/response/ProductionRef.java
// Représente l'objet "production" imbriqué dans chaque certificat
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductionRef(

    @JsonProperty("reference")
    String reference,       // "PROD-2026-00456"

    @JsonProperty("user")
    UserDetail user         // Utilisateur avec code, name, email, telephone

) {}