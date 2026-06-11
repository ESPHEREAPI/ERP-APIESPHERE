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
@Table(name = "dbx45ty_categorie_prestataire")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyCategoriePrestataire.findAll", query = "SELECT d FROM Dbx45tyCategoriePrestataire d"),
    @NamedQuery(name = "Dbx45tyCategoriePrestataire.findById", query = "SELECT d FROM Dbx45tyCategoriePrestataire d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyCategoriePrestataire.findByNom", query = "SELECT d FROM Dbx45tyCategoriePrestataire d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyCategoriePrestataire.findByStatut", query = "SELECT d FROM Dbx45tyCategoriePrestataire d WHERE d.statut = :statut")})
public class Dbx45tyCategoriePrestataire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "categorieId")
    private List<Dbx45tyPrestataire> dbx45tyPrestataireList;

    public Dbx45tyCategoriePrestataire() {
    }

    public Dbx45tyCategoriePrestataire(String id) {
        this.id = id;
    }

    public Dbx45tyCategoriePrestataire(String id, String nom, String statut) {
        this.id = id;
        this.nom = nom;
        this.statut = statut;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<Dbx45tyPrestataire> getDbx45tyPrestataireList() {
        return dbx45tyPrestataireList;
    }

    public void setDbx45tyPrestataireList(List<Dbx45tyPrestataire> dbx45tyPrestataireList) {
        this.dbx45tyPrestataireList = dbx45tyPrestataireList;
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
        if (!(object instanceof Dbx45tyCategoriePrestataire)) {
            return false;
        }
        Dbx45tyCategoriePrestataire other = (Dbx45tyCategoriePrestataire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyCategoriePrestataire[ id=" + id + " ]";
    }
    
}
