package db.biometry.biometry.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Utilitaire de génération JWT pour biometry-partenaire.
 *
 * SEUL CHANGEMENT par rapport à l'original :
 *   - Avant : Keys.secretKeyFor(HS512) → clé ALÉATOIRE à chaque démarrage
 *   - Après : Keys.hmacShaKeyFor(secret.getBytes()) → clé FIXE = même que Gateway
 *
 * Le reste du code est strictement identique à l'original.
 * Aucun impact sur la logique métier.
 */
@Component
public class JwtExpiration {

    // Secret injecté depuis application.yml — même valeur que Gateway
    private static String staticSecret;

    @Value("${app.jwt.secret}")
    public void setStaticSecret(String secret) {
        JwtExpiration.staticSecret = secret;
    }

    // ── Inchangé par rapport à l'original ────────────────────────────────────

    public static Date expiresAt(long jwtExpirationMs) {
        Instant now = Instant.now();
        Instant expirationInstant = now.plusMillis(jwtExpirationMs);
        LocalDateTime expirationDateTime = LocalDateTime.ofInstant(
                expirationInstant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = expirationDateTime.format(formatter);
        SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatterDate.parse(formattedDate);
        } catch (ParseException e) {
            System.out.println("Erreur lors de la conversion de la date : " + e.getMessage());
        }
        return new Date();
    }

    public static String generateJwtToken(String username, Date expiryDate) {
        return generateJwtToken(username, expiryDate, null);
    }

    public static String generateJwtToken(String username, Date expiryDate, String profilCode) {
        Date now = new Date();
        var builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate);
        if (profilCode != null && !profilCode.isBlank()) {
            builder.claim("profilCode", profilCode);
        }
        return builder.signWith(getKey()).compact();
    }

    // ── Clé fixe dérivée du secret — identique à Gateway JwtProvider.getKey() ─

    private static SecretKey getKey() {
        if (staticSecret == null) {
            throw new IllegalStateException(
                "[JwtExpiration] app.jwt.secret non chargé — vérifier application.yml");
        }
        // Keys.hmacShaKeyFor(secret.getBytes()) = exactement ce que fait la Gateway
        return Keys.hmacShaKeyFor(staticSecret.getBytes(StandardCharsets.UTF_8));
    }
}