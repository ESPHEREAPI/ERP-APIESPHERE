/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *
 * @author USER01
 */
@Embeddable
public class PolicePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "CODEINTE")
    private int codeinte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMEPOLI")
    private long numepoli;

    public PolicePK() {
    }

    public PolicePK(int codeinte, long numepoli) {
        this.codeinte = codeinte;
        this.numepoli = numepoli;
    }

    public int getCodeinte() {
        return codeinte;
    }

    public void setCodeinte(int codeinte) {
        this.codeinte = codeinte;
    }

    public long getNumepoli() {
        return numepoli;
    }

    public void setNumepoli(long numepoli) {
        this.numepoli = numepoli;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codeinte;
        hash += (int) numepoli;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PolicePK)) {
            return false;
        }
        PolicePK other = (PolicePK) object;
        if (this.codeinte != other.codeinte) {
            return false;
        }
        if (this.numepoli != other.numepoli) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.PolicePK[ codeinte=" + codeinte + ", numepoli=" + numepoli + " ]";
    }
    
}
