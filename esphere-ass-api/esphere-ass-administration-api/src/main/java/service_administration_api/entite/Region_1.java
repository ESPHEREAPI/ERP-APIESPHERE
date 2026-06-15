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
@Table(name = "REGION")
@NamedQueries({
    @NamedQuery(name = "Region_1.findAll", query = "SELECT r FROM Region_1 r")})
public class Region_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODEREGI")
    private Short coderegi;
    @Size(max = 30)
    @Column(name = "LIBEREGI")
    private String liberegi;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @OneToMany(mappedBy = "coderegi")
    private List<Ville_1> villeList;
    @JoinColumn(name = "CODECOMM", referencedColumnName = "CODECOMM")
    @ManyToOne
    private Communaute_1 codecomm;

    public Region_1() {
    }

    public Region_1(Short coderegi) {
        this.coderegi = coderegi;
    }

    public Short getCoderegi() {
        return coderegi;
    }

    public void setCoderegi(Short coderegi) {
        this.coderegi = coderegi;
    }

    public String getLiberegi() {
        return liberegi;
    }

    public void setLiberegi(String liberegi) {
        this.liberegi = liberegi;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public List<Ville_1> getVilleList() {
        return villeList;
    }

    public void setVilleList(List<Ville_1> villeList) {
        this.villeList = villeList;
    }

    public Communaute_1 getCodecomm() {
        return codecomm;
    }

    public void setCodecomm(Communaute_1 codecomm) {
        this.codecomm = codecomm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coderegi != null ? coderegi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Region_1)) {
            return false;
        }
        Region_1 other = (Region_1) object;
        if ((this.coderegi == null && other.coderegi != null) || (this.coderegi != null && !this.coderegi.equals(other.coderegi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Region_1[ coderegi=" + coderegi + " ]";
    }
    
}
