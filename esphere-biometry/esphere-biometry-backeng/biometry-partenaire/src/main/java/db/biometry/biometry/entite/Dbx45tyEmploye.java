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
@Table(name = "dbx45ty_employe")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyEmploye.findAll", query = "SELECT d FROM Dbx45tyEmploye d"),
    @NamedQuery(name = "Dbx45tyEmploye.findById", query = "SELECT d FROM Dbx45tyEmploye d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyEmploye.findByConnexionAppli", query = "SELECT d FROM Dbx45tyEmploye d WHERE d.connexionAppli = :connexionAppli")})
public class Dbx45tyEmploye implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "connexion_appli")
    private String connexionAppli;
    @JoinColumn(name = "prestataire_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyPrestataire prestataireId;
    @JoinColumn(name = "filiale_agence_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyFilialeAgence filialeAgenceId;
    @JoinColumn(name = "profil_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyProfil profilId;
    @JoinColumn(name = "utilisateur_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Utilisateur utilisateurId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeId")
    private List<Dbx45tyLignePrestationAudit> dbx45tyLignePrestationAuditList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employeId")
    private List<Dbx45tyConsultationAudit> dbx45tyConsultationAuditList;
    @OneToMany(mappedBy = "employeId")
    private List<Dbx45tyVisite> dbx45tyVisiteList;
    @OneToMany(mappedBy = "employeValideRejeteId")
    private List<Dbx45tyConsultation> dbx45tyConsultationList;
    @OneToMany(mappedBy = "employeValideRejeteId")
    private List<Dbx45tyLignePrestation> dbx45tyLignePrestationList;

    public Dbx45tyEmploye() {
    }

    public Dbx45tyEmploye(Integer id) {
        this.id = id;
    }

    public Dbx45tyEmploye(Integer id, String connexionAppli) {
        this.id = id;
        this.connexionAppli = connexionAppli;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConnexionAppli() {
        return connexionAppli;
    }

    public void setConnexionAppli(String connexionAppli) {
        this.connexionAppli = connexionAppli;
    }

    public Dbx45tyPrestataire getPrestataireId() {
        return prestataireId;
    }

    public void setPrestataireId(Dbx45tyPrestataire prestataireId) {
        this.prestataireId = prestataireId;
    }

    public Dbx45tyFilialeAgence getFilialeAgenceId() {
        return filialeAgenceId;
    }

    public void setFilialeAgenceId(Dbx45tyFilialeAgence filialeAgenceId) {
        this.filialeAgenceId = filialeAgenceId;
    }

    public Dbx45tyProfil getProfilId() {
        return profilId;
    }

    public void setProfilId(Dbx45tyProfil profilId) {
        this.profilId = profilId;
    }

    public Utilisateur getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Utilisateur utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public List<Dbx45tyLignePrestationAudit> getDbx45tyLignePrestationAuditList() {
        return dbx45tyLignePrestationAuditList;
    }

    public void setDbx45tyLignePrestationAuditList(List<Dbx45tyLignePrestationAudit> dbx45tyLignePrestationAuditList) {
        this.dbx45tyLignePrestationAuditList = dbx45tyLignePrestationAuditList;
    }

    public List<Dbx45tyConsultationAudit> getDbx45tyConsultationAuditList() {
        return dbx45tyConsultationAuditList;
    }

    public void setDbx45tyConsultationAuditList(List<Dbx45tyConsultationAudit> dbx45tyConsultationAuditList) {
        this.dbx45tyConsultationAuditList = dbx45tyConsultationAuditList;
    }

    public List<Dbx45tyVisite> getDbx45tyVisiteList() {
        return dbx45tyVisiteList;
    }

    public void setDbx45tyVisiteList(List<Dbx45tyVisite> dbx45tyVisiteList) {
        this.dbx45tyVisiteList = dbx45tyVisiteList;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dbx45tyEmploye)) {
            return false;
        }
        Dbx45tyEmploye other = (Dbx45tyEmploye) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyEmploye[ id=" + id + " ]";
    }
    
}
