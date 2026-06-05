package com.esphere.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class OtpStore {

    private static final long OTP_TTL_SECONDS = 1800; // 30 minutes

    // ── Record public pour accès depuis AuthService ───────
    public record OtpEntry(
        String prestataireId,
        String codeVisite,
        String annee,
        String naturePrestation,
        Instant expiresAt
    ) {}

    private final Map<String, OtpEntry> store =
        new ConcurrentHashMap<>();

    public void save(String otp,
                     String prestataireId,
                     String codeVisite,
                     String annee,
                     String naturePrestation) {

        // Nettoyage des OTP expirés avant chaque ajout
        store.entrySet().removeIf(e ->
            e.getValue().expiresAt().isBefore(Instant.now()));

        store.put(otp, new OtpEntry(
            prestataireId,
            codeVisite,
            annee,
            naturePrestation,
            Instant.now().plusSeconds(OTP_TTL_SECONDS)
        ));

        log.debug("OTP enregistré — prestataire={} visite={}",
                  prestataireId, codeVisite);
    }

    public OtpEntry consumeIfValid(String otp) {
        OtpEntry entry = store.remove(otp); // usage unique
        if (entry == null) {
            log.warn("OTP inconnu ou déjà utilisé : {}", otp);
            return null;
        }
        if (entry.expiresAt().isBefore(Instant.now())) {
            log.warn("OTP expiré pour prestataire={}",
                     entry.prestataireId());
            return null;
        }
        return entry;
    }
}