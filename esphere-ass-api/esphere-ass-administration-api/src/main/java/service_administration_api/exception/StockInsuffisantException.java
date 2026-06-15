package service_administration_api.exception;

public class StockInsuffisantException extends RuntimeException {

    private final String officeCode;
    private final String certTypeCode;
    private final int demande;
    private final int disponible;

    public StockInsuffisantException(String officeCode, String certTypeCode, int demande, int disponible) {
        super(String.format(
            "Stock insuffisant pour le bureau %s (type=%s) : demandé=%d, disponible=%d",
            officeCode, certTypeCode != null ? certTypeCode : "GLOBAL", demande, disponible
        ));
        this.officeCode   = officeCode;
        this.certTypeCode = certTypeCode;
        this.demande      = demande;
        this.disponible   = disponible;
    }

    public String getOfficeCode()   { return officeCode; }
    public String getCertTypeCode() { return certTypeCode; }
    public int getDemande()         { return demande; }
    public int getDisponible()      { return disponible; }
}
