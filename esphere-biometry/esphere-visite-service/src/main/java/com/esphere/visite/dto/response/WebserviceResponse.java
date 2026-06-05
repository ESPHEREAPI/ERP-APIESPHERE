package com.esphere.visite.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Enveloppe compatible avec l'ancienne app PHP Zend.
 * Format : { "status": "200", "status_message": "", "data": {...} }
 */
@Data
@Builder
public class WebserviceResponse<T> {

    @JsonProperty("status")
    private String status;

    @JsonProperty("status_message")
    private String statusMessage;

    @JsonProperty("data")
    private T data;

    public static <T> WebserviceResponse<T> ok(T data) {
        return WebserviceResponse.<T>builder()
            .status("200")
            .statusMessage("")
            .data(data)
            .build();
    }

    public static <T> WebserviceResponse<T> error(
            String message) {
        return WebserviceResponse.<T>builder()
            .status("400")
            .statusMessage(message)
            .data(null)
            .build();
    }
}