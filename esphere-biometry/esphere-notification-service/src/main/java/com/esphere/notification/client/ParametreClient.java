package com.esphere.notification.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "esphere-validation-service", url = "${esphere.validation.url:http://localhost:8085}")
public interface ParametreClient {

    @GetMapping("/parametres/{cle}")
    Map<String, String> getParametre(@PathVariable("cle") String cle);
}
