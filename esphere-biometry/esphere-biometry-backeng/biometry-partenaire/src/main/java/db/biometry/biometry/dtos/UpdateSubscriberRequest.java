/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import db.biometry.biometry.enums.PasswordMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER01
 */

// ── UpdateSubscriberRequest ───────────────────────────────────────────────────
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSubscriberRequest {

    @Pattern(regexp = "^\\+?[\\d\\s\\-]{9,20}$", message = "Numéro de téléphone invalide")
    private String phoneNumber;

    @Email(message = "Format email invalide")
    @Size(max = 100)
    private String email;

    @Size(min = 3, max = 60)
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Caractères non autorisés")
    private String username;

    private Boolean active;

    private PasswordMode passwordMode;

    @Size(min = 8, message = "Minimum 8 caractères")
    private String password;

    @Min(1) @Max(720)
    private Integer activationDurationHours;
    
}
