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
@Table(name = "dbx45ty_prestataire")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyPrestataire.findAll", query = "SELECT d FROM Dbx45tyPrestataire d"),
    @NamedQuery(name = "Dbx45tyPrestataire.findById", query = "SELECT d FROM Dbx45tyPrestataire d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyPrestataire.findByNom", query = "SELECT d FROM Dbx45tyPrestataire d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyPrestataire.findByAdresse", query = "SELECT d FROM Dbx45tyPrestataire d WHERE d.adresse = :adresse"),
    @NamedQuery(name = "Dbx45tyPrestataire.findByEmail", query = "SELECT d FROM Dbx45tyPrestataire d WHERE d.email = :email"),
    @NamedQuery(name = "Dbx45tyPrestataire.findByTelephone", query = "SELECT d FROM Dbx45tyPrestataire d WHERE d.telephone = :telephone"),
    @NamedQuery(name = "Dbx45tyPrestataire.findByRegistre", query = "SELECT d FROM Dbx45tyPrestataire d WHERE d.registre = :registre"),
    @NamedQuery(name = "Dbx45tyPrestataire.findByLogo", query = "SELECT d FROM Dbx45tyPrestataire d WHERE d.logo = :logo"),
    @NamedQuery(name = "Dbx45tyPrestataire.findByStatut", query = "SELECT d FROM Dbx45tyPrestataire d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyPrestataire.findBySupprime", query = "SELECT d FROM Dbx45tyPrestataire d WHERE d.supprime = :supprime")})
public class Dbx45tyPrestataire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Column(name = "nom")
    private String nom;
    @Column(name = "adresse")
    private String adresse;
    @Column(name = "email")
    private String email;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "registre")
    private String registre;
    @Column(name = "logo")
    private String logo;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "prestataireId")
    private List<Dbx45tyEmploye> dbx45tyEmployeList;
    @OneToMany(mappedBy = "prestataireId")
    private List<Dbx45tyMedicament> dbx45tyMedicamentList;
    @JoinColumn(name = "categorie_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyCategoriePrestataire categorieId;
    @JoinColumn(name = "ville_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyVille villeId;
    @OneToMany(mappedBy = "prestataireId")
    private List<Dbx45tyPrestation> dbx45tyPrestationList;
    @OneToMany(mappedBy = "prestataireId")
    private List<Dbx45tyLignePrestation> dbx45tyLignePrestationList;

    public Dbx45tyPrestataire() {
    }

    public Dbx45tyPrestataire(String id) {
        this.id = id;
    }

    public Dbx45tyPrestataire(String id, String statut, String supprime) {
        this.id = id;
        this.statut = statut;
        this.supprime = supprime;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRegistre() {
        return registre;
    }

    public void setRegistre(String registre) {
        this.registre = registre;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public List<Dbx45tyMedicament> getDbx45tyMedicamentList() {
        return dbx45tyMedicamentList;
    }

    public void setDbx45tyMedicamentList(List<Dbx45tyMedicament> dbx45tyMedicamentList) {
        this.dbx45tyMedicamentList = dbx45tyMedicamentList;
    }

    public Dbx45tyCategoriePrestataire getCategorieId() {
        return categorieId;
    }

    public void setCategorieId(Dbx45tyCategoriePrestataire categorieId) {
        this.categorieId = categorieId;
    }

    public Dbx45tyVille getVilleId() {
        return villeId;
    }

    public void setVilleId(Dbx45tyVille villeId) {
        this.villeId = villeId;
    }

    public List<Dbx45tyPrestation> getDbx45tyPrestationList() {
        return dbx45tyPrestationList;
    }

    public void setDbx45tyPrestationList(List<Dbx45tyPrestation> dbx45tyPrestationList) {
        this.dbx45tyPrestationList = dbx45tyPrestationList;
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
        if (!(object instanceof Dbx45tyPrestataire)) {
            return false;
        }
        Dbx45tyPrestataire other = (Dbx45tyPrestataire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyPrestataire[ id=" + id + " ]";
    }
    
}
