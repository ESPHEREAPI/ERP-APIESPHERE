package com.esphere.media.exception;

public class MediaException extends RuntimeException {

    private final int status;

    public MediaException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}