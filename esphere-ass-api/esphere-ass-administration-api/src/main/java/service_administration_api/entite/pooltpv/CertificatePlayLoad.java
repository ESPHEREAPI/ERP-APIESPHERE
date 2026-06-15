/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite.pooltpv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author USER01 ZEN_CERTIFICATES_PLAYLOAD
 */
@Entity
@Table(name = "ZEN_CERTIFICATES_PAYLOAD")
@Getter
@Setter
public class CertificatePlayLoad implements Serializable {

   @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cert_seq")
    @SequenceGenerator(name = "cert_seq", sequenceName = "SEQ_CERTIFICATES", allocationSize = 1)
    private Long id;

    // Clé étrangère vers PRODUCTIONS
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCTION_ID", nullable = false)
    private ProductionPayload production;

    @Column(name = "REFERENCE")              private String reference;         // "CERT-2026-78901"
   // @Column(name = "STATE")                  private String state;             // "Éditée"
    // ✅ state décomposé
    @Column(name = "STATE_NAME")              private String stateName;    // "active"
    @Column(name = "STATE_LABEL")             private String stateLabel;   // "Active"

    @Column(name = "DOWNLOAD_LINK", length = 500) private String downloadLink;
    @Column(name = "PDF_BYTES")
    @Lob                                                                        // Stockage binaire Oracle (BLOB)
    private byte[] pdfBytes;                                                    // PDF téléchargé stocké en base

    // --- Type et variante ---
    @Column(name = "CERT_TYPE_CODE")         private String certTypeCode;      // "TERR"
    @Column(name = "CERT_TYPE_NAME")         private String certTypeName;      // "Attestation Terrestre"
    @Column(name = "CERT_VARIANT_CODE")      private String certVariantCode;   // "JAUNE"
    @Column(name = "CERT_VARIANT_NAME")      private String certVariantName;

    // --- Véhicule ---
    @Column(name = "LICENCE_PLATE")          private String licencePlate;
    @Column(name = "CHASSIS_NUMBER")         private String chassisNumber;
    @Column(name = "POLICE_NUMBER")          private String policeNumber;

    // --- Assuré ---
    @Column(name = "INSURED_NAME")           private String insuredName;
    @Column(name = "INSURED_PHONE")          private String insuredPhone;
    @Column(name = "INSURED_EMAIL")          private String insuredEmail;

    // --- Dates ---
    @Column(name = "STARTS_AT")             private String startsAt;          // "15/03/2026"
    @Column(name = "ENDS_AT")               private String endsAt;
    @Column(name = "PRINTED_AT")            private String printedAt;         // nullable

    // --- Organisation & Office ---
    @Column(name = "ORG_CODE")              private String orgCode;
    @Column(name = "OFFICE_CODE")           private String officeCode;

    // --- User du certificat ---
    @Column(name = "USER_CODE")             private String userCode;
    @Column(name = "USER_NAME")             private String userName;
    @Column(name = "USER_EMAIL")            private String userEmail;
    @Column(name = "USER_TELEPHONE")        private String userTelephone;

    public CertificatePlayLoad() {}
    // Getters & Setters
    public Long getId() { return id; }
    public ProductionPayload getProduction() { return production; }
    public void setProduction(ProductionPayload production) { this.production = production; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getStateName() { return stateName; }
    public void setStateName(String state) { this.stateName = state; }
    
       public String getStateLabel() { return stateLabel; }
    public void setStateLabel(String state) { this.stateLabel = state; }
    public String getDownloadLink() { return downloadLink; }
    public void setDownloadLink(String downloadLink) { this.downloadLink = downloadLink; }
    public byte[] getPdfBytes() { return pdfBytes; }
    public void setPdfBytes(byte[] pdfBytes) { this.pdfBytes = pdfBytes; }
    public String getCertTypeCode() { return certTypeCode; }
    public void setCertTypeCode(String certTypeCode) { this.certTypeCode = certTypeCode; }
    public String getCertTypeName() { return certTypeName; }
    public void setCertTypeName(String certTypeName) { this.certTypeName = certTypeName; }
    public String getCertVariantCode() { return certVariantCode; }
    public void setCertVariantCode(String certVariantCode) { this.certVariantCode = certVariantCode; }
    public String getCertVariantName() { return certVariantName; }
    public void setCertVariantName(String certVariantName) { this.certVariantName = certVariantName; }
    public String getLicencePlate() { return licencePlate; }
    public void setLicencePlate(String licencePlate) { this.licencePlate = licencePlate; }
    public String getChassisNumber() { return chassisNumber; }
    public void setChassisNumber(String chassisNumber) { this.chassisNumber = chassisNumber; }
    public String getPoliceNumber() { return policeNumber; }
    public void setPoliceNumber(String policeNumber) { this.policeNumber = policeNumber; }
    public String getInsuredName() { return insuredName; }
    public void setInsuredName(String insuredName) { this.insuredName = insuredName; }
    public String getInsuredPhone() { return insuredPhone; }
    public void setInsuredPhone(String insuredPhone) { this.insuredPhone = insuredPhone; }
    public String getInsuredEmail() { return insuredEmail; }
    public void setInsuredEmail(String insuredEmail) { this.insuredEmail = insuredEmail; }
    public String getStartsAt() { return startsAt; }
    public void setStartsAt(String startsAt) { this.startsAt = startsAt; }
    public String getEndsAt() { return endsAt; }
    public void setEndsAt(String endsAt) { this.endsAt = endsAt; }
    public String getPrintedAt() { return printedAt; }
    public void setPrintedAt(String printedAt) { this.printedAt = printedAt; }
    public String getOrgCode() { return orgCode; }
    public void setOrgCode(String orgCode) { this.orgCode = orgCode; }
    public String getOfficeCode() { return officeCode; }
    public void setOfficeCode(String officeCode) { this.officeCode = officeCode; }
    public String getUserCode() { return userCode; }
    public void setUserCode(String userCode) { this.userCode = userCode; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getUserTelephone() { return userTelephone; }
    public void setUserTelephone(String userTelephone) { this.userTelephone = userTelephone; }
}
