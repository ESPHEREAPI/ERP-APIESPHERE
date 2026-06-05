package com.esphere.reporting.util;

import java.util.Set;

public class DroitsAcces {

    private static final Set<String> ACCES_CONSULTATION = Set.of(
            "CENTRE_HOSPITALIER",
            "CENTRE_HOSPITALIER_DENTISTE",
            "CENTRE_HOSPITALIER_SIMPLE",
            "SERVICE_SANTE",
            "SUP_ADMIN"
    );

    private static final Set<String> ACCES_ORDONNANCE = Set.of(
            "CENTRE_HOSPITALIER",
            "CENTRE_HOSPITALIER_DENTISTE",
            "CENTRE_HOSPITALIER_SIMPLE",
            "PHARMACIE",
            "SERVICE_SANTE",
            "SUP_ADMIN"
    );

    private static final Set<String> ACCES_EXAMEN = Set.of(
            "CENTRE_HOSPITALIER",
            "CENTRE_HOSPITALIER_DENTISTE",
            "CENTRE_HOSPITALIER_OPTIQUE",
            "CENTRE_HOSPITALIER_SIMPLE",
            "LABORATOIRE",
            "SERVICE_SANTE",
            "SUP_ADMIN"
    );

    // Tous ont accès aux bons manuels
    private static final Set<String> ACCES_BON_MANUEL = Set.of(
            "CENTRE_HOSPITALIER",
            "CENTRE_HOSPITALIER_DENTISTE",
            "CENTRE_HOSPITALIER_OPTIQUE",
            "CENTRE_HOSPITALIER_SIMPLE",
            "LABORATOIRE",
            "PHARMACIE",
            "SERVICE_SANTE",
            "SUP_ADMIN"
    );

    // 🔜 AM-05 — Hospitalisation non encore validée
    private static final Set<String> ACCES_HOSPITALISATION = Set.of(
            // "CENTRE_HOSPITALIER",
            // "CENTRE_HOSPITALIER_SIMPLE",
            // "SERVICE_SANTE",
            // "SUP_ADMIN"
    );

    public static boolean peutVoirConsultation(String categorieId) {
        return ACCES_CONSULTATION.contains(categorieId);
    }

    public static boolean peutVoirOrdonnance(String categorieId) {
        return ACCES_ORDONNANCE.contains(categorieId);
    }

    public static boolean peutVoirExamen(String categorieId) {
        return ACCES_EXAMEN.contains(categorieId);
    }

    public static boolean peutVoirBonManuel(String categorieId) {
        return ACCES_BON_MANUEL.contains(categorieId);
    }

    public static boolean peutVoirHospitalisation(String categorieId) {
        return ACCES_HOSPITALISATION.contains(categorieId);
    }
}