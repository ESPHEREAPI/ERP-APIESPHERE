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
@Table(name = "dbx45ty_ligne_prestation_audit")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyLignePrestationAudit.findAll", query = "SELECT d FROM Dbx45tyLignePrestationAudit d"),
    @NamedQuery(name = "Dbx45tyLignePrestationAudit.findById", query = "SELECT d FROM Dbx45tyLignePrestationAudit d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyLignePrestationAudit.findByEtatLignePrestation", query = "SELECT d FROM Dbx45tyLignePrestationAudit d WHERE d.etatLignePrestation = :etatLignePrestation"),
    @NamedQuery(name = "Dbx45tyLignePrestationAudit.findByDate", query = "SELECT d FROM Dbx45tyLignePrestationAudit d WHERE d.date = :date")})
public class Dbx45tyLignePrestationAudit implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "etat_ligne_prestation")
    private String etatLignePrestation;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @JoinColumn(name = "employe_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyEmploye employeId;
    @JoinColumn(name = "ligne_prestation_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyLignePrestation lignePrestationId;

    public Dbx45tyLignePrestationAudit() {
    }

    public Dbx45tyLignePrestationAudit(Integer id) {
        this.id = id;
    }

    public Dbx45tyLignePrestationAudit(Integer id, String etatLignePrestation, Date date) {
        this.id = id;
        this.etatLignePrestation = etatLignePrestation;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEtatLignePrestation() {
        return etatLignePrestation;
    }

    public void setEtatLignePrestation(String etatLignePrestation) {
        this.etatLignePrestation = etatLignePrestation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Dbx45tyEmploye getEmployeId() {
        return employeId;
    }

    public void setEmployeId(Dbx45tyEmploye employeId) {
        this.employeId = employeId;
    }

    public Dbx45tyLignePrestation getLignePrestationId() {
        return lignePrestationId;
    }

    public void setLignePrestationId(Dbx45tyLignePrestation lignePrestationId) {
        this.lignePrestationId = lignePrestationId;
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
        if (!(object instanceof Dbx45tyLignePrestationAudit)) {
            return false;
        }
        Dbx45tyLignePrestationAudit other = (Dbx45tyLignePrestationAudit) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyLignePrestationAudit[ id=" + id + " ]";
    }
    
}
