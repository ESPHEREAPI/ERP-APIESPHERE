package service_administration_api.DTO.stock;

public class AjustementStockRequest {

    private String  certTypeCode;
    private String  certVariantCode;
    /** Valeur absolue — le signe est porté par delta (positif = ajout, négatif = retrait). */
    private Integer delta;
    private String  motif;

    // ── Getters / Setters ────────────────────────────────────────
    public String  getCertTypeCode()    { return certTypeCode; }
    public void    setCertTypeCode(String v)    { this.certTypeCode = v; }
    public String  getCertVariantCode() { return certVariantCode; }
    public void    setCertVariantCode(String v) { this.certVariantCode = v; }
    public Integer getDelta()           { return delta; }
    public void    setDelta(Integer v)          { this.delta = v; }
    public String  getMotif()           { return motif; }
    public void    setMotif(String v)           { this.motif = v; }
}
