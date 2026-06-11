/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_ayant_droit")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyAyantDroit.findAll", query = "SELECT d FROM Dbx45tyAyantDroit d"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findByCodeAyantDroit", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.codeAyantDroit = :codeAyantDroit"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findByNom", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findBySexe", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.sexe = :sexe"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findByNaissance", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.naissance = :naissance"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findByLienpare", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.lienpare = :lienpare"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findByTelephone", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.telephone = :telephone"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findByPolice", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.police = :police"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findByEnrole", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.enrole = :enrole"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findByDateEnrole", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.dateEnrole = :dateEnrole"),
    @NamedQuery(name = "Dbx45tyAyantDroit.findByStatut", query = "SELECT d FROM Dbx45tyAyantDroit d WHERE d.statut = :statut")})
public class Dbx45tyAyantDroit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "code_ayant_droit")
    private String codeAyantDroit;
    @Column(name = "nom")
    private String nom;
    @Column(name = "sexe")
    private String sexe;
    @Column(name = "naissance")
    @Temporal(TemporalType.DATE)
    private Date naissance;
    @Basic(optional = false)
    @Column(name = "lienpare")
    private String lienpare;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "police")
    private String police;
    @Basic(optional = false)
    @Column(name = "enrole")
    private String enrole;
    @Column(name = "date_enrole")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnrole;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @JoinColumn(name = "code_adherent", referencedColumnName = "code_adherent")
    @ManyToOne
    private Dbx45tyAdherent codeAdherent;

    public Dbx45tyAyantDroit() {
    }

    public Dbx45tyAyantDroit(String codeAyantDroit) {
        this.codeAyantDroit = codeAyantDroit;
    }

    public Dbx45tyAyantDroit(String codeAyantDroit, String lienpare, String enrole, String statut) {
        this.codeAyantDroit = codeAyantDroit;
        this.lienpare = lienpare;
        this.enrole = enrole;
        this.statut = statut;
    }

    public String getCodeAyantDroit() {
        return codeAyantDroit;
    }

    public void setCodeAyantDroit(String codeAyantDroit) {
        this.codeAyantDroit = codeAyantDroit;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Date getNaissance() {
        return naissance;
    }

    public void setNaissance(Date naissance) {
        this.naissance = naissance;
    }

    public String getLienpare() {
        return lienpare;
    }

    public void setLienpare(String lienpare) {
        this.lienpare = lienpare;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public String getEnrole() {
        return enrole;
    }

    public void setEnrole(String enrole) {
        this.enrole = enrole;
    }

    public Date getDateEnrole() {
        return dateEnrole;
    }

    public void setDateEnrole(Date dateEnrole) {
        this.dateEnrole = dateEnrole;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Dbx45tyAdherent getCodeAdherent() {
        return codeAdherent;
    }

    public void setCodeAdherent(Dbx45tyAdherent codeAdherent) {
        this.codeAdherent = codeAdherent;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeAyantDroit != null ? codeAyantDroit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dbx45tyAyantDroit)) {
            return false;
        }
        Dbx45tyAyantDroit other = (Dbx45tyAyantDroit) object;
        if ((this.codeAyantDroit == null && other.codeAyantDroit != null) || (this.codeAyantDroit != null && !this.codeAyantDroit.equals(other.codeAyantDroit))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyAyantDroit[ codeAyantDroit=" + codeAyantDroit + " ]";
    }
    
}
