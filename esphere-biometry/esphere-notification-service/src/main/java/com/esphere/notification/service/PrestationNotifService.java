package com.esphere.notification.service;

import com.esphere.notification.dto.request.ConsoAlertRequest;
import com.esphere.notification.dto.request.FraudeCheckRequest;
import com.esphere.notification.dto.request.PrestationNotifRequest;
import com.esphere.notification.dto.request.SoumissionNotifRequest;
import com.esphere.notification.client.AuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrestationNotifService {

    private final NotificationService notificationService;
    private final ParametreHelper     params;
    private final AuthClient          authClient;

    @Value("${esphere.service-sante.telephones:}")
    private String telephonesSS;

    public void notifierTraitementPrestation(PrestationNotifRequest req) {
        if (!params.getBoolean("NOTIF_ASSURE_TRAITEMENT_PRESTATION", true)) {
            log.info("NOTIF_ASSURE_TRAITEMENT_PRESTATION désactivé — notification ignorée");
            return;
        }

        // Récupérer la langue du prestataire
        boolean isFrench = true;
        try {
            Map<String, Object> langueInfo = authClient.getLanguePrestataire(req.getPrestataireId());
            int langue = langueInfo.get("langue") != null ? ((Number) langueInfo.get("langue")).intValue() : 2;
            isFrench = (langue == 2);
        } catch (Exception e) {
            log.warn("Impossible de récupérer la langue du prestataire {} — défaut FR", req.getPrestataireId());
        }
        String destinataire = req.getCodeAyantDroit() != null ? req.getCodeAyantDroit() : req.getCodeAdherent();

        String nature = req.getNaturePrestation() != null ? req.getNaturePrestation() : "prestation";
        String numVisite = req.getCodeVisite() != null ? req.getCodeVisite() : "";
        String etat = req.getEtat();

        String eventType = switch (etat) {
            case "valide"        -> "prestation_validee";
            case "partiel"       -> "prestation_partielle";
            default              -> "prestation_rejetee";
        };

        String messageFr, messageEn;
        switch (etat) {
            case "valide" -> {
                messageFr = MessageTemplates.prestationValideeFr(req.getPrestataireNom(), nature, numVisite, req.getMontantZenithe(), req.getMontantPartAssure());
                messageEn = MessageTemplates.prestationValideeEn(req.getPrestataireNom(), nature, numVisite, req.getMontantZenithe(), req.getMontantPartAssure());
            }
            case "partiel" -> {
                messageFr = MessageTemplates.prestationPartielleFr(req.getPrestataireNom(), nature, numVisite, req.getMontantZenithe(), req.getMontantPartAssure());
                messageEn = MessageTemplates.prestationPartielleEn(req.getPrestataireNom(), nature, numVisite, req.getMontantZenithe(), req.getMontantPartAssure());
            }
            default -> {
                messageFr = MessageTemplates.prestationRejeteeFr(req.getPrestataireNom(), nature, numVisite);
                messageEn = MessageTemplates.prestationRejeteeEn(req.getPrestataireNom(), nature, numVisite);
            }
        }

        String message = isFrench ? messageFr : messageEn;
        String sujet = isFrench ? "Traitement prestation" : "Claim processing";

        // Le téléphone dépend du patient : ayant-droit si présent et non null, sinon adhérent
        String telephone = null;
        if (req.getCodeAyantDroit() != null) {
            telephone = (req.getTelephoneAyantDroit() != null && !req.getTelephoneAyantDroit().isBlank())
                    ? req.getTelephoneAyantDroit()
                    : req.getTelephoneAdherent();
        } else {
            telephone = req.getTelephoneAdherent();
        }

        if (telephone != null && !telephone.isBlank()) {
            notificationService.envoyerAuto(
                    destinataire, "assure", "sms", null, message,
                    telephone, null, eventType, req.getPrestationId());
            log.info("SMS prestation {} ({}) envoyé au {} — patient={}", req.getPrestationId(), req.getEtat(), telephone, destinataire);
        } else {
            log.warn("Pas de téléphone pour notifier le patient {} — prestation {}", destinataire, req.getPrestationId());
        }
    }

    public void verifierFraude(FraudeCheckRequest req) {
        if (!params.getBoolean("NOTIF_ALERTE_FRAUDE_PRESTATAIRE", false)) {
            return;
        }

        int seuil = params.getInt("SEUIL_ALERTE_FRAUDE_NB_PRESTATIONS_JOUR", 20);

        if (req.getNbPrestationsJour() >= seuil) {
            String message = MessageTemplates.alerteFraudeFr(req.getPrestataireId(), req.getNbPrestationsJour(), seuil);
            String sujet = "⚠ Alerte fraude — " + req.getPrestataireId();

            // Alerte in-app pour tous les agents SS
            notificationService.envoyerAuto(
                    "SERVICE_SANTE", "agent_ss", "alerte", sujet, message,
                    null, null, "alerte_fraude", req.getPrestataireId());

            // Email aux destinataires stats si configuré
            String destEmail = params.getString("NOTIF_STATS_EMAIL_DESTINATAIRES", "");
            if (!destEmail.isBlank()) {
                for (String email : destEmail.split(",")) {
                    notificationService.envoyerAuto(
                            "SERVICE_SANTE", "agent_ss", "email", sujet, message,
                            null, email.trim(), "alerte_fraude", req.getPrestataireId());
                }
            }

            log.warn("ALERTE FRAUDE : {} a {} prestations (seuil={})", req.getPrestataireId(), req.getNbPrestationsJour(), seuil);
        }
    }

    public void verifierConsoAbusive(ConsoAlertRequest req) {
        if (!params.getBoolean("NOTIF_ALERTE_CONSO_ABUSIVE", false)) {
            return;
        }

        int seuil = params.getInt("SEUIL_ALERTE_CONSO", 80);

        if (req.getPourcentageConsomme() >= seuil) {
            String message = MessageTemplates.alerteConsoAbusiveFr(req.getCodeAdherent(), req.getPourcentageConsomme(), seuil);
            String sujet = "⚠ Alerte consommation — " + req.getCodeAdherent();

            notificationService.envoyerAuto(
                    "SERVICE_SANTE", "agent_ss", "alerte", sujet, message,
                    null, null, "alerte_conso_abusive", req.getCodeAdherent());

            log.warn("ALERTE CONSO : {} à {}% (seuil={}%)", req.getCodeAdherent(), req.getPourcentageConsomme(), seuil);
        }
    }

    public void notifierNouvelleSoumission(SoumissionNotifRequest req) {
        if (!params.getBoolean("NOTIF_SS_NOUVELLE_PRESTATION", true)) {
            log.info("NOTIF_SS_NOUVELLE_PRESTATION désactivé — notification SS ignorée");
            return;
        }

        String msgFr = MessageTemplates.nouvelleSoumissionFr(req.getPrestataireNom(), req.getNaturePrestation(), req.getCodeVisite());
        String msgEn = MessageTemplates.nouvelleSoumissionEn(req.getPrestataireNom(), req.getNaturePrestation(), req.getCodeVisite());

        // 1. Envoyer aux agents SS depuis la base (auth-service)
        try {
            List<Map<String, Object>> agents = authClient.getAgentsSS();
            for (Map<String, Object> agent : agents) {
                String tel = String.valueOf(agent.get("telephone"));
                int langue = agent.get("langue") != null ? ((Number) agent.get("langue")).intValue() : 2;
                String msg = (langue == 1) ? msgEn : msgFr;
                notificationService.envoyerAuto(
                        "SERVICE_SANTE", "agent_ss", "sms", null, msg,
                        tel, null, "prestation_soumise", req.getCodeVisite());
            }
            log.info("SMS soumission envoyé à {} agents SS depuis la base", agents.size());
        } catch (Exception e) {
            log.error("Erreur récupération agents SS : {}", e.getMessage());
        }

        // 2. Envoyer aussi aux numéros dans application.yml (fallback/complémentaire)
        if (telephonesSS != null && !telephonesSS.isBlank()) {
            for (String tel : telephonesSS.split(",")) {
                String telTrim = tel.trim();
                if (!telTrim.isBlank()) {
                    notificationService.envoyerAuto(
                            "SERVICE_SANTE", "agent_ss", "sms", null, msgFr,
                            telTrim, null, "prestation_soumise", req.getCodeVisite());
                }
            }
        }

        // 3. Alerte in-app
        notificationService.envoyerAuto(
                "SERVICE_SANTE", "agent_ss", "alerte",
                "Nouvelle " + req.getNaturePrestation() + " — " + req.getPrestataireNom(),
                msgFr, null, null, "prestation_soumise", req.getCodeVisite());

        log.info("Notification SS envoyée — {} soumise par {} (visite {})",
                req.getNaturePrestation(), req.getPrestataireNom(), req.getCodeVisite());
    }
}
