/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;

/**
 *
 * @author USER01
 */
import db.biometry.biometry.dtos.ActivationResponse;
import db.biometry.biometry.dtos.CreateSubscriberRequest;
import db.biometry.biometry.dtos.PagedResponse;
import db.biometry.biometry.dtos.PolicyCheckResponse;
import db.biometry.biometry.dtos.SendActivationRequest;
import db.biometry.biometry.dtos.SetPasswordRequest;
import db.biometry.biometry.dtos.SubscriberResponse;
import db.biometry.biometry.dtos.UpdateSubscriberRequest;
import db.biometry.biometry.entite.ActivationLog;
import db.biometry.biometry.entite.Subscribers;
import db.biometry.biometry.enums.LogStatus;
import db.biometry.biometry.enums.PasswordMode;
import db.biometry.biometry.exceptions.DuplicateResourceException;
import db.biometry.biometry.exceptions.ResourceNotFoundException;
import db.biometry.biometry.exceptions.TokenExpiredException;
import db.biometry.biometry.repositories.ActivationLogRepository;
import db.biometry.biometry.repositories.PolicyRepository;
import db.biometry.biometry.repositories.SubscriberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubscriberServiceImpl implements SubscriberService {

    private final SubscriberRepository subscriberRepo;
    private final PolicyRepository policyRepo;
    private final ActivationLogRepository activationLogRepo;
    private final SubscriberMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    // ═══════════════════════════════════════════════════════════════════════
    // CREATE
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public SubscriberResponse create(CreateSubscriberRequest request) {

        // 1. Vérifier que la police existe
        var policy = policyRepo.findByPoliceValide(request.getPolicyNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                "Police introuvable : " + request.getPolicyNumber()));

        // 2. Contraintes d'unicité
        if (subscriberRepo.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email déjà utilisé : " + request.getEmail());
        }
        if (subscriberRepo.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Nom d'utilisateur déjà pris : " + request.getUsername());
        }
        if (subscriberRepo.existsByPolicyNumber(request.getPolicyNumber())) {
            throw new DuplicateResourceException("Un compte existe déjà pour la police : " + request.getPolicyNumber());
        }

        // 3. Construire l'entité
        Subscribers sub = mapper.toEntity(request);
        sub.setFullName(policy.getSouscripteur()); // Nom authoratif depuis la police
        sub.setEffetPolice(policy.getEffetPolice());
        sub.setEcheancePolice(policy.getEcheancePolice());

        // 4. Mot de passe selon le mode
        switch (request.getPasswordMode()) {
            case MANUAL -> {
                if (request.getPassword() == null || request.getPassword().isBlank()) {
                    throw new IllegalArgumentException("Le mot de passe est obligatoire en mode MANUAL");
                }
                sub.setPasswordHash(passwordEncoder.encode(request.getPassword()));
            }
            case ACTIVATION_LINK -> {
                // Le mot de passe sera défini par l'utilisateur via le lien
                sub.setPasswordHash(null);
                sub.setActive(false); // Compte inactif jusqu'à activation
            }
        }

        Subscribers saved = subscriberRepo.save(sub);
        log.info("✅ Souscripteur créé : id={} policy={}", saved.getId(), saved.getPolicyNumber());

        // 5. Envoi automatique du lien si mode activation
        if (request.getPasswordMode() == PasswordMode.ACTIVATION_LINK) {
            int duration = request.getActivationDurationHours() != null
                    ? request.getActivationDurationHours() : 48;
            generateAndSendActivationLink(saved, request.getEmail(), duration);
        } else {
            // Envoyer email de bienvenue
            mailService.sendWelcomeEmail(saved.getEmail(), saved.getFullName(), saved.getPolicyNumber(), saved.getUsername());
        }

        return mapper.toResponse(saved);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // READ ONE
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    public SubscriberResponse findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // READ PAGINATED
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    public PagedResponse<SubscriberResponse> findAll(
            String search, Boolean isActive, int page, int limit) {

        Pageable pageable = PageRequest.of(page - 1, limit,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Subscribers> result = subscriberRepo.findAllWithFilter(search, isActive, pageable);

        List<SubscriberResponse> data = result.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        return PagedResponse.of(data, result.getTotalElements(), page, limit);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // UPDATE
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public SubscriberResponse update(Long id, UpdateSubscriberRequest request) {
        Subscribers sub = getOrThrow(id);

        if (request.getPhoneNumber() != null) {
            sub.setPhoneNumber(request.getPhoneNumber());
        }

        if (request.getEmail() != null) {
            if (subscriberRepo.existsByEmailAndIdNot(request.getEmail(), id)) {
                throw new DuplicateResourceException("Email déjà utilisé : " + request.getEmail());
            }
            sub.setEmail(request.getEmail());
        }

        if (request.getUsername() != null) {
            if (subscriberRepo.existsByUsernameAndIdNot(request.getUsername(), id)) {
                throw new DuplicateResourceException("Nom d'utilisateur déjà pris : " + request.getUsername());
            }
            sub.setUsername(request.getUsername());
        }

        if (request.getActive() != null) {
            sub.setActive(request.getActive());
        }

        if (request.getPasswordMode() != null) {
            sub.setPasswordMode(request.getPasswordMode());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            sub.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getActivationDurationHours() != null) {
            sub.setActivationDurationHrs(request.getActivationDurationHours());
        }

        Subscribers saved = subscriberRepo.save(sub);
        log.info("📝 Souscripteur mis à jour : id={}", id);
        return mapper.toResponse(saved);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // DELETE
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public void delete(Long id) {
        Subscribers sub = getOrThrow(id);
        subscriberRepo.delete(sub);
        log.info("🗑️  Souscripteur supprimé : id={}", id);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TOGGLE STATUS
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public SubscriberResponse toggleStatus(Long id, boolean isActive) {
        getOrThrow(id); // vérifier existence
        subscriberRepo.updateStatus(id, isActive, Instant.now());
        log.info("🔄 Statut souscripteur id={} → {}", id, isActive ? "ACTIF" : "INACTIF");
        return mapper.toResponse(getOrThrow(id));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CHECK POLICY
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    public PolicyCheckResponse checkPolicy(String policyNumber) {
        String email = subscriberRepo
                .findByPolicyNumber(policyNumber)
                .map(Subscribers::getEmail)
                .orElse("");
        return policyRepo.findByPoliceValide(policyNumber, new Date())
                .map(p -> PolicyCheckResponse.builder()
                .policyNumber(p.getPolice())
                .fullName(p.getSouscripteur())
                .exists(true)
                .email(email)
                .alreadyHasAccount(subscriberRepo.existsByPolicyNumber(p.getPolice()))
                .build())
                .orElseGet(() -> PolicyCheckResponse.builder()
                .policyNumber(policyNumber)
                .exists(false)
                .alreadyHasAccount(false)
                .build());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // SEND ACTIVATION LINK
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public ActivationResponse sendActivationLink(Long subscriberId, SendActivationRequest request) {
        Subscribers sub = getOrThrow(subscriberId);
        generateAndSendActivationLink(sub, request.getEmail(), request.getDuration());
        return ActivationResponse.builder()
                .message("Lien d'activation envoyé à " + request.getEmail())
                .expiry(sub.getActivationTokenExpiry())
                .durationHours(request.getDuration())
                .build();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // SET PASSWORD VIA TOKEN (page publique)
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public SubscriberResponse setPasswordViaToken(SetPasswordRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Les mots de passe ne correspondent pas");
        }

        Subscribers sub = subscriberRepo.findByActivationToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Token d'activation invalide"));

        if (!sub.getActive() && Instant.now().isAfter(sub.getActivationTokenExpiry()) == true) {
            throw new TokenExpiredException("Le lien d'activation a expiré. Demandez un nouveau lien.");
        }

        // Définir le mot de passe et activer le compte
        sub.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        sub.setActive(true);
        sub.setAccountActivatedAt(Instant.now());

        // Invalider le token
        sub.setActivationToken(null);
        sub.setActivationTokenExpiry(null);

        // Marquer le log comme utilisé
        activationLogRepo.findByToken(request.getToken()).ifPresent(log -> {
            log.setStatus(LogStatus.USED);
            log.setUsedAt(Instant.now());
            activationLogRepo.save(log);
        });

        Subscribers saved = subscriberRepo.save(sub);
        log.info("🔓 Compte activé via token : id={}", saved.getId());

        // Email de bienvenue
        mailService.sendWelcomeEmail(saved.getEmail(), saved.getFullName(), saved.getPolicyNumber(), saved.getUsername());

        return mapper.toResponse(saved);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // RESET PASSWORD (admin)
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    @Transactional
    public SubscriberResponse resetPassword(Long id, String newPassword) {
        Subscribers sub = getOrThrow(id);
        sub.setPasswordHash(passwordEncoder.encode(newPassword));
        Subscribers saved = subscriberRepo.save(sub);
        log.info("🔑 Mot de passe réinitialisé pour id={}", id);
        mailService.sendPasswordResetNotification(saved.getEmail(), saved.getFullName());
        return mapper.toResponse(saved);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // USERNAME AVAILABILITY
    // ═══════════════════════════════════════════════════════════════════════
    @Override
    public boolean isUsernameAvailable(String username, Long excludeId) {
        return subscriberRepo.isUsernameAvailable(username, excludeId);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRIVATE HELPERS
    // ═══════════════════════════════════════════════════════════════════════
    private Subscribers getOrThrow(Long id) {
        return subscriberRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Souscripteur", id));
    }
  private Subscribers getOrThrow(String email) {
        return subscriberRepo.findByEmail(email)
                 .orElseThrow(() -> new ResourceNotFoundException("Souscripteur", email));
    }
    /**
     * Génère un token sécurisé, met à jour l'entité et envoie l'email de façon
     * asynchrone. Enregistre aussi un log d'activation.
     */
    private void generateAndSendActivationLink(Subscribers sub, String email, int durationHours) {
        String token = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        Instant expiry = Instant.now().plus(durationHours, ChronoUnit.HOURS);

        sub.setActivationToken(token);
        sub.setActivationTokenExpiry(expiry);
        sub.setActivationSentAt(Instant.now());
        sub.setActivationDurationHrs(durationHours);
        subscriberRepo.save(sub);

        // Log d'activation
        ActivationLog logEntry = ActivationLog.builder()
                .subscriber(sub)
                .email(email)
                .token(token)
                .expiresAt(expiry)
                .status(LogStatus.PENDING)
                .build();
        activationLogRepo.save(logEntry);

        // Envoi asynchrone (non bloquant) — le reste de la requête continue
        mailService.sendActivationLink(email, sub.getFullName(), token, expiry, durationHours)
                .exceptionally(ex -> {
                    log.error("❌ Échec envoi lien activation pour sub id={} : {}", sub.getId(), ex.getMessage());
                    activationLogRepo.findByToken(token).ifPresent(l -> {
                        l.setStatus(LogStatus.FAILED);
                        l.setErrorMessage(ex.getMessage());
                        activationLogRepo.save(l);
                    });
                    return null;
                });

        log.info("📨 Lien activation généré pour sub id={} — expire à {}", sub.getId(), expiry);
    }

    @Override
    public SubscriberResponse resetPassword(String email, String newPassword) {
    Subscribers sub = getOrThrow(email);
        sub.setPasswordHash(passwordEncoder.encode(newPassword));
        Subscribers saved = subscriberRepo.save(sub);
        log.info("🔑 Mot de passe réinitialisé pour id={}", sub.getId());
        mailService.sendPasswordResetNotification(saved.getEmail(), saved.getFullName());
        return mapper.toResponse(saved); 
    
    }

}
