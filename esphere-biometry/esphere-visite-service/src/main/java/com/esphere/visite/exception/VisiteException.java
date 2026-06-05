package com.esphere.visite.exception;

public class VisiteException extends RuntimeException {

    private final int status;

    public VisiteException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}