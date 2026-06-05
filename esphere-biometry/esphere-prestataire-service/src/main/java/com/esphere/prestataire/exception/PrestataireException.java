package com.esphere.prestataire.exception;

public class PrestataireException extends RuntimeException {

    private final int status;

    public PrestataireException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}