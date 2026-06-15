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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "DUREE")
@NamedQueries({
    @NamedQuery(name = "Duree.findAll", query = "SELECT d FROM Duree d")})
public class Duree implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "CODEDURE")
    private String codedure;
    @Size(max = 30)
    @Column(name = "LIBEDURE")
    private String libedure;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DUREDURE")
    private BigDecimal duredure;
    @Size(max = 1)
    @Column(name = "UNITDURE")
    private String unitdure;
    @Size(max = 1)
    @Column(name = "MODCALMA")
    private String modcalma;
    @Column(name = "MAXIDURE")
    private BigDecimal maxidure;
    @Size(max = 1)
    @Column(name = "TYPEDURE")
    private String typedure;
    @Column(name = "BASEPROR")
    private BigDecimal basepror;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Column(name = "VAADDUDU")
    private BigDecimal vaaddudu;
    @Size(max = 1)
    @Column(name = "UNDURADD")
    private String unduradd;
    @Size(max = 2)
    @Column(name = "FLACALDU")
    private String flacaldu;
    @Size(max = 3)
    @Column(name = "ACCECHDU")
    private String accechdu;
    @Size(max = 3)
    @Column(name = "BASCALEC")
    private String bascalec;
    @OneToMany(mappedBy = "codedure")
    private List<Police> policeList;

    public Duree() {
    }

    public Duree(String codedure) {
        this.codedure = codedure;
    }

    public String getCodedure() {
        return codedure;
    }

    public void setCodedure(String codedure) {
        this.codedure = codedure;
    }

    public String getLibedure() {
        return libedure;
    }

    public void setLibedure(String libedure) {
        this.libedure = libedure;
    }

    public BigDecimal getDuredure() {
        return duredure;
    }

    public void setDuredure(BigDecimal duredure) {
        this.duredure = duredure;
    }

    public String getUnitdure() {
        return unitdure;
    }

    public void setUnitdure(String unitdure) {
        this.unitdure = unitdure;
    }

    public String getModcalma() {
        return modcalma;
    }

    public void setModcalma(String modcalma) {
        this.modcalma = modcalma;
    }

    public BigDecimal getMaxidure() {
        return maxidure;
    }

    public void setMaxidure(BigDecimal maxidure) {
        this.maxidure = maxidure;
    }

    public String getTypedure() {
        return typedure;
    }

    public void setTypedure(String typedure) {
        this.typedure = typedure;
    }

    public BigDecimal getBasepror() {
        return basepror;
    }

    public void setBasepror(BigDecimal basepror) {
        this.basepror = basepror;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public BigDecimal getVaaddudu() {
        return vaaddudu;
    }

    public void setVaaddudu(BigDecimal vaaddudu) {
        this.vaaddudu = vaaddudu;
    }

    public String getUnduradd() {
        return unduradd;
    }

    public void setUnduradd(String unduradd) {
        this.unduradd = unduradd;
    }

    public String getFlacaldu() {
        return flacaldu;
    }

    public void setFlacaldu(String flacaldu) {
        this.flacaldu = flacaldu;
    }

    public String getAccechdu() {
        return accechdu;
    }

    public void setAccechdu(String accechdu) {
        this.accechdu = accechdu;
    }

    public String getBascalec() {
        return bascalec;
    }

    public void setBascalec(String bascalec) {
        this.bascalec = bascalec;
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
        hash += (codedure != null ? codedure.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Duree)) {
            return false;
        }
        Duree other = (Duree) object;
        if ((this.codedure == null && other.codedure != null) || (this.codedure != null && !this.codedure.equals(other.codedure))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Duree[ codedure=" + codedure + " ]";
    }
    
}
