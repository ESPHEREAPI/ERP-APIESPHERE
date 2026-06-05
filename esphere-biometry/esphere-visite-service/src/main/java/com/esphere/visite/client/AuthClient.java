package com.esphere.visite.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "auth-client",
    url  = "${esphere.services.auth-url:http://localhost:8081}"
)
public interface AuthClient {

    @PostMapping("/auth/login")
    LoginResponseDto loginLegacy(
        @RequestBody LoginRequestDto request);
}