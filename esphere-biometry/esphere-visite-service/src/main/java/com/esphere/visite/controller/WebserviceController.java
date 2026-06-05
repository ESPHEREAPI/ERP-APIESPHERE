package com.esphere.visite.controller;

import com.esphere.visite.client.AdherentClient;
import com.esphere.visite.client.AyantDroitDto;
import com.esphere.visite.client.LoginRequestDto;
import com.esphere.visite.dto.response.AyantDroitWebserviceResponse;
import com.esphere.visite.dto.response.VisiteWebserviceResponse;
import com.esphere.visite.dto.response.WebserviceResponse;
import com.esphere.visite.service.VisiteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Endpoints compatibles avec l'ancienne app PHP Zend Framework.
 * Même format de réponse : { "status", "status_message", "data" }
 *
 * Routes :
 *   GET /public/webservice/generer-visite-login
 *   GET /public/webservice/connexion
 */
@Slf4j
@RestController
@RequestMapping("/public/webservice")
@RequiredArgsConstructor
public class WebserviceController {

    private final VisiteService visiteService;
    private final AdherentClient adherentClient;

    /**
     * GET /public/webservice/generer-visite-login
     *
     * Appelé par l'app VB.NET SecuGen.
     * Même signature que genererVisiteLoginAction() PHP.
     *
     * Params :
     *   codeAdherent   : ex "359_1017-2130000003"
     *   prestataire    : ex "DLA_BINGO"
     *   codeAyantDroit : ex "null" si assuré principal
     *   login          : login employé (optionnel)
     *   telephone      : ex "694923568"
     */
    @GetMapping("/generer-visite-login")
    public ResponseEntity<WebserviceResponse
           <VisiteWebserviceResponse>> genererVisiteLogin(
        @RequestParam String codeAdherent,
        @RequestParam String prestataire,
        @RequestParam(required = false,
                      defaultValue = "null")
            String codeAyantDroit,
        @RequestParam(required = false,
                      defaultValue = "null")
            String login,
        @RequestParam(required = false,
                      defaultValue = "000000000")
            String telephone
    ) {
        log.info("generer-visite-login — adherent={} " +
                 "prestataire={} ayantDroit={} login={}",
                 codeAdherent, prestataire,
                 codeAyantDroit, login);

        WebserviceResponse<VisiteWebserviceResponse>
            response = visiteService.genererVisiteLogin(
                codeAdherent,
                prestataire,
                "null".equals(codeAyantDroit)
                    ? null : codeAyantDroit,
                "null".equals(login)
                    ? null : login,
                telephone
            );

        return ResponseEntity.ok(response);
    }

    /**
     * GET /public/webservice/connexion
     *
     * Appelé par Login.vb pour l'authentification.
     * Redirige vers auth-service via la logique interne.
     * Retourne { "status": "200", "data": "1" } si OK.
     */
    @GetMapping("/connexion")
    public ResponseEntity<WebserviceResponse<String>>
    connexion(
        @RequestParam String login,
        @RequestParam String motPasse
    ) {
        log.info("connexion webservice — login={}", login);
         LoginRequestDto request= LoginRequestDto.builder()
                 .login(login)
                 .password(motPasse)
                 .build();
        WebserviceResponse<String> response =
            visiteService.connexionLegacy(request);
        return ResponseEntity.ok(response);
    }
    
    /**
 * GET /public/webservice/get-liste-ayant-droit
 * Appelé par l'app VB.NET — même format que PHP
 */
@GetMapping("/get-liste-ayant-droit")
public ResponseEntity<WebserviceResponse<
        List<AyantDroitWebserviceResponse>>>
getListeAyantDroit(
        @RequestParam String codeAdherent) {

    log.info("get-liste-ayant-droit codeAdherent={}",
             codeAdherent);

    try {
        List<AyantDroitDto> ayantsDroit =
            adherentClient.getAyantsDroit(codeAdherent);

        if (ayantsDroit == null || ayantsDroit.isEmpty()) {
            return ResponseEntity.ok(
                WebserviceResponse.ok(List.of()));
        }

        List<AyantDroitWebserviceResponse> liste =
            ayantsDroit.stream()
                .filter(a -> "1".equals(a.getStatut()))
                .map(a -> AyantDroitWebserviceResponse
                    .builder()
                    .codeAyantDroit(a.getCodeAyantDroit())
                    .codeAdherent(codeAdherent)
                    .nom(a.getNom())
                    .sexe(a.getSexe())
                    .naissance(a.getNaissance())
                    .police(a.getPolice())
                    .telephone(a.getTelephone())
                    .build())
                .toList();

        return ResponseEntity.ok(
            WebserviceResponse.ok(liste));

    } catch (Exception e) {
        log.error("Erreur get-liste-ayant-droit : {}",
                  e.getMessage());
        return ResponseEntity.ok(
            WebserviceResponse.ok(List.of()));
    }
}


}