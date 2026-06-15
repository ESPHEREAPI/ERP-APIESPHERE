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
@Table(name = "ASV_CODE_DEVISE")
@NamedQueries({
    @NamedQuery(name = "AsvCodeDevise.findAll", query = "SELECT a FROM AsvCodeDevise a")})
public class AsvCodeDevise implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "CODEDEVI")
    private String codedevi;
    @Size(max = 50)
    @Column(name = "LIBEDEVI")
    private String libedevi;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 10)
    @Column(name = "SIGLDEVI")
    private String sigldevi;
    @Column(name = "NOMBDECI")
    private Short nombdeci;
    @Size(max = 4)
    @Column(name = "SYMBDEVI")
    private String symbdevi;
    @Size(max = 3)
    @Column(name = "NUMEDEVI")
    private String numedevi;
    @Size(max = 100)
    @Column(name = "PAYSDEVI")
    private String paysdevi;
    @OneToMany(mappedBy = "codedevi")
    private List<Police> policeList;

    public AsvCodeDevise() {
    }

    public AsvCodeDevise(String codedevi) {
        this.codedevi = codedevi;
    }

    public String getCodedevi() {
        return codedevi;
    }

    public void setCodedevi(String codedevi) {
        this.codedevi = codedevi;
    }

    public String getLibedevi() {
        return libedevi;
    }

    public void setLibedevi(String libedevi) {
        this.libedevi = libedevi;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getSigldevi() {
        return sigldevi;
    }

    public void setSigldevi(String sigldevi) {
        this.sigldevi = sigldevi;
    }

    public Short getNombdeci() {
        return nombdeci;
    }

    public void setNombdeci(Short nombdeci) {
        this.nombdeci = nombdeci;
    }

    public String getSymbdevi() {
        return symbdevi;
    }

    public void setSymbdevi(String symbdevi) {
        this.symbdevi = symbdevi;
    }

    public String getNumedevi() {
        return numedevi;
    }

    public void setNumedevi(String numedevi) {
        this.numedevi = numedevi;
    }

    public String getPaysdevi() {
        return paysdevi;
    }

    public void setPaysdevi(String paysdevi) {
        this.paysdevi = paysdevi;
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
        hash += (codedevi != null ? codedevi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AsvCodeDevise)) {
            return false;
        }
        AsvCodeDevise other = (AsvCodeDevise) object;
        if ((this.codedevi == null && other.codedevi != null) || (this.codedevi != null && !this.codedevi.equals(other.codedevi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.AsvCodeDevise[ codedevi=" + codedevi + " ]";
    }
    
}
