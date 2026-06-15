/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApiPoolTPV;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;

/**
 *
 * @author USER01
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductionPayloadResponse (
  @JsonProperty("status")
    Integer status,          // 201, 200, 404...

    @JsonProperty("message")
    String message,          // "La demande d'édition a été effectuée avec succès"

    @JsonProperty("data")
    ProductionPayloadData data      // L'objet principal retourné
) {}