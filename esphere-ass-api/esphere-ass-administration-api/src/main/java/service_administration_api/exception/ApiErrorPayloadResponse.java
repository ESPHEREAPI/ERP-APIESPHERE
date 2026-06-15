/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

/**
 *
 * @author USER01
 */
// dto/error/ApiErrorResponse.java
// Représente la réponse d'erreur de l'API externe (422, 401, 403)
public record ApiErrorPayloadResponse (

    @JsonProperty("message")
    String message,                         // "Description générale de l'erreur"

    @JsonProperty("errors")
    Map<String, List<String>> errors        // { "productions.0.vehicle_chassis": ["..."] }

) {}
