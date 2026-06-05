package com.esphere.visite.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@FeignClient(
    name = "adherent-client",
    url  = "${esphere.services.adherent-url:http://localhost:8082}"
)
public interface AdherentClient {

    @GetMapping("/adherents/{codeAdherent}")
    AdherentDto getAdherent(
        @PathVariable String codeAdherent);
    @GetMapping("/adherents/{codeAdherent}/ayants-droit")
    List<AyantDroitDto> getAyantsDroit(
        @PathVariable("codeAdherent") String codeAdherent);
}