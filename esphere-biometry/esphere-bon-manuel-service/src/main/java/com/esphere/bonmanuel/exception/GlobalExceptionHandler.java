package com.esphere.bonmanuel.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BonManuelException.class)
    public ResponseEntity<Map<String, Object>> handle(BonManuelException ex) {
        return ResponseEntity.status(ex.getStatus()).body(Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status",    ex.getStatus(),
                "erreur",    ex.getMessage()
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