package com.esphere.adherent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Point d'entrée – EsphereAdherentApplication
 * Plateforme Biométrie ESPHERE
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EsphereAdherentApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsphereAdherentApplication.class, args);
    }
}
