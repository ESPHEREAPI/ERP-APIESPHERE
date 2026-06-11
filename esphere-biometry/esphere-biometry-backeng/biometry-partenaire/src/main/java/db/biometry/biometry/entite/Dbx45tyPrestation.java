/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_prestation")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyPrestation.findAll", query = "SELECT d FROM Dbx45tyPrestation d"),
    @NamedQuery(name = "Dbx45tyPrestation.findById", query = "SELECT d FROM Dbx45tyPrestation d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyPrestation.findByNaturePrestation", query = "SELECT d FROM Dbx45tyPrestation d WHERE d.naturePrestation = :naturePrestation"),
    @NamedQuery(name = "Dbx45tyPrestation.findByDate", query = "SELECT d FROM Dbx45tyPrestation d WHERE d.date = :date"),
    @NamedQuery(name = "Dbx45tyPrestation.findBySupprime", query = "SELECT d FROM Dbx45tyPrestation d WHERE d.supprime = :supprime")})
public class Dbx45tyPrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nature_prestation")
    private String naturePrestation;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @JoinColumn(name = "visite_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyVisite visiteId;
    @JoinColumn(name = "prestataire_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyPrestataire prestataireId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prestationId")
    private List<Dbx45tyLignePrestation> dbx45tyLignePrestationList;

    public Dbx45tyPrestation() {
    }

    public Dbx45tyPrestation(Integer id) {
        this.id = id;
    }

    public Dbx45tyPrestation(Integer id, String naturePrestation, Date date, String supprime) {
        this.id = id;
        this.naturePrestation = naturePrestation;
        this.date = date;
        this.supprime = supprime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNaturePrestation() {
        return naturePrestation;
    }

    public void setNaturePrestation(String naturePrestation) {
        this.naturePrestation = naturePrestation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSupprime() {
        return supprime;
    }

    public void setSupprime(String supprime) {
        this.supprime = supprime;
    }

    public Dbx45tyVisite getVisiteId() {
        return visiteId;
    }

    public void setVisiteId(Dbx45tyVisite visiteId) {
        this.visiteId = visiteId;
    }

    public Dbx45tyPrestataire getPrestataireId() {
        return prestataireId;
    }

    public void setPrestataireId(Dbx45tyPrestataire prestataireId) {
        this.prestataireId = prestataireId;
    }

    public List<Dbx45tyLignePrestation> getDbx45tyLignePrestationList() {
        return dbx45tyLignePrestationList;
    }

    public void setDbx45tyLignePrestationList(List<Dbx45tyLignePrestation> dbx45tyLignePrestationList) {
        this.dbx45tyLignePrestationList = dbx45tyLignePrestationList;
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
        if (!(object instanceof Dbx45tyPrestation)) {
            return false;
        }
        Dbx45tyPrestation other = (Dbx45tyPrestation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyPrestation[ id=" + id + " ]";
    }
    
}
