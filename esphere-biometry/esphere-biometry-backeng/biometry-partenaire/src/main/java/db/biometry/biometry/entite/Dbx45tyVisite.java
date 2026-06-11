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
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_visite")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyVisite.findAll", query = "SELECT d FROM Dbx45tyVisite d"),
    @NamedQuery(name = "Dbx45tyVisite.findById", query = "SELECT d FROM Dbx45tyVisite d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyVisite.findByCodeAdherent", query = "SELECT d FROM Dbx45tyVisite d WHERE d.codeAdherent = :codeAdherent"),
    @NamedQuery(name = "Dbx45tyVisite.findByCodeAyantDroit", query = "SELECT d FROM Dbx45tyVisite d WHERE d.codeAyantDroit = :codeAyantDroit"),
    @NamedQuery(name = "Dbx45tyVisite.findByPrestataireId", query = "SELECT d FROM Dbx45tyVisite d WHERE d.prestataireId = :prestataireId"),
    @NamedQuery(name = "Dbx45tyVisite.findByCodeCourt", query = "SELECT d FROM Dbx45tyVisite d WHERE d.codeCourt = :codeCourt"),
    @NamedQuery(name = "Dbx45tyVisite.findByTelephone", query = "SELECT d FROM Dbx45tyVisite d WHERE d.telephone = :telephone"),
    @NamedQuery(name = "Dbx45tyVisite.findByDate", query = "SELECT d FROM Dbx45tyVisite d WHERE d.date = :date")})
public class Dbx45tyVisite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private String id;
    @Basic(optional = false)
    //@Column(name = "code_adherent")
   @JoinColumn(name = "code_adherent", referencedColumnName = "code_adherent")
    @OneToOne(optional = false)
    private Dbx45tyAdherent codeAdherent;
    @Column(name = "code_ayant_droit")
    private String codeAyantDroit;
    @Basic(optional = false)
    @Column(name = "prestataire_id")
    private String prestataireId;
    @Basic(optional = false)
    @Column(name = "code_court")
    private String codeCourt;
    @Basic(optional = false)
    @Column(name = "telephone")
    private String telephone;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "visiteId")
    private List<Dbx45tyPrestation> dbx45tyPrestationList;
    @JoinColumn(name = "employe_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyEmploye employeId;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "visiteId")
    private Dbx45tyConsultation dbx45tyConsultation;

    public Dbx45tyVisite() {
    }

    public Dbx45tyVisite(String id) {
        this.id = id;
    }

    public Dbx45tyVisite(String id, Dbx45tyAdherent codeAdherent, String prestataireId, String codeCourt, String telephone, Date date) {
        this.id = id;
        this.codeAdherent = codeAdherent;
        this.prestataireId = prestataireId;
        this.codeCourt = codeCourt;
        this.telephone = telephone;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Dbx45tyAdherent getCodeAdherent() {
        return codeAdherent;
    }

    public void setCodeAdherent(Dbx45tyAdherent codeAdherent) {
        this.codeAdherent = codeAdherent;
    }

    public String getCodeAyantDroit() {
        return codeAyantDroit;
    }

    public void setCodeAyantDroit(String codeAyantDroit) {
        this.codeAyantDroit = codeAyantDroit;
    }

    public String getPrestataireId() {
        return prestataireId;
    }

    public void setPrestataireId(String prestataireId) {
        this.prestataireId = prestataireId;
    }

    public String getCodeCourt() {
        return codeCourt;
    }

    public void setCodeCourt(String codeCourt) {
        this.codeCourt = codeCourt;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Dbx45tyPrestation> getDbx45tyPrestationList() {
        return dbx45tyPrestationList;
    }

    public void setDbx45tyPrestationList(List<Dbx45tyPrestation> dbx45tyPrestationList) {
        this.dbx45tyPrestationList = dbx45tyPrestationList;
    }

    public Dbx45tyEmploye getEmployeId() {
        return employeId;
    }

    public void setEmployeId(Dbx45tyEmploye employeId) {
        this.employeId = employeId;
    }

    public Dbx45tyConsultation getDbx45tyConsultation() {
        return dbx45tyConsultation;
    }

    public void setDbx45tyConsultation(Dbx45tyConsultation dbx45tyConsultation) {
        this.dbx45tyConsultation = dbx45tyConsultation;
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
        if (!(object instanceof Dbx45tyVisite)) {
            return false;
        }
        Dbx45tyVisite other = (Dbx45tyVisite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyVisite[ id=" + id + " ]";
    }
    
}
