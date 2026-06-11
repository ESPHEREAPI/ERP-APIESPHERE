/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_ville")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyVille.findAll", query = "SELECT d FROM Dbx45tyVille d"),
    @NamedQuery(name = "Dbx45tyVille.findById", query = "SELECT d FROM Dbx45tyVille d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyVille.findByRegionId", query = "SELECT d FROM Dbx45tyVille d WHERE d.regionId = :regionId"),
    @NamedQuery(name = "Dbx45tyVille.findByCode", query = "SELECT d FROM Dbx45tyVille d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyVille.findByCodeZone", query = "SELECT d FROM Dbx45tyVille d WHERE d.codeZone = :codeZone"),
    @NamedQuery(name = "Dbx45tyVille.findByStatut", query = "SELECT d FROM Dbx45tyVille d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyVille.findBySupprime", query = "SELECT d FROM Dbx45tyVille d WHERE d.supprime = :supprime")})
public class Dbx45tyVille implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "region_id")
    private int regionId;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Column(name = "code_zone")
    private String codeZone;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(mappedBy = "villeId")
    private List<Dbx45tyPrestataire> dbx45tyPrestataireList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "villeId")
    private List<Dbx45tyFilialeAgence> dbx45tyFilialeAgenceList;
    @OneToMany(mappedBy = "chefLieuId")
    private List<Dbx45tyRegion> dbx45tyRegionList;
    @OneToMany(mappedBy = "capitaleId")
    private List<Dbx45tyPays> dbx45tyPaysList;

    public Dbx45tyVille() {
    }

    public Dbx45tyVille(Integer id) {
        this.id = id;
    }

    public Dbx45tyVille(Integer id, int regionId, String code, String statut, String supprime) {
        this.id = id;
        this.regionId = regionId;
        this.code = code;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeZone() {
        return codeZone;
    }

    public void setCodeZone(String codeZone) {
        this.codeZone = codeZone;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getSupprime() {
        return supprime;
    }

    public void setSupprime(String supprime) {
        this.supprime = supprime;
    }

    public List<Dbx45tyPrestataire> getDbx45tyPrestataireList() {
        return dbx45tyPrestataireList;
    }

    public void setDbx45tyPrestataireList(List<Dbx45tyPrestataire> dbx45tyPrestataireList) {
        this.dbx45tyPrestataireList = dbx45tyPrestataireList;
    }

    public List<Dbx45tyFilialeAgence> getDbx45tyFilialeAgenceList() {
        return dbx45tyFilialeAgenceList;
    }

    public void setDbx45tyFilialeAgenceList(List<Dbx45tyFilialeAgence> dbx45tyFilialeAgenceList) {
        this.dbx45tyFilialeAgenceList = dbx45tyFilialeAgenceList;
    }

    public List<Dbx45tyRegion> getDbx45tyRegionList() {
        return dbx45tyRegionList;
    }

    public void setDbx45tyRegionList(List<Dbx45tyRegion> dbx45tyRegionList) {
        this.dbx45tyRegionList = dbx45tyRegionList;
    }

    public List<Dbx45tyPays> getDbx45tyPaysList() {
        return dbx45tyPaysList;
    }

    public void setDbx45tyPaysList(List<Dbx45tyPays> dbx45tyPaysList) {
        this.dbx45tyPaysList = dbx45tyPaysList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dbx45tyVille)) {
            return false;
        }
        Dbx45tyVille other = (Dbx45tyVille) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyVille[ id=" + id + " ]";
    }
    
}
