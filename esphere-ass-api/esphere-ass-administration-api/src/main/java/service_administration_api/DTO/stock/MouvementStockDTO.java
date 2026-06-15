package service_administration_api.DTO.stock;

import service_administration_api.entite.pooltpv.MouvementStock;

public class MouvementStockDTO {

    private Long   id;
    private Long   stockId;
    private String officeCode;
    private String certTypeCode;
    private String certVariantCode;
    private String typeMouvement;
    private Integer quantite;
    private Integer quantiteAvant;
    private Integer quantiteApres;
    private String referenceSource;
    private String motif;
    private String createdAt;
    private String createdBy;

    // ── Factory ─────────────────────────────────────────────────
    public static MouvementStockDTO from(MouvementStock m) {
        MouvementStockDTO dto = new MouvementStockDTO();
        dto.id               = m.getId();
        dto.stockId          = m.getStockAttestation() != null ? m.getStockAttestation().getId() : null;
        dto.officeCode       = m.getOfficeCode();
        dto.certTypeCode     = m.getCertTypeCode();
        dto.certVariantCode  = m.getCertVariantCode();
        dto.typeMouvement    = m.getTypeMouvement()   != null ? m.getTypeMouvement().name() : null;
        dto.quantite         = m.getQuantite();
        dto.quantiteAvant    = m.getQuantiteAvant();
        dto.quantiteApres    = m.getQuantiteApres();
        dto.referenceSource  = m.getReferenceSource();
        dto.motif            = m.getMotif();
        dto.createdAt        = m.getCreatedAt() != null ? m.getCreatedAt().toString() : null;
        dto.createdBy        = m.getCreatedBy();
        return dto;
    }

    // ── Getters ─────────────────────────────────────────────────
    public Long    getId()              { return id; }
    public Long    getStockId()         { return stockId; }
    public String  getOfficeCode()      { return officeCode; }
    public String  getCertTypeCode()    { return certTypeCode; }
    public String  getCertVariantCode() { return certVariantCode; }
    public String  getTypeMouvement()   { return typeMouvement; }
    public Integer getQuantite()        { return quantite; }
    public Integer getQuantiteAvant()   { return quantiteAvant; }
    public Integer getQuantiteApres()   { return quantiteApres; }
    public String  getReferenceSource() { return referenceSource; }
    public String  getMotif()           { return motif; }
    public String  getCreatedAt()       { return createdAt; }
    public String  getCreatedBy()       { return createdBy; }
}
