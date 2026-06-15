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
@Table(name = "REDUCTION_AUTOMOBILE")
@NamedQueries({
    @NamedQuery(name = "ReductionAutomobile.findAll", query = "SELECT r FROM ReductionAutomobile r")})
public class ReductionAutomobile implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "CODEREDU")
    private String coderedu;
    @Size(max = 60)
    @Column(name = "LIBEREDU")
    private String liberedu;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 1)
    @Column(name = "COMBRCBM")
    private String combrcbm;
    @Size(max = 1)
    @Column(name = "COMBRCRP")
    private String combrcrp;
    @OneToMany(mappedBy = "coderedu")
    private List<Police> policeList;

    public ReductionAutomobile() {
    }

    public ReductionAutomobile(String coderedu) {
        this.coderedu = coderedu;
    }

    public String getCoderedu() {
        return coderedu;
    }

    public void setCoderedu(String coderedu) {
        this.coderedu = coderedu;
    }

    public String getLiberedu() {
        return liberedu;
    }

    public void setLiberedu(String liberedu) {
        this.liberedu = liberedu;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getCombrcbm() {
        return combrcbm;
    }

    public void setCombrcbm(String combrcbm) {
        this.combrcbm = combrcbm;
    }

    public String getCombrcrp() {
        return combrcrp;
    }

    public void setCombrcrp(String combrcrp) {
        this.combrcrp = combrcrp;
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
        hash += (coderedu != null ? coderedu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReductionAutomobile)) {
            return false;
        }
        ReductionAutomobile other = (ReductionAutomobile) object;
        if ((this.coderedu == null && other.coderedu != null) || (this.coderedu != null && !this.coderedu.equals(other.coderedu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.ReductionAutomobile[ coderedu=" + coderedu + " ]";
    }
    
}
