/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite.pooltpv;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "ZEN_PRODUCTION_PAYLOAD")
@Getter
@Setter
public class ProductionPayload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_production_payload")
    @SequenceGenerator(name = "seq_production_payload", sequenceName = "SEQ_PRODUCTION_PAYLOAD", allocationSize = 1)
    private Long id;

   // --- Infos Production ---
    @Column(name = "EXTERNAL_ID")           private String externalId;       // "a1b2c3d4e5"
    @Column(name = "REFERENCE")             private String reference;        // "PROD-2026-00456"
    @Column(name = "CHANNEL")               private String channel;
    @Column(name = "QUANTITY")              private Integer quantity;
    @Column(name = "SENT_TO_STORAGE")       private Boolean sentToStorage;
    @Column(name = "DOWNLOAD_LINK", length = 500) private String downloadLink;

    // --- User ---
    @Column(name = "USER_ID")              private String userId;
    @Column(name = "USER_NAME")            private String userName;
    @Column(name = "USER_EMAIL")           private String userEmail;

    // --- Organisation ---
    @Column(name = "ORG_ID")              private String orgId;
    @Column(name = "ORG_NAME")            private String orgName;
    @Column(name = "ORG_CODE")            private String orgCode;

    // --- Office ---
    @Column(name = "OFFICE_ID")           private String officeId;
    @Column(name = "OFFICE_NAME")         private String officeName;
    @Column(name = "OFFICE_CODE")         private String officeCode;

    // --- Dates ---
    @Column(name = "CREATED_AT")          private OffsetDateTime createdAt;
    @Column(name = "UPDATED_AT")          private OffsetDateTime updatedAt;
    @Column(name = "FORMATTED_CREATED_AT") private String formattedCreatedAt;

    // Relation One-To-Many : une production a plusieurs certificats
    @OneToMany(mappedBy = "production", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CertificatePlayLoad> certificates = new ArrayList<>();

    // Constructeur vide obligatoire JPA
    public ProductionPayload() {}

    // Getters & Setters
    public Long getId() { return id; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public String getReference() { return reference; }
    public void setReference(String reference) { this.reference = reference; }
    public String getChannel() { return channel; }
    public void setChannel(String channel) { this.channel = channel; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Boolean getSentToStorage() { return sentToStorage; }
    public void setSentToStorage(Boolean sentToStorage) { this.sentToStorage = sentToStorage; }
    public String getDownloadLink() { return downloadLink; }
    public void setDownloadLink(String downloadLink) { this.downloadLink = downloadLink; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getOrgId() { return orgId; }
    public void setOrgId(String orgId) { this.orgId = orgId; }
    public String getOrgName() { return orgName; }
    public void setOrgName(String orgName) { this.orgName = orgName; }
    public String getOrgCode() { return orgCode; }
    public void setOrgCode(String orgCode) { this.orgCode = orgCode; }
    public String getOfficeId() { return officeId; }
    public void setOfficeId(String officeId) { this.officeId = officeId; }
    public String getOfficeName() { return officeName; }
    public void setOfficeName(String officeName) { this.officeName = officeName; }
    public String getOfficeCode() { return officeCode; }
    public void setOfficeCode(String officeCode) { this.officeCode = officeCode; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getFormattedCreatedAt() { return formattedCreatedAt; }
    public void setFormattedCreatedAt(String formattedCreatedAt) { this.formattedCreatedAt = formattedCreatedAt; }
    public List<CertificatePlayLoad> getCertificates() { return certificates; }
    public void setCertificates(List<CertificatePlayLoad> certificates) { this.certificates = certificates; }
 
}
