/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "TYPE_AVENANT")
@NamedQueries({
    @NamedQuery(name = "TypeAvenant.findAll", query = "SELECT t FROM TypeAvenant t"),
    @NamedQuery(name = "TypeAvenant.findByCodtypav", query = "SELECT t FROM TypeAvenant t WHERE t.codtypav = :codtypav"),
    @NamedQuery(name = "TypeAvenant.findByLibtypav", query = "SELECT t FROM TypeAvenant t WHERE t.libtypav = :libtypav"),
    @NamedQuery(name = "TypeAvenant.findByFlatimdi", query = "SELECT t FROM TypeAvenant t WHERE t.flatimdi = :flatimdi"),
    @NamedQuery(name = "TypeAvenant.findByFlatimgr", query = "SELECT t FROM TypeAvenant t WHERE t.flatimgr = :flatimgr"),
    @NamedQuery(name = "TypeAvenant.findByNatuaven", query = "SELECT t FROM TypeAvenant t WHERE t.natuaven = :natuaven"),
    @NamedQuery(name = "TypeAvenant.findByTypecont", query = "SELECT t FROM TypeAvenant t WHERE t.typecont = :typecont"),
    @NamedQuery(name = "TypeAvenant.findByFlanumge", query = "SELECT t FROM TypeAvenant t WHERE t.flanumge = :flanumge"),
    @NamedQuery(name = "TypeAvenant.findByMessaver", query = "SELECT t FROM TypeAvenant t WHERE t.messaver = :messaver"),
    @NamedQuery(name = "TypeAvenant.findByDurrlimi", query = "SELECT t FROM TypeAvenant t WHERE t.durrlimi = :durrlimi"),
    @NamedQuery(name = "TypeAvenant.findByCoefpror", query = "SELECT t FROM TypeAvenant t WHERE t.coefpror = :coefpror"),
    @NamedQuery(name = "TypeAvenant.findByFlagreco", query = "SELECT t FROM TypeAvenant t WHERE t.flagreco = :flagreco"),
    @NamedQuery(name = "TypeAvenant.findByFlagPab", query = "SELECT t FROM TypeAvenant t WHERE t.flagPab = :flagPab"),
    @NamedQuery(name = "TypeAvenant.findByObseaven", query = "SELECT t FROM TypeAvenant t WHERE t.obseaven = :obseaven"),
    @NamedQuery(name = "TypeAvenant.findByGenraven", query = "SELECT t FROM TypeAvenant t WHERE t.genraven = :genraven"),
    @NamedQuery(name = "TypeAvenant.findByRecotigr", query = "SELECT t FROM TypeAvenant t WHERE t.recotigr = :recotigr"),
    @NamedQuery(name = "TypeAvenant.findByFlagpror", query = "SELECT t FROM TypeAvenant t WHERE t.flagpror = :flagpror"),
    @NamedQuery(name = "TypeAvenant.findByNumeLot", query = "SELECT t FROM TypeAvenant t WHERE t.numeLot = :numeLot"),
    @NamedQuery(name = "TypeAvenant.findByFlaimpat", query = "SELECT t FROM TypeAvenant t WHERE t.flaimpat = :flaimpat"),
    @NamedQuery(name = "TypeAvenant.findByFlagreva", query = "SELECT t FROM TypeAvenant t WHERE t.flagreva = :flagreva"),
    @NamedQuery(name = "TypeAvenant.findByTypprepa", query = "SELECT t FROM TypeAvenant t WHERE t.typprepa = :typprepa"),
    @NamedQuery(name = "TypeAvenant.findByPlataupr", query = "SELECT t FROM TypeAvenant t WHERE t.plataupr = :plataupr"),
    @NamedQuery(name = "TypeAvenant.findByCocodupr", query = "SELECT t FROM TypeAvenant t WHERE t.cocodupr = :cocodupr"),
    @NamedQuery(name = "TypeAvenant.findByDurmaxpr", query = "SELECT t FROM TypeAvenant t WHERE t.durmaxpr = :durmaxpr"),
    @NamedQuery(name = "TypeAvenant.findByCodtypso", query = "SELECT t FROM TypeAvenant t WHERE t.codtypso = :codtypso")})
public class TypeAvenant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODTYPAV")
    private Short codtypav;
    @Column(name = "LIBTYPAV")
    private String libtypav;
    @Column(name = "FLATIMDI")
    private String flatimdi;
    @Column(name = "FLATIMGR")
    private String flatimgr;
    @Column(name = "NATUAVEN")
    private String natuaven;
    @Column(name = "TYPECONT")
    private String typecont;
    @Column(name = "FLANUMGE")
    private String flanumge;
    @Column(name = "MESSAVER")
    private String messaver;
    @Column(name = "DURRLIMI")
    private Short durrlimi;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "COEFPROR")
    private BigDecimal coefpror;
    @Basic(optional = false)
    @Column(name = "FLAGRECO")
    private String flagreco;
    @Basic(optional = false)
    @Column(name = "FLAG_PAB")
    private String flagPab;
    @Column(name = "OBSEAVEN")
    private String obseaven;
    @Basic(optional = false)
    @Column(name = "GENRAVEN")
    private String genraven;
    @Basic(optional = false)
    @Column(name = "RECOTIGR")
    private String recotigr;
    @Column(name = "FLAGPROR")
    private String flagpror;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Column(name = "FLAIMPAT")
    private String flaimpat;
    @Column(name = "FLAGREVA")
    private String flagreva;
    @Column(name = "TYPPREPA")
    private String typprepa;
    @Column(name = "PLATAUPR", columnDefinition = "NUMBER")
    private BigInteger plataupr;
    @Column(name = "COCODUPR")
    private String cocodupr;
   @Column(name = "DURMAXPR", columnDefinition = "NUMBER")
private BigInteger durmaxpr;
    @Column(name = "CODTYPSO")
    private String codtypso;
    @OneToMany(mappedBy = "cotyavte")
    private List<Categorie> categorieList;

    public TypeAvenant() {
    }

    public TypeAvenant(Short codtypav) {
        this.codtypav = codtypav;
    }

    public TypeAvenant(Short codtypav, String flagreco, String flagPab, String genraven, String recotigr) {
        this.codtypav = codtypav;
        this.flagreco = flagreco;
        this.flagPab = flagPab;
        this.genraven = genraven;
        this.recotigr = recotigr;
    }

    public Short getCodtypav() {
        return codtypav;
    }

    public void setCodtypav(Short codtypav) {
        this.codtypav = codtypav;
    }

    public String getLibtypav() {
        return libtypav;
    }

    public void setLibtypav(String libtypav) {
        this.libtypav = libtypav;
    }

    public String getFlatimdi() {
        return flatimdi;
    }

    public void setFlatimdi(String flatimdi) {
        this.flatimdi = flatimdi;
    }

    public String getFlatimgr() {
        return flatimgr;
    }

    public void setFlatimgr(String flatimgr) {
        this.flatimgr = flatimgr;
    }

    public String getNatuaven() {
        return natuaven;
    }

    public void setNatuaven(String natuaven) {
        this.natuaven = natuaven;
    }

    public String getTypecont() {
        return typecont;
    }

    public void setTypecont(String typecont) {
        this.typecont = typecont;
    }

    public String getFlanumge() {
        return flanumge;
    }

    public void setFlanumge(String flanumge) {
        this.flanumge = flanumge;
    }

    public String getMessaver() {
        return messaver;
    }

    public void setMessaver(String messaver) {
        this.messaver = messaver;
    }

    public Short getDurrlimi() {
        return durrlimi;
    }

    public void setDurrlimi(Short durrlimi) {
        this.durrlimi = durrlimi;
    }

    public BigDecimal getCoefpror() {
        return coefpror;
    }

    public void setCoefpror(BigDecimal coefpror) {
        this.coefpror = coefpror;
    }

    public String getFlagreco() {
        return flagreco;
    }

    public void setFlagreco(String flagreco) {
        this.flagreco = flagreco;
    }

    public String getFlagPab() {
        return flagPab;
    }

    public void setFlagPab(String flagPab) {
        this.flagPab = flagPab;
    }

    public String getObseaven() {
        return obseaven;
    }

    public void setObseaven(String obseaven) {
        this.obseaven = obseaven;
    }

    public String getGenraven() {
        return genraven;
    }

    public void setGenraven(String genraven) {
        this.genraven = genraven;
    }

    public String getRecotigr() {
        return recotigr;
    }

    public void setRecotigr(String recotigr) {
        this.recotigr = recotigr;
    }

    public String getFlagpror() {
        return flagpror;
    }

    public void setFlagpror(String flagpror) {
        this.flagpror = flagpror;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getFlaimpat() {
        return flaimpat;
    }

    public void setFlaimpat(String flaimpat) {
        this.flaimpat = flaimpat;
    }

    public String getFlagreva() {
        return flagreva;
    }

    public void setFlagreva(String flagreva) {
        this.flagreva = flagreva;
    }

    public String getTypprepa() {
        return typprepa;
    }

    public void setTypprepa(String typprepa) {
        this.typprepa = typprepa;
    }

    public BigInteger getPlataupr() {
        return plataupr;
    }

    public void setPlataupr(BigInteger plataupr) {
        this.plataupr = plataupr;
    }

    public String getCocodupr() {
        return cocodupr;
    }

    public void setCocodupr(String cocodupr) {
        this.cocodupr = cocodupr;
    }

    public BigInteger getDurmaxpr() {
        return durmaxpr;
    }

    public void setDurmaxpr(BigInteger durmaxpr) {
        this.durmaxpr = durmaxpr;
    }

    public String getCodtypso() {
        return codtypso;
    }

    public void setCodtypso(String codtypso) {
        this.codtypso = codtypso;
    }

    public List<Categorie> getCategorieList() {
        return categorieList;
    }

    public void setCategorieList(List<Categorie> categorieList) {
        this.categorieList = categorieList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtypav != null ? codtypav.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeAvenant)) {
            return false;
        }
        TypeAvenant other = (TypeAvenant) object;
        if ((this.codtypav == null && other.codtypav != null) || (this.codtypav != null && !this.codtypav.equals(other.codtypav))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.TypeAvenant[ codtypav=" + codtypav + " ]";
    }

}
