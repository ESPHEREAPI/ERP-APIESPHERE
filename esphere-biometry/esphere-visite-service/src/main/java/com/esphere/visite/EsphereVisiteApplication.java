package com.esphere.visite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Point d'entrée – EsphereVisiteApplication
 * Plateforme Biométrie ESPHERE
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients  // ← AJOUTEZ CETTE ANNOTATION
public class EsphereVisiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsphereVisiteApplication.class, args);
    }
}
