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
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "BRANCHE")
@NamedQueries({
    @NamedQuery(name = "Branche.findAll", query = "SELECT b FROM Branche b"),
    @NamedQuery(name = "Branche.findByCodebran", query = "SELECT b FROM Branche b WHERE b.codebran = :codebran"),
    @NamedQuery(name = "Branche.findByLibebran", query = "SELECT b FROM Branche b WHERE b.libebran = :libebran"),
    @NamedQuery(name = "Branche.findByNumeLot", query = "SELECT b FROM Branche b WHERE b.numeLot = :numeLot"),
    @NamedQuery(name = "Branche.findByCodbraor", query = "SELECT b FROM Branche b WHERE b.codbraor = :codbraor")})
public class Branche implements Serializable {

    @OneToMany(mappedBy = "codebran")
    private List<Categorie> categorieList;

//    @OneToMany(mappedBy = "codebran")
//    private List<Categorie> categorieList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODEBRAN")
    private Short codebran;
    @Column(name = "LIBEBRAN")
    private String libebran;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Basic(optional = false)
    @Column(name = "CODBRAOR")
    private short codbraor;
    @JoinColumn(name = "CODECLAS", referencedColumnName = "CODECLAS")
    @ManyToOne
    private Classe codeclas;

    public Branche() {
    }

    public Branche(Short codebran) {
        this.codebran = codebran;
    }

    public Branche(Short codebran, short codbraor) {
        this.codebran = codebran;
        this.codbraor = codbraor;
    }

    public Short getCodebran() {
        return codebran;
    }

    public void setCodebran(Short codebran) {
        this.codebran = codebran;
    }

    public String getLibebran() {
        return libebran;
    }

    public void setLibebran(String libebran) {
        this.libebran = libebran;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public short getCodbraor() {
        return codbraor;
    }

    public void setCodbraor(short codbraor) {
        this.codbraor = codbraor;
    }

    public Classe getCodeclas() {
        return codeclas;
    }

    public void setCodeclas(Classe codeclas) {
        this.codeclas = codeclas;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codebran != null ? codebran.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Branche)) {
            return false;
        }
        Branche other = (Branche) object;
        if ((this.codebran == null && other.codebran != null) || (this.codebran != null && !this.codebran.equals(other.codebran))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Branche[ codebran=" + codebran + " ]";
    }

    public List<Categorie> getCategorieList() {
        return categorieList;
    }

    public void setCategorieList(List<Categorie> categorieList) {
        this.categorieList = categorieList;
    }

   
    
}
