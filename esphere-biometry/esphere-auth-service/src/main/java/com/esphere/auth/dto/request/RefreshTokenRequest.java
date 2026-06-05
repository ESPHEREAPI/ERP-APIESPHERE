// dto/request/RefreshTokenRequest.java
package com.esphere.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {
    @NotBlank(message = "Le token est obligatoire")
    private String token;
}