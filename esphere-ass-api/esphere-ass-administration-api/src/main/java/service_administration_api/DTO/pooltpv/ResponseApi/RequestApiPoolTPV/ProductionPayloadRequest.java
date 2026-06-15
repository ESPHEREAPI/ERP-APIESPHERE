/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;

/**
 *
 * @author USER01
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductionPayloadRequest(
        @JsonProperty("certificate_variant_code")
        String COULEUR_D_ATTESTATION_A_EDITER,
        @JsonProperty("rc")
        Integer PRIME_RC,
        @JsonProperty("police_number")
        String NUMERO_DE_POLICE,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonProperty("starts_at")
        LocalDate DATE_D_EFFET_DU_CONTRAT,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonProperty("ends_at")
        LocalDate DATE_D_ECHEANCE_DU_CONTRAT,
        // Customer
        @JsonProperty("customer_name")
        String NOM_DU_SOUSCRIPTEUR,
        @JsonProperty("customer_phone")
        String NUMERO_DE_TELEPHONE_DU_SOUSCRIPTEUR,
        @JsonProperty("customer_email")
        String ADRESSE_EMAIL_DU_SOUSCRIPTEUR,
        @JsonProperty("customer_postal_code")
        String BOITE_POSTALE_DU_SOUSCRIPTEUR,
        @JsonProperty("customer_type")
        String TYPE_DE_SOUSCRIPTEUR,
        // Insured
        @JsonProperty("insured_name")
        String NOM_DE_L_ASSURE,
        @JsonProperty("insured_phone")
        String TELEPHONE_MOBILE_DE_L_ASSURE,
        @JsonProperty("insured_email")
        String ADRESSE_EMAIL_DE_L_ASSURE,
        @JsonProperty("insured_postal_code")
        String BOITE_POSTALE_DE_L_ASSURE,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonProperty("insured_birthdate")
        LocalDate DATE_DE_NAISSANCE_DE_L_ASSURE,//ajouter au constructeur

        // Vehicle
        @JsonProperty("licence_plate")
        String IMMATRICULATION_DU_VEHICULE,
        @JsonProperty("vehicle_chassis")
        String NUMERO_DE_CHASSIS_DU_VEHICULE,
        @JsonProperty("vehicle_brand")
        String MARQUE_DU_VEHICULE,
        @JsonProperty("vehicle_model")
        String MODELE_DU_VEHICULE,
        @JsonProperty("vehicle_category")
        String CATEGORIE_DU_VEHICULE,
        @JsonProperty("vehicle_genre")
        String GENRE_DU_VEHICULE,
        @JsonProperty("vehicle_type")
        String TYPE_DU_VEHICULE,
        @JsonProperty("vehicule_usage")
        String USAGE_DU_VEHICULE,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonProperty("vehicle_first_registration_date")
        LocalDate DATE_PREMIERE_MISE_EN_CIRCULATION, //ajouter au constructeur

        @JsonProperty("vehicle_energy")
        String ENERGIE_DU_VEHICULE,
        @JsonProperty("nb_of_seats")
        Integer NOMBRE_DE_PLACE_DU_VEHICULE,
        @JsonProperty("fiscal_power")
        Integer PUISSANCE_FISCALE,
        @JsonProperty("circulation_zone")
        String ZONE_DE_CIRCULATION,
        // Driver
        @JsonProperty("driver_name")
        String NOM_PRENOM_CONDUCTEUR,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonProperty("driver_birthdate")
        LocalDate DATE_DE_NAISSANCE_CONDUCTEUR,
        @JsonProperty("driver_permis")
        String NUMERO_PERMIS,//ajouter au constructeur
        @JsonProperty("driver_permis_categorie")
        String CATEGORIE_PERMIS,//ajouter au constructeur
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonProperty("driver_licence_issued_at")
        LocalDate DATE_DE_DELIVRANCE_PERMIS,
        // @JsonProperty("vehicle_has_trailer")     Boolean vehicleHasTrailer,
        //  @JsonProperty("trailer_licence_plate")   String trailerLicencePlate  // ✅ champ ajouté
//        @JsonProperty("insured_code")
//        String CODE_ASSURE,
//        @JsonProperty("insured_profession")
//        String PROFESSION_DE_L_ASSURE,
//        @JsonProperty("insured_city")
//        String VILLE_DE_L_ASSURE,
        @JsonProperty("taxpayer_number")
        String TAXPAYER_NUMBER,
        @JsonProperty("vehicle_gross_weight")
        Integer VEHICLE_GROSS_WEIGHT
        ) {

}
