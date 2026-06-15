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
@Table(name = "TYPE_APPORTEUR")
@NamedQueries({
    @NamedQuery(name = "TypeApporteur.findAll", query = "SELECT t FROM TypeApporteur t")})
public class TypeApporteur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "CODTYPAP")
    private String codtypap;
    @Size(max = 30)
    @Column(name = "LIBTYPAP")
    private String libtypap;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Column(name = "NIVEHEAR")
    private Short nivehear;
    @OneToMany(mappedBy = "codtypap")
    private List<Apporteur> apporteurList;

    public TypeApporteur() {
    }

    public TypeApporteur(String codtypap) {
        this.codtypap = codtypap;
    }

    public String getCodtypap() {
        return codtypap;
    }

    public void setCodtypap(String codtypap) {
        this.codtypap = codtypap;
    }

    public String getLibtypap() {
        return libtypap;
    }

    public void setLibtypap(String libtypap) {
        this.libtypap = libtypap;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public Short getNivehear() {
        return nivehear;
    }

    public void setNivehear(Short nivehear) {
        this.nivehear = nivehear;
    }

    public List<Apporteur> getApporteurList() {
        return apporteurList;
    }

    public void setApporteurList(List<Apporteur> apporteurList) {
        this.apporteurList = apporteurList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtypap != null ? codtypap.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeApporteur)) {
            return false;
        }
        TypeApporteur other = (TypeApporteur) object;
        if ((this.codtypap == null && other.codtypap != null) || (this.codtypap != null && !this.codtypap.equals(other.codtypap))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.TypeApporteur[ codtypap=" + codtypap + " ]";
    }
    
}
