package com.esphere.gateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtProvider jwtProvider;

private static final List<String> ENDPOINTS_PUBLICS = List.of(
    "/auth/login",
    "/auth/otp",
    "/auth/validate-otp",
    "/auth/refresh-token",
    "/public/webservice/",
    "/validations/consommation/visite/",   // ← pré-remplissage formulaire
    "/validations/dashboard/",
    "/capture/",
    "/actuator",
    "/auth/health"
);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (ENDPOINTS_PUBLICS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }
        String authHeader = exchange.getRequest().getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return rejeter(exchange, "Token JWT manquant.");
        }
        String token = authHeader.substring(7);
        if (!jwtProvider.validerToken(token)) {
            return rejeter(exchange, "Token JWT invalide ou expire.");
        }
        try {
            Claims claims = jwtProvider.getClaims(token);
            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id", claims.get("userId", Integer.class).toString())
                    .header("X-User-Login", claims.getSubject())
                    .header("X-Profil-Code", claims.get("profilCode", String.class))
                    .header("X-Prestataire-Id", claims.get("prestId", String.class))
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } catch (Exception e) {
            return rejeter(exchange, "Erreur de traitement du token.");
        }
    }

    private Mono<Void> rejeter(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        String body = "{\"status\":401,\"erreur\":\"" + message + "\"}";
        var buffer = response.bufferFactory().wrap(body.getBytes());
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder() { return -1; }
}
