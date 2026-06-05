package com.esphere.auth.exception;

/**
 * Exception métier pour les erreurs d'authentification.
 */
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
