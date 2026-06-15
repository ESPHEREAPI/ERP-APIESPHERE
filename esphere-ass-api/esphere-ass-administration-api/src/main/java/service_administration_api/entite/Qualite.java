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
@Table(name = "QUALITE")
@NamedQueries({
    @NamedQuery(name = "Qualite.findAll", query = "SELECT q FROM Qualite q")})
public class Qualite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODEQUAL")
    private Short codequal;
    @Size(max = 30)
    @Column(name = "LIBEQUAL")
    private String libequal;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "GENRASSU")
    private String genrassu;
    @Size(max = 15)
    @Column(name = "ABREQUAL")
    private String abrequal;
    @Column(name = "DELAENCA")
    private Short delaenca;
    @OneToMany(mappedBy = "codequal")
    private List<Assure> assureList;

    public Qualite() {
    }

    public Qualite(Short codequal) {
        this.codequal = codequal;
    }

    public Qualite(Short codequal, String genrassu) {
        this.codequal = codequal;
        this.genrassu = genrassu;
    }

    public Short getCodequal() {
        return codequal;
    }

    public void setCodequal(Short codequal) {
        this.codequal = codequal;
    }

    public String getLibequal() {
        return libequal;
    }

    public void setLibequal(String libequal) {
        this.libequal = libequal;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getGenrassu() {
        return genrassu;
    }

    public void setGenrassu(String genrassu) {
        this.genrassu = genrassu;
    }

    public String getAbrequal() {
        return abrequal;
    }

    public void setAbrequal(String abrequal) {
        this.abrequal = abrequal;
    }

    public Short getDelaenca() {
        return delaenca;
    }

    public void setDelaenca(Short delaenca) {
        this.delaenca = delaenca;
    }

    public List<Assure> getAssureList() {
        return assureList;
    }

    public void setAssureList(List<Assure> assureList) {
        this.assureList = assureList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codequal != null ? codequal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Qualite)) {
            return false;
        }
        Qualite other = (Qualite) object;
        if ((this.codequal == null && other.codequal != null) || (this.codequal != null && !this.codequal.equals(other.codequal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Qualite[ codequal=" + codequal + " ]";
    }
    
}
