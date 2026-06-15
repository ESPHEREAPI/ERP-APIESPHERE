/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApiPoolTPV;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.time.OffsetDateTime;   // ← Import manquant

/**
 *
 * @author USER01
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductionPayloadData 
(

    @JsonProperty("id")
    String id,                          // "a1b2c3d4e5"

    @JsonProperty("reference")
    String reference,                   // "PROD-2026-00456"

    @JsonProperty("channel")
    String channel,                     // "api"

    @JsonProperty("quantity")
    Integer quantity,                   // 2

    @JsonProperty("sent_to_storage")
    Boolean sentToStorage,              // false

    @JsonProperty("download_link")
    String downloadLink,                // "https://eattestation.cm/..."

    // --- Objets imbriqués ---
    @JsonProperty("user")
    UserInfo user,                      // Utilisateur qui a créé la production

    @JsonProperty("organization")
    OrganizationInfo organization,      // Organisation (ACTIVA)

    @JsonProperty("office")
    OfficeInfo office,                  // Agence

    @JsonProperty("certificates")
    List<CertificateInfo> certificates, // Liste des attestations générées

    // --- Dates ---
    @JsonProperty("created_at")
    OffsetDateTime createdAt,           // "2026-03-03T14:30:00+01:00" → OffsetDateTime

    @JsonProperty("formatted_created_at")
    String formattedCreatedAt,          // "03/03/2026 14:30" (format affichage)

    @JsonProperty("updated_at")
    OffsetDateTime updatedAt

) {}