package com.esphere.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Point d'entrée – EsphereNotificationApplication
 * Plateforme Biométrie ESPHERE
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync 
public class EsphereNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsphereNotificationApplication.class, args);
    }
}
