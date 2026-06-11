/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package db.biometry.biometry.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USER01
 */
public enum PasswordMode {

    @JsonProperty("manual")
    MANUAL,

    @JsonProperty("activation_link")
    ACTIVATION_LINK;

    @JsonCreator
    public static PasswordMode fromString(String value) {
        if (value == null) return null;
        return switch (value.toLowerCase()) {
            case "manual"          -> MANUAL;
            case "activation_link" -> ACTIVATION_LINK;
            default -> throw new IllegalArgumentException(
                "Valeur PasswordMode inconnue : " + value
            );
        };
    }
}