package com.esphere.auth.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/**
 * Reproduction exacte de l'algorithme de hachage existant.
 *
 * ALGORITHME loginBiometrie :
 *   1. MD5  ( "RS_" + password + "-er" )
 *   2. SHA1 ( résultat_MD5 )
 *
 * Ce code est intentionnellement identique à l'original pour garantir
 * la compatibilité avec tous les mots de passe déjà en base de données.
 *
 * IMPORTANT : Ne pas modifier cet algorithme sans prévoir une migration
 * des mots de passe existants en base. Voir EspherePlanMigrationBcrypt
 * pour la future migration vers BCrypt (v2 - amélioration AM-01 sécurité).
 */
public final class CryptoLegacy {

    // Classe utilitaire — pas d'instanciation
    private CryptoLegacy() {}

    /**
     * Hash SHA-256 d'une chaîne (conservé pour compatibilité, non utilisé pour les mots de passe)
     */
    public static String sha256(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Erreur SHA-256", ex);
        }
    }

    /**
     * Hash SHA-1 d'une chaîne
     */
    public static String sha1(String base) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(base.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Erreur SHA-1", ex);
        }
    }

    /**
     * Hash MD5 d'une chaîne
     */
    public static String cryptoMD5(String wordToHash) {
        StringBuilder hexString = new StringBuilder();
        try {
            byte[] hash = MessageDigest.getInstance("MD5")
                    .digest(wordToHash.getBytes(StandardCharsets.UTF_8));
            for (byte b : hash) {
                String hex = Integer.toHexString(b);
                if (hex.length() == 1) {
                    hexString.append('0');
                    hexString.append(hex.charAt(hex.length() - 1));
                } else {
                    hexString.append(hex, hex.length() - 2, hex.length());
                }
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Erreur MD5", ex);
        }
    }

    /**
     * Algorithme principal utilisé pour les mots de passe ESPHERE.
     *
     * Étape 1 : MD5("RS_" + password + "-er")
     * Étape 2 : SHA1(résultat étape 1)
     *
     * @param password Le mot de passe en clair saisi par l'utilisateur
     * @return Le hash à comparer avec le champ mot_passe en base
     */
    public static String loginBiometrie(String password) {
        String crypMd5 = cryptoMD5("RS_" + password + "-er");
        return sha1(crypMd5);
    }

    /**
     * Vérifie si un mot de passe en clair correspond au hash stocké en base.
     *
     * @param rawPassword    Mot de passe saisi par l'utilisateur
     * @param hashedPassword Hash stocké dans dbx45ty_utilisateur.mot_passe
     * @return true si les mots de passe correspondent
     */
    public static boolean matches(String rawPassword, String hashedPassword) {
        if (rawPassword == null || hashedPassword == null) return false;
        return loginBiometrie(rawPassword).equals(hashedPassword);
    }
}
