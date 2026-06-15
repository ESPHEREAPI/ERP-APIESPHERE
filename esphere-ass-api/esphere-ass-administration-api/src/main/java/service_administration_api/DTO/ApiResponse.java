/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Wrapper générique pour toutes les réponses API
 * Compatible avec le frontend Angular
 * 
 * @param <T> Type de données retournées
 */

/**
 *
 * @author USER01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    
    /**
     * Indique si la requête a réussi
     */
    @JsonProperty("success")
    private boolean success;
    
    /**
     * Message descriptif
     */
    @JsonProperty("message")
    private String message;
    
    /**
     * Données de la réponse
     */
    @JsonProperty("data")
    private T data;
    
    /**
     * Code d'erreur (optionnel)
     */
    @JsonProperty("errorCode")
    private String errorCode;
    
    /**
     * Constructeur pour succès avec données
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Opération réussie", data, null);
    }
    
    /**
     * Constructeur pour succès avec message personnalisé
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }
    
    /**
     * Constructeur pour erreur
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null, null);
    }
    
    /**
     * Constructeur pour erreur avec code
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<>(false, message, null, errorCode);
    }
    
    /**
     * Constructeur pour erreur avec données partielles
     */
    public static <T> ApiResponse<T> error(String message, T data, String errorCode) {
        return new ApiResponse<>(false, message, data, errorCode);
    }
}
