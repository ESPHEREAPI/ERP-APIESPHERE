package com.esphere.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpResponse {

    /** URL de redirection Angular avec OTP en paramètre */
    private String redirectUrl;

    /** Durée de validité en secondes (300 = 5 min) */
    private long expiresIn;
}