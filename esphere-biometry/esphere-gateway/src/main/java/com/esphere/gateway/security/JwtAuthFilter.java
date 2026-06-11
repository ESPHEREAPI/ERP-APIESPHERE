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

    // ⚠️ Règle : préfixes exacts, pas de "/**".
    // Chaque entrée doit être aussi précise que possible — un préfixe trop court
    // ouvre involontairement des routes protégées (ex: "/subscriber" couvre tout le CRUD).
    private static final List<String> ENDPOINTS_PUBLICS = List.of(
            // ── Service auth ────────────────────────────────────────────
            "/auth/login",
            "/auth/otp",
            "/auth/validate-otp",
            "/auth/refresh-token",
            "/auth/health",

            // ── biometry-partenaire : login uniquement (pas de CRUD) ───
            "/users/login",
            "/users/compagnie/login",
            "/users/adherent/login",
            "/users/logout",

            // ── biometry-partenaire : activation compte (lien email) ───
            // UNIQUEMENT /subscriber/activate — pas tout /subscriber
            "/subscriber/activate",

            // ── Routes publiques métier ──────────────────────────────────
            "/public/webservice/",
            "/validations/consommation/visite/",
            "/capture/",

            // ── Monitoring / docs techniques ────────────────────────────
            "/actuator",
            "/v3/api-docs",
            "/swagger-ui"

            // ❌ RETIRÉ : "/dashboard/"     → données sensibles, JWT requis
            // ❌ RETIRÉ : "/subscriber"      → couvrait tout le CRUD souscripteur
            // ❌ RETIRÉ : "/adherents"       → données assurés, JWT requis
            // ❌ RETIRÉ : "/validations/dashboard/" → stats métier, JWT requis
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        String method = exchange.getRequest().getMethod().name();

        // Route publique → laisser passer sans vérification
        if (ENDPOINTS_PUBLICS.stream().anyMatch(path::startsWith)) {
            log.debug("[Gateway] Route publique autorisée : {} {}", method, path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("[Gateway] Accès refusé (token manquant) : {} {}", method, path);
            return rejeter(exchange, "Token JWT manquant.");
        }

        String token = authHeader.substring(7);
        if (!jwtProvider.validerToken(token)) {
            log.warn("[Gateway] Accès refusé (token invalide) : {} {}", method, path);
            return rejeter(exchange, "Token JWT invalide ou expiré.");
        }

        try {
            Claims claims = jwtProvider.getClaims(token);

            String userId     = String.valueOf(claims.get("userId", Integer.class));
            String userLogin  = claims.getSubject();
            String profilCode = claims.get("profilCode", String.class);
            String prestId    = claims.get("prestId", String.class);

            log.debug("[Gateway] Accès autorisé : {} {} | user={} profil={}",
                    method, path, userLogin, profilCode);

            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("X-User-Id",        userId)
                    .header("X-User-Login",     userLogin)
                    .header("X-Profil-Code",    profilCode != null ? profilCode : "")
                    .header("X-Prestataire-Id", prestId    != null ? prestId    : "")
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e) {
            log.error("[Gateway] Erreur traitement token pour {} {} : {}", method, path, e.getMessage());
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
    public int getOrder() {
        return -1;
    }
}