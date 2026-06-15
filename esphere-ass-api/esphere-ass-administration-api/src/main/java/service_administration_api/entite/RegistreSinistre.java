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
@Table(name = "REGISTRE_SINISTRE")
@NamedQueries({
    @NamedQuery(name = "RegistreSinistre.findAll", query = "SELECT r FROM RegistreSinistre r"),
    @NamedQuery(name = "RegistreSinistre.findByCodregsi", query = "SELECT r FROM RegistreSinistre r WHERE r.codregsi = :codregsi"),
    @NamedQuery(name = "RegistreSinistre.findByPreFixe", query = "SELECT r FROM RegistreSinistre r WHERE r.preFixe = :preFixe"),
    @NamedQuery(name = "RegistreSinistre.findByPlagnume", query = "SELECT r FROM RegistreSinistre r WHERE r.plagnume = :plagnume"),
    @NamedQuery(name = "RegistreSinistre.findByNumeLot", query = "SELECT r FROM RegistreSinistre r WHERE r.numeLot = :numeLot"),
    @NamedQuery(name = "RegistreSinistre.findByNatnumsi", query = "SELECT r FROM RegistreSinistre r WHERE r.natnumsi = :natnumsi")})
public class RegistreSinistre implements Serializable {

    @OneToMany(mappedBy = "codregsi")
    private List<Categorie> categorieList;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODREGSI")
    private String codregsi;
    @Column(name = "PRE_FIXE")
    private Short preFixe;
    @Column(name = "PLAGNUME")
    private Long plagnume;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Column(name = "NATNUMSI")
    private String natnumsi;
//    @OneToMany(mappedBy = "codregsi")
//    private List<Categorie> categorieList;

    public RegistreSinistre() {
    }

    public RegistreSinistre(String codregsi) {
        this.codregsi = codregsi;
    }

    public String getCodregsi() {
        return codregsi;
    }

    public void setCodregsi(String codregsi) {
        this.codregsi = codregsi;
    }

    public Short getPreFixe() {
        return preFixe;
    }

    public void setPreFixe(Short preFixe) {
        this.preFixe = preFixe;
    }

    public Long getPlagnume() {
        return plagnume;
    }

    public void setPlagnume(Long plagnume) {
        this.plagnume = plagnume;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getNatnumsi() {
        return natnumsi;
    }

    public void setNatnumsi(String natnumsi) {
        this.natnumsi = natnumsi;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codregsi != null ? codregsi.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RegistreSinistre)) {
            return false;
        }
        RegistreSinistre other = (RegistreSinistre) object;
        if ((this.codregsi == null && other.codregsi != null) || (this.codregsi != null && !this.codregsi.equals(other.codregsi))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.RegistreSinistre[ codregsi=" + codregsi + " ]";
    }

    public List<Categorie> getCategorieList() {
        return categorieList;
    }

    public void setCategorieList(List<Categorie> categorieList) {
        this.categorieList = categorieList;
    }
    
}
