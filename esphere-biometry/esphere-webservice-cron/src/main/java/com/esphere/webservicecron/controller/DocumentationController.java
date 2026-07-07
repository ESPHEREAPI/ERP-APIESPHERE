package com.esphere.webservicecron.controller;

import com.esphere.webservicecron.dto.response.EndpointDocResponse;
import com.esphere.webservicecron.dto.response.GuideResponse;
import com.esphere.webservicecron.dto.response.WebserviceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Guide/documentation des endpoints exposés par esphere-webservice-cron —
 * équivalent Java du module PHP Zend "Webservice".
 *
 * GET /webservice  →  liste tous les endpoints, leur méthode, leurs
 * paramètres et un exemple d'appel, au même format de réponse legacy
 * { "status", "status_message", "data" } que les autres routes.
 *
 * Le Swagger UI standard (springdoc) reste aussi disponible sur
 * /swagger-ui.html pour une exploration interactive.
 */
@RestController
@RequestMapping("/webservice")
public class DocumentationController {

    @GetMapping
    public ResponseEntity<WebserviceResponse<GuideResponse>> guide() {

        List<EndpointDocResponse> endpoints = List.of(

                EndpointDocResponse.builder()
                        .methode("GET, POST")
                        .chemin("/webservice")
                        .description("Affiche ce guide listant tous les endpoints du service.")
                        .parametres(List.of("(aucun)"))
                        .exemple("GET http://localhost:8091/webservice")
                        .build(),

                EndpointDocResponse.builder()
                        .methode("GET, POST")
                        .chemin("/webservice/recuperer-donnees-adherent[/{police}]")
                        .description("Importe les adhérents depuis le serveur biométrie externe legacy "
                                + "(création ou mise à jour), applique les règles métier "
                                + "(souscripteurs forcés par police, taux 80/100% ONCC).")
                        .parametres(List.of("police (optionnel, dans le chemin) : filtre sur une police précise"))
                        .exemple("GET http://localhost:8091/webservice/recuperer-donnees-adherent/1017-2130000110")
                        .build(),

                EndpointDocResponse.builder()
                        .methode("GET, POST")
                        .chemin("/webservice/recuperer-donnees-ayant-droit[/{police}]")
                        .description("Importe les ayants droit depuis le serveur externe. Un ayant droit "
                                + "est ignoré si son adhérent n'existe pas encore en base "
                                + "(exécuter recuperer-donnees-adherent avant).")
                        .parametres(List.of("police (optionnel, dans le chemin) : filtre sur une police précise"))
                        .exemple("GET http://localhost:8091/webservice/recuperer-donnees-ayant-droit/1017-2130000110")
                        .build(),

                EndpointDocResponse.builder()
                        .methode("GET, POST")
                        .chemin("/webservice/recuperer-donnees-taux-prestation[/{police}]")
                        .description("Importe les taux de prestation depuis le serveur externe, crée les "
                                + "types de prestation manquants, applique le mapping des anciens codes "
                                + "(tabEquiv) et force le taux à 100% pour 3 polices ONCC.")
                        .parametres(List.of("police (optionnel, dans le chemin) : filtre sur une police precise, "
                                + "ex 1017-2130000110"))
                        .exemple("GET http://localhost:8091/webservice/recuperer-donnees-taux-prestation/1017-2130000110")
                        .build(),

                EndpointDocResponse.builder()
                        .methode("GET, POST")
                        .chemin("/webservice/desactiver-donnees-adherent[/{police}]")
                        .description("Désactive (statut=-1) tous les adhérents absents de la liste source, "
                                + "sauf ceux des groupes ONCC codés en dur. Réactive ceux présents dans la "
                                + "source, sauf les fraudeurs CAMTEL connus. A exécuter après les imports.")
                        .parametres(List.of("police (optionnel, dans le chemin) : restreint la désactivation a une police"))
                        .exemple("GET http://localhost:8091/webservice/desactiver-donnees-adherent")
                        .build(),

                EndpointDocResponse.builder()
                        .methode("GET, POST")
                        .chemin("/webservice/desactiver-donnees-ayant-droit[/{codeAdherent}]")
                        .description("Désactive les ayants droit absents de la source, en se basant sur le "
                                + "statut (déjà à jour) de leur adhérent. A exécuter après "
                                + "desactiver-donnees-adherent.")
                        .parametres(List.of("codeAdherent (optionnel, dans le chemin) : restreint a un adherent"))
                        .exemple("GET http://localhost:8091/webservice/desactiver-donnees-ayant-droit")
                        .build(),

                EndpointDocResponse.builder()
                        .methode("GET, POST")
                        .chemin("/webservice/synchronisation-complete")
                        .description("Lance manuellement la séquence complète dans l'ordre "
                                + "(adhérents -> ayants droit -> taux de prestation -> désactivations), "
                                + "la même que celle exécutée automatiquement à 06h30/18h00/20h00.")
                        .parametres(List.of("(aucun)"))
                        .exemple("POST http://localhost:8091/webservice/synchronisation-complete")
                        .build(),

                EndpointDocResponse.builder()
                        .methode("GET")
                        .chemin("/webservice/historique")
                        .description("Liste paginée de l'historique des synchronisations (manuelles ou "
                                + "planifiées), avec leur statut, durée, nombre de lignes traitées/échouées.")
                        .parametres(List.of(
                                "type (optionnel) : ADHERENT | AYANT_DROIT | TAUX_PRESTATION | DESACTIVATION_ADHERENT | DESACTIVATION_AYANT_DROIT",
                                "dateDebut, dateFin (optionnel) : bornes ISO-8601, ex 2026-06-01T00:00:00",
                                "page (defaut 0), size (defaut 20)"))
                        .exemple("GET http://localhost:8091/webservice/historique?type=ADHERENT&page=0&size=20")
                        .build(),

                EndpointDocResponse.builder()
                        .methode("GET")
                        .chemin("/webservice/historique/{id}")
                        .description("Détail d'une exécution précise de l'historique, avec le détail complet "
                                + "des lignes en échec et le message d'erreur le cas échéant.")
                        .parametres(List.of("id (obligatoire, dans le chemin) : identifiant de l'historique"))
                        .exemple("GET http://localhost:8091/webservice/historique/42")
                        .build()
        );

        GuideResponse guide = GuideResponse.builder()
                .service("esphere-webservice-cron")
                .description("Équivalent Java du module PHP Zend \"Webservice\" : synchronisation "
                        + "périodique (adhérents, ayants droit, taux de prestation) depuis le serveur "
                        + "biométrie externe legacy, avec historique des passages. "
                        + "Documentation interactive Swagger disponible sur /swagger-ui.html.")
                .ordreRecommande("recuperer-donnees-adherent -> recuperer-donnees-ayant-droit -> "
                        + "recuperer-donnees-taux-prestation -> desactiver-donnees-adherent -> "
                        + "desactiver-donnees-ayant-droit (ou directement synchronisation-complete)")
                .endpoints(endpoints)
                .build();

        return ResponseEntity.ok(WebserviceResponse.ok(guide));
    }
}
