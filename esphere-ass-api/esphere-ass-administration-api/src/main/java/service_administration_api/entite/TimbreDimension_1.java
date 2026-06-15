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
@Table(name = "TIMBRE_DIMENSION")
@NamedQueries({
    @NamedQuery(name = "TimbreDimension_1.findAll", query = "SELECT t FROM TimbreDimension_1 t")})
public class TimbreDimension_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "CODTIMDI")
    private String codtimdi;
    @Size(max = 30)
    @Column(name = "LIBTIMDI")
    private String libtimdi;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 1)
    @Column(name = "MOACSATD")
    private String moacsatd;
    @Size(max = 1)
    @Column(name = "FLAGRISQ")
    private String flagrisq;
    @OneToMany(mappedBy = "codtimdi")
    private List<Police_1> policeList;

    public TimbreDimension_1() {
    }

    public TimbreDimension_1(String codtimdi) {
        this.codtimdi = codtimdi;
    }

    public String getCodtimdi() {
        return codtimdi;
    }

    public void setCodtimdi(String codtimdi) {
        this.codtimdi = codtimdi;
    }

    public String getLibtimdi() {
        return libtimdi;
    }

    public void setLibtimdi(String libtimdi) {
        this.libtimdi = libtimdi;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getMoacsatd() {
        return moacsatd;
    }

    public void setMoacsatd(String moacsatd) {
        this.moacsatd = moacsatd;
    }

    public String getFlagrisq() {
        return flagrisq;
    }

    public void setFlagrisq(String flagrisq) {
        this.flagrisq = flagrisq;
    }

    public List<Police_1> getPoliceList() {
        return policeList;
    }

    public void setPoliceList(List<Police_1> policeList) {
        this.policeList = policeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtimdi != null ? codtimdi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimbreDimension_1)) {
            return false;
        }
        TimbreDimension_1 other = (TimbreDimension_1) object;
        if ((this.codtimdi == null && other.codtimdi != null) || (this.codtimdi != null && !this.codtimdi.equals(other.codtimdi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.TimbreDimension_1[ codtimdi=" + codtimdi + " ]";
    }
    
}
