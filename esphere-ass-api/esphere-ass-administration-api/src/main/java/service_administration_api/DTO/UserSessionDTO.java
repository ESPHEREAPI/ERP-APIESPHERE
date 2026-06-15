/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import service_administration_api.entite.pooltpv.Infos_AdministrateurAgencePayLoad;
import service_administration_api.enums.ProfilType;

/**
 * DTO pour la session utilisateur après authentification
 * ✅ Compatible avec le frontend Angular
 * ✅ expiresAt en long (timestamp millisecondes)
 */

/**
 *
 * @author USER01
 */
@NoArgsConstructor

@Data
public class UserSessionDTO {
     /**
     * Informations de l'utilisateur
     */
    @JsonProperty("userDTO")
    private UserDTO userDTO;
     @JsonProperty("userapiasac")
    private String userapiasac;
    
    /**
     * Token JWT d'accès
     */
    @JsonProperty("token")
    private String token;
      @JsonProperty("profilType")
    private ProfilType  profilType;
    /**
     * Token de rafraîchissement
     */
    @JsonProperty("refreshToken")
    private String refreshToken;
    
    /**
     * ✅ IMPORTANT : Date d'expiration en timestamp (millisecondes)
     * Format attendu par Angular : 1705147200000
     */
    @JsonProperty("expiresAt")
    // ⭐ Utiliser Date au lieu de LocalDateTime
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date expiresAt;
    private String agencyName;
    private String agencyCode;
    private String companyName;
    private String profilAgent;
    private Boolean canEdit;
    
    public UserSessionDTO(UserDTO userDTO, String token, String refreshToken, Date expiresAt,String userapiasac) {
        this.userDTO = userDTO;
        this.token = token;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
        this.userapiasac=userapiasac;
    }
    
    /**
     * Vérifier si la session est expirée
     */
    public boolean isExpired() {
        return System.currentTimeMillis() > (this.expiresAt.getTime());
    }
    
    /**
     * Obtenir le temps restant avant expiration (en secondes)
     */
    public long getTimeUntilExpiration() {
        long remaining = this.expiresAt.getTime() - System.currentTimeMillis();
        return Math.max(0, remaining / 1000);
    }
}
