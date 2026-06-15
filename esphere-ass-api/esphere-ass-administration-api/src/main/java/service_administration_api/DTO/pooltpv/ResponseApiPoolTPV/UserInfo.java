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
@JsonIgnoreProperties(ignoreUnknown = true)
public record UserInfo (

    @JsonProperty("id")
    String id,              // "u1x2y3"

    @JsonProperty("name")
    String name,            // "Jean Dupont"

    @JsonProperty("email")
    String email            // "jean.dupont@activa.cm"

) {}
