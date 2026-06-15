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
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "CLASSE")
@NamedQueries({
    @NamedQuery(name = "Classe.findAll", query = "SELECT c FROM Classe c"),
    @NamedQuery(name = "Classe.findByCodeclas", query = "SELECT c FROM Classe c WHERE c.codeclas = :codeclas"),
    @NamedQuery(name = "Classe.findByLibeclas", query = "SELECT c FROM Classe c WHERE c.libeclas = :libeclas"),
    @NamedQuery(name = "Classe.findByNumeLot", query = "SELECT c FROM Classe c WHERE c.numeLot = :numeLot"),
    @NamedQuery(name = "Classe.findByColumn4", query = "SELECT c FROM Classe c WHERE c.column4 = :column4")})
public class Classe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODECLAS")
    private Short codeclas;
    @Column(name = "LIBECLAS")
    private String libeclas;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Column(name = "COLUMN4")
    private BigInteger column4;
    @OneToMany(mappedBy = "codeclas")
    private List<Branche> brancheList;

    public Classe() {
    }

    public Classe(Short codeclas) {
        this.codeclas = codeclas;
    }

    public Short getCodeclas() {
        return codeclas;
    }

    public void setCodeclas(Short codeclas) {
        this.codeclas = codeclas;
    }

    public String getLibeclas() {
        return libeclas;
    }

    public void setLibeclas(String libeclas) {
        this.libeclas = libeclas;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public BigInteger getColumn4() {
        return column4;
    }

    public void setColumn4(BigInteger column4) {
        this.column4 = column4;
    }

    public List<Branche> getBrancheList() {
        return brancheList;
    }

    public void setBrancheList(List<Branche> brancheList) {
        this.brancheList = brancheList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeclas != null ? codeclas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Classe)) {
            return false;
        }
        Classe other = (Classe) object;
        if ((this.codeclas == null && other.codeclas != null) || (this.codeclas != null && !this.codeclas.equals(other.codeclas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Classe[ codeclas=" + codeclas + " ]";
    }
    
}
