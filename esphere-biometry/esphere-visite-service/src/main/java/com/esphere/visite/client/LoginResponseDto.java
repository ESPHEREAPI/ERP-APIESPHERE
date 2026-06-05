package com.esphere.visite.client;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String token;
    private String profilCode;
    private String prestataireId;
}