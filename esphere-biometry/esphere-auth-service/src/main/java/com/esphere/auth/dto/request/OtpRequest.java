package com.esphere.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpRequest {

    /** Code prestataire — lu depuis <code value="DLA_BINGO"/> */
    @NotBlank(message = "prestataireId obligatoire")
    private String prestataireId;

    /** Serial lecteur — lu depuis <serial value="F5C240600712"/> */
    @NotBlank(message = "serial obligatoire")
    private String serial;

    /** Code visite généré par /generer-visite-login */
    @NotBlank(message = "codeVisite obligatoire")
    private String codeVisite;

    /** Année en cours ex: 2026 */
    @NotBlank(message = "annee obligatoire")
    private String annee;

    /** Type de prestation : consultation | ordonnance | examen */
    @NotBlank(message = "naturePrestation obligatoire")
    private String naturePrestation;
}