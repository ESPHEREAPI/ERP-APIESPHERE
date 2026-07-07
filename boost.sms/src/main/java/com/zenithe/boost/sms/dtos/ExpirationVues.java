/*
 * Decompiled with CFR 0.152.
 */
package com.zenithe.boost.sms.dtos;

import java.util.Date;

public class ExpirationVues {
    private String TELEASSU;
    private Long CODEINTE;
    private Long NUMEPOLI;
    private String NUMEIMMA;
    private Long NUMEAVEN;
    private String NOMASSURE;
    private Date DATEECHE;
    private String LANGUE;

    public String getTELEASSU() {
        return this.TELEASSU;
    }

    public Long getCODEINTE() {
        return this.CODEINTE;
    }

    public Long getNUMEPOLI() {
        return this.NUMEPOLI;
    }

    public String getNUMEIMMA() {
        return this.NUMEIMMA;
    }

    public Long getNUMEAVEN() {
        return this.NUMEAVEN;
    }

    public String getNOMASSURE() {
        return this.NOMASSURE;
    }

    public Date getDATEECHE() {
        return this.DATEECHE;
    }

    public String getLANGUE() {
        return this.LANGUE;
    }

    public void setTELEASSU(String TELEASSU) {
        this.TELEASSU = TELEASSU;
    }

    public void setCODEINTE(Long CODEINTE) {
        this.CODEINTE = CODEINTE;
    }

    public void setNUMEPOLI(Long NUMEPOLI) {
        this.NUMEPOLI = NUMEPOLI;
    }

    public void setNUMEIMMA(String NUMEIMMA) {
        this.NUMEIMMA = NUMEIMMA;
    }

    public void setNUMEAVEN(Long NUMEAVEN) {
        this.NUMEAVEN = NUMEAVEN;
    }

    public void setNOMASSURE(String NOMASSURE) {
        this.NOMASSURE = NOMASSURE;
    }

    public void setDATEECHE(Date DATEECHE) {
        this.DATEECHE = DATEECHE;
    }

    public void setLANGUE(String LANGUE) {
        this.LANGUE = LANGUE;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ExpirationVues)) {
            return false;
        }
        ExpirationVues other = (ExpirationVues)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Long this$CODEINTE = this.getCODEINTE();
        Long other$CODEINTE = other.getCODEINTE();
        if (this$CODEINTE == null ? other$CODEINTE != null : !((Object)this$CODEINTE).equals(other$CODEINTE)) {
            return false;
        }
        Long this$NUMEPOLI = this.getNUMEPOLI();
        Long other$NUMEPOLI = other.getNUMEPOLI();
        if (this$NUMEPOLI == null ? other$NUMEPOLI != null : !((Object)this$NUMEPOLI).equals(other$NUMEPOLI)) {
            return false;
        }
        Long this$NUMEAVEN = this.getNUMEAVEN();
        Long other$NUMEAVEN = other.getNUMEAVEN();
        if (this$NUMEAVEN == null ? other$NUMEAVEN != null : !((Object)this$NUMEAVEN).equals(other$NUMEAVEN)) {
            return false;
        }
        String this$TELEASSU = this.getTELEASSU();
        String other$TELEASSU = other.getTELEASSU();
        if (this$TELEASSU == null ? other$TELEASSU != null : !this$TELEASSU.equals(other$TELEASSU)) {
            return false;
        }
        String this$NUMEIMMA = this.getNUMEIMMA();
        String other$NUMEIMMA = other.getNUMEIMMA();
        if (this$NUMEIMMA == null ? other$NUMEIMMA != null : !this$NUMEIMMA.equals(other$NUMEIMMA)) {
            return false;
        }
        String this$NOMASSURE = this.getNOMASSURE();
        String other$NOMASSURE = other.getNOMASSURE();
        if (this$NOMASSURE == null ? other$NOMASSURE != null : !this$NOMASSURE.equals(other$NOMASSURE)) {
            return false;
        }
        Date this$DATEECHE = this.getDATEECHE();
        Date other$DATEECHE = other.getDATEECHE();
        if (this$DATEECHE == null ? other$DATEECHE != null : !((Object)this$DATEECHE).equals(other$DATEECHE)) {
            return false;
        }
        String this$LANGUE = this.getLANGUE();
        String other$LANGUE = other.getLANGUE();
        return !(this$LANGUE == null ? other$LANGUE != null : !this$LANGUE.equals(other$LANGUE));
    }

    protected boolean canEqual(Object other) {
        return other instanceof ExpirationVues;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Long $CODEINTE = this.getCODEINTE();
        result = result * 59 + ($CODEINTE == null ? 43 : ((Object)$CODEINTE).hashCode());
        Long $NUMEPOLI = this.getNUMEPOLI();
        result = result * 59 + ($NUMEPOLI == null ? 43 : ((Object)$NUMEPOLI).hashCode());
        Long $NUMEAVEN = this.getNUMEAVEN();
        result = result * 59 + ($NUMEAVEN == null ? 43 : ((Object)$NUMEAVEN).hashCode());
        String $TELEASSU = this.getTELEASSU();
        result = result * 59 + ($TELEASSU == null ? 43 : $TELEASSU.hashCode());
        String $NUMEIMMA = this.getNUMEIMMA();
        result = result * 59 + ($NUMEIMMA == null ? 43 : $NUMEIMMA.hashCode());
        String $NOMASSURE = this.getNOMASSURE();
        result = result * 59 + ($NOMASSURE == null ? 43 : $NOMASSURE.hashCode());
        Date $DATEECHE = this.getDATEECHE();
        result = result * 59 + ($DATEECHE == null ? 43 : ((Object)$DATEECHE).hashCode());
        String $LANGUE = this.getLANGUE();
        result = result * 59 + ($LANGUE == null ? 43 : $LANGUE.hashCode());
        return result;
    }

    public String toString() {
        return "ExpirationVues(TELEASSU=" + this.getTELEASSU() + ", CODEINTE=" + this.getCODEINTE() + ", NUMEPOLI=" + this.getNUMEPOLI() + ", NUMEIMMA=" + this.getNUMEIMMA() + ", NUMEAVEN=" + this.getNUMEAVEN() + ", NOMASSURE=" + this.getNOMASSURE() + ", DATEECHE=" + this.getDATEECHE() + ", LANGUE=" + this.getLANGUE() + ")";
    }
}
