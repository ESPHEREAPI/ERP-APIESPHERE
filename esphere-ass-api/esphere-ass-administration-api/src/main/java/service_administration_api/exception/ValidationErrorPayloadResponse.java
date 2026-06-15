/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.exception;

import java.util.List;
import java.util.Map;

/**
 *
 * @author USER01
 */
public record ValidationErrorPayloadResponse (

    int httpStatus,                         // 422, 401, 403
    String message,                         // Message général
    Map<String, List<String>> errors,       // Erreurs par champ
    ErrorType errorType                     // Type d'erreur pour Angular

) {
    // Enum imbriqué dans le record pour catégoriser les erreurs
    public enum ErrorType {
        VALIDATION,         // 422 - erreurs de champs
        UNAUTHORIZED,       // 401 - non authentifié
        FORBIDDEN,          // 403 - permissions
        STOCK_INSUFFICIENT, // 422 - stock insuffisant
        UNKNOWN             // Autres
    }
}