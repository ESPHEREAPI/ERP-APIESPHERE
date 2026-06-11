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

// ── Vérification de police ────────────────────────────────────────────────────
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PolicyCheckResponse {
    private String policyNumber;
    private String fullName;
    private boolean exists;
    private boolean alreadyHasAccount;
    private String email;
    
}
