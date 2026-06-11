/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package db.biometry.biometry;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import org.springframework.stereotype.Component;

/**
 *
 * @author USER01
 */
@Component
@ConfigurationProperties("app")
@RefreshScope
@Data
public class ApplicationPropertiesConfiguration {
 private int limitDeProduits;
 //private Long jwtExpirationMs;
 //private String jwtSecret; 
  private Jwt jwt = new Jwt(); // ← initialiser pour éviter NPE
 

@Data
    public static class Jwt {
        private String secret;
        private Long expirationMs;
    }// ← manquait
}
