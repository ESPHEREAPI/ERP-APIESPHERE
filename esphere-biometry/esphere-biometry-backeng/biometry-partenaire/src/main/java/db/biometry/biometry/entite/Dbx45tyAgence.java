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
@Table(name = "dbx45ty_agence")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyAgence.findAll", query = "SELECT d FROM Dbx45tyAgence d"),
    @NamedQuery(name = "Dbx45tyAgence.findById", query = "SELECT d FROM Dbx45tyAgence d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyAgence.findByNom", query = "SELECT d FROM Dbx45tyAgence d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyAgence.findByCode", query = "SELECT d FROM Dbx45tyAgence d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyAgence.findByDateCreation", query = "SELECT d FROM Dbx45tyAgence d WHERE d.dateCreation = :dateCreation"),
    @NamedQuery(name = "Dbx45tyAgence.findBySiteWeb", query = "SELECT d FROM Dbx45tyAgence d WHERE d.siteWeb = :siteWeb"),
    @NamedQuery(name = "Dbx45tyAgence.findByStatut", query = "SELECT d FROM Dbx45tyAgence d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyAgence.findBySupprime", query = "SELECT d FROM Dbx45tyAgence d WHERE d.supprime = :supprime")})
public class Dbx45tyAgence implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "code",unique = true)
    private String code;
    @Column(name = "date_creation")
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    @Column(name = "site_web")
    private String siteWeb;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agenceId")
    private List<Dbx45tyFilialeAgence> dbx45tyFilialeAgenceList;
    @JoinColumn(name = "siege_social_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyFilialeAgence siegeSocialId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agenceId")
    private List<Dbx45tyAgenceLangue> dbx45tyAgenceLangueList;

    public Dbx45tyAgence() {
    }

    public Dbx45tyAgence(Integer id) {
        this.id = id;
    }

    public Dbx45tyAgence(Integer id, String nom, String code, String statut, String supprime) {
        this.id = id;
        this.nom = nom;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
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

    public List<Dbx45tyFilialeAgence> getDbx45tyFilialeAgenceList() {
        return dbx45tyFilialeAgenceList;
    }

    public void setDbx45tyFilialeAgenceList(List<Dbx45tyFilialeAgence> dbx45tyFilialeAgenceList) {
        this.dbx45tyFilialeAgenceList = dbx45tyFilialeAgenceList;
    }

    public Dbx45tyFilialeAgence getSiegeSocialId() {
        return siegeSocialId;
    }

    public void setSiegeSocialId(Dbx45tyFilialeAgence siegeSocialId) {
        this.siegeSocialId = siegeSocialId;
    }

    public List<Dbx45tyAgenceLangue> getDbx45tyAgenceLangueList() {
        return dbx45tyAgenceLangueList;
    }

    public void setDbx45tyAgenceLangueList(List<Dbx45tyAgenceLangue> dbx45tyAgenceLangueList) {
        this.dbx45tyAgenceLangueList = dbx45tyAgenceLangueList;
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
        if (!(object instanceof Dbx45tyAgence)) {
            return false;
        }
        Dbx45tyAgence other = (Dbx45tyAgence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyAgence[ id=" + id + " ]";
    }
    
}
