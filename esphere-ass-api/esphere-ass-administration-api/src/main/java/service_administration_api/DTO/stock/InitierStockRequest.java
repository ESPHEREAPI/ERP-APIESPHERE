package service_administration_api.DTO.stock;

public class InitierStockRequest {

    private String  officeCode;       // obligatoire
    private String  officeName;
    private String  orgCode;
    private String  certTypeCode;     // null = stock global
    private String  certTypeName;
    private String  certVariantCode;  // null = toutes variantes
    private String  certVariantName;
    private Integer quantiteInitiale; // stock de départ
    private Integer seuilAlerte;      // défaut 50
    private Integer seuilCritique;    // défaut 10
    private String  motif;

    // ── Getters / Setters ────────────────────────────────────────
    public String  getOfficeCode()       { return officeCode; }
    public void    setOfficeCode(String v)       { this.officeCode = v; }
    public String  getOfficeName()       { return officeName; }
    public void    setOfficeName(String v)       { this.officeName = v; }
    public String  getOrgCode()          { return orgCode; }
    public void    setOrgCode(String v)          { this.orgCode = v; }
    public String  getCertTypeCode()     { return certTypeCode; }
    public void    setCertTypeCode(String v)     { this.certTypeCode = v; }
    public String  getCertTypeName()     { return certTypeName; }
    public void    setCertTypeName(String v)     { this.certTypeName = v; }
    public String  getCertVariantCode()  { return certVariantCode; }
    public void    setCertVariantCode(String v)  { this.certVariantCode = v; }
    public String  getCertVariantName()  { return certVariantName; }
    public void    setCertVariantName(String v)  { this.certVariantName = v; }
    public Integer getQuantiteInitiale() { return quantiteInitiale; }
    public void    setQuantiteInitiale(Integer v){ this.quantiteInitiale = v; }
    public Integer getSeuilAlerte()      { return seuilAlerte; }
    public void    setSeuilAlerte(Integer v)     { this.seuilAlerte = v; }
    public Integer getSeuilCritique()    { return seuilCritique; }
    public void    setSeuilCritique(Integer v)   { this.seuilCritique = v; }
    public String  getMotif()            { return motif; }
    public void    setMotif(String v)            { this.motif = v; }
}
