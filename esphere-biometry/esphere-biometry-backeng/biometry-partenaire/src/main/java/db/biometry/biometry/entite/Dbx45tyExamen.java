/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
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
@Table(name = "dbx45ty_examen")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyExamen.findAll", query = "SELECT d FROM Dbx45tyExamen d"),
    @NamedQuery(name = "Dbx45tyExamen.findById", query = "SELECT d FROM Dbx45tyExamen d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyExamen.findByCode", query = "SELECT d FROM Dbx45tyExamen d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyExamen.findByNom", query = "SELECT d FROM Dbx45tyExamen d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyExamen.findByCotation", query = "SELECT d FROM Dbx45tyExamen d WHERE d.cotation = :cotation"),
    @NamedQuery(name = "Dbx45tyExamen.findByPrix", query = "SELECT d FROM Dbx45tyExamen d WHERE d.prix = :prix"),
    @NamedQuery(name = "Dbx45tyExamen.findByStatut", query = "SELECT d FROM Dbx45tyExamen d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyExamen.findBySupprime", query = "SELECT d FROM Dbx45tyExamen d WHERE d.supprime = :supprime")})
public class Dbx45tyExamen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "cotation")
    private short cotation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix")
    private Double prix;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(mappedBy = "examenId")
    private List<Dbx45tyLignePrestation> dbx45tyLignePrestationList;

    public Dbx45tyExamen() {
    }

    public Dbx45tyExamen(Integer id) {
        this.id = id;
    }

    public Dbx45tyExamen(Integer id, String code, String nom, short cotation, String statut, String supprime) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.cotation = cotation;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public short getCotation() {
        return cotation;
    }

    public void setCotation(short cotation) {
        this.cotation = cotation;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
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
        if (!(object instanceof Dbx45tyExamen)) {
            return false;
        }
        Dbx45tyExamen other = (Dbx45tyExamen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyExamen[ id=" + id + " ]";
    }
    
}
