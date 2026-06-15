
package service_administration_api.mapper;

import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.ProductionPayloadRequest;
import service_administration_api.entite.ZenAttdigAsac;

public class ZenAttdigAsacMapper {

    // ============================================
// GENRE : correspondance flexible
// ============================================
private static String convertGenre(String genre) {
    if (genre == null) return null;
    String g = genre.toUpperCase().trim();

    if (g.contains("CAMIONNETTE"))           return "GV02";
    if (g.contains("SEMI-REMORQUE") || 
        g.contains("SEMI REMORQUE"))         return "GV10";
    if (g.contains("REMORQUE"))              return "GV08";
    if (g.contains("CAMION"))                return "GV01";
    if (g.contains("CYCLOMOTEUR") || 
        g.contains("2/3") || 
        g.contains("MOTO"))                  return "GV03";
    if (g.contains("SCOOTER"))               return "GV09";
    if (g.contains("FOURGONNETTE") || 
        g.contains("FOURGON") ||
        g.contains("UTILITAIRE") ||
        g.contains("PICK UP") ||
        g.contains("PICKUP") ||
        g.contains("VAN"))                   return "GV07";
    if (g.contains("CAR") && 
        !g.contains("VOITURE"))              return "GV06";
    if (g.contains("TRACTEUR AGRICOLE"))     return "GV11";
    if (g.contains("TRACTEUR"))              return "GV12";
    if (g.contains("ENGIN"))                 return "GV05";
    if (g.contains("VOITURE") || 
        g.contains("4 ROUES") ||
        g.contains("BERLINE") ||
        g.contains("BREAK"))                 return "GV04";

    return "GV04"; // défaut : voiture
}

// ============================================
// USAGE : correspondance flexible
// ============================================
private static String convertUsage(String usage) {
    if (usage == null) return null;
    String u = usage.toUpperCase().trim();

    if (u.contains("AUTO-ECOLE") || 
        u.contains("AUTO ECOLE"))            return "UV06";
    if (u.contains("LOCATION"))              return "UV07";
    if (u.contains("SPECIAUX") || 
        u.contains("SPECIAL"))               return "UV08";
    if (u.contains("CHANTIER"))              return "UV09";
    if (u.contains("2 A 3") || 
        u.contains("2 OU 3") ||
        u.contains("MOTO"))                  return "UV10";
    if (u.contains("PUBLIC") && 
        u.contains("MARCHANDISE"))           return "UV04";
    if (u.contains("PUBLIC") && 
        u.contains("VOYAGEUR"))              return "UV05";
    if (u.contains("PRIVE") && 
        u.contains("VOYAGEUR"))              return "UV03";
    if (u.contains("PROPRE COMPTE") ||
        u.contains("PICK UP") ||
        u.contains("PICKUP") ||
        u.contains("CAMIONNETTE") ||
        u.contains("UTILITAIRE"))            return "UV02";
    if (u.contains("PROMENADE") || 
        u.contains("AFFAIRE") ||
        u.contains("TOURISME"))              return "UV01";

    return "UV01"; // défaut : promenade ou affaire
}

// ============================================
// TYPE : correspondance flexible
// ============================================
private static String convertType(String type) {
    if (type == null) return null;
    String t = type.toUpperCase().trim();

    if (t.contains("AMBULANCE"))             return "TV01";
    if (t.contains("CORBILLARD"))            return "TV03";
    if (t.contains("AUTO CAR") || 
        t.contains("AUTOCAR") ||
        (t.contains("CAR") && 
         t.contains("41")))                  return "TV02";
    if (t.contains("MINI CAR") || 
        t.contains("MINICAR") ||
        (t.contains("CAR") && 
         t.contains("40")))                  return "TV04";
    if (t.contains("TAXI") && 
        t.contains("COMMUN"))                return "TV05";
    if (t.contains("TAXI") || 
        t.contains("VTC") ||
        t.contains("MATCA"))                 return "TV06";
    if (t.contains("AUTO-ECOLE") || 
        t.contains("AUTO ECOLE"))            return "TV07";
    if (t.contains("SERVICE PUBLIC") || 
        t.contains("ORDURE"))                return "TV08";
    if (t.contains("TOURISME") && 
        t.contains("CHAUFFEUR"))             return "TV09";
    if (t.contains("LOCATION"))              return "TV12";
    if (t.contains("CYCLOMOTEUR") || 
        t.contains("2/3"))                   return "TV13";
    if (t.contains("UTILITAIRE") ||
        t.contains("VAN") ||
        t.contains("FOURGON") ||
        t.contains("CAMIONNETTE") ||
        t.contains("PICK UP") ||
        t.contains("PICKUP"))                return "TV11";
    if (t.contains("PARTICULIER") || 
        t.contains("3,5"))                   return "TV10";

    // Si aucun libellé reconnu (ex: "JNKB40" = modèle saisi par erreur)
    // → retourner TV10 par défaut (Véhicule Particulier)
    return "TV10";
}

// ============================================
// ENERGIE : correspondance flexible
// ============================================
private static String convertEnergie(String energie) {
    if (energie == null) return null;
    String e = energie.toUpperCase().trim();

    if (e.contains("ESSENCE") || e.equals("E"))   return "SEES";
    if (e.contains("DIEZEL") || 
        e.contains("DIESEL") || e.equals("D"))     return "SEDI";
    if (e.contains("ELECTRI"))                     return "SEEL";
    if (e.contains("HYBRIDE") || e.equals("H"))    return "SEHY";

    return null;
}

    // ============================================
    // MAPPER PRINCIPAL
    // ============================================
    public static ProductionPayloadRequest toProductionPayloadRequestFinal(ProductionPayloadRequest z) {

        String typeSouscripteur = z.TYPE_DE_SOUSCRIPTEUR()!= null 
                                  ? z.TYPE_DE_SOUSCRIPTEUR().trim() 
                                  : "";

        // Logs de debug pour les conversions
        System.out.println("energie raw      : " + z.ENERGIE_DU_VEHICULE());
        System.out.println("energie converti : " + convertEnergie(z.ENERGIE_DU_VEHICULE()));
        System.out.println("genre raw        : " + z.GENRE_DU_VEHICULE());
        System.out.println("genre converti   : " + convertGenre(z.GENRE_DU_VEHICULE()));
        System.out.println("usage raw        : " + z.USAGE_DU_VEHICULE());
        System.out.println("usage converti   : " + convertUsage(z.USAGE_DU_VEHICULE()));
        System.out.println("type raw         : " + z.TYPE_DU_VEHICULE());
        System.out.println("type converti    : " + convertType(z.TYPE_DU_VEHICULE()));

        return new ProductionPayloadRequest(
                // certificate_variant_code
                z.COULEUR_D_ATTESTATION_A_EDITER(),
                // rc
                z.PRIME_RC(),
                // police_number
                z.NUMERO_DE_POLICE(),
                // starts_at
                z.DATE_D_EFFET_DU_CONTRAT(),
                // ends_at
                z.DATE_D_ECHEANCE_DU_CONTRAT(),
                // Customer
                z.NOM_DU_SOUSCRIPTEUR(),
                z.NUMERO_DE_TELEPHONE_DU_SOUSCRIPTEUR(),
                z.ADRESSE_EMAIL_DU_SOUSCRIPTEUR(),
                z.BOITE_POSTALE_DU_SOUSCRIPTEUR(),
                "PP".equalsIgnoreCase(typeSouscripteur) ? "TSPP" : "TSPM", // ✅ TAPP/TAPM
                // Insured
                z.NOM_DE_L_ASSURE(),
                z.TELEPHONE_MOBILE_DE_L_ASSURE(),
                z.ADRESSE_EMAIL_DE_L_ASSURE(),
                z.BOITE_POSTALE_DE_L_ASSURE(),
                z.DATE_DE_NAISSANCE_DE_L_ASSURE(),
                // Vehicle
                z.IMMATRICULATION_DU_VEHICULE(),
                z.NUMERO_DE_CHASSIS_DU_VEHICULE(),
                z.MARQUE_DU_VEHICULE(),
                z.MODELE_DU_VEHICULE(),
                z.CATEGORIE_DU_VEHICULE(),
                convertGenre(z.GENRE_DU_VEHICULE()),          // ✅ GV01..GV12
                convertType(z.TYPE_DU_VEHICULE()),             // ✅ TV01..TV13
                convertUsage(z.USAGE_DU_VEHICULE()),           // ✅ UV01..UV10
                z.DATE_PREMIERE_MISE_EN_CIRCULATION(),
                convertEnergie(z.ENERGIE_DU_VEHICULE()),       // ✅ SEES/SEDI/SEEL/SEHY
                z.NOMBRE_DE_PLACE_DU_VEHICULE(),
                z.PUISSANCE_FISCALE(),
                z.ZONE_DE_CIRCULATION(),
                // Driver
                z.NOM_PRENOM_CONDUCTEUR(),
                z.DATE_DE_NAISSANCE_CONDUCTEUR(),
                z.NUMERO_PERMIS(),
                z.CATEGORIE_PERMIS(),
                z.DATE_DE_DELIVRANCE_PERMIS(),
                //z.CODE_ASSURE(),
               // z.PROFESSION_DE_L_ASSURE(),
                //z.VILLE_DE_L_ASSURE(),
                z.TAXPAYER_NUMBER(),
                z.VEHICLE_GROSS_WEIGHT()
        );
    }
    
    
    public static ProductionPayloadRequest toProductionPayloadRequest(ZenAttdigAsac z) {
        String type_souscripteur = z.getTypeSouscripteur().trim();
        System.out.println("type_souscripteur :" + type_souscripteur);
        return new ProductionPayloadRequest(
                // certificate_variant_code → couleurAttestation (ex: "auto")
                z.getCouleurAttestation(),
                // rc
                z.getPrimeRc(),
                // police_number
                z.getNumeroPolice(),
                // starts_at
                z.getDateEffet(),
                // ends_at
                z.getDateEcheance(),
                // Customer (Souscripteur)
                z.getNomSouscripteur(),
                z.getTelSouscripteur(),
                z.getEmailSouscripteur(),
                z.getBpSouscripteur(),
                "PP".equalsIgnoreCase(type_souscripteur) ? "TSPP" : "TSPM",
                // Insured (Assuré)
                z.getNomAssure(),
                z.getTelAssure(),
                z.getEmailAssure(),
                z.getBpAssure(),
                z.getDateNaissanceAssure(),
                // Vehicle
                z.getImmatriculation(), // licence_plate
                z.getNumeroChassis(),
                z.getMarque(),
                z.getModele(),
                z.getCategorie(),
                z.getGenre(),
                z.getType(),
                z.getUsage(),
                z.getDateMiseCirculation(),
                z.getEnergie(),
                z.getNombrePlaces(),
                z.getPuissanceFiscale(),
                z.getZoneCirculation(),
                // Driver
                z.getNomConducteur(),
                z.getDateNaissanceConducteur(),
                z.getNumeroPermis(),
                z.getCategoriePermis(),
                z.getDateDelivrancePermis(),
               // z.getCodeassure(),
                //z.getCodeassureprof(),
                //z.getAssureville(),
                z.getNumerocontribuable(),
                z.getVehiculpoids()
                // vehicle_has_trailer → non présent dans ZenAttdigAsac, on met false par défaut
              //  false,
                // trailer_licence_plate → non présent, on met null
               // null
                
        );
    }
}

///
///
//////*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package service_administration_api.mapper;
//
//import service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV.ProductionPayloadRequest;
//import service_administration_api.entite.ZenAttdigAsac;
//
///**
// *
// * @author USER01
// */
//public class ZenAttdigAsacMapper {
//
//    public static ProductionPayloadRequest toProductionPayloadRequest(ZenAttdigAsac z) {
//        String type_souscripteur = z.getTypeSouscripteur().trim();
//        System.out.println("type_souscripteur :" + type_souscripteur);
//        return new ProductionPayloadRequest(
//                // certificate_variant_code → couleurAttestation (ex: "auto")
//                z.getCouleurAttestation(),
//                // rc
//                z.getPrimeRc(),
//                // police_number
//                z.getNumeroPolice(),
//                // starts_at
//                z.getDateEffet(),
//                // ends_at
//                z.getDateEcheance(),
//                // Customer (Souscripteur)
//                z.getNomSouscripteur(),
//                z.getTelSouscripteur(),
//                z.getEmailSouscripteur(),
//                z.getBpSouscripteur(),
//                "PP".equalsIgnoreCase(type_souscripteur) ? "TSPP" : "TSPM",
//                // Insured (Assuré)
//                z.getNomAssure(),
//                z.getTelAssure(),
//                z.getEmailAssure(),
//                z.getBpAssure(),
//                z.getDateNaissanceAssure(),
//                // Vehicle
//                z.getImmatriculation(), // licence_plate
//                z.getNumeroChassis(),
//                z.getMarque(),
//                z.getModele(),
//                z.getCategorie(),
//                z.getGenre(),
//                z.getType(),
//                z.getUsage(),
//                z.getDateMiseCirculation(),
//                z.getEnergie(),
//                z.getNombrePlaces(),
//                z.getPuissanceFiscale(),
//                z.getZoneCirculation(),
//                // Driver
//                z.getNomConducteur(),
//                z.getDateNaissanceConducteur(),
//                z.getNumeroPermis(),
//                z.getCategoriePermis(),
//                z.getDateDelivrancePermis(),
//                z.getCodeassure(),
//                z.getCodeassureprof(),
//                z.getAssureville(),
//                z.getNumerocontribuable(),
//                z.getVehiculpoids()
//                // vehicle_has_trailer → non présent dans ZenAttdigAsac, on met false par défaut
//              //  false,
//                // trailer_licence_plate → non présent, on met null
//               // null
//                
//        );
//    }
//}




