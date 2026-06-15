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
@Table(name = "ACTIVITE")
@NamedQueries({
    @NamedQuery(name = "Activite_1.findAll", query = "SELECT a FROM Activite_1 a")})
public class Activite_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODEACTI")
    private Integer codeacti;
    @Size(max = 50)
    @Column(name = "LIBEACTI")
    private String libeacti;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @OneToMany(mappedBy = "codactra")
    private List<Activite_1> activiteList;
    @JoinColumn(name = "CODACTRA", referencedColumnName = "CODEACTI")
    @ManyToOne
    private Activite_1 codactra;
    @OneToMany(mappedBy = "codeacti")
    private List<Assure_1> assureList;

    public Activite_1() {
    }

    public Activite_1(Integer codeacti) {
        this.codeacti = codeacti;
    }

    public Integer getCodeacti() {
        return codeacti;
    }

    public void setCodeacti(Integer codeacti) {
        this.codeacti = codeacti;
    }

    public String getLibeacti() {
        return libeacti;
    }

    public void setLibeacti(String libeacti) {
        this.libeacti = libeacti;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public List<Activite_1> getActiviteList() {
        return activiteList;
    }

    public void setActiviteList(List<Activite_1> activiteList) {
        this.activiteList = activiteList;
    }

    public Activite_1 getCodactra() {
        return codactra;
    }

    public void setCodactra(Activite_1 codactra) {
        this.codactra = codactra;
    }

    public List<Assure_1> getAssureList() {
        return assureList;
    }

    public void setAssureList(List<Assure_1> assureList) {
        this.assureList = assureList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeacti != null ? codeacti.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Activite_1)) {
            return false;
        }
        Activite_1 other = (Activite_1) object;
        if ((this.codeacti == null && other.codeacti != null) || (this.codeacti != null && !this.codeacti.equals(other.codeacti))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Activite_1[ codeacti=" + codeacti + " ]";
    }
    
}
