/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;

/**
 *
 * @author USER01
 */
import db.biometry.biometry.repositories.ActivationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Tâche planifiée : marque les tokens d'activation expirés en base.
 * Fréquence : toutes les 30 minutes.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenCleanupScheduler {

    private final ActivationLogRepository activationLogRepo;

    @Scheduled(fixedDelay = 30 * 60 * 1000) // toutes les 30 min
    @Transactional
    public void expireOldTokens() {
        int count = activationLogRepo.expireOldTokens(Instant.now());
        if (count > 0) {
            log.info("⏰ {} token(s) d'activation marqué(s) expiré(s)", count);
        }
    }
    
}
