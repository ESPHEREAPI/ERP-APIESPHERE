/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gateway_proxy;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration des routes Spring Cloud Gateway
 * 
 * ✅ CORRECTION : Suppression des metadata.timeout qui causaient l'erreur PT30S
 * Les timeouts sont maintenant gérés globalement dans application.properties :
 * - spring.cloud.gateway.httpclient.connect-timeout=30000
 * - spring.cloud.gateway.httpclient.response-timeout=120000
 * 
 * @author JIATOU FRANCK
 */
@Configuration
public class GatewayTimeoutConfiguration {

    /**
     * Configuration des routes pour tous les services
     * Les timeouts sont configurés globalement dans application.properties
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                
                // ========================================
                // Route 1 : Service Biométrie (Port 8081)
                // ========================================
                .route("service-biometrie", r -> r
                        .path("/gateway-proxy/api/service-biometrie/**")
                        .filters(f -> f
                                .stripPrefix(3)
                                .preserveHostHeader()
                                // ✅ SUPPRIMÉ : metadata timeout (causait l'erreur PT30S)
                        )
                        .uri("http://127.0.0.1:8081")
                )
                
                // ========================================
                // Route 2 : Service Admin (Port 8083)
                // ========================================
                .route("service-admin", r -> r
                        .path("/gateway-proxy/api/esphere-ass-microservice-admin/**")
                        .filters(f -> f
                                .stripPrefix(3)
                                .preserveHostHeader()
                                // ✅ SUPPRIMÉ : metadata timeout
                        )
                        .uri("http://127.0.0.1:8083")
                )
                
                // ========================================
                // Route 3 : Service Biométrie Partenaire (Port 8082)
                // ========================================
                .route("service-biometrie-partenaire", r -> r
                        .path("/gateway-proxy/api/service-biometrie-partenaire/**")
                        .filters(f -> f
                                .stripPrefix(3)
                                .preserveHostHeader()
                                // ✅ SUPPRIMÉ : metadata timeout
                        )
                        .uri("http://127.0.0.1:8082")
                )
                
                // ========================================
                // Route 4 : Gateway Health Check (Optionnel)
                // ========================================
                .route("gateway-health", r -> r
                        .path("/gateway-proxy/health")
                        .filters(f -> f
                                .stripPrefix(1)
                                // ✅ SUPPRIMÉ : metadata timeout
                        )
                        .uri("http://127.0.0.1:8080")
                )
                
                .build();
    }
}