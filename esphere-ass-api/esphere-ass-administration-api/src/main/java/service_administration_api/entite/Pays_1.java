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
@Table(name = "PAYS")
@NamedQueries({
    @NamedQuery(name = "Pays_1.findAll", query = "SELECT p FROM Pays_1 p")})
public class Pays_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "CODEPAYS")
    private String codepays;
    @Size(max = 100)
    @Column(name = "LIBEPAYS")
    private String libepays;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 60)
    @Column(name = "LIBENATI")
    private String libenati;
    @OneToMany(mappedBy = "codepays")
    private List<Assure_1> assureList;

    public Pays_1() {
    }

    public Pays_1(String codepays) {
        this.codepays = codepays;
    }

    public String getCodepays() {
        return codepays;
    }

    public void setCodepays(String codepays) {
        this.codepays = codepays;
    }

    public String getLibepays() {
        return libepays;
    }

    public void setLibepays(String libepays) {
        this.libepays = libepays;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getLibenati() {
        return libenati;
    }

    public void setLibenati(String libenati) {
        this.libenati = libenati;
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
        hash += (codepays != null ? codepays.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pays_1)) {
            return false;
        }
        Pays_1 other = (Pays_1) object;
        if ((this.codepays == null && other.codepays != null) || (this.codepays != null && !this.codepays.equals(other.codepays))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Pays_1[ codepays=" + codepays + " ]";
    }
    
}
