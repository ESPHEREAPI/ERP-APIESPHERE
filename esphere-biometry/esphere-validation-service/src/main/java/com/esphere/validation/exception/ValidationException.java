package com.esphere.validation.exception;

public class ValidationException extends RuntimeException {

    private final int status;

    public ValidationException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}