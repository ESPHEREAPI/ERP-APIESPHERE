package com.esphere.notification.service;

public final class MessageTemplates {

    private MessageTemplates() {}

    // ── Prestation validée ───────────────────────────────────────

    public static String prestationValideeFr(String prestataireNom, String naturePrestation, String codeVisite, double montantZenithe, double montantPartAssure) {
        return "Bonjour, votre demande emise par %s concernant la prestation %s N°%s a ete traitee. Statut: VALIDEE. Part Zenithe: %.0f FCFA. Part assure: %.0f FCFA. ZENITHE INSURANCE"
                .formatted(prestataireNom, naturePrestation, codeVisite, montantZenithe, montantPartAssure);
    }

    public static String prestationValideeEn(String prestataireNom, String naturePrestation, String codeVisite, double montantZenithe, double montantPartAssure) {
        return "Hello, your request issued by %s regarding the %s claim N°%s has been processed. Status: APPROVED. Zenithe share: %.0f FCFA. Insured share: %.0f FCFA. ZENITHE INSURANCE"
                .formatted(prestataireNom, naturePrestation, codeVisite, montantZenithe, montantPartAssure);
    }

    // ── Prestation rejetée ───────────────────────────────────────

    public static String prestationRejeteeFr(String prestataireNom, String naturePrestation, String codeVisite) {
        return "Bonjour, votre demande emise par %s concernant la prestation %s N°%s a ete traitee. Statut: REJETEE. Contactez le service sante. ZENITHE INSURANCE"
                .formatted(prestataireNom, naturePrestation, codeVisite);
    }

    public static String prestationRejeteeEn(String prestataireNom, String naturePrestation, String codeVisite) {
        return "Hello, your request issued by %s regarding the %s claim N°%s has been processed. Status: REJECTED. Contact the health service. ZENITHE INSURANCE"
                .formatted(prestataireNom, naturePrestation, codeVisite);
    }

    // ── Prestation partiellement validée ─────────────────────────

    public static String prestationPartielleFr(String prestataireNom, String naturePrestation, String codeVisite, double montantZenithe, double montantPartAssure) {
        return "Bonjour, votre demande emise par %s concernant la prestation %s N°%s a ete traitee. Statut: PARTIELLEMENT VALIDEE. Part Zenithe: %.0f FCFA. Part assure: %.0f FCFA. ZENITHE INSURANCE"
                .formatted(prestataireNom, naturePrestation, codeVisite, montantZenithe, montantPartAssure);
    }

    public static String prestationPartielleEn(String prestataireNom, String naturePrestation, String codeVisite, double montantZenithe, double montantPartAssure) {
        return "Hello, your request issued by %s regarding the %s claim N°%s has been processed. Status: PARTIALLY APPROVED. Zenithe share: %.0f FCFA. Insured share: %.0f FCFA. ZENITHE INSURANCE"
                .formatted(prestataireNom, naturePrestation, codeVisite, montantZenithe, montantPartAssure);
    }

    // ── Bon manuel confirmé ──────────────────────────────────────

    public static String bonConfirmeFr(String reference, double montant) {
        return """
            Bonjour,
            Votre bon manuel %s a été CONFIRMÉ.
            Montant confirmé : %.0f FCFA
            L'assuré peut se présenter avec ce bon.
            — ZENITHE INSURANCE, Plateforme Biométrie""".formatted(reference, montant);
    }

    public static String bonConfirmeEn(String reference, double montant) {
        return """
            Hello,
            Your manual voucher %s has been CONFIRMED.
            Confirmed amount: %.0f FCFA
            The insured may present this voucher.
            — ZENITHE INSURANCE, Biometry Platform""".formatted(reference, montant);
    }

    // ── Bon manuel rejeté ────────────────────────────────────────

    public static String bonRejeteFr(String reference) {
        return """
            Bonjour,
            Votre bon manuel %s a été REJETÉ.
            Veuillez contacter le service santé pour plus d'informations.
            — ZENITHE INSURANCE, Plateforme Biométrie""".formatted(reference);
    }

    public static String bonRejeteEn(String reference) {
        return """
            Hello,
            Your manual voucher %s has been REJECTED.
            Please contact the health service for more information.
            — ZENITHE INSURANCE, Biometry Platform""".formatted(reference);
    }

    // ── Alerte fraude prestataire ─────────────────────────────────

    public static String alerteFraudeFr(String prestataireId, int nbPrestations, int seuil) {
        return """
            ⚠ ALERTE FRAUDE POTENTIELLE
            Le prestataire %s a soumis %d prestations aujourd'hui (seuil : %d).
            Veuillez vérifier ce comportement anormal.
            — Plateforme Biométrie ZENITHE""".formatted(prestataireId, nbPrestations, seuil);
    }

    // ── Alerte consommation abusive ──────────────────────────────

    public static String alerteConsoAbusiveFr(String codeAdherent, double pourcentage, int seuil) {
        return """
            ⚠ ALERTE CONSOMMATION EXCESSIVE
            L'assuré %s a atteint %.0f%% de son plafond annuel (seuil d'alerte : %d%%).
            Veuillez surveiller cette consommation.
            — Plateforme Biométrie ZENITHE""".formatted(codeAdherent, pourcentage, seuil);
    }

    // ── Nouvelle soumission prestation (notification SS) ─────────

    public static String nouvelleSoumissionFr(String prestataireNom, String nature, String codeVisite) {
        return "Prestataire: %s. Demande de Traitement %s pour la visite %s. ZENITHE INSURANCE"
                .formatted(prestataireNom, nature, codeVisite);
    }

    public static String nouvelleSoumissionEn(String prestataireNom, String nature, String codeVisite) {
        return "Provider: %s. Treatment request %s for visit %s. ZENITHE INSURANCE"
                .formatted(prestataireNom, nature, codeVisite);
    }
}
