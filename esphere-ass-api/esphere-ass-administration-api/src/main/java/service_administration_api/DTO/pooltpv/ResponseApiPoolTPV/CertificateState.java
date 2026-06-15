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
// CertificateState.java — NOUVELLE CLASSE
@JsonIgnoreProperties(ignoreUnknown = true)
public record CertificateState(
    @JsonProperty("name")  String name,   // "active"
    @JsonProperty("label") String label   // "Active"
) {}

