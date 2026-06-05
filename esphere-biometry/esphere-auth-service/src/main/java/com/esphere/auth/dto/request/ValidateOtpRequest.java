package com.esphere.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ValidateOtpRequest {

    @NotBlank(message = "otp obligatoire")
    private String otp;
}