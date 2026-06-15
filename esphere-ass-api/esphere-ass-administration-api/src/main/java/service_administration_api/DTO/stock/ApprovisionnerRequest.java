package service_administration_api.DTO.stock;

public class ApprovisionnerRequest {

    private String  certTypeCode;     // null = stock global
    private String  certVariantCode;  // null = toutes variantes
    private Integer quantite;         // obligatoire, > 0
    private String  referenceSource;  // n° bon de livraison / bordereau
    private String  motif;

    // ── Getters / Setters ────────────────────────────────────────
    public String  getCertTypeCode()    { return certTypeCode; }
    public void    setCertTypeCode(String v)    { this.certTypeCode = v; }
    public String  getCertVariantCode() { return certVariantCode; }
    public void    setCertVariantCode(String v) { this.certVariantCode = v; }
    public Integer getQuantite()        { return quantite; }
    public void    setQuantite(Integer v)       { this.quantite = v; }
    public String  getReferenceSource() { return referenceSource; }
    public void    setReferenceSource(String v) { this.referenceSource = v; }
    public String  getMotif()           { return motif; }
    public void    setMotif(String v)           { this.motif = v; }
}
