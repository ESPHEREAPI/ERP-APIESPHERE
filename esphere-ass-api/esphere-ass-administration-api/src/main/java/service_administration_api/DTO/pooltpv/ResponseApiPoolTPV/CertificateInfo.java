/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApiPoolTPV;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USER01
 */
// dto/response/CertificateInfo.java
// Représente UN certificat dans la liste "certificates"
@JsonIgnoreProperties(ignoreUnknown = true)
public record CertificateInfo(

//    @JsonProperty("production")
//    ProductionRef production,           // Référence à la production parente
//
//    @JsonProperty("reference")
//    String reference,                   // "CERT-2026-78901"
//
//    @JsonProperty("organization")
//    OrganizationInfo organization,
//
//    @JsonProperty("office")
//    OfficeInfo office,
//
//    @JsonProperty("certificate_type")
//    CodeName certificateType,           // { code: "TERR", name: "Attestation Terrestre" }
//
//    @JsonProperty("certificate_variant")
//    CodeName certificateVariant,        // { code: "JAUNE", name: "Jaune" }
//
//    @JsonProperty("state")
//    String state,                       // "Éditée"
//
//    @JsonProperty("download_link")
//    String downloadLink,                // Lien de téléchargement du certificat
//
//    @JsonProperty("licence_plate")
//    String licencePlate,                // "LT-1234-AB"
//
//    @JsonProperty("chassis_number")
//    String chassisNumber,               // "WVWZZZ3CZWE123456"
//
//    @JsonProperty("police_number")
//    String policeNumber,                // "POL-2026-00123"
//
//    @JsonProperty("insured_name")
//    String insuredName,                 // "Entreprise ABC SARL"
//
//    @JsonProperty("insured_phone")
//    String insuredPhone,
//
//    @JsonProperty("insured_email")
//    String insuredEmail,
//
//    // Dates au format "dd/MM/yyyy" → String car format non-ISO
//    // Si tu veux LocalDate : utilise @JsonDeserialize avec un formatter custom
//    @JsonProperty("starts_at")
//    String startsAt,                    // "15/03/2026"
//
//    @JsonProperty("ends_at")
//    String endsAt,                      // "14/03/2027"
//
//    @JsonProperty("printed_at")
//    String printedAt                    // null possible → String nullable
        
         @JsonProperty("production")          ProductionRef production,
    @JsonProperty("reference")           String reference,
    @JsonProperty("organization")        OrganizationInfo organization,
    @JsonProperty("office")              OfficeInfo office,
    @JsonProperty("certificate_type")    CodeName certificateType,
    @JsonProperty("certificate_variant") CodeName certificateVariant,
    @JsonProperty("state")               CertificateState state,   // ✅ objet
    @JsonProperty("download_link")       String downloadLink,
    @JsonProperty("licence_plate")       String licencePlate,
    @JsonProperty("chassis_number")      String chassisNumber,
    @JsonProperty("police_number")       String policeNumber,
    @JsonProperty("insured_name")        String insuredName,
    @JsonProperty("insured_phone")       String insuredPhone,
    @JsonProperty("insured_email")       String insuredEmail,
    @JsonProperty("starts_at")           String startsAt,
    @JsonProperty("ends_at")             String endsAt,
    @JsonProperty("printed_at")          String printedAt

) {}