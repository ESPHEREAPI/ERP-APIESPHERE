package com.esphere.prestataire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Point d'entrée – EspherePrestaireApplication
 * Plateforme Biométrie ESPHERE
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EspherePrestaireApplication {

    public static void main(String[] args) {
        SpringApplication.run(EspherePrestaireApplication.class, args);
    }
}
