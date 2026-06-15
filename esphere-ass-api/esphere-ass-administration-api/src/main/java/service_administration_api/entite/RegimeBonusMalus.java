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
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "REGIME_BONUS_MALUS")
@NamedQueries({
    @NamedQuery(name = "RegimeBonusMalus.findAll", query = "SELECT r FROM RegimeBonusMalus r")})
public class RegimeBonusMalus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "CODREGBM")
    private String codregbm;
    @Size(max = 30)
    @Column(name = "LIBREGBM")
    private String libregbm;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 1)
    @Column(name = "MOACSABM")
    private String moacsabm;
    @OneToMany(mappedBy = "codregbm")
    private List<Convention> conventionList;
    @OneToMany(mappedBy = "codregbm")
    private List<Police> policeList;

    public RegimeBonusMalus() {
    }

    public RegimeBonusMalus(String codregbm) {
        this.codregbm = codregbm;
    }

    public String getCodregbm() {
        return codregbm;
    }

    public void setCodregbm(String codregbm) {
        this.codregbm = codregbm;
    }

    public String getLibregbm() {
        return libregbm;
    }

    public void setLibregbm(String libregbm) {
        this.libregbm = libregbm;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getMoacsabm() {
        return moacsabm;
    }

    public void setMoacsabm(String moacsabm) {
        this.moacsabm = moacsabm;
    }

    public List<Convention> getConventionList() {
        return conventionList;
    }

    public void setConventionList(List<Convention> conventionList) {
        this.conventionList = conventionList;
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
        hash += (codregbm != null ? codregbm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegimeBonusMalus)) {
            return false;
        }
        RegimeBonusMalus other = (RegimeBonusMalus) object;
        if ((this.codregbm == null && other.codregbm != null) || (this.codregbm != null && !this.codregbm.equals(other.codregbm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.RegimeBonusMalus[ codregbm=" + codregbm + " ]";
    }
    
}
