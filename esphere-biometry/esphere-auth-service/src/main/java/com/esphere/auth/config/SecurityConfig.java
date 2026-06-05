package com.esphere.auth.config;

import com.esphere.auth.security.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration Spring Security pour Auth Service.
 *
 * Politique : - Stateless (JWT, pas de session HTTP) - CSRF désactivé (API
 * REST) - /auth/login et /auth/refresh → publics - Tout le reste → authentifié
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Désactiver CSRF — API REST stateless, pas de formulaires HTML
                .csrf(AbstractHttpConfigurer::disable)
                // Pas de session HTTP — le JWT est le seul état
                .sessionManagement(session
                        -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Règles d'autorisation
                .authorizeHttpRequests(auth -> auth
                // Routes publiques
                .requestMatchers(
                        "/auth/login",
                        "/auth/otp",
                        "/auth/validate-otp",
                        "/auth/refresh-token",
                        "/actuator/health",
                        "/actuator/info",
                        "/v3/api-docs/**",
                        "/swagger-ui/**"
                ).permitAll()
                // Tout le reste nécessite un token valide
                .anyRequest().authenticated()
                )
                // Injecter notre filtre JWT avant le filtre d'authentification standard
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
