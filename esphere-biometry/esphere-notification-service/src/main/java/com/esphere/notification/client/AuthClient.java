package com.esphere.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "esphere-auth-service", url = "${esphere.auth.url:http://localhost:8081}")
public interface AuthClient {

    @GetMapping("/auth/users/agents-ss")
    List<Map<String, Object>> getAgentsSS();

    @GetMapping("/auth/users/prestataire/{prestataireId}/langue")
    Map<String, Object> getLanguePrestataire(@PathVariable("prestataireId") String prestataireId);
}
