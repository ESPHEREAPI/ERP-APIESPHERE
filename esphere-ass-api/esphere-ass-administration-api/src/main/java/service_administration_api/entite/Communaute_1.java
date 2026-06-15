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
@Table(name = "COMMUNAUTE")
@NamedQueries({
    @NamedQuery(name = "Communaute_1.findAll", query = "SELECT c FROM Communaute_1 c")})
public class Communaute_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "CODECOMM")
    private String codecomm;
    @Size(max = 50)
    @Column(name = "LIBECOMM")
    private String libecomm;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 10)
    @Column(name = "FAMICOMM")
    private String famicomm;
    @OneToMany(mappedBy = "codecomm")
    private List<Region_1> regionList;

    public Communaute_1() {
    }

    public Communaute_1(String codecomm) {
        this.codecomm = codecomm;
    }

    public String getCodecomm() {
        return codecomm;
    }

    public void setCodecomm(String codecomm) {
        this.codecomm = codecomm;
    }

    public String getLibecomm() {
        return libecomm;
    }

    public void setLibecomm(String libecomm) {
        this.libecomm = libecomm;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getFamicomm() {
        return famicomm;
    }

    public void setFamicomm(String famicomm) {
        this.famicomm = famicomm;
    }

    public List<Region_1> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<Region_1> regionList) {
        this.regionList = regionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codecomm != null ? codecomm.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Communaute_1)) {
            return false;
        }
        Communaute_1 other = (Communaute_1) object;
        if ((this.codecomm == null && other.codecomm != null) || (this.codecomm != null && !this.codecomm.equals(other.codecomm))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Communaute_1[ codecomm=" + codecomm + " ]";
    }
    
}
