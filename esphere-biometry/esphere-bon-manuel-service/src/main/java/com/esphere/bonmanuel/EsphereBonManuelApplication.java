package com.esphere.bonmanuel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Point d'entrée – EsphereBonManuelApplication
 * Plateforme Biométrie ESPHERE
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EsphereBonManuelApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsphereBonManuelApplication.class, args);
    }
}
