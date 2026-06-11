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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_consultation")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyConsultation.findAll", query = "SELECT d FROM Dbx45tyConsultation d"),
    @NamedQuery(name = "Dbx45tyConsultation.findById", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyConsultation.findByTaux", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.taux = :taux"),
    @NamedQuery(name = "Dbx45tyConsultation.findByNatureConsultation", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.natureConsultation = :natureConsultation"),
    @NamedQuery(name = "Dbx45tyConsultation.findByNatureAffection", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.natureAffection = :natureAffection"),
    @NamedQuery(name = "Dbx45tyConsultation.findByMontant", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.montant = :montant"),
    @NamedQuery(name = "Dbx45tyConsultation.findByMontantModif", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.montantModif = :montantModif"),
    @NamedQuery(name = "Dbx45tyConsultation.findByDate", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.date = :date"),
    @NamedQuery(name = "Dbx45tyConsultation.findByDateValideRejete", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.dateValideRejete = :dateValideRejete"),
    @NamedQuery(name = "Dbx45tyConsultation.findByObservations", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.observations = :observations"),
    @NamedQuery(name = "Dbx45tyConsultation.findByEtatConsultation", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.etatConsultation = :etatConsultation"),
    @NamedQuery(name = "Dbx45tyConsultation.findBySupprime", query = "SELECT d FROM Dbx45tyConsultation d WHERE d.supprime = :supprime")})
public class Dbx45tyConsultation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Basic(optional = false)
    @Column(name = "nature_consultation")
    private String natureConsultation;
    @Column(name = "nature_affection")
    private String natureAffection;
    @Basic(optional = false)
    @Column(name = "montant")
    private double montant;
    @Column(name = "montant_modif")
    private Double montantModif;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "date_valide_rejete")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValideRejete;
    @Column(name = "observations")
    private String observations;
    @Basic(optional = false)
    @Column(name = "etat_consultation")
    private String etatConsultation;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consultationId")
    private List<Dbx45tyConsultationAudit> dbx45tyConsultationAuditList;
    @JoinColumn(name = "visite_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    private Dbx45tyVisite visiteId;
    @JoinColumn(name = "employe_valide_rejete_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyEmploye employeValideRejeteId;
    @JoinColumn(name = "type_consultation", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyTypePrestation typeConsultation;

    public Dbx45tyConsultation() {
    }

    public Dbx45tyConsultation(Integer id) {
        this.id = id;
    }

    public Dbx45tyConsultation(Integer id, String natureConsultation, double montant, Date date, String etatConsultation, String supprime) {
        this.id = id;
        this.natureConsultation = natureConsultation;
        this.montant = montant;
        this.date = date;
        this.etatConsultation = etatConsultation;
        this.supprime = supprime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public String getNatureConsultation() {
        return natureConsultation;
    }

    public void setNatureConsultation(String natureConsultation) {
        this.natureConsultation = natureConsultation;
    }

    public String getNatureAffection() {
        return natureAffection;
    }

    public void setNatureAffection(String natureAffection) {
        this.natureAffection = natureAffection;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Double getMontantModif() {
        return montantModif;
    }

    public void setMontantModif(Double montantModif) {
        this.montantModif = montantModif;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateValideRejete() {
        return dateValideRejete;
    }

    public void setDateValideRejete(Date dateValideRejete) {
        this.dateValideRejete = dateValideRejete;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getEtatConsultation() {
        return etatConsultation;
    }

    public void setEtatConsultation(String etatConsultation) {
        this.etatConsultation = etatConsultation;
    }

    public String getSupprime() {
        return supprime;
    }

    public void setSupprime(String supprime) {
        this.supprime = supprime;
    }

    public List<Dbx45tyConsultationAudit> getDbx45tyConsultationAuditList() {
        return dbx45tyConsultationAuditList;
    }

    public void setDbx45tyConsultationAuditList(List<Dbx45tyConsultationAudit> dbx45tyConsultationAuditList) {
        this.dbx45tyConsultationAuditList = dbx45tyConsultationAuditList;
    }

    public Dbx45tyVisite getVisiteId() {
        return visiteId;
    }

    public void setVisiteId(Dbx45tyVisite visiteId) {
        this.visiteId = visiteId;
    }

    public Dbx45tyEmploye getEmployeValideRejeteId() {
        return employeValideRejeteId;
    }

    public void setEmployeValideRejeteId(Dbx45tyEmploye employeValideRejeteId) {
        this.employeValideRejeteId = employeValideRejeteId;
    }

    public Dbx45tyTypePrestation getTypeConsultation() {
        return typeConsultation;
    }

    public void setTypeConsultation(Dbx45tyTypePrestation typeConsultation) {
        this.typeConsultation = typeConsultation;
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
        if (!(object instanceof Dbx45tyConsultation)) {
            return false;
        }
        Dbx45tyConsultation other = (Dbx45tyConsultation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

//    @Override
//    public String toString() {
//        return "db.biometry.biometry.entite.Dbx45tyConsultation[ id=" + id + " ]";
//    }
    
}
