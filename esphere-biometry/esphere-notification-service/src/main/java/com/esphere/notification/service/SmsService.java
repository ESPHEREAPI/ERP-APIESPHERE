package com.esphere.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Service
public class SmsService {

    @Value("${sms.api.url:https://smsvas.com/bulk/public/index.php/api/v1/sendsms}")
    private String smsApiUrl;

    @Value("${sms.api.user:info@zenitheinsurance.com}")
    private String smsUser;

    @Value("${sms.api.password:biometrie2023}")
    private String smsPassword;

    @Value("${sms.api.senderid:ZENITHE}")
    private String smsSenderId;

    @Value("${esphere.alerte-sms.telephones:694923568,674032771}")
    private String alerteTelephones;

    @Value("${esphere.alerte-sms.seuil:50}")
    private int alerteSeuil;

    private final RestTemplate restTemplate = new RestTemplate();
    private int dernierSolde = -1;
    private boolean alerteSoldeEnvoyee = false;

    public int getSolde() { return dernierSolde; }

    public boolean soldeDisponible() {
        return dernierSolde != 0;
    }

    @Async
    public void envoyer(String telephone, String message) {
        try {
            String telFormate = formaterTelephone(telephone);

            String url = UriComponentsBuilder
                    .fromHttpUrl(smsApiUrl)
                    .queryParam("user",     smsUser)
                    .queryParam("password", smsPassword)
                    .queryParam("senderid", smsSenderId)
                    .queryParam("mobiles",  telFormate)
                    .queryParam("sms",      message)
                    .build()
                    .toUriString();

            Map response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                // Vérifier la réponse de l'API SMS
                java.util.List smsList = (java.util.List) response.get("sms");
                if (smsList != null && !smsList.isEmpty()) {
                    Map smsResult = (Map) smsList.get(0);
                    String status = String.valueOf(smsResult.get("status"));
                    String errorDesc = String.valueOf(smsResult.get("errordescription"));
                    int balance = smsResult.get("balance") != null ? ((Number) smsResult.get("balance")).intValue() : -1;

                    if ("error".equalsIgnoreCase(status)) {
                        dernierSolde = balance;
                        log.error("❌ SMS ÉCHEC vers {} — {} (solde: {})", telFormate, errorDesc, balance);
                        if ("Balance not enough".equalsIgnoreCase(errorDesc)) {
                            log.error("🚨 SOLDE SMS ÉPUISÉ ! Rechargez le compte smsvas.com");
                        }
                        throw new RuntimeException("SMS non envoyé : " + errorDesc);
                    }

                    dernierSolde = balance;
                    log.info("✅ SMS envoyé à {} | solde restant: {}", telFormate, balance);

                    if (balance >= 0 && balance < alerteSeuil) {
                        log.warn("⚠️ SOLDE SMS FAIBLE : {} SMS restants (seuil: {})", balance, alerteSeuil);
                        envoyerAlerteSolde(balance);
                    } else {
                        alerteSoldeEnvoyee = false;
                    }
                } else {
                    log.info("SMS envoyé à {} | Réponse : {}", telFormate, response.get("responsedescription"));
                }
            }

        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            log.error("Erreur envoi SMS à {} : {}", telephone, e.getMessage());
            throw new RuntimeException("Échec envoi SMS : " + e.getMessage());
        }
    }

    private void envoyerAlerteSolde(int solde) {
        if (alerteSoldeEnvoyee || alerteTelephones == null || alerteTelephones.isBlank()) return;
        alerteSoldeEnvoyee = true;

        String msg = "ALERTE ZENITHE: Solde SMS faible! Restant: " + solde + " SMS. Rechargez le compte smsvas.com.";
        for (String tel : alerteTelephones.split(",")) {
            String t = tel.trim();
            if (!t.isBlank()) {
                try {
                    String telF = formaterTelephone(t);
                    String url = UriComponentsBuilder.fromHttpUrl(smsApiUrl)
                            .queryParam("user", smsUser).queryParam("password", smsPassword)
                            .queryParam("senderid", smsSenderId).queryParam("mobiles", telF)
                            .queryParam("sms", msg).build().toUriString();
                    restTemplate.getForObject(url, Map.class);
                    log.info("🚨 Alerte solde SMS envoyée à {}", telF);
                } catch (Exception e) {
                    log.error("Erreur envoi alerte solde à {} : {}", t, e.getMessage());
                }
            }
        }
    }

    private String formaterTelephone(String telephone) {
        String tel = telephone.replaceAll("[\\s-]", "");

        if (tel.startsWith("+")) {
            tel = tel.substring(1);
        }
        if (tel.startsWith("237")) {
            return tel;
        }
        if (tel.startsWith("6") || tel.startsWith("2")) {
            return "237" + tel;
        }
        return tel;
    }
}