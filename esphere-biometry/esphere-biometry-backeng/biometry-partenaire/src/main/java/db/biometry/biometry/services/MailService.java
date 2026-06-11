/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.services;

/**
 *
 * @author USER01
 */
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * Service d'envoi d'emails asynchrone.
 *
 * Stratégie choisie : Spring @Async + JavaMailSender
 * ───────────────────────────────────────────────── ✅ Aucun broker externe
 * requis (vs JMS/RabbitMQ) ✅ Thread pool configurable via spring.task.execution
 * ✅ Templates HTML Thymeleaf riches ✅ Retry manuel possible via
 * CompletableFuture.exceptionally() ✅ Upgrade facile → Spring Integration ou
 * RabbitMQ si volume ↑
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String from;

    @Value("${app.mail.from-name}")
    private String fromName;

    @Value("${app.activation.base-url}")
    private String baseUrl;

    @Value("${app.activation.path}")
    private String activationPath;
    
    @Value("${mail.logo.url}")
private String logoUrl;

    private static final DateTimeFormatter FORMATTER
            = DateTimeFormatter.ofPattern("dd/MM/yyyy 'à' HH:mm")
                    .withZone(ZoneId.of("Africa/Douala"));

    // ── Envoi du lien d'activation ────────────────────────────────────────────
    /**
     * Envoie le lien d'activation par email de façon asynchrone. Retourne un
     * CompletableFuture pour suivre le résultat.
     */
    @Async
    public CompletableFuture<Void> sendActivationLink(
            String recipientEmail,
            String recipientName,
            String token,
            Instant expiry,
            long durationHours) {

        String activationUrl = baseUrl + activationPath + "?token=" + token;
        String expiryFormatted = FORMATTER.format(expiry);
        String pathimage = getImagesLogo();

        Context ctx = new Context(Locale.FRENCH);
        ctx.setVariable("recipientName", recipientName);
        ctx.setVariable("activationUrl", activationUrl);
        ctx.setVariable("expiryFormatted", expiryFormatted);
        ctx.setVariable("durationHours", durationHours);
        ctx.setVariable("fromName", fromName);
        //ctx.setVariable("logoBase64", "data:image/png;base64," + pathimage);
         ctx.setVariable("logoUrl", logoUrl);  // ← simple URL, plus de base64
        String htmlBody = templateEngine.process("email/activation-link", ctx);

        return sendHtmlEmail(
                recipientEmail,
                "🔐 Activez votre compte " + fromName,
                htmlBody
        );
    }

    // ── Envoi de notification de création de compte ───────────────────────────
    @Async
    public CompletableFuture<Void> sendWelcomeEmail(
            String recipientEmail,
            String recipientName,
            String username, String titulaire) {
        String pathimage = getImagesLogo();
        Context ctx = new Context(Locale.FRENCH);
        ctx.setVariable("recipientName", recipientName);
        ctx.setVariable("username", username);
        ctx.setVariable("titulaire", titulaire);
        ctx.setVariable("loginUrl", baseUrl + "/login");
        ctx.setVariable("fromName", fromName);
        //ctx.setVariable("logoBase64", "data:image/png;base64," + pathimage);
         ctx.setVariable("logoUrl", logoUrl);  // ← simple URL, plus de base64
        String htmlBody = templateEngine.process("email/welcome", ctx);

        return sendHtmlEmail(
                recipientEmail,
                "✅ Bienvenue sur " + fromName + " !",
                htmlBody
        );
    }

    // ── Envoi de réinitialisation de mot de passe (admin) ────────────────────
    @Async
    public CompletableFuture<Void> sendPasswordResetNotification(
            String recipientEmail,
            String recipientName) {
        String pathimage = getImagesLogo();

        Context ctx = new Context(Locale.FRENCH);
        ctx.setVariable("recipientName", recipientName);
        ctx.setVariable("loginUrl", baseUrl + "/login");
        ctx.setVariable("fromName", fromName);
         ctx.setVariable("logoUrl", logoUrl);  // ← simple URL, plus de base64
       // ctx.setVariable("logoBase64", "data:image/png;base64," + pathimage);

        String htmlBody = templateEngine.process("email/password-reset", ctx);

        return sendHtmlEmail(
                recipientEmail,
                "🔑 Votre mot de passe a été modifié — " + fromName,
                htmlBody
        );
    }

    public String getImagesLogo() {
        try {
            InputStream imageStream = getClass()
                    .getClassLoader()
                    .getResourceAsStream("static/images/logo.png");
            String base64 = Base64.getEncoder()
                    .encodeToString(imageStream.readAllBytes());
            return base64;
        } catch (IOException e) {
            return "";
        }
    }

    // ── Core : envoi générique asynchrone ────────────────────────────────────
    @Async
    public CompletableFuture<Void> sendHtmlEmail(
            String to, String subject, String htmlContent) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from, fromName);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = HTML

            mailSender.send(message);
            log.info("✉️  Email envoyé à {} — sujet: {}", to, subject);
            return CompletableFuture.completedFuture(null);

        } catch (MailException | MessagingException | java.io.UnsupportedEncodingException ex) {
            log.error("❌ Échec envoi email à {} : {}", to, ex.getMessage(), ex);
            // On ne propage pas l'exception pour ne pas bloquer le thread métier
            CompletableFuture<Void> failed = new CompletableFuture<>();
            failed.completeExceptionally(ex);
            return failed;
        }
    }

}
