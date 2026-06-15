/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "CONVENTION")
@NamedQueries({
    @NamedQuery(name = "Convention.findAll", query = "SELECT c FROM Convention c")})
public class Convention implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "CODECONV")
    private String codeconv;
    @Size(max = 30)
    @Column(name = "LIBECONV")
    private String libeconv;
    @Size(max = 1)
    @Column(name = "FLAGREDU")
    private String flagredu;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TAUXREDU")
    private BigDecimal tauxredu;
    @Size(max = 4)
    @Column(name = "FLAEXOTG")
    private String flaexotg;
    @Column(name = "MONTIMGR")
    private BigDecimal montimgr;
    @Column(name = "DATEEFFE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateeffe;
    @Column(name = "DATERESI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateresi;
    @Size(max = 30)
    @Column(name = "PERSCONT")
    private String perscont;
    @Size(max = 60)
    @Column(name = "ADRECONT")
    private String adrecont;
    @Size(max = 20)
    @Column(name = "TELECONT")
    private String telecont;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 2)
    @Column(name = "CODEREDU")
    private String coderedu;
    @Column(name = "MONTACCE")
    private BigDecimal montacce;
    @Size(max = 3)
    @Column(name = "CODREDFL")
    private String codredfl;
    @Size(max = 1)
    @Column(name = "FLAREDFL")
    private String flaredfl;
    @Size(max = 60)
    @Column(name = "TELCOPCL")
    private String telcopcl;
    @Size(max = 200)
    @Column(name = "SERVCLIE")
    private String servclie;
    @Size(max = 60)
    @Column(name = "CONT_CIE")
    private String contCie;
    @Size(max = 200)
    @Column(name = "SERV_CIE")
    private String servCie;
    @Size(max = 60)
    @Column(name = "TELE_CIE")
    private String teleCie;
    @Size(max = 60)
    @Column(name = "TELCOPCI")
    private String telcopci;
    @Size(max = 100)
    @Column(name = "SOCICONV")
    private String sociconv;
    @Size(max = 30)
    @Column(name = "REFEPOLI")
    private String refepoli;
    @Column(name = "PRIMFORF")
    private BigDecimal primforf;
    @JoinColumn(name = "CODREGBM", referencedColumnName = "CODREGBM")
    @ManyToOne
    private RegimeBonusMalus codregbm;
    @JoinColumn(name = "CODTYPTA", referencedColumnName = "CODTYPTA")
    @ManyToOne
    private TypeTarif codtypta;
    @OneToMany(mappedBy = "codeconv")
    private List<Police> policeList;

    public Convention() {
    }

    public Convention(String codeconv) {
        this.codeconv = codeconv;
    }

    public String getCodeconv() {
        return codeconv;
    }

    public void setCodeconv(String codeconv) {
        this.codeconv = codeconv;
    }

    public String getLibeconv() {
        return libeconv;
    }

    public void setLibeconv(String libeconv) {
        this.libeconv = libeconv;
    }

    public String getFlagredu() {
        return flagredu;
    }

    public void setFlagredu(String flagredu) {
        this.flagredu = flagredu;
    }

    public BigDecimal getTauxredu() {
        return tauxredu;
    }

    public void setTauxredu(BigDecimal tauxredu) {
        this.tauxredu = tauxredu;
    }

    public String getFlaexotg() {
        return flaexotg;
    }

    public void setFlaexotg(String flaexotg) {
        this.flaexotg = flaexotg;
    }

    public BigDecimal getMontimgr() {
        return montimgr;
    }

    public void setMontimgr(BigDecimal montimgr) {
        this.montimgr = montimgr;
    }

    public Date getDateeffe() {
        return dateeffe;
    }

    public void setDateeffe(Date dateeffe) {
        this.dateeffe = dateeffe;
    }

    public Date getDateresi() {
        return dateresi;
    }

    public void setDateresi(Date dateresi) {
        this.dateresi = dateresi;
    }

    public String getPerscont() {
        return perscont;
    }

    public void setPerscont(String perscont) {
        this.perscont = perscont;
    }

    public String getAdrecont() {
        return adrecont;
    }

    public void setAdrecont(String adrecont) {
        this.adrecont = adrecont;
    }

    public String getTelecont() {
        return telecont;
    }

    public void setTelecont(String telecont) {
        this.telecont = telecont;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getCoderedu() {
        return coderedu;
    }

    public void setCoderedu(String coderedu) {
        this.coderedu = coderedu;
    }

    public BigDecimal getMontacce() {
        return montacce;
    }

    public void setMontacce(BigDecimal montacce) {
        this.montacce = montacce;
    }

    public String getCodredfl() {
        return codredfl;
    }

    public void setCodredfl(String codredfl) {
        this.codredfl = codredfl;
    }

    public String getFlaredfl() {
        return flaredfl;
    }

    public void setFlaredfl(String flaredfl) {
        this.flaredfl = flaredfl;
    }

    public String getTelcopcl() {
        return telcopcl;
    }

    public void setTelcopcl(String telcopcl) {
        this.telcopcl = telcopcl;
    }

    public String getServclie() {
        return servclie;
    }

    public void setServclie(String servclie) {
        this.servclie = servclie;
    }

    public String getContCie() {
        return contCie;
    }

    public void setContCie(String contCie) {
        this.contCie = contCie;
    }

    public String getServCie() {
        return servCie;
    }

    public void setServCie(String servCie) {
        this.servCie = servCie;
    }

    public String getTeleCie() {
        return teleCie;
    }

    public void setTeleCie(String teleCie) {
        this.teleCie = teleCie;
    }

    public String getTelcopci() {
        return telcopci;
    }

    public void setTelcopci(String telcopci) {
        this.telcopci = telcopci;
    }

    public String getSociconv() {
        return sociconv;
    }

    public void setSociconv(String sociconv) {
        this.sociconv = sociconv;
    }

    public String getRefepoli() {
        return refepoli;
    }

    public void setRefepoli(String refepoli) {
        this.refepoli = refepoli;
    }

    public BigDecimal getPrimforf() {
        return primforf;
    }

    public void setPrimforf(BigDecimal primforf) {
        this.primforf = primforf;
    }

    public RegimeBonusMalus getCodregbm() {
        return codregbm;
    }

    public void setCodregbm(RegimeBonusMalus codregbm) {
        this.codregbm = codregbm;
    }

    public TypeTarif getCodtypta() {
        return codtypta;
    }

    public void setCodtypta(TypeTarif codtypta) {
        this.codtypta = codtypta;
    }

    public List<Police> getPoliceList() {
        return policeList;
    }

    public void setPoliceList(List<Police> policeList) {
        this.policeList = policeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeconv != null ? codeconv.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Convention)) {
            return false;
        }
        Convention other = (Convention) object;
        if ((this.codeconv == null && other.codeconv != null) || (this.codeconv != null && !this.codeconv.equals(other.codeconv))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Convention[ codeconv=" + codeconv + " ]";
    }
    
}
