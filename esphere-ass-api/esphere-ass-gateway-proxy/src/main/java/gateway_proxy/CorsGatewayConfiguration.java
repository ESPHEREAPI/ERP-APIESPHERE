package gateway_proxy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Configuration CORS pour Spring Cloud Gateway
 *
 * RÔLE : Permet au frontend Angular (localhost:4200) de communiquer avec les
 * microservices via la Gateway (localhost:8081)
 *
 * ORDRE D'EXÉCUTION : @Order(-1) = Priorité maximale Ce filtre s'exécute AVANT
 * tous les autres
 *
 * @author USER01
 */
@Configuration
public class CorsGatewayConfiguration {

    /**
     * Configuration CORS globale pour toutes les routes Gateway
     *
     * POURQUOI @Order(-1) ? - Les requêtes OPTIONS (preflight) doivent être
     * traitées EN PREMIER - Si un autre filtre s'exécute avant, la requête CORS
     * peut échouer - L'ordre -1 garantit que CORS est traité en priorité
     *
     * @return
     */
//    @Bean
//    @Order(-1)
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//
//        // ========================================
//        // 1. ORIGINES AUTORISÉES
//        // ========================================
//        /**
//         * addAllowedOriginPattern vs addAllowedOrigin :
//         *
//         * - addAllowedOrigin("http://localhost:4200") → Accepte UNIQUEMENT
//         * cette URL exacte
//         *
//         * - addAllowedOriginPattern("http://localhost:*") → Accepte tous les
//         * ports localhost (4200, 8080, 3000, etc.) → Plus flexible pour le
//         * développement
//         *
//         * ⚠️ IMPORTANT : Quand allowCredentials=true, on NE PEUT PAS utiliser
//         * "*" comme origine On DOIT spécifier les origines exactes ou patterns
//         */
//        // Pattern pour localhost (tous les ports)
//        corsConfig.addAllowedOriginPattern("http://localhost:*");
//        corsConfig.addAllowedOriginPattern("https://localhost:*");
//
//        // Pattern pour 127.0.0.1 (équivalent localhost en IP)
//        corsConfig.addAllowedOriginPattern("http://127.0.0.1:*");
//        corsConfig.addAllowedOriginPattern("https://127.0.0.1:*");
//
//        // Pattern pour réseau local (Docker, VM, etc.)
//        corsConfig.addAllowedOriginPattern("http://192.168.1.*:*");    // Réseau local 1
//        corsConfig.addAllowedOriginPattern("http://192.168.123.*:*");  // Réseau local 2
//
//        // Origine Docker (conteneur nommé)
//        corsConfig.addAllowedOriginPattern("http://easycom-app*");
//
//        // Origines spécifiques (plus sécurisé pour production)
//        corsConfig.addAllowedOriginPattern("http://localhost:4200");   // Angular dev
//        corsConfig.addAllowedOriginPattern("https://localhost:4200");  // Angular dev HTTPS
//        corsConfig.addAllowedOriginPattern("http://localhost:8282");   // Angular dev
//        corsConfig.addAllowedOriginPattern("https://localhost:8282");  // Angular dev HTTPS
//
//        // Ajouter APRÈS les patterns localhost
//        corsConfig.addAllowedOriginPattern("http://77.68.94.193:*");
//        corsConfig.addAllowedOriginPattern("https://77.68.94.193:*");
//
//// Spécifique
//        corsConfig.addAllowedOriginPattern("https://77.68.94.193");
//        corsConfig.addAllowedOriginPattern("http://77.68.94.193");
//
//        /**
//         * 📝 RECOMMANDATION PRODUCTION : Remplacer les patterns par des
//         * origines exactes :
//         *
//         * corsConfig.addAllowedOrigin("https://app.zenithe-insurance.com");
//         * corsConfig.addAllowedOrigin("https://www.zenithe-insurance.com");
//         */
//        // ========================================
//        // 2. CREDENTIALS (Cookies, Auth Headers)
//        // ========================================
//        /**
//         * setAllowCredentials(true) :
//         *
//         * - Permet l'envoi de cookies et headers d'authentification -
//         * Nécessaire pour JWT, sessions, tokens - Quand activé, INTERDIT
//         * l'utilisation de "*" pour origins
//         *
//         * ⚠️ Si false, les tokens Bearer ne seront PAS envoyés automatiquement
//         */
//        corsConfig.setAllowCredentials(true);
//
//        // ========================================
//        // 3. HEADERS AUTORISÉS (Request)
//        // ========================================
//        /**
//         * Headers que le frontend PEUT envoyer au backend
//         *
//         * Ces headers sont vérifiés lors de la requête preflight (OPTIONS) Si
//         * un header n'est pas autorisé, la requête échouera
//         */
//        corsConfig.addAllowedHeader("Authorization");      // Token JWT Bearer
//        corsConfig.addAllowedHeader("Content-Type");       // application/json
//        corsConfig.addAllowedHeader("Accept");             // application/json
//        corsConfig.addAllowedHeader("X-Requested-With");   // XMLHttpRequest
//        corsConfig.addAllowedHeader("Cache-Control");      // Gestion cache
//        corsConfig.addAllowedHeader("X-Gateway-Token");    // Token Gateway personnalisé
//
//        /**
//         * Alternative : corsConfig.addAllowedHeader("*"); Autorise TOUS les
//         * headers ⚠️ Moins sécurisé mais plus flexible en développement
//         */
//        // ========================================
//        // 4. MÉTHODES HTTP AUTORISÉES
//        // ========================================
//        /**
//         * Méthodes que le frontend PEUT utiliser
//         *
//         * Liste exhaustive pour couvrir tous les cas d'usage REST
//         */
//        corsConfig.addAllowedMethod("GET");      // Lecture
//        corsConfig.addAllowedMethod("POST");     // Création
//        corsConfig.addAllowedMethod("PUT");      // Mise à jour complète
//        corsConfig.addAllowedMethod("PATCH");    // Mise à jour partielle
//        corsConfig.addAllowedMethod("DELETE");   // Suppression
//        corsConfig.addAllowedMethod("OPTIONS");  // Preflight (obligatoire)
//        corsConfig.addAllowedMethod("HEAD");     // Headers seulement
//
//        /**
//         * Alternative : corsConfig.addAllowedMethod("*"); Autorise TOUTES les
//         * méthodes
//         */
//        // ========================================
//        // 5. HEADERS EXPOSÉS (Response)
//        // ========================================
//        /**
//         * Headers que le frontend PEUT lire dans la réponse
//         *
//         * Par défaut, seuls ces headers sont accessibles côté client : -
//         * Cache-Control - Content-Language - Content-Type - Expires -
//         * Last-Modified - Pragma
//         *
//         * Pour accéder à d'autres headers (ex: Authorization), il faut les
//         * exposer explicitement
//         */
//        corsConfig.addExposedHeader("Authorization");   // Token refresh
//        corsConfig.addExposedHeader("Content-Type");    // Type de contenu
//        corsConfig.addExposedHeader("X-Total-Count");   // Pagination
//
//        /**
//         * Exemple d'utilisation côté Angular :
//         *
//         * this.http.get(url).subscribe(response => { const token =
//         * response.headers.get('Authorization'); const totalCount =
//         * response.headers.get('X-Total-Count'); });
//         */
//        // ========================================
//        // 6. CACHE PREFLIGHT
//        // ========================================
//        /**
//         * setMaxAge(3600L) :
//         *
//         * - Cache la réponse preflight (OPTIONS) pendant 3600 secondes (1
//         * heure) - Évite de refaire la requête OPTIONS à chaque requête réelle
//         * - Améliore les performances
//         *
//         * FONCTIONNEMENT : 1. Frontend fait OPTIONS → Gateway répond avec
//         * headers CORS 2. Navigateur met en cache cette réponse pendant 1 heure
//         * 3. Pendant 1 heure, les requêtes suivantes ne refont PAS de preflight
//         * 4. Après 1 heure, une nouvelle requête OPTIONS sera faite
//         */
//        corsConfig.setMaxAge(3600L);
//
//        // ========================================
//        // 7. ENREGISTREMENT DE LA CONFIGURATION
//        // ========================================
//        /**
//         * Applique la configuration CORS à toutes les routes
//         *
//         * "/**" = Toutes les URLs passant par la Gateway
//         *
//         * Exemples d'URLs couvertes : -
//         * /gateway-proxy/api/service-biometrie/auth/users/login -
//         * /gateway-proxy/api/esphere-ass-microservice-admin/users - Toutes les
//         * autres routes définies dans bootstrap.properties
//         */
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//
//        return new CorsWebFilter(source);
//    }

        


    @Bean
    @Order(-1)
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Origines autorisées
        corsConfig.addAllowedOriginPattern("http://localhost:*");
        corsConfig.addAllowedOriginPattern("https://localhost:*");
        corsConfig.addAllowedOriginPattern("https://77.68.94.193");
        corsConfig.addAllowedOriginPattern("http://77.68.94.193");
        corsConfig.addAllowedOriginPattern("https://77.68.94.193:*");
        corsConfig.addAllowedOriginPattern("https://zenbio.zenitheinsurance.com");

        // Credentials
        corsConfig.setAllowCredentials(true);

        // Headers autorisés
        corsConfig.addAllowedHeader("*");

        // Méthodes
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("PATCH");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedMethod("OPTIONS");
        corsConfig.addAllowedMethod("HEAD");

        // Headers exposés au frontend
        corsConfig.addExposedHeader("Authorization");
        corsConfig.addExposedHeader("X-Refresh-Token");
        corsConfig.addExposedHeader("X-Total-Count");
        corsConfig.addExposedHeader("Content-Type");

        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
    /**
     * ======================================== NOTES IMPORTANTES
     * ========================================
     *
     * 1. ORDRE DES FILTRES :
     *
     * @Order(-1) garantit que CORS s'exécute EN PREMIER Si un autre filtre a
     * @Order(0) ou plus, CORS passera avant
     *
     * 2. DOUBLE CONFIGURATION CORS : ⚠️ Évitez d'avoir CORS configuré dans : -
     * CorsGatewayConfiguration.java (cette classe) - bootstrap.properties
     * (spring.cloud.gateway.globalcors.*) - Backend Spring Boot
     * (WebConfig.java)
     *
     * Choisissez UNE seule approche, sinon conflit possible
     *
     * 3. DEBUGGING : Pour tester si CORS fonctionne :
     *
     * curl -X OPTIONS \ -H "Origin: http://localhost:4200" \ -H
     * "Access-Control-Request-Method: POST" \ -H
     * "Access-Control-Request-Headers: Content-Type" \ -v
     * https://localhost:8081/gateway-proxy/api/service-biometrie/auth/users/login
     *
     * Réponse attendue : < Access-Control-Allow-Origin: http://localhost:4200 <
     * Access-Control-Allow-Credentials: true < Access-Control-Allow-Methods:
     * GET,POST,PUT,DELETE,OPTIONS,PATCH,HEAD
     *
     * 4. PRODUCTION : ❌ Ne PAS utiliser
     * addAllowedOriginPattern("http://localhost:*") ✅ Utiliser
     * addAllowedOrigin("https://votredomaine.com")
     *
     * 5. SÉCURITÉ : - allowCredentials + wildcard origins = INTERDIT - Toujours
     * spécifier les origines exactes en production - Limiter les headers
     * autorisés au strict nécessaire
     */


/**
 * ======================================== FAQ - Questions Fréquentes
 * ========================================
 *
 * Q: Pourquoi ma requête échoue avec "CORS policy" ? R: 1. Vérifiez que
 * l'origine est dans addAllowedOriginPattern 2. Vérifiez que la méthode est
 * dans addAllowedMethod 3. Vérifiez que les headers sont dans addAllowedHeader
 * 4. Vérifiez les logs Gateway pour voir la requête OPTIONS
 *
 * Q: Dois-je aussi configurer CORS dans le backend Spring Boot ? R: NON si la
 * Gateway gère déjà CORS Le backend ne voit QUE la Gateway, pas le frontend
 * CORS doit être configuré à la frontière (Gateway)
 *
 * Q: Quelle est la différence entre cette config et bootstrap.properties ? R:
 * Cette classe = Configuration Java programmatique (plus flexible)
 * bootstrap.properties = Configuration déclarative (plus simple) Choisissez
 * l'une ou l'autre, pas les deux
 *
 * Q: Pourquoi @Order(-1) ? R: Pour que CORS s'exécute AVANT les autres filtres
 * Sinon, une authentification pourrait bloquer OPTIONS
 *
 * Q: Que fait exactement maxAge(3600L) ? R: Cache la réponse preflight pendant
 * 1 heure Réduit le nombre de requêtes OPTIONS Améliore les performances
 */
