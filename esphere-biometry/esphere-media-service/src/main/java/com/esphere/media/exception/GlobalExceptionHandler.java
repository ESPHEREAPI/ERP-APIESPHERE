package com.esphere.media.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MediaException.class)
    public ResponseEntity<Map<String, Object>> handle(MediaException ex) {
        return ResponseEntity.status(ex.getStatus()).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status",    ex.getStatus(),
                "erreur",    ex.getMessage()
        ));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleSize(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(400).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status",    400,
                "erreur",    "Fichier trop volumineux. Maximum autorisé : 100 MB."
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Erreur interne : {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status",    500,
                "erreur",    "Erreur interne. Contactez l'administrateur."
        ));
    }
}