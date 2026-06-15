package service_administration_api.DTO.stock;

import service_administration_api.entite.pooltpv.StockAttestation;

public class StockAttestationDTO {

    private Long    id;
    private String  officeCode;
    private String  officeName;
    private String  orgCode;
    private String  certTypeCode;
    private String  certTypeName;
    private String  certVariantCode;
    private String  certVariantName;
    private Integer quantiteDisponible;
    private Integer quantiteReservee;
    private Integer quantiteTotaleApprovisionnee;
    private Integer quantiteTotalConsommee;
    private Integer seuilAlerte;
    private Integer seuilCritique;
    private String  statut;
    private String  createdAt;
    private String  updatedAt;
    private String  createdBy;

    // ── Factory ─────────────────────────────────────────────────
    public static StockAttestationDTO from(StockAttestation s) {
        StockAttestationDTO dto = new StockAttestationDTO();
        dto.id                          = s.getId();
        dto.officeCode                  = s.getOfficeCode();
        dto.officeName                  = s.getOfficeName();
        dto.orgCode                     = s.getOrgCode();
        dto.certTypeCode                = s.getCertTypeCode();
        dto.certTypeName                = s.getCertTypeName();
        dto.certVariantCode             = s.getCertVariantCode();
        dto.certVariantName             = s.getCertVariantName();
        dto.quantiteDisponible          = s.getQuantiteDisponible();
        dto.quantiteReservee            = s.getQuantiteReservee();
        dto.quantiteTotaleApprovisionnee= s.getQuantiteTotaleApprovisionnee();
        dto.quantiteTotalConsommee      = s.getQuantiteTotalConsommee();
        dto.seuilAlerte                 = s.getSeuilAlerte();
        dto.seuilCritique               = s.getSeuilCritique();
        dto.statut                      = s.getStatut();
        dto.createdAt  = s.getCreatedAt()  != null ? s.getCreatedAt().toString()  : null;
        dto.updatedAt  = s.getUpdatedAt()  != null ? s.getUpdatedAt().toString()  : null;
        dto.createdBy  = s.getCreatedBy();
        return dto;
    }

    // ── Getters ─────────────────────────────────────────────────
    public Long    getId()                           { return id; }
    public String  getOfficeCode()                   { return officeCode; }
    public String  getOfficeName()                   { return officeName; }
    public String  getOrgCode()                      { return orgCode; }
    public String  getCertTypeCode()                 { return certTypeCode; }
    public String  getCertTypeName()                 { return certTypeName; }
    public String  getCertVariantCode()              { return certVariantCode; }
    public String  getCertVariantName()              { return certVariantName; }
    public Integer getQuantiteDisponible()           { return quantiteDisponible; }
    public Integer getQuantiteReservee()             { return quantiteReservee; }
    public Integer getQuantiteTotaleApprovisionnee() { return quantiteTotaleApprovisionnee; }
    public Integer getQuantiteTotalConsommee()       { return quantiteTotalConsommee; }
    public Integer getSeuilAlerte()                  { return seuilAlerte; }
    public Integer getSeuilCritique()                { return seuilCritique; }
    public String  getStatut()                       { return statut; }
    public String  getCreatedAt()                    { return createdAt; }
    public String  getUpdatedAt()                    { return updatedAt; }
    public String  getCreatedBy()                    { return createdBy; }
}
