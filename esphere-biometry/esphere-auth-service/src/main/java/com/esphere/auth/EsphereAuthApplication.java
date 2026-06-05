package com.esphere.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Point d'entrée – EsphereAuthApplication
 * Plateforme Biométrie ESPHERE
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EsphereAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsphereAuthApplication.class, args);
    }
}
