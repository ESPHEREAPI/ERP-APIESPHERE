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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_langue")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyLangue.findAll", query = "SELECT d FROM Dbx45tyLangue d"),
    @NamedQuery(name = "Dbx45tyLangue.findById", query = "SELECT d FROM Dbx45tyLangue d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyLangue.findByNom", query = "SELECT d FROM Dbx45tyLangue d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyLangue.findByCodeIso", query = "SELECT d FROM Dbx45tyLangue d WHERE d.codeIso = :codeIso"),
    @NamedQuery(name = "Dbx45tyLangue.findByCodeFin", query = "SELECT d FROM Dbx45tyLangue d WHERE d.codeFin = :codeFin"),
    @NamedQuery(name = "Dbx45tyLangue.findByCode", query = "SELECT d FROM Dbx45tyLangue d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyLangue.findByFormatDate", query = "SELECT d FROM Dbx45tyLangue d WHERE d.formatDate = :formatDate"),
    @NamedQuery(name = "Dbx45tyLangue.findByStatut", query = "SELECT d FROM Dbx45tyLangue d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyLangue.findBySupprime", query = "SELECT d FROM Dbx45tyLangue d WHERE d.supprime = :supprime")})
public class Dbx45tyLangue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Basic(optional = false)
    @Column(name = "code_iso")
    private String codeIso;
    @Basic(optional = false)
    @Column(name = "code_fin")
    private String codeFin;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "format_date")
    private String formatDate;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyMenuLangue> dbx45tyMenuLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueDefaut")
    private List<Utilisateur> UtilisateurList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyProfilLangue> dbx45tyProfilLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyRegionLangue> dbx45tyRegionLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyPaysLangue> dbx45tyPaysLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyLangueHasPays> dbx45tyLangueHasPaysList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "langueId")
    private List<Dbx45tyAgenceLangue> dbx45tyAgenceLangueList;

    public Dbx45tyLangue() {
    }

    public Dbx45tyLangue(Short id) {
        this.id = id;
    }

    public Dbx45tyLangue(Short id, String nom, String codeIso, String codeFin, String code, String formatDate, String statut, String supprime) {
        this.id = id;
        this.nom = nom;
        this.codeIso = codeIso;
        this.codeFin = codeFin;
        this.code = code;
        this.formatDate = formatDate;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodeIso() {
        return codeIso;
    }

    public void setCodeIso(String codeIso) {
        this.codeIso = codeIso;
    }

    public String getCodeFin() {
        return codeFin;
    }

    public void setCodeFin(String codeFin) {
        this.codeFin = codeFin;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFormatDate() {
        return formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
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

    public List<Dbx45tyMenuLangue> getDbx45tyMenuLangueList() {
        return dbx45tyMenuLangueList;
    }

    public void setDbx45tyMenuLangueList(List<Dbx45tyMenuLangue> dbx45tyMenuLangueList) {
        this.dbx45tyMenuLangueList = dbx45tyMenuLangueList;
    }

    public List<Utilisateur> getUtilisateurList() {
        return UtilisateurList;
    }

    public void setUtilisateurList(List<Utilisateur> utilisateurList) {
        this.UtilisateurList = utilisateurList;
    }

    public List<Dbx45tyProfilLangue> getDbx45tyProfilLangueList() {
        return dbx45tyProfilLangueList;
    }

    public void setDbx45tyProfilLangueList(List<Dbx45tyProfilLangue> dbx45tyProfilLangueList) {
        this.dbx45tyProfilLangueList = dbx45tyProfilLangueList;
    }

    public List<Dbx45tyRegionLangue> getDbx45tyRegionLangueList() {
        return dbx45tyRegionLangueList;
    }

    public void setDbx45tyRegionLangueList(List<Dbx45tyRegionLangue> dbx45tyRegionLangueList) {
        this.dbx45tyRegionLangueList = dbx45tyRegionLangueList;
    }

    public List<Dbx45tyPaysLangue> getDbx45tyPaysLangueList() {
        return dbx45tyPaysLangueList;
    }

    public void setDbx45tyPaysLangueList(List<Dbx45tyPaysLangue> dbx45tyPaysLangueList) {
        this.dbx45tyPaysLangueList = dbx45tyPaysLangueList;
    }

    public List<Dbx45tyLangueHasPays> getDbx45tyLangueHasPaysList() {
        return dbx45tyLangueHasPaysList;
    }

    public void setDbx45tyLangueHasPaysList(List<Dbx45tyLangueHasPays> dbx45tyLangueHasPaysList) {
        this.dbx45tyLangueHasPaysList = dbx45tyLangueHasPaysList;
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
        if (!(object instanceof Dbx45tyLangue)) {
            return false;
        }
        Dbx45tyLangue other = (Dbx45tyLangue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyLangue[ id=" + id + " ]";
    }
    
}
