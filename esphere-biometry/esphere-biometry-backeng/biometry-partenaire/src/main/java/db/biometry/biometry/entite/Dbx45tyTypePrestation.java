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
@Table(name = "dbx45ty_type_prestation")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyTypePrestation.findAll", query = "SELECT d FROM Dbx45tyTypePrestation d"),
    @NamedQuery(name = "Dbx45tyTypePrestation.findById", query = "SELECT d FROM Dbx45tyTypePrestation d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyTypePrestation.findByNom", query = "SELECT d FROM Dbx45tyTypePrestation d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyTypePrestation.findByAffiche", query = "SELECT d FROM Dbx45tyTypePrestation d WHERE d.affiche = :affiche"),
    @NamedQuery(name = "Dbx45tyTypePrestation.findByCategorie", query = "SELECT d FROM Dbx45tyTypePrestation d WHERE d.categorie = :categorie")})
public class Dbx45tyTypePrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "affiche")
    private int affiche;
    @Basic(optional = false)
    @Column(name = "categorie")
    private String categorie;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeConsultation")
    private List<Dbx45tyConsultation> dbx45tyConsultationList;
    @OneToMany(mappedBy = "typeExamen")
    private List<Dbx45tyLignePrestation> dbx45tyLignePrestationList;
    @OneToMany(mappedBy = "descriptionSoins")
    private List<Dbx45tyLignePrestation> dbx45tyLignePrestationList1;

    public Dbx45tyTypePrestation() {
    }

    public Dbx45tyTypePrestation(String id) {
        this.id = id;
    }

    public Dbx45tyTypePrestation(String id, String nom, int affiche, String categorie) {
        this.id = id;
        this.nom = nom;
        this.affiche = affiche;
        this.categorie = categorie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getAffiche() {
        return affiche;
    }

    public void setAffiche(int affiche) {
        this.affiche = affiche;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public List<Dbx45tyConsultation> getDbx45tyConsultationList() {
        return dbx45tyConsultationList;
    }

    public void setDbx45tyConsultationList(List<Dbx45tyConsultation> dbx45tyConsultationList) {
        this.dbx45tyConsultationList = dbx45tyConsultationList;
    }

    public List<Dbx45tyLignePrestation> getDbx45tyLignePrestationList() {
        return dbx45tyLignePrestationList;
    }

    public void setDbx45tyLignePrestationList(List<Dbx45tyLignePrestation> dbx45tyLignePrestationList) {
        this.dbx45tyLignePrestationList = dbx45tyLignePrestationList;
    }

    public List<Dbx45tyLignePrestation> getDbx45tyLignePrestationList1() {
        return dbx45tyLignePrestationList1;
    }

    public void setDbx45tyLignePrestationList1(List<Dbx45tyLignePrestation> dbx45tyLignePrestationList1) {
        this.dbx45tyLignePrestationList1 = dbx45tyLignePrestationList1;
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
        if (!(object instanceof Dbx45tyTypePrestation)) {
            return false;
        }
        Dbx45tyTypePrestation other = (Dbx45tyTypePrestation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyTypePrestation[ id=" + id + " ]";
    }
    
}
