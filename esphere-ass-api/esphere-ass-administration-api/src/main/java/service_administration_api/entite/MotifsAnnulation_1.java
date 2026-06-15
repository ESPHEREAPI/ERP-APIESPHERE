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
@Table(name = "MOTIFS_ANNULATION")
@NamedQueries({
    @NamedQuery(name = "MotifsAnnulation_1.findAll", query = "SELECT m FROM MotifsAnnulation_1 m")})
public class MotifsAnnulation_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "CODMOTAN")
    private String codmotan;
    @Size(max = 100)
    @Column(name = "DESMOTAN")
    private String desmotan;
    @Size(max = 2)
    @Column(name = "NATMOTAN")
    private String natmotan;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @OneToMany(mappedBy = "codmotan")
    private List<Police_1> policeList;

    public MotifsAnnulation_1() {
    }

    public MotifsAnnulation_1(String codmotan) {
        this.codmotan = codmotan;
    }

    public String getCodmotan() {
        return codmotan;
    }

    public void setCodmotan(String codmotan) {
        this.codmotan = codmotan;
    }

    public String getDesmotan() {
        return desmotan;
    }

    public void setDesmotan(String desmotan) {
        this.desmotan = desmotan;
    }

    public String getNatmotan() {
        return natmotan;
    }

    public void setNatmotan(String natmotan) {
        this.natmotan = natmotan;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
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
        hash += (codmotan != null ? codmotan.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MotifsAnnulation_1)) {
            return false;
        }
        MotifsAnnulation_1 other = (MotifsAnnulation_1) object;
        if ((this.codmotan == null && other.codmotan != null) || (this.codmotan != null && !this.codmotan.equals(other.codmotan))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.MotifsAnnulation_1[ codmotan=" + codmotan + " ]";
    }
    
}
