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
@Table(name = "dbx45ty_zone_monde")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyZoneMonde.findAll", query = "SELECT d FROM Dbx45tyZoneMonde d"),
    @NamedQuery(name = "Dbx45tyZoneMonde.findById", query = "SELECT d FROM Dbx45tyZoneMonde d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyZoneMonde.findByCode", query = "SELECT d FROM Dbx45tyZoneMonde d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyZoneMonde.findByStatut", query = "SELECT d FROM Dbx45tyZoneMonde d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyZoneMonde.findBySupprime", query = "SELECT d FROM Dbx45tyZoneMonde d WHERE d.supprime = :supprime")})
public class Dbx45tyZoneMonde implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "zoneMondeId")
    private List<Dbx45tyPays> dbx45tyPaysList;

    public Dbx45tyZoneMonde() {
    }

    public Dbx45tyZoneMonde(Short id) {
        this.id = id;
    }

    public Dbx45tyZoneMonde(Short id, String code, String statut, String supprime) {
        this.id = id;
        this.code = code;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        if (!(object instanceof Dbx45tyZoneMonde)) {
            return false;
        }
        Dbx45tyZoneMonde other = (Dbx45tyZoneMonde) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyZoneMonde[ id=" + id + " ]";
    }
    
}
