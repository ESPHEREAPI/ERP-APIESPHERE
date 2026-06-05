package com.esphere.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Point d'entrée – EsphereMediaApplication
 * Plateforme Biométrie ESPHERE
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EsphereMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsphereMediaApplication.class, args);
    }
}
