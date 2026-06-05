package com.esphere.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void envoyer(String destinataire, String sujet, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage, true, "UTF-8");

            helper.setFrom("info@zenitheinsurance.com");
            helper.setTo(destinataire);
            helper.setSubject(sujet);
            helper.setText(formaterHtml(message), true);

            mailSender.send(mimeMessage);
            log.info("Email envoyé à : {}", destinataire);

        } catch (MessagingException e) {
            log.error("Erreur envoi email à {} : {}", destinataire, e.getMessage());
            throw new RuntimeException("Échec envoi email : " + e.getMessage());
        }
    }

    private String formaterHtml(String message) {
        return """
            <html>
            <body style="font-family: Arial, sans-serif; padding: 20px;">
                <div style="max-width: 600px; margin: auto; border: 1px solid #e0e0e0;
                            border-radius: 8px; padding: 30px;">
                    <div style="text-align: center; margin-bottom: 20px;">
                        <h2 style="color: #1a5276;">ZENITHE INSURANCE</h2>
                        <h3 style="color: #2874a6;">Plateforme Biométrie</h3>
                    </div>
                    <div style="color: #333; line-height: 1.6;">
                        %s
                    </div>
                    <hr style="margin-top: 30px; border-color: #e0e0e0;">
                    <p style="color: #888; font-size: 12px; text-align: center;">
                        Ce message est généré automatiquement.<br>
                        Zenithe Insurance — Douala, Cameroun
                    </p>
                </div>
            </body>
            </html>
            """.formatted(message.replace("\n", "<br>"));
    }
}