package com.esphere.webservicecron.controller;

import com.esphere.webservicecron.dto.response.PoliceStatusResponse;
import com.esphere.webservicecron.dto.response.WebserviceResponse;
import com.esphere.webservicecron.service.PoliceStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webservice/police")
@RequiredArgsConstructor
public class PoliceStatusController {

    private final PoliceStatusService policeStatusService;

    // GET /webservice/police/{police}/statut
    @GetMapping("/{police}/statut")
    public ResponseEntity<WebserviceResponse<PoliceStatusResponse>> statut(@PathVariable String police) {
        PoliceStatusResponse statut = policeStatusService.obtenirStatut(police);
        if (statut == null) {
            return ResponseEntity.ok(WebserviceResponse.error("Aucune donnee pour la police " + police));
        }
        return ResponseEntity.ok(WebserviceResponse.ok(statut));
    }
}
