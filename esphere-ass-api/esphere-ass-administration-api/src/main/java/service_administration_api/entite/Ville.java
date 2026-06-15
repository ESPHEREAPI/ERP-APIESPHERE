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
@Table(name = "VILLE")
@NamedQueries({
    @NamedQuery(name = "Ville.findAll", query = "SELECT v FROM Ville v")})
public class Ville implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODEVILL")
    private Integer codevill;
    @Size(max = 30)
    @Column(name = "LIBEVILL")
    private String libevill;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @OneToMany(mappedBy = "codevill")
    private List<Apporteur> apporteurList;
    @OneToMany(mappedBy = "codevill")
    private List<Assure> assureList;
    @JoinColumn(name = "CODEREGI", referencedColumnName = "CODEREGI")
    @ManyToOne
    private Region coderegi;

    public Ville() {
    }

    public Ville(Integer codevill) {
        this.codevill = codevill;
    }

    public Integer getCodevill() {
        return codevill;
    }

    public void setCodevill(Integer codevill) {
        this.codevill = codevill;
    }

    public String getLibevill() {
        return libevill;
    }

    public void setLibevill(String libevill) {
        this.libevill = libevill;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public List<Apporteur> getApporteurList() {
        return apporteurList;
    }

    public void setApporteurList(List<Apporteur> apporteurList) {
        this.apporteurList = apporteurList;
    }

    public List<Assure> getAssureList() {
        return assureList;
    }

    public void setAssureList(List<Assure> assureList) {
        this.assureList = assureList;
    }

    public Region getCoderegi() {
        return coderegi;
    }

    public void setCoderegi(Region coderegi) {
        this.coderegi = coderegi;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codevill != null ? codevill.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ville)) {
            return false;
        }
        Ville other = (Ville) object;
        if ((this.codevill == null && other.codevill != null) || (this.codevill != null && !this.codevill.equals(other.codevill))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Ville[ codevill=" + codevill + " ]";
    }
    
}
