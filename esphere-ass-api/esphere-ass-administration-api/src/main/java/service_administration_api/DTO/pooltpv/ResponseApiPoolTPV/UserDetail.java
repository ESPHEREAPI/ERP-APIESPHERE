/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApiPoolTPV;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USER01
 */
public record UserDetail (

    @JsonProperty("code")
    String code,            // "jdupont"

    @JsonProperty("name")
    String name,

    @JsonProperty("email")
    String email,

    @JsonProperty("telephone")
    String telephone        // "+237690000000"

) {}
