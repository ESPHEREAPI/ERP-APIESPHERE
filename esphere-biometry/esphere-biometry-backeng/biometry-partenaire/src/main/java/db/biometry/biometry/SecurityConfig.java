package db.biometry.biometry;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de sécurité de biometry-partenaire.
 *
 * Ce service est déployé derrière esphere-gateway-service qui assure :
 *   - La validation du token JWT (JwtAuthFilter)
 *   - La propagation des claims dans les headers X-User-Id, X-User-Login,
 *     X-Profil-Code, X-Prestataire-Id
 *   - Le contrôle CORS
 *
 * Ce service ne reçoit donc que des requêtes déjà authentifiées par la Gateway.
 * Il n'a pas besoin d'un filtre JWT propre — cela évite une double validation
 * et les conflits de configuration.
 *
 * IMPORTANT : En développement direct (sans Gateway), toutes les routes sont
 * accessibles sans token. Ne jamais exposer le port 8090 directement en production.
 *
 * Le contrôle fin des accès par profil (DII, SOUSCRIPTEUR, etc.) se fait
 * via @PreAuthorize dans les controllers, en lisant le header X-Profil-Code.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Pas de CSRF — API REST stateless
            .csrf(AbstractHttpConfigurer::disable)

            // Pas de session HTTP — le JWT Gateway est le seul état
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Toutes les routes autorisées au niveau Spring Security :
            // la Gateway est le gardien. Le contrôle métier par profil
            // se fait via @PreAuthorize dans chaque controller.
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }
}