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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "PROFIL_USER")
@NamedQueries({
    @NamedQuery(name = "ProfilUser.findAll", query = "SELECT p FROM ProfilUser p"),
    @NamedQuery(name = "ProfilUser.findByCodeprof", query = "SELECT p FROM ProfilUser p WHERE p.codeprof = :codeprof"),
    @NamedQuery(name = "ProfilUser.findByLibeprof", query = "SELECT p FROM ProfilUser p WHERE p.libeprof = :libeprof"),
    @NamedQuery(name = "ProfilUser.findByCodelang", query = "SELECT p FROM ProfilUser p WHERE p.codelang = :codelang"),
    @NamedQuery(name = "ProfilUser.findBySuivsini", query = "SELECT p FROM ProfilUser p WHERE p.suivsini = :suivsini"),
    @NamedQuery(name = "ProfilUser.findByAucrasva", query = "SELECT p FROM ProfilUser p WHERE p.aucrasva = :aucrasva"),
    @NamedQuery(name = "ProfilUser.findByCtrimpat", query = "SELECT p FROM ProfilUser p WHERE p.ctrimpat = :ctrimpat"),
    @NamedQuery(name = "ProfilUser.findByAczoatpr", query = "SELECT p FROM ProfilUser p WHERE p.aczoatpr = :aczoatpr"),
    @NamedQuery(name = "ProfilUser.findByForinteq", query = "SELECT p FROM ProfilUser p WHERE p.forinteq = :forinteq"),
    @NamedQuery(name = "ProfilUser.findByOusisusi", query = "SELECT p FROM ProfilUser p WHERE p.ousisusi = :ousisusi"),
    @NamedQuery(name = "ProfilUser.findByValtecre", query = "SELECT p FROM ProfilUser p WHERE p.valtecre = :valtecre"),
    @NamedQuery(name = "ProfilUser.findByValcomre", query = "SELECT p FROM ProfilUser p WHERE p.valcomre = :valcomre"),
    @NamedQuery(name = "ProfilUser.findByFixerecu", query = "SELECT p FROM ProfilUser p WHERE p.fixerecu = :fixerecu"),
    @NamedQuery(name = "ProfilUser.findByFlagstoc", query = "SELECT p FROM ProfilUser p WHERE p.flagstoc = :flagstoc"),
    @NamedQuery(name = "ProfilUser.findByFordevat", query = "SELECT p FROM ProfilUser p WHERE p.fordevat = :fordevat"),
    @NamedQuery(name = "ProfilUser.findByAbreuser", query = "SELECT p FROM ProfilUser p WHERE p.abreuser = :abreuser"),
    @NamedQuery(name = "ProfilUser.findByForintre", query = "SELECT p FROM ProfilUser p WHERE p.forintre = :forintre")})
public class ProfilUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODEPROF")
    private Integer codeprof;
    @Column(name = "LIBEPROF")
    private String libeprof;
    @Column(name = "CODELANG")
    private String codelang;
    @Column(name = "SUIVSINI")
    private String suivsini;
    @Column(name = "AUCRASVA")
    private String aucrasva;
    @Column(name = "CTRIMPAT")
    private String ctrimpat;
    @Column(name = "ACZOATPR")
    private String aczoatpr;
    @Column(name = "FORINTEQ")
    private String forinteq;
    @Column(name = "OUSISUSI")
    private String ousisusi;
    @Column(name = "VALTECRE")
    private String valtecre;
    @Column(name = "VALCOMRE")
    private String valcomre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "FIXERECU")
    private BigDecimal fixerecu;
    @Column(name = "FLAGSTOC")
    private String flagstoc;
    @Column(name = "FORDEVAT")
    private String fordevat;
    @Column(name = "ABREUSER")
    private String abreuser;
    @Column(name = "FORINTRE")
    private String forintre;
//    @JoinTable(name = "AFFECT_PROFIL_USER", joinColumns = {
//        @JoinColumn(name = "CODEPROF", referencedColumnName = "CODEPROF")}, inverseJoinColumns = {
//        @JoinColumn(name = "NOM_UTIL", referencedColumnName = "NOM_UTIL")})
//    @ManyToMany
//    private List<PosteTravail> posteTravailList;
//    @OneToMany(mappedBy = "codpropr")
//    private List<PosteTravail> posteTravailList1;

    public ProfilUser() {
    }

    public ProfilUser(Integer codeprof) {
        this.codeprof = codeprof;
    }

    public Integer getCodeprof() {
        return codeprof;
    }

    public void setCodeprof(Integer codeprof) {
        this.codeprof = codeprof;
    }

    public String getLibeprof() {
        return libeprof;
    }

    public void setLibeprof(String libeprof) {
        this.libeprof = libeprof;
    }

    public String getCodelang() {
        return codelang;
    }

    public void setCodelang(String codelang) {
        this.codelang = codelang;
    }

    public String getSuivsini() {
        return suivsini;
    }

    public void setSuivsini(String suivsini) {
        this.suivsini = suivsini;
    }

    public String getAucrasva() {
        return aucrasva;
    }

    public void setAucrasva(String aucrasva) {
        this.aucrasva = aucrasva;
    }

    public String getCtrimpat() {
        return ctrimpat;
    }

    public void setCtrimpat(String ctrimpat) {
        this.ctrimpat = ctrimpat;
    }

    public String getAczoatpr() {
        return aczoatpr;
    }

    public void setAczoatpr(String aczoatpr) {
        this.aczoatpr = aczoatpr;
    }

    public String getForinteq() {
        return forinteq;
    }

    public void setForinteq(String forinteq) {
        this.forinteq = forinteq;
    }

    public String getOusisusi() {
        return ousisusi;
    }

    public void setOusisusi(String ousisusi) {
        this.ousisusi = ousisusi;
    }

    public String getValtecre() {
        return valtecre;
    }

    public void setValtecre(String valtecre) {
        this.valtecre = valtecre;
    }

    public String getValcomre() {
        return valcomre;
    }

    public void setValcomre(String valcomre) {
        this.valcomre = valcomre;
    }

    public BigDecimal getFixerecu() {
        return fixerecu;
    }

    public void setFixerecu(BigDecimal fixerecu) {
        this.fixerecu = fixerecu;
    }

    public String getFlagstoc() {
        return flagstoc;
    }

    public void setFlagstoc(String flagstoc) {
        this.flagstoc = flagstoc;
    }

    public String getFordevat() {
        return fordevat;
    }

    public void setFordevat(String fordevat) {
        this.fordevat = fordevat;
    }

    public String getAbreuser() {
        return abreuser;
    }

    public void setAbreuser(String abreuser) {
        this.abreuser = abreuser;
    }

    public String getForintre() {
        return forintre;
    }

    public void setForintre(String forintre) {
        this.forintre = forintre;
    }

 

   
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeprof != null ? codeprof.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ProfilUser)) {
            return false;
        }
        ProfilUser other = (ProfilUser) object;
        if ((this.codeprof == null && other.codeprof != null) || (this.codeprof != null && !this.codeprof.equals(other.codeprof))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.ProfilUser[ codeprof=" + codeprof + " ]";
    }
    
}
