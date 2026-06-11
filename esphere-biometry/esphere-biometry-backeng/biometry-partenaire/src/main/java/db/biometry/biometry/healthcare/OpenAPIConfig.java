/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.healthcare;

/**
 *
 * @author USER01
 */
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration de la documentation API OpenAPI/Swagger
 * Définit les métadonnées et les schémas de sécurité
 * 
 * @author JIATOU FRANCK
 * @version 1.0
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API Gestion Couverture Santé - Espace Souscripteur",
                version = "1.0.0",
                description = "API REST pour la gestion de la couverture santé - Module Souscripteur. " +
                        "Cette API permet aux souscripteurs (entreprises/organisations) de gérer leurs " +
                        "adhérents, consulter les statistiques de consommation, générer des rapports et " +
                        "superviser leurs ayants droit.",
                contact = @Contact(
                        name = "JIATOU FRANCK",
                        email = "support@biometry-health.com"
                ),
                license = @License(
                        name = "Propriétaire",
                        url = "https://biometry-health.com/license"
                )
        ),
        servers = {
                @Server(
                        description = "Environnement de développement",
                        url = "http://localhost:9112"
                ),
                @Server(
                        description = "Environnement de production",
                        url = "https://api.biometry-health.com"
                )
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Authentification JWT via Bearer token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {
}
