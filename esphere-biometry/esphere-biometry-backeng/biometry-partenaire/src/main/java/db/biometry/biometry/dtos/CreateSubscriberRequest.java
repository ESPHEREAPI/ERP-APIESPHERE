/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

/**
 *
 * @author USER01
 */

import db.biometry.biometry.enums.PasswordMode;
import jakarta.validation.constraints.*;
import lombok.*;

//@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSubscriberRequest {

    @NotBlank(message = "Le numéro de police est obligatoire")
   @Pattern(regexp = "^\\d{4}-\\d{10}$", message = "Format invalide (ex: 1017-2130000100)")
    private String policyNumber;

    @NotBlank(message = "Le nom complet est obligatoire")
    @Size(max = 150)
    private String fullName;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[\\d\\s\\-]{9,20}$", message = "Numéro de téléphone invalide")
    private String phoneNumber;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    @Size(max = 100)
    private String email;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Size(min = 3, max = 60, message = "Entre 3 et 60 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Caractères non autorisés")
    private String username;

    @Builder.Default
    private boolean active = true;

    @NotNull(message = "Le mode de mot de passe est obligatoire")
    private PasswordMode passwordMode;

    // Requis si passwordMode = MANUAL
    @Size(min = 8, message = "Minimum 8 caractères")
    private String password;

    // Requis si passwordMode = ACTIVATION_LINK
    @Min(value = 1, message = "Minimum 1 heure")
    @Max(value = 720, message = "Maximum 30 jours")
    private Integer activationDurationHours;

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean active() {
        return active;
    }

    public void setActive(boolean isActive) {
        this.active = isActive;
    }

    public PasswordMode getPasswordMode() {
        return passwordMode;
    }

    public void setPasswordMode(PasswordMode passwordMode) {
        this.passwordMode = passwordMode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getActivationDurationHours() {
        return activationDurationHours;
    }

    public void setActivationDurationHours(Integer activationDurationHours) {
        this.activationDurationHours = activationDurationHours;
    }

  
    
    
}
