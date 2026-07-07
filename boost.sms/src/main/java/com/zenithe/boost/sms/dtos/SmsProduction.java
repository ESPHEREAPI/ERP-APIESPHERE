/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.persistence.Entity
 *  javax.persistence.Id
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.Temporal
 *  javax.persistence.TemporalType
 */
package com.zenithe.boost.sms.dtos;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries(value={@NamedQuery(name="SmsProduction.findAll", query="SELECT s FROM SmsProduction s")})
public class SmsProduction
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    private String TELEASSU;
    private Integer CODEINTE;
    private String BUREAU;
    private Long NUMEPOLI;
    private Integer NUMEAVEN;
    private String CHARGER_CLI;
    private String CHARGER_CLI_TEL;
    private String LANGUE;
    private String BRANCHE;
    private String NOMASSURE;
    private long CODECATE;
    private String LIBECATE;
    @Temporal(value=TemporalType.TIMESTAMP)
    private Date DATEEMIS;
    @Temporal(value=TemporalType.TIMESTAMP)
    private Date DATEEFFE;
    @Temporal(value=TemporalType.TIMESTAMP)
    private Date DATEECHE;
    private Long PRIMNETT;
    private Long PRIMTOTA;

    public String getTELEASSU() {
        return this.TELEASSU;
    }

    public Integer getCODEINTE() {
        return this.CODEINTE;
    }

    public String getBUREAU() {
        return this.BUREAU;
    }

    public Long getNUMEPOLI() {
        return this.NUMEPOLI;
    }

    public Integer getNUMEAVEN() {
        return this.NUMEAVEN;
    }

    public String getCHARGER_CLI() {
        return this.CHARGER_CLI;
    }

    public String getCHARGER_CLI_TEL() {
        return this.CHARGER_CLI_TEL;
    }

    public String getLANGUE() {
        return this.LANGUE;
    }

    public String getBRANCHE() {
        return this.BRANCHE;
    }

    public String getNOMASSURE() {
        return this.NOMASSURE;
    }

    public long getCODECATE() {
        return this.CODECATE;
    }

    public String getLIBECATE() {
        return this.LIBECATE;
    }

    public Date getDATEEMIS() {
        return this.DATEEMIS;
    }

    public Date getDATEEFFE() {
        return this.DATEEFFE;
    }

    public Date getDATEECHE() {
        return this.DATEECHE;
    }

    public Long getPRIMNETT() {
        return this.PRIMNETT;
    }

    public Long getPRIMTOTA() {
        return this.PRIMTOTA;
    }

    public void setTELEASSU(String TELEASSU) {
        this.TELEASSU = TELEASSU;
    }

    public void setCODEINTE(Integer CODEINTE) {
        this.CODEINTE = CODEINTE;
    }

    public void setBUREAU(String BUREAU) {
        this.BUREAU = BUREAU;
    }

    public void setNUMEPOLI(Long NUMEPOLI) {
        this.NUMEPOLI = NUMEPOLI;
    }

    public void setNUMEAVEN(Integer NUMEAVEN) {
        this.NUMEAVEN = NUMEAVEN;
    }

    public void setCHARGER_CLI(String CHARGER_CLI) {
        this.CHARGER_CLI = CHARGER_CLI;
    }

    public void setCHARGER_CLI_TEL(String CHARGER_CLI_TEL) {
        this.CHARGER_CLI_TEL = CHARGER_CLI_TEL;
    }

    public void setLANGUE(String LANGUE) {
        this.LANGUE = LANGUE;
    }

    public void setBRANCHE(String BRANCHE) {
        this.BRANCHE = BRANCHE;
    }

    public void setNOMASSURE(String NOMASSURE) {
        this.NOMASSURE = NOMASSURE;
    }

    public void setCODECATE(long CODECATE) {
        this.CODECATE = CODECATE;
    }

    public void setLIBECATE(String LIBECATE) {
        this.LIBECATE = LIBECATE;
    }

    public void setDATEEMIS(Date DATEEMIS) {
        this.DATEEMIS = DATEEMIS;
    }

    public void setDATEEFFE(Date DATEEFFE) {
        this.DATEEFFE = DATEEFFE;
    }

    public void setDATEECHE(Date DATEECHE) {
        this.DATEECHE = DATEECHE;
    }

    public void setPRIMNETT(Long PRIMNETT) {
        this.PRIMNETT = PRIMNETT;
    }

    public void setPRIMTOTA(Long PRIMTOTA) {
        this.PRIMTOTA = PRIMTOTA;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SmsProduction)) {
            return false;
        }
        SmsProduction other = (SmsProduction)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getCODECATE() != other.getCODECATE()) {
            return false;
        }
        Integer this$CODEINTE = this.getCODEINTE();
        Integer other$CODEINTE = other.getCODEINTE();
        if (this$CODEINTE == null ? other$CODEINTE != null : !((Object)this$CODEINTE).equals(other$CODEINTE)) {
            return false;
        }
        Long this$NUMEPOLI = this.getNUMEPOLI();
        Long other$NUMEPOLI = other.getNUMEPOLI();
        if (this$NUMEPOLI == null ? other$NUMEPOLI != null : !((Object)this$NUMEPOLI).equals(other$NUMEPOLI)) {
            return false;
        }
        Integer this$NUMEAVEN = this.getNUMEAVEN();
        Integer other$NUMEAVEN = other.getNUMEAVEN();
        if (this$NUMEAVEN == null ? other$NUMEAVEN != null : !((Object)this$NUMEAVEN).equals(other$NUMEAVEN)) {
            return false;
        }
        Long this$PRIMNETT = this.getPRIMNETT();
        Long other$PRIMNETT = other.getPRIMNETT();
        if (this$PRIMNETT == null ? other$PRIMNETT != null : !((Object)this$PRIMNETT).equals(other$PRIMNETT)) {
            return false;
        }
        Long this$PRIMTOTA = this.getPRIMTOTA();
        Long other$PRIMTOTA = other.getPRIMTOTA();
        if (this$PRIMTOTA == null ? other$PRIMTOTA != null : !((Object)this$PRIMTOTA).equals(other$PRIMTOTA)) {
            return false;
        }
        String this$TELEASSU = this.getTELEASSU();
        String other$TELEASSU = other.getTELEASSU();
        if (this$TELEASSU == null ? other$TELEASSU != null : !this$TELEASSU.equals(other$TELEASSU)) {
            return false;
        }
        String this$BUREAU = this.getBUREAU();
        String other$BUREAU = other.getBUREAU();
        if (this$BUREAU == null ? other$BUREAU != null : !this$BUREAU.equals(other$BUREAU)) {
            return false;
        }
        String this$CHARGER_CLI = this.getCHARGER_CLI();
        String other$CHARGER_CLI = other.getCHARGER_CLI();
        if (this$CHARGER_CLI == null ? other$CHARGER_CLI != null : !this$CHARGER_CLI.equals(other$CHARGER_CLI)) {
            return false;
        }
        String this$CHARGER_CLI_TEL = this.getCHARGER_CLI_TEL();
        String other$CHARGER_CLI_TEL = other.getCHARGER_CLI_TEL();
        if (this$CHARGER_CLI_TEL == null ? other$CHARGER_CLI_TEL != null : !this$CHARGER_CLI_TEL.equals(other$CHARGER_CLI_TEL)) {
            return false;
        }
        String this$LANGUE = this.getLANGUE();
        String other$LANGUE = other.getLANGUE();
        if (this$LANGUE == null ? other$LANGUE != null : !this$LANGUE.equals(other$LANGUE)) {
            return false;
        }
        String this$BRANCHE = this.getBRANCHE();
        String other$BRANCHE = other.getBRANCHE();
        if (this$BRANCHE == null ? other$BRANCHE != null : !this$BRANCHE.equals(other$BRANCHE)) {
            return false;
        }
        String this$NOMASSURE = this.getNOMASSURE();
        String other$NOMASSURE = other.getNOMASSURE();
        if (this$NOMASSURE == null ? other$NOMASSURE != null : !this$NOMASSURE.equals(other$NOMASSURE)) {
            return false;
        }
        String this$LIBECATE = this.getLIBECATE();
        String other$LIBECATE = other.getLIBECATE();
        if (this$LIBECATE == null ? other$LIBECATE != null : !this$LIBECATE.equals(other$LIBECATE)) {
            return false;
        }
        Date this$DATEEMIS = this.getDATEEMIS();
        Date other$DATEEMIS = other.getDATEEMIS();
        if (this$DATEEMIS == null ? other$DATEEMIS != null : !((Object)this$DATEEMIS).equals(other$DATEEMIS)) {
            return false;
        }
        Date this$DATEEFFE = this.getDATEEFFE();
        Date other$DATEEFFE = other.getDATEEFFE();
        if (this$DATEEFFE == null ? other$DATEEFFE != null : !((Object)this$DATEEFFE).equals(other$DATEEFFE)) {
            return false;
        }
        Date this$DATEECHE = this.getDATEECHE();
        Date other$DATEECHE = other.getDATEECHE();
        return !(this$DATEECHE == null ? other$DATEECHE != null : !((Object)this$DATEECHE).equals(other$DATEECHE));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SmsProduction;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $CODECATE = this.getCODECATE();
        result = result * 59 + (int)($CODECATE >>> 32 ^ $CODECATE);
        Integer $CODEINTE = this.getCODEINTE();
        result = result * 59 + ($CODEINTE == null ? 43 : ((Object)$CODEINTE).hashCode());
        Long $NUMEPOLI = this.getNUMEPOLI();
        result = result * 59 + ($NUMEPOLI == null ? 43 : ((Object)$NUMEPOLI).hashCode());
        Integer $NUMEAVEN = this.getNUMEAVEN();
        result = result * 59 + ($NUMEAVEN == null ? 43 : ((Object)$NUMEAVEN).hashCode());
        Long $PRIMNETT = this.getPRIMNETT();
        result = result * 59 + ($PRIMNETT == null ? 43 : ((Object)$PRIMNETT).hashCode());
        Long $PRIMTOTA = this.getPRIMTOTA();
        result = result * 59 + ($PRIMTOTA == null ? 43 : ((Object)$PRIMTOTA).hashCode());
        String $TELEASSU = this.getTELEASSU();
        result = result * 59 + ($TELEASSU == null ? 43 : $TELEASSU.hashCode());
        String $BUREAU = this.getBUREAU();
        result = result * 59 + ($BUREAU == null ? 43 : $BUREAU.hashCode());
        String $CHARGER_CLI = this.getCHARGER_CLI();
        result = result * 59 + ($CHARGER_CLI == null ? 43 : $CHARGER_CLI.hashCode());
        String $CHARGER_CLI_TEL = this.getCHARGER_CLI_TEL();
        result = result * 59 + ($CHARGER_CLI_TEL == null ? 43 : $CHARGER_CLI_TEL.hashCode());
        String $LANGUE = this.getLANGUE();
        result = result * 59 + ($LANGUE == null ? 43 : $LANGUE.hashCode());
        String $BRANCHE = this.getBRANCHE();
        result = result * 59 + ($BRANCHE == null ? 43 : $BRANCHE.hashCode());
        String $NOMASSURE = this.getNOMASSURE();
        result = result * 59 + ($NOMASSURE == null ? 43 : $NOMASSURE.hashCode());
        String $LIBECATE = this.getLIBECATE();
        result = result * 59 + ($LIBECATE == null ? 43 : $LIBECATE.hashCode());
        Date $DATEEMIS = this.getDATEEMIS();
        result = result * 59 + ($DATEEMIS == null ? 43 : ((Object)$DATEEMIS).hashCode());
        Date $DATEEFFE = this.getDATEEFFE();
        result = result * 59 + ($DATEEFFE == null ? 43 : ((Object)$DATEEFFE).hashCode());
        Date $DATEECHE = this.getDATEECHE();
        result = result * 59 + ($DATEECHE == null ? 43 : ((Object)$DATEECHE).hashCode());
        return result;
    }

    public String toString() {
        return "SmsProduction(TELEASSU=" + this.getTELEASSU() + ", CODEINTE=" + this.getCODEINTE() + ", BUREAU=" + this.getBUREAU() + ", NUMEPOLI=" + this.getNUMEPOLI() + ", NUMEAVEN=" + this.getNUMEAVEN() + ", CHARGER_CLI=" + this.getCHARGER_CLI() + ", CHARGER_CLI_TEL=" + this.getCHARGER_CLI_TEL() + ", LANGUE=" + this.getLANGUE() + ", BRANCHE=" + this.getBRANCHE() + ", NOMASSURE=" + this.getNOMASSURE() + ", CODECATE=" + this.getCODECATE() + ", LIBECATE=" + this.getLIBECATE() + ", DATEEMIS=" + this.getDATEEMIS() + ", DATEEFFE=" + this.getDATEEFFE() + ", DATEECHE=" + this.getDATEECHE() + ", PRIMNETT=" + this.getPRIMNETT() + ", PRIMTOTA=" + this.getPRIMTOTA() + ")";
    }
}
