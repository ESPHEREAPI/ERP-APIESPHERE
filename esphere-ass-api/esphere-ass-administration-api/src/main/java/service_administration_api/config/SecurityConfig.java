package service_administration_api.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    /**
     * Patterns d'origines autorisées — supporte les wildcards via
     * setAllowedOriginPatterns()
     *
     * Valeur par défaut couvre :
     * - localhost tous ports (dev Angular)
     * - 127.0.0.1 tous ports
     * - IP publique 77.68.94.193 tous ports HTTP et HTTPS
     *
     * Surchargeable dans application.yml :
     * cors.allowed-origins: http://localhost:*,https://mondomaine.com
     */
    @Value("#{'${cors.allowed-origins:http://localhost:*,https://localhost:*,http://127.0.0.1:*,https://127.0.0.1:*,http://77.68.94.193,https://77.68.94.193,http://77.68.94.193:*,https://77.68.94.193:*}'.split(',')}")
    private List<String> allowedOriginPatterns;

    @Value("${cors.max-age:3600}")
    private long corsMaxAge;

    /** Endpoints publics sans authentification */
    private static final String[] PUBLIC_ENDPOINTS = {
        "/auth/login",
        "/auth/health",
        "/actuator/health",
        "/actuator/info",
        "/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(this::handleUnauthorized)
                .accessDeniedHandler(this::handleForbidden)
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Utilise setAllowedOriginPatterns pour supporter les wildcards (*)
        config.setAllowedOriginPatterns(allowedOriginPatterns);

        config.setAllowedMethods(List.of(
            "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"
        ));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization", "X-Refresh-Token"));
        config.setAllowCredentials(true);
        config.setMaxAge(corsMaxAge);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /** Réponse 401 structurée — remontée au frontend */
    private void handleUnauthorized(HttpServletRequest req, HttpServletResponse res,
            org.springframework.security.core.AuthenticationException ex) throws IOException {
        writeErrorResponse(res, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED",
                "Token manquant ou invalide. Veuillez vous reconnecter.");
    }

    /** Réponse 403 structurée — remontée au frontend */
    private void handleForbidden(HttpServletRequest req, HttpServletResponse res,
            org.springframework.security.access.AccessDeniedException ex) throws IOException {
        writeErrorResponse(res, HttpStatus.FORBIDDEN, "FORBIDDEN",
                "Accès refusé. Vous n'avez pas les permissions nécessaires.");
    }

    private void writeErrorResponse(HttpServletResponse res, HttpStatus status,
            String errorCode, String message) throws IOException {
        res.setStatus(status.value());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setCharacterEncoding("UTF-8");
        res.getWriter().write("""
            {
              "success": false,
              "status": %d,
              "errorCode": "%s",
              "message": "%s",
              "timestamp": %d
            }
            """.formatted(status.value(), errorCode, message, System.currentTimeMillis()));
    }
}