package com.esphere.validation.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "esphere-notification-service", url = "${esphere.notification.url:http://localhost:8088}")
public interface NotificationClient {

    @PostMapping("/notifications/integration/prestation-traitee")
    Map<String, String> notifierPrestationTraitee(@RequestBody Map<String, Object> request);

    @PostMapping("/notifications/integration/prestation-soumise")
    Map<String, String> notifierPrestationSoumise(@RequestBody Map<String, Object> request);
}
