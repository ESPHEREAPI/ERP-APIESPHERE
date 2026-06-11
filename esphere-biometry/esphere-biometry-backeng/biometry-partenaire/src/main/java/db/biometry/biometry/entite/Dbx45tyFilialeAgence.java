/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Basic;
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
@Table(name = "dbx45ty_filiale_agence")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyFilialeAgence.findAll", query = "SELECT d FROM Dbx45tyFilialeAgence d"),
    @NamedQuery(name = "Dbx45tyFilialeAgence.findById", query = "SELECT d FROM Dbx45tyFilialeAgence d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyFilialeAgence.findByDateCreation", query = "SELECT d FROM Dbx45tyFilialeAgence d WHERE d.dateCreation = :dateCreation"),
    @NamedQuery(name = "Dbx45tyFilialeAgence.findByStatut", query = "SELECT d FROM Dbx45tyFilialeAgence d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyFilialeAgence.findBySupprime", query = "SELECT d FROM Dbx45tyFilialeAgence d WHERE d.supprime = :supprime")})
public class Dbx45tyFilialeAgence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(mappedBy = "filialeAgenceId")
    private List<Dbx45tyEmploye> dbx45tyEmployeList;
    @JoinColumn(name = "agence_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyAgence agenceId;
    @JoinColumn(name = "ville_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyVille villeId;
    @OneToMany(mappedBy = "siegeSocialId")
    private List<Dbx45tyAgence> dbx45tyAgenceList;

    public Dbx45tyFilialeAgence() {
    }

    public Dbx45tyFilialeAgence(Integer id) {
        this.id = id;
    }

    public Dbx45tyFilialeAgence(Integer id, String statut, String supprime) {
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

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
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

    public List<Dbx45tyEmploye> getDbx45tyEmployeList() {
        return dbx45tyEmployeList;
    }

    public void setDbx45tyEmployeList(List<Dbx45tyEmploye> dbx45tyEmployeList) {
        this.dbx45tyEmployeList = dbx45tyEmployeList;
    }

    public Dbx45tyAgence getAgenceId() {
        return agenceId;
    }

    public void setAgenceId(Dbx45tyAgence agenceId) {
        this.agenceId = agenceId;
    }

    public Dbx45tyVille getVilleId() {
        return villeId;
    }

    public void setVilleId(Dbx45tyVille villeId) {
        this.villeId = villeId;
    }

    public List<Dbx45tyAgence> getDbx45tyAgenceList() {
        return dbx45tyAgenceList;
    }

    public void setDbx45tyAgenceList(List<Dbx45tyAgence> dbx45tyAgenceList) {
        this.dbx45tyAgenceList = dbx45tyAgenceList;
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
        if (!(object instanceof Dbx45tyFilialeAgence)) {
            return false;
        }
        Dbx45tyFilialeAgence other = (Dbx45tyFilialeAgence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyFilialeAgence[ id=" + id + " ]";
    }
    
}
