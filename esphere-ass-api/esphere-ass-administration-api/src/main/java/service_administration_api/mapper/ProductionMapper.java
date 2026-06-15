/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.mapper;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.CertificateInfo;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.CertificateState;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.CodeName;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.OfficeInfo;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.OrganizationInfo;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.ProductionPayloadData;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.ProductionPayloadResponse;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.ProductionRef;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.UserDetail;
import service_administration_api.DTO.pooltpv.ResponseApiPoolTPV.UserInfo;
import service_administration_api.entite.pooltpv.CertificatePlayLoad;
import service_administration_api.entite.pooltpv.ProductionPayload;

/**
 *
 * @author USER01
 */
// mapper/ProductionMapper.java
@Component // Bean Spring pour pouvoir l'injecter avec @Autowired
public class ProductionMapper {

  /**
     * ============================================================
     * Convertit le record ProductionData (réponse API externe)
     * en entité JPA ProductionEntity (pour sauvegarde Oracle)
     * ============================================================
     * Syntaxe record : data.reference() au lieu de data.getReference()
     */
    public ProductionPayload apiResponseToEntity(ProductionPayloadData data) {

        ProductionPayload entity = new ProductionPayload();

        // --- Infos principales de la production ---
        entity.setExternalId(data.id());                        // ID externe "a1b2c3d4e5"
        entity.setReference(data.reference());                  // "PROD-2026-00456"
        entity.setChannel(data.channel());                      // "api"
        entity.setQuantity(data.quantity());                    // 2
        entity.setSentToStorage(data.sentToStorage());          // false
        entity.setDownloadLink(data.downloadLink());
        entity.setFormattedCreatedAt(data.formattedCreatedAt());
        entity.setCreatedAt(data.createdAt());
        entity.setUpdatedAt(data.updatedAt());

        // --- Aplatissement de l'objet user imbriqué ---
        // data.user() retourne le record UserInfo
        // On "aplatit" : pas de table séparée, on stocke dans les colonnes de PRODUCTIONS
        if (data.user() != null) {
            entity.setUserId(data.user().id());
            entity.setUserName(data.user().name());
            entity.setUserEmail(data.user().email());
        }

        // --- Aplatissement de l'organisation ---
        if (data.organization() != null) {
            entity.setOrgId(data.organization().id());
            entity.setOrgName(data.organization().name());
            entity.setOrgCode(data.organization().code());
        }

        // --- Aplatissement de l'office ---
        if (data.office() != null) {
            entity.setOfficeId(data.office().id());
            entity.setOfficeName(data.office().name());
            entity.setOfficeCode(data.office().code());
        }

        // --- Conversion de chaque CertificateInfo → CertificateEntity ---
        // data.certificates() retourne List<CertificateInfo>
        if (data.certificates() != null) {
            List<CertificatePlayLoad> certEntities = (List<CertificatePlayLoad>) data.certificates()
                .stream()
                .map(certInfo -> certInfoToEntity(certInfo, entity)) // on passe entity pour la FK
                .collect(Collectors.toList());

            entity.setCertificates(certEntities);
        }

        return entity;
    }

    /**
     * ============================================================
     * Convertit un CertificateInfo (record réponse API)
     * en CertificateEntity (pour table CERTIFICATES Oracle)
     * ============================================================
     * @param certInfo  Le record certificat venant de l'API
     * @param production L'entité production parente (pour la clé étrangère)
     */
    public CertificatePlayLoad certInfoToEntity(CertificateInfo certInfo,
                                               ProductionPayload production) {
        CertificatePlayLoad entity = new CertificatePlayLoad();

        // Lien vers la production parente (clé étrangère PRODUCTION_ID)
        entity.setProduction(production);

        entity.setReference(certInfo.reference());              // "CERT-2026-78901"
        entity.setStateName(certInfo.state().name());
        entity.setStateLabel(certInfo.state().label());                      // "Éditée"
        entity.setDownloadLink(certInfo.downloadLink());

        // --- Type de certificat : CodeName { code, name } ---
        if (certInfo.certificateType() != null) {
            entity.setCertTypeCode(certInfo.certificateType().code());   // "TERR"
            entity.setCertTypeName(certInfo.certificateType().name());   // "Attestation Terrestre"
        }

        // --- Variante : CodeName { code, name } ---
        if (certInfo.certificateVariant() != null) {
            entity.setCertVariantCode(certInfo.certificateVariant().code()); // "JAUNE"
            entity.setCertVariantName(certInfo.certificateVariant().name());
        }

        // --- Véhicule ---
        entity.setLicencePlate(certInfo.licencePlate());
        entity.setChassisNumber(certInfo.chassisNumber());
        entity.setPoliceNumber(certInfo.policeNumber());

        // --- Assuré ---
        entity.setInsuredName(certInfo.insuredName());
        entity.setInsuredPhone(certInfo.insuredPhone());
        entity.setInsuredEmail(certInfo.insuredEmail());

        // --- Dates (format "dd/MM/yyyy" → stockées comme String) ---
        entity.setStartsAt(certInfo.startsAt());
        entity.setEndsAt(certInfo.endsAt());
        entity.setPrintedAt(certInfo.printedAt());              // peut être null

        // --- Organisation & Office du certificat ---
        if (certInfo.organization() != null) {
            entity.setOrgCode(certInfo.organization().code());
        }
        if (certInfo.office() != null) {
            entity.setOfficeCode(certInfo.office().code());
        }

        // --- User imbriqué dans production > user ---
        // certInfo.production() retourne ProductionRef
        // ProductionRef.user() retourne UserDetail { code, name, email, telephone }
        if (certInfo.production() != null && certInfo.production().user() != null) {
            UserDetail user = certInfo.production().user();
            entity.setUserCode(user.code());
            entity.setUserName(user.name());
            entity.setUserEmail(user.email());
            entity.setUserTelephone(user.telephone());
        }

        return entity;
    }

    /**
     * ============================================================
     * Convertit une CertificateEntity Oracle
     * en record CertificateInfo (pour renvoyer au frontend Angular)
     * ============================================================
     */
    public CertificateInfo entityToCertInfo(CertificatePlayLoad entity) {
        return new CertificateInfo(
            // ProductionRef imbriqué
            new ProductionRef(
                entity.getProduction().getReference(),
                new UserDetail(
                    entity.getUserCode(),
                    entity.getUserName(),
                    entity.getUserEmail(),
                    entity.getUserTelephone()
                )
            ),
            entity.getReference(),
            new OrganizationInfo(null, null, entity.getOrgCode()),
            new OfficeInfo(null, null, entity.getOfficeCode()),
            new CodeName(entity.getCertTypeCode(), entity.getCertTypeName()),
            new CodeName(entity.getCertVariantCode(), entity.getCertVariantName()),
            new CertificateState(entity.getStateName(), entity.getStateLabel()),
            entity.getDownloadLink(),
            entity.getLicencePlate(),
            entity.getChassisNumber(),
            entity.getPoliceNumber(),
            entity.getInsuredName(),
            entity.getInsuredPhone(),
            entity.getInsuredEmail(),
            entity.getStartsAt(),
            entity.getEndsAt(),
            entity.getPrintedAt()
        );
    }
    
    
    /**
 * Convertit une ProductionPayload (entité Oracle)
 * en record ProductionPayloadResponse (pour renvoyer à Angular)
 */
public ProductionPayloadResponse entityToResponse(ProductionPayload entity) {

    // Reconstruit le record UserInfo depuis les colonnes aplaties
    UserInfo user = new UserInfo(
        entity.getUserId(),
        entity.getUserName(),
        entity.getUserEmail()
    );

    // Reconstruit OrganizationInfo
    OrganizationInfo org = new OrganizationInfo(
        entity.getOrgId(),
        entity.getOrgName(),
        entity.getOrgCode()
    );

    // Reconstruit OfficeInfo
    OfficeInfo office = new OfficeInfo(
        entity.getOfficeId(),
        entity.getOfficeName(),
        entity.getOfficeCode()
    );

    // Convertit chaque CertificatePlayLoad → record CertificateInfo
    List<CertificateInfo> certificates = entity.getCertificates()
        .stream()
        .map(this::entityToCertInfo)   // méthode déjà existante dans ton mapper
        .toList();

    // Reconstruit le record ProductionData complet
    ProductionPayloadData data = new ProductionPayloadData(
        entity.getExternalId(),
        entity.getReference(),
        entity.getChannel(),
        entity.getQuantity(),
        entity.getSentToStorage(),
        entity.getDownloadLink(),
        user,
        org,
        office,
        certificates,
        entity.getCreatedAt(),
        entity.getFormattedCreatedAt(),
        entity.getUpdatedAt()
    );

    // Retourne le record enveloppe complet
    return new ProductionPayloadResponse(201, "Succès", data);
}
}
