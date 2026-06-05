package com.esphere.auth.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * Composant responsable de la génération et validation des tokens JWT.
 *
 * Le token JWT contient :
 *   - sub        : login de l'utilisateur
 *   - userId     : id de l'utilisateur
 *   - profilCode : code du profil (SUP_ADMIN, SERVICE_SANTE, etc.)
 *   - prestId    : id du prestataire (null si agent SS ou admin)
 *   - appli      : connexion_appli (biometry | admin)
 *   - iat        : date d'émission
 *   - exp        : date d'expiration
 */
@Slf4j
@Component
public class JwtProvider {

    @Value("${esphere.jwt.secret}")
    private String jwtSecret;

    @Value("${esphere.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Génère un token JWT pour un utilisateur authentifié.
     */
    public String generateToken(Integer userId,
                                String login,
                                String profilCode,
                                String prestataireId,
                                String connexionAppli) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

        return Jwts.builder()
                .subject(login)
                .claims(Map.of(
                        "userId",     userId,
                        "profilCode", profilCode,
                        "prestId",    prestataireId != null ? prestataireId : "",
                        "appli",      connexionAppli
                ))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key)
                .compact();
    }

    /**
     * Valide un token JWT.
     * @return true si le token est valide et non expiré
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("Token JWT expiré : {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.warn("Token JWT malformé : {}", e.getMessage());
        } catch (Exception e) {
            log.warn("Token JWT invalide : {}", e.getMessage());
        }
        return false;
    }

    /**
     * Extrait les claims d'un token valide.
     */
    public Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    

    public String getLogin(String token) {
        return getClaims(token).getSubject();
    }

    public Integer getUserId(String token) {
        return getClaims(token).get("userId", Integer.class);
    }

    public String getProfilCode(String token) {
        return getClaims(token).get("profilCode", String.class);
    }

    public String getPrestataireId(String token) {
        String prestId = getClaims(token).get("prestId", String.class);
        return (prestId == null || prestId.isBlank()) ? null : prestId;
    }

    public String getConnexionAppli(String token) {
        return getClaims(token).get("appli", String.class);
    }
    
    /**
 * Extrait les claims même si le token est expiré.
 * Utilisé uniquement pour le refresh.
 */
private Claims getClaimsIgnoreExpiry(String token) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    try {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    } catch (ExpiredJwtException e) {
        // Token expiré mais signature valide → on récupère quand même les claims
        return e.getClaims();
    }
}

/**
 * Valide la signature du token même s'il est expiré.
 * Retourne false uniquement si le token est altéré/malformé.
 */
public boolean validateTokenIgnoreExpiry(String token) {
    try {
        getClaimsIgnoreExpiry(token);
        return true;
    } catch (MalformedJwtException e) {
        log.warn("Token JWT malformé (refresh refusé) : {}", e.getMessage());
        return false;
    } catch (Exception e) {
        log.warn("Token JWT invalide (refresh refusé) : {}", e.getMessage());
        return false;
    }
}

/**
 * Génère un nouveau token à partir d'un ancien (expiré ou non).
 * La signature est vérifiée — le contenu est relu depuis les claims.
 */
public String refreshToken(String oldToken) {
    Claims claims = getClaimsIgnoreExpiry(oldToken);

    Integer userId        = claims.get("userId",     Integer.class);
    String  login         = claims.getSubject();
    String  profilCode    = claims.get("profilCode", String.class);
    String  prestId       = claims.get("prestId",    String.class);
    String  connexionAppli = claims.get("appli",     String.class);

    String prestataireId = (prestId == null || prestId.isBlank()) ? null : prestId;

    return generateToken(userId, login, profilCode, prestataireId, connexionAppli);
}

// Ajouter dans JwtProvider.java

public String getLoginIgnoreExpiry(String token) {
    return getClaimsIgnoreExpiry(token).getSubject();
}
}
