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
@Table(name = "EXONERATION")
@NamedQueries({
    @NamedQuery(name = "Exoneration.findAll", query = "SELECT e FROM Exoneration e")})
public class Exoneration implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "FLAEXOTG")
    private String flaexotg;
    @Size(max = 30)
    @Column(name = "LIBEXOTG")
    private String libexotg;
    @Size(max = 1)
    @Column(name = "EXOTIMGR")
    private String exotimgr;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 4)
    @Column(name = "NATREGTA")
    private String natregta;
    @OneToMany(mappedBy = "flaexotg")
    private List<Police> policeList;

    public Exoneration() {
    }

    public Exoneration(String flaexotg) {
        this.flaexotg = flaexotg;
    }

    public String getFlaexotg() {
        return flaexotg;
    }

    public void setFlaexotg(String flaexotg) {
        this.flaexotg = flaexotg;
    }

    public String getLibexotg() {
        return libexotg;
    }

    public void setLibexotg(String libexotg) {
        this.libexotg = libexotg;
    }

    public String getExotimgr() {
        return exotimgr;
    }

    public void setExotimgr(String exotimgr) {
        this.exotimgr = exotimgr;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getNatregta() {
        return natregta;
    }

    public void setNatregta(String natregta) {
        this.natregta = natregta;
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
        hash += (flaexotg != null ? flaexotg.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Exoneration)) {
            return false;
        }
        Exoneration other = (Exoneration) object;
        if ((this.flaexotg == null && other.flaexotg != null) || (this.flaexotg != null && !this.flaexotg.equals(other.flaexotg))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Exoneration[ flaexotg=" + flaexotg + " ]";
    }
    
}
