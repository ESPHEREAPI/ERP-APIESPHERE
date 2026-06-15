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
@Table(name = "PROFESSION")
@NamedQueries({
    @NamedQuery(name = "Profession_1.findAll", query = "SELECT p FROM Profession_1 p")})
public class Profession_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODEPROF")
    private Integer codeprof;
    @Size(max = 50)
    @Column(name = "LIBEPROF")
    private String libeprof;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @OneToMany(mappedBy = "codeprof")
    private List<Assure_1> assureList;
    @OneToMany(mappedBy = "codprora")
    private List<Profession_1> professionList;
    @JoinColumn(name = "CODPRORA", referencedColumnName = "CODEPROF")
    @ManyToOne
    private Profession_1 codprora;

    public Profession_1() {
    }

    public Profession_1(Integer codeprof) {
        this.codeprof = codeprof;
    }

    public Integer getCodeprof() {
        return codeprof;
    }

    public void setCodeprof(Integer codeprof) {
        this.codeprof = codeprof;
    }

    public String getLibeprof() {
        return libeprof;
    }

    public void setLibeprof(String libeprof) {
        this.libeprof = libeprof;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public List<Assure_1> getAssureList() {
        return assureList;
    }

    public void setAssureList(List<Assure_1> assureList) {
        this.assureList = assureList;
    }

    public List<Profession_1> getProfessionList() {
        return professionList;
    }

    public void setProfessionList(List<Profession_1> professionList) {
        this.professionList = professionList;
    }

    public Profession_1 getCodprora() {
        return codprora;
    }

    public void setCodprora(Profession_1 codprora) {
        this.codprora = codprora;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeprof != null ? codeprof.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Profession_1)) {
            return false;
        }
        Profession_1 other = (Profession_1) object;
        if ((this.codeprof == null && other.codeprof != null) || (this.codeprof != null && !this.codeprof.equals(other.codeprof))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Profession_1[ codeprof=" + codeprof + " ]";
    }
    
}
