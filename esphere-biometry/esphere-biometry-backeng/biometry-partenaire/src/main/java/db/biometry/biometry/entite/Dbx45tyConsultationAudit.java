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
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "dbx45ty_consultation_audit")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyConsultationAudit.findAll", query = "SELECT d FROM Dbx45tyConsultationAudit d"),
    @NamedQuery(name = "Dbx45tyConsultationAudit.findById", query = "SELECT d FROM Dbx45tyConsultationAudit d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyConsultationAudit.findByEtatConsultation", query = "SELECT d FROM Dbx45tyConsultationAudit d WHERE d.etatConsultation = :etatConsultation"),
    @NamedQuery(name = "Dbx45tyConsultationAudit.findByDate", query = "SELECT d FROM Dbx45tyConsultationAudit d WHERE d.date = :date")})
public class Dbx45tyConsultationAudit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "etat_consultation")
    private String etatConsultation;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "consultation_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyConsultation consultationId;
    @JoinColumn(name = "employe_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyEmploye employeId;

    public Dbx45tyConsultationAudit() {
    }

    public Dbx45tyConsultationAudit(Integer id) {
        this.id = id;
    }

    public Dbx45tyConsultationAudit(Integer id, String etatConsultation, Date date) {
        this.id = id;
        this.etatConsultation = etatConsultation;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEtatConsultation() {
        return etatConsultation;
    }

    public void setEtatConsultation(String etatConsultation) {
        this.etatConsultation = etatConsultation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Dbx45tyConsultation getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(Dbx45tyConsultation consultationId) {
        this.consultationId = consultationId;
    }

    public Dbx45tyEmploye getEmployeId() {
        return employeId;
    }

    public void setEmployeId(Dbx45tyEmploye employeId) {
        this.employeId = employeId;
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
        if (!(object instanceof Dbx45tyConsultationAudit)) {
            return false;
        }
        Dbx45tyConsultationAudit other = (Dbx45tyConsultationAudit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyConsultationAudit[ id=" + id + " ]";
    }
    
}
