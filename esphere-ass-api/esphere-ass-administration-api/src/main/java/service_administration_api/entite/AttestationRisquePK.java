/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * @author USER01
 */
@Embeddable
public class AttestationRisquePK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "CODEINTE")
    private int codeinte;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMEPOLI")
    private long numepoli;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMEAVEN")
    private short numeaven;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODERISQ")
    private long coderisq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "NATURISQ")
    private String naturisq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "CODTYPDO")
    private String codtypdo;

    public AttestationRisquePK() {
    }

    public AttestationRisquePK(int codeinte, long numepoli, short numeaven, long coderisq, String naturisq, String codtypdo) {
        this.codeinte = codeinte;
        this.numepoli = numepoli;
        this.numeaven = numeaven;
        this.coderisq = coderisq;
        this.naturisq = naturisq;
        this.codtypdo = codtypdo;
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

    public short getNumeaven() {
        return numeaven;
    }

    public void setNumeaven(short numeaven) {
        this.numeaven = numeaven;
    }

    public long getCoderisq() {
        return coderisq;
    }

    public void setCoderisq(long coderisq) {
        this.coderisq = coderisq;
    }

    public String getNaturisq() {
        return naturisq;
    }

    public void setNaturisq(String naturisq) {
        this.naturisq = naturisq;
    }

    public String getCodtypdo() {
        return codtypdo;
    }

    public void setCodtypdo(String codtypdo) {
        this.codtypdo = codtypdo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) codeinte;
        hash += (int) numepoli;
        hash += (int) numeaven;
        hash += (int) coderisq;
        hash += (naturisq != null ? naturisq.hashCode() : 0);
        hash += (codtypdo != null ? codtypdo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttestationRisquePK)) {
            return false;
        }
        AttestationRisquePK other = (AttestationRisquePK) object;
        if (this.codeinte != other.codeinte) {
            return false;
        }
        if (this.numepoli != other.numepoli) {
            return false;
        }
        if (this.numeaven != other.numeaven) {
            return false;
        }
        if (this.coderisq != other.coderisq) {
            return false;
        }
        if ((this.naturisq == null && other.naturisq != null) || (this.naturisq != null && !this.naturisq.equals(other.naturisq))) {
            return false;
        }
        if ((this.codtypdo == null && other.codtypdo != null) || (this.codtypdo != null && !this.codtypdo.equals(other.codtypdo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.AttestationRisquePK[ codeinte=" + codeinte + ", numepoli=" + numepoli + ", numeaven=" + numeaven + ", coderisq=" + coderisq + ", naturisq=" + naturisq + ", codtypdo=" + codtypdo + " ]";
    }
    
}
