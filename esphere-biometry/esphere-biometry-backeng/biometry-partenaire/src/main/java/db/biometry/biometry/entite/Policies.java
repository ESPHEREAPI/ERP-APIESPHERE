/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "policies")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Policies.findAll", query = "SELECT p FROM Policies p"),
    @NamedQuery(name = "Policies.findByPolicyNumber", query = "SELECT p FROM Policies p WHERE p.policyNumber = :policyNumber"),
    @NamedQuery(name = "Policies.findByFullName", query = "SELECT p FROM Policies p WHERE p.fullName = :fullName"),
    @NamedQuery(name = "Policies.findByStatus", query = "SELECT p FROM Policies p WHERE p.status = :status"),
    @NamedQuery(name = "Policies.findByCreatedAt", query = "SELECT p FROM Policies p WHERE p.createdAt = :createdAt")})
public class Policies implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "policy_number")
    private String policyNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "full_name")
    private String fullName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "status")
    private String status;
    @Basic(optional = false)
    @NotNull
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Policies() {
    }

    public Policies(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Policies(String policyNumber, String fullName, String status, Date createdAt) {
        this.policyNumber = policyNumber;
        this.fullName = fullName;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (policyNumber != null ? policyNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Policies)) {
            return false;
        }
        Policies other = (Policies) object;
        if ((this.policyNumber == null && other.policyNumber != null) || (this.policyNumber != null && !this.policyNumber.equals(other.policyNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Policies[ policyNumber=" + policyNumber + " ]";
    }
    
}
