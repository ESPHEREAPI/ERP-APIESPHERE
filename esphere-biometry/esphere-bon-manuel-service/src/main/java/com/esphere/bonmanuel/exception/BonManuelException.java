package com.esphere.bonmanuel.exception;

public class BonManuelException extends RuntimeException {

    private final int status;

    public BonManuelException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}