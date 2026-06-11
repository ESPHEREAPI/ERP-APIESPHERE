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
@Table(name = "dbx45ty_medicament")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyMedicament.findAll", query = "SELECT d FROM Dbx45tyMedicament d"),
    @NamedQuery(name = "Dbx45tyMedicament.findById", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyMedicament.findByCode", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyMedicament.findByNom", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyMedicament.findByOrigine", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.origine = :origine"),
    @NamedQuery(name = "Dbx45tyMedicament.findByPrix", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.prix = :prix"),
    @NamedQuery(name = "Dbx45tyMedicament.findByQuantite", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.quantite = :quantite"),
    @NamedQuery(name = "Dbx45tyMedicament.findByCategorie", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.categorie = :categorie"),
    @NamedQuery(name = "Dbx45tyMedicament.findByStatut", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyMedicament.findBySupprime", query = "SELECT d FROM Dbx45tyMedicament d WHERE d.supprime = :supprime")})
public class Dbx45tyMedicament implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Column(name = "origine")
    private String origine;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "prix")
    private Double prix;
    @Column(name = "quantite")
    private Double quantite;
    @Basic(optional = false)
    @Column(name = "categorie")
    private String categorie;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @JoinColumn(name = "prestataire_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyPrestataire prestataireId;
    @OneToMany(mappedBy = "medicamentId")
    private List<Dbx45tyLignePrestation> dbx45tyLignePrestationList;

    public Dbx45tyMedicament() {
    }

    public Dbx45tyMedicament(Integer id) {
        this.id = id;
    }

    public Dbx45tyMedicament(Integer id, String nom, String categorie, String statut, String supprime) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
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

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
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
        if (!(object instanceof Dbx45tyMedicament)) {
            return false;
        }
        Dbx45tyMedicament other = (Dbx45tyMedicament) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyMedicament[ id=" + id + " ]";
    }
    
}
