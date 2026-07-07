package com.esphere.webservicecron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Point d'entrée – EsphereWebserviceCronApplication
 * Plateforme Biométrie ESPHERE
 *
 * Équivalent Java du module Zend Framework "Webservice" (PHP) :
 * synchronisation périodique (adhérents, ayants droit, taux de
 * prestation) depuis le serveur biométrie externe legacy.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class EsphereWebserviceCronApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsphereWebserviceCronApplication.class, args);
    }
}
