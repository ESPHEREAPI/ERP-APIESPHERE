package com.esphere.reporting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Point d'entrée – EsphereReportingApplication
 * Plateforme Biométrie ESPHERE
 */
@SpringBootApplication
@EnableDiscoveryClient
public class EsphereReportingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsphereReportingApplication.class, args);
    }
}
