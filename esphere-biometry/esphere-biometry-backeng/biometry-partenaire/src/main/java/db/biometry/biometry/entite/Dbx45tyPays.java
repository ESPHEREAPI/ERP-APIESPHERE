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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_pays")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyPays.findAll", query = "SELECT d FROM Dbx45tyPays d"),
    @NamedQuery(name = "Dbx45tyPays.findById", query = "SELECT d FROM Dbx45tyPays d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyPays.findByCodeIso", query = "SELECT d FROM Dbx45tyPays d WHERE d.codeIso = :codeIso"),
    @NamedQuery(name = "Dbx45tyPays.findByCodePostal", query = "SELECT d FROM Dbx45tyPays d WHERE d.codePostal = :codePostal"),
    @NamedQuery(name = "Dbx45tyPays.findByStatut", query = "SELECT d FROM Dbx45tyPays d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyPays.findBySupprime", query = "SELECT d FROM Dbx45tyPays d WHERE d.supprime = :supprime")})
public class Dbx45tyPays implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code_iso")
    private String codeIso;
    @Column(name = "code_postal")
    private String codePostal;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paysId")
    private List<Dbx45tyPaysLangue> dbx45tyPaysLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paysId")
    private List<Dbx45tyLangueHasPays> dbx45tyLangueHasPaysList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paysId")
    private List<Dbx45tyRegion> dbx45tyRegionList;
    @JoinColumn(name = "zone_monde_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyZoneMonde zoneMondeId;
    @JoinColumn(name = "capitale_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyVille capitaleId;

    public Dbx45tyPays() {
    }

    public Dbx45tyPays(Integer id) {
        this.id = id;
    }

    public Dbx45tyPays(Integer id, String statut, String supprime) {
        this.id = id;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeIso() {
        return codeIso;
    }

    public void setCodeIso(String codeIso) {
        this.codeIso = codeIso;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
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

    public List<Dbx45tyPaysLangue> getDbx45tyPaysLangueList() {
        return dbx45tyPaysLangueList;
    }

    public void setDbx45tyPaysLangueList(List<Dbx45tyPaysLangue> dbx45tyPaysLangueList) {
        this.dbx45tyPaysLangueList = dbx45tyPaysLangueList;
    }

    public List<Dbx45tyLangueHasPays> getDbx45tyLangueHasPaysList() {
        return dbx45tyLangueHasPaysList;
    }

    public void setDbx45tyLangueHasPaysList(List<Dbx45tyLangueHasPays> dbx45tyLangueHasPaysList) {
        this.dbx45tyLangueHasPaysList = dbx45tyLangueHasPaysList;
    }

    public List<Dbx45tyRegion> getDbx45tyRegionList() {
        return dbx45tyRegionList;
    }

    public void setDbx45tyRegionList(List<Dbx45tyRegion> dbx45tyRegionList) {
        this.dbx45tyRegionList = dbx45tyRegionList;
    }

    public Dbx45tyZoneMonde getZoneMondeId() {
        return zoneMondeId;
    }

    public void setZoneMondeId(Dbx45tyZoneMonde zoneMondeId) {
        this.zoneMondeId = zoneMondeId;
    }

    public Dbx45tyVille getCapitaleId() {
        return capitaleId;
    }

    public void setCapitaleId(Dbx45tyVille capitaleId) {
        this.capitaleId = capitaleId;
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
        if (!(object instanceof Dbx45tyPays)) {
            return false;
        }
        Dbx45tyPays other = (Dbx45tyPays) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyPays[ id=" + id + " ]";
    }
    
}
