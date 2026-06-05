package com.esphere.validation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Point d'entrée – EsphereValidationApplication
 * Plateforme Biométrie ESPHERE
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class EsphereValidationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsphereValidationApplication.class, args);
    }
}
