/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

/**
 *
 * @author USER01
 */
import lombok.*;
import java.time.Instant;

// ── Réponse envoi lien d'activation ──────────────────────────────────────────
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ActivationResponse {
    private String message;
    private Instant expiry;
    private long durationHours;
    
}
