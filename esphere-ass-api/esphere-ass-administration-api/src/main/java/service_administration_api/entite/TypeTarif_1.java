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
@Table(name = "TYPE_TARIF")
@NamedQueries({
    @NamedQuery(name = "TypeTarif_1.findAll", query = "SELECT t FROM TypeTarif_1 t")})
public class TypeTarif_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "CODTYPTA")
    private String codtypta;
    @Size(max = 30)
    @Column(name = "LIBTYPTA")
    private String libtypta;
    @Size(max = 1)
    @Column(name = "TYPTYPTA")
    private String typtypta;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @OneToMany(mappedBy = "codtypta")
    private List<Convention_1> conventionList;

    public TypeTarif_1() {
    }

    public TypeTarif_1(String codtypta) {
        this.codtypta = codtypta;
    }

    public String getCodtypta() {
        return codtypta;
    }

    public void setCodtypta(String codtypta) {
        this.codtypta = codtypta;
    }

    public String getLibtypta() {
        return libtypta;
    }

    public void setLibtypta(String libtypta) {
        this.libtypta = libtypta;
    }

    public String getTyptypta() {
        return typtypta;
    }

    public void setTyptypta(String typtypta) {
        this.typtypta = typtypta;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public List<Convention_1> getConventionList() {
        return conventionList;
    }

    public void setConventionList(List<Convention_1> conventionList) {
        this.conventionList = conventionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codtypta != null ? codtypta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TypeTarif_1)) {
            return false;
        }
        TypeTarif_1 other = (TypeTarif_1) object;
        if ((this.codtypta == null && other.codtypta != null) || (this.codtypta != null && !this.codtypta.equals(other.codtypta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.TypeTarif_1[ codtypta=" + codtypta + " ]";
    }
    
}
