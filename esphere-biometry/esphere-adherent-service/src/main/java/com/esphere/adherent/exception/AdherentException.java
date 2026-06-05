package com.esphere.adherent.exception;

public class AdherentException extends RuntimeException {

    private final int status;

    public AdherentException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}