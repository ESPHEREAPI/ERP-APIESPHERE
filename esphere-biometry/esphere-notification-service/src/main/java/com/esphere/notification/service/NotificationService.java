package com.esphere.notification.service;

import com.esphere.notification.dto.request.NotificationRequest;
import com.esphere.notification.dto.request.SmsManuelRequest;
import com.esphere.notification.dto.response.NotificationResponse;
import com.esphere.notification.entity.Notification;
import com.esphere.notification.exception.NotificationException;
import com.esphere.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SmsService             smsService;
    private final EmailService           emailService;

    // ── ENVOI AUTOMATIQUE ─────────────────────────────────────────

    @Transactional
    public void envoyerAuto(
            String destinataireId,
            String typeDest,
            String canal,
            String sujet,
            String message,
            String telephone,
            String emailDest,
            String eventType,
            String referenceId) {

        Notification notif = Notification.builder()
                .destinataireId(destinataireId)
                .typeDest(typeDest)
                .canal(canal)
                .sujet(sujet)
                .message(message)
                .telephone(telephone)
                .emailDest(emailDest)
                .statut("en_attente")
                .eventType(eventType)
                .referenceId(referenceId)
                .lu(false)
                .dateCreation(LocalDateTime.now())
                .build();

        try {
            switch (canal) {
                case "sms" -> {
                    if (telephone != null && !telephone.isBlank()) {
                        smsService.envoyer(telephone, message);
                        notif.setStatut("envoye");
                        notif.setDateEnvoi(LocalDateTime.now());
                    }
                }
                case "email" -> {
                    if (emailDest != null && !emailDest.isBlank()) {
                        emailService.envoyer(emailDest, sujet, message);
                        notif.setStatut("envoye");
                        notif.setDateEnvoi(LocalDateTime.now());
                    }
                }
                case "alerte" -> {
                    // Alertes in-app : juste enregistrer en base
                    notif.setStatut("envoye");
                    notif.setDateEnvoi(LocalDateTime.now());
                }
            }
        } catch (Exception e) {
            notif.setStatut("echec");
            notif.setErreur(e.getMessage());
            log.error("Échec envoi notification {} → {} : {}",
                    canal, destinataireId, e.getMessage());
        }

        notificationRepository.save(notif);
    }

    // ── ENVOI MANUEL PAR AGENT SS ─────────────────────────────────

    @Transactional
    public NotificationResponse envoyerSmsManuel(SmsManuelRequest request) {

        Notification notif = Notification.builder()
                .destinataireId(request.getTelephone())
                .typeDest("assure")
                .canal("sms")
                .message(request.getMessage())
                .telephone(request.getTelephone())
                .statut("en_attente")
                .eventType("manuel")
                .referenceId(request.getReferenceId())
                .lu(false)
                .envoyePar(request.getEnvoyePar())
                .dateCreation(LocalDateTime.now())
                .build();

        try {
            smsService.envoyer(request.getTelephone(), request.getMessage());
            notif.setStatut("envoye");
            notif.setDateEnvoi(LocalDateTime.now());
            log.info("SMS manuel envoyé par employé {} au {}",
                    request.getEnvoyePar(), request.getTelephone());
        } catch (Exception e) {
            notif.setStatut("echec");
            notif.setErreur(e.getMessage());
        }

        notificationRepository.save(notif);
        return toResponse(notif);
    }

    // ── ENVOI GENERAL (depuis d'autres services) ──────────────────

    @Transactional
    public NotificationResponse envoyer(NotificationRequest request) {

        Notification notif = Notification.builder()
                .destinataireId(request.getDestinataireId())
                .typeDest(request.getTypeDest())
                .canal(request.getCanal())
                .sujet(request.getSujet())
                .message(request.getMessage())
                .telephone(request.getTelephone())
                .emailDest(request.getEmailDest())
                .statut("en_attente")
                .eventType(request.getEventType() != null
                        ? request.getEventType() : "manuel")
                .referenceId(request.getReferenceId())
                .lu(false)
                .envoyePar(request.getEnvoyePar())
                .dateCreation(LocalDateTime.now())
                .build();

        try {
            switch (request.getCanal()) {
                case "sms" -> {
                    smsService.envoyer(request.getTelephone(), request.getMessage());
                    notif.setStatut("envoye");
                    notif.setDateEnvoi(LocalDateTime.now());
                }
                case "email" -> {
                    emailService.envoyer(request.getEmailDest(),
                            request.getSujet(), request.getMessage());
                    notif.setStatut("envoye");
                    notif.setDateEnvoi(LocalDateTime.now());
                }
                case "alerte" -> {
                    notif.setStatut("envoye");
                    notif.setDateEnvoi(LocalDateTime.now());
                }
            }
        } catch (Exception e) {
            notif.setStatut("echec");
            notif.setErreur(e.getMessage());
        }

        notificationRepository.save(notif);
        return toResponse(notif);
    }

    // ── ALERTES IN-APP ────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<NotificationResponse> getAlertes(String destinataireId) {
        return notificationRepository.findAlertes(destinataireId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getAlerteNonLues(String destinataireId) {
        return notificationRepository.findAlerteNonLues(destinataireId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long compterNonLues(String destinataireId) {
        return notificationRepository.compterNonLues(destinataireId);
    }

    @Transactional
    public void marquerCommeLu(Integer id) {
        notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationException(
                        "Notification introuvable : " + id, 404));
        notificationRepository.marquerCommeLu(id);
    }

    @Transactional
    public void marquerToutesCommeLues(String destinataireId) {
        notificationRepository.marquerToutesCommeLues(destinataireId);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getHistorique(String destinataireId) {
        return notificationRepository.findByDestinataire(destinataireId)
                .stream().map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ── Messages pré-formatés ─────────────────────────────────────

    public String messagePrestation(String etat, String codeAdherent,
            String prestataireId, double montant) {
        return switch (etat) {
            case "valide" -> """
                Bonjour,
                La prestation pour l'assuré %s a été VALIDÉE.
                Montant : %.0f FCFA
                Vous pouvez procéder à l'encaissement.
                Plateforme Biométrie - ZENITHE INSURANCE
                """.formatted(codeAdherent, montant);
            case "rejete" -> """
                Bonjour,
                La prestation pour l'assuré %s a été REJETÉE.
                Veuillez contacter le service santé pour plus d'informations.
                Plateforme Biométrie - ZENITHE INSURANCE
                """.formatted(codeAdherent);
            default -> "Notification prestation - Plateforme Biométrie ZENITHE";
        };
    }

    public String messageBonManuel(String etat, String reference,
            double montantConfirme) {
        return switch (etat) {
            case "confirme" -> """
                Bonjour,
                Votre bon manuel %s a été CONFIRMÉ.
                Montant confirmé : %.0f FCFA
                L'assuré peut se présenter avec ce bon.
                Plateforme Biométrie - ZENITHE INSURANCE
                """.formatted(reference, montantConfirme);
            case "rejete" -> """
                Bonjour,
                Votre bon manuel %s a été REJETÉ.
                Veuillez contacter le service santé pour plus d'informations.
                Plateforme Biométrie - ZENITHE INSURANCE
                """.formatted(reference);
            default -> "Notification bon manuel - Plateforme Biométrie ZENITHE";
        };
    }

    // ── Mapper ───────────────────────────────────────────────────

    private NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .id(n.getId())
                .destinataireId(n.getDestinataireId())
                .typeDest(n.getTypeDest())
                .canal(n.getCanal())
                .sujet(n.getSujet())
                .message(n.getMessage())
                .statut(n.getStatut())
                .eventType(n.getEventType())
                .referenceId(n.getReferenceId())
                .lu(n.getLu())
                .envoyePar(n.getEnvoyePar())
                .dateLecture(n.getDateLecture())
                .dateEnvoi(n.getDateEnvoi())
                .dateCreation(n.getDateCreation())
                .erreur(n.getErreur())
                .build();
    }
}