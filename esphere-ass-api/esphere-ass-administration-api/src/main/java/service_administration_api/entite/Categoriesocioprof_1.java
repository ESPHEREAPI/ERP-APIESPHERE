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
@Table(name = "CATEGORIESOCIOPROF")
@NamedQueries({
    @NamedQuery(name = "Categoriesocioprof_1.findAll", query = "SELECT c FROM Categoriesocioprof_1 c")})
public class Categoriesocioprof_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "COCASOPR")
    private String cocasopr;
    @Size(max = 90)
    @Column(name = "LICASOPR")
    private String licasopr;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @OneToMany(mappedBy = "cocasopr")
    private List<Assure_1> assureList;

    public Categoriesocioprof_1() {
    }

    public Categoriesocioprof_1(String cocasopr) {
        this.cocasopr = cocasopr;
    }

    public String getCocasopr() {
        return cocasopr;
    }

    public void setCocasopr(String cocasopr) {
        this.cocasopr = cocasopr;
    }

    public String getLicasopr() {
        return licasopr;
    }

    public void setLicasopr(String licasopr) {
        this.licasopr = licasopr;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cocasopr != null ? cocasopr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categoriesocioprof_1)) {
            return false;
        }
        Categoriesocioprof_1 other = (Categoriesocioprof_1) object;
        if ((this.cocasopr == null && other.cocasopr != null) || (this.cocasopr != null && !this.cocasopr.equals(other.cocasopr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Categoriesocioprof_1[ cocasopr=" + cocasopr + " ]";
    }
    
}
