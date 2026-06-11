/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

/**
 *
 * @author USER01
 */
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SendActivationRequest {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    private String email;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1,   message = "Minimum 1 heure")
    @Max(value = 720, message = "Maximum 30 jours (720 h)")
    private Integer duration;  // en heures
    
}
