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
@Table(name = "PERIODICITE_PRIME")
@NamedQueries({
    @NamedQuery(name = "PeriodicitePrime.findAll", query = "SELECT p FROM PeriodicitePrime p")})
public class PeriodicitePrime implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "CODEPERI")
    private String codeperi;
    @Size(max = 30)
    @Column(name = "LIBEPERI")
    private String libeperi;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NOMBFRAC")
    private short nombfrac;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 1)
    @Column(name = "TYPECONT")
    private String typecont;
    @OneToMany(mappedBy = "codeperi")
    private List<Police> policeList;

    public PeriodicitePrime() {
    }

    public PeriodicitePrime(String codeperi) {
        this.codeperi = codeperi;
    }

    public PeriodicitePrime(String codeperi, short nombfrac) {
        this.codeperi = codeperi;
        this.nombfrac = nombfrac;
    }

    public String getCodeperi() {
        return codeperi;
    }

    public void setCodeperi(String codeperi) {
        this.codeperi = codeperi;
    }

    public String getLibeperi() {
        return libeperi;
    }

    public void setLibeperi(String libeperi) {
        this.libeperi = libeperi;
    }

    public short getNombfrac() {
        return nombfrac;
    }

    public void setNombfrac(short nombfrac) {
        this.nombfrac = nombfrac;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getTypecont() {
        return typecont;
    }

    public void setTypecont(String typecont) {
        this.typecont = typecont;
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
        hash += (codeperi != null ? codeperi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeriodicitePrime)) {
            return false;
        }
        PeriodicitePrime other = (PeriodicitePrime) object;
        if ((this.codeperi == null && other.codeperi != null) || (this.codeperi != null && !this.codeperi.equals(other.codeperi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.PeriodicitePrime[ codeperi=" + codeperi + " ]";
    }
    
}
