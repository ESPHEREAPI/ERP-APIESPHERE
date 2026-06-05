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

    private final RestTemplate restTemplate = new RestTemplate();

    @Async
    public void envoyer(String telephone, String message) {
        try {
            // Formater le numéro : ajouter 237 si absent
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
                log.info("SMS envoyé à {} | Réponse : {}", telFormate,
                        response.get("responsedescription"));
            }

        } catch (Exception e) {
            log.error("Erreur envoi SMS à {} : {}", telephone, e.getMessage());
            throw new RuntimeException("Échec envoi SMS : " + e.getMessage());
        }
    }

    private String formaterTelephone(String telephone) {
        // Supprimer espaces et tirets
        String tel = telephone.replaceAll("[\\s-]", "");

        // Ajouter indicatif Cameroun si absent
        if (tel.startsWith("6") || tel.startsWith("2")) {
            return "237" + tel;
        }
        if (tel.startsWith("+")) {
            return tel.substring(1);
        }
        return tel;
    }
}