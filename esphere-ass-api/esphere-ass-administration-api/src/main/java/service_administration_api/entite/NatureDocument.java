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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "NATURE_DOCUMENT")
@NamedQueries({
    @NamedQuery(name = "NatureDocument.findAll", query = "SELECT n FROM NatureDocument n")})
public class NatureDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "NATUDOCU")
    private String natudocu;
    @Size(max = 40)
    @Column(name = "LIBNATDO")
    private String libnatdo;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;

    public NatureDocument() {
    }

    public NatureDocument(String natudocu) {
        this.natudocu = natudocu;
    }

    public String getNatudocu() {
        return natudocu;
    }

    public void setNatudocu(String natudocu) {
        this.natudocu = natudocu;
    }

    public String getLibnatdo() {
        return libnatdo;
    }

    public void setLibnatdo(String libnatdo) {
        this.libnatdo = libnatdo;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (natudocu != null ? natudocu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NatureDocument)) {
            return false;
        }
        NatureDocument other = (NatureDocument) object;
        if ((this.natudocu == null && other.natudocu != null) || (this.natudocu != null && !this.natudocu.equals(other.natudocu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.NatureDocument[ natudocu=" + natudocu + " ]";
    }
    
}
