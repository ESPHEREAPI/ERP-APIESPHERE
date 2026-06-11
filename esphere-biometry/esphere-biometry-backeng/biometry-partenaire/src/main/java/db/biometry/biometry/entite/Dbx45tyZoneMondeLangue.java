/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_zone_monde_langue")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyZoneMondeLangue.findAll", query = "SELECT d FROM Dbx45tyZoneMondeLangue d"),
    @NamedQuery(name = "Dbx45tyZoneMondeLangue.findById", query = "SELECT d FROM Dbx45tyZoneMondeLangue d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyZoneMondeLangue.findByLangueId", query = "SELECT d FROM Dbx45tyZoneMondeLangue d WHERE d.langueId = :langueId"),
    @NamedQuery(name = "Dbx45tyZoneMondeLangue.findByZoneMondeId", query = "SELECT d FROM Dbx45tyZoneMondeLangue d WHERE d.zoneMondeId = :zoneMondeId"),
    @NamedQuery(name = "Dbx45tyZoneMondeLangue.findByNom", query = "SELECT d FROM Dbx45tyZoneMondeLangue d WHERE d.nom = :nom")})
public class Dbx45tyZoneMondeLangue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "langue_id")
    private short langueId;
    @Basic(optional = false)
    @Column(name = "zone_monde_id")
    private short zoneMondeId;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;

    public Dbx45tyZoneMondeLangue() {
    }

    public Dbx45tyZoneMondeLangue(Integer id) {
        this.id = id;
    }

    public Dbx45tyZoneMondeLangue(Integer id, short langueId, short zoneMondeId, String nom) {
        this.id = id;
        this.langueId = langueId;
        this.zoneMondeId = zoneMondeId;
        this.nom = nom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getLangueId() {
        return langueId;
    }

    public void setLangueId(short langueId) {
        this.langueId = langueId;
    }

    public short getZoneMondeId() {
        return zoneMondeId;
    }

    public void setZoneMondeId(short zoneMondeId) {
        this.zoneMondeId = zoneMondeId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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
        if (!(object instanceof Dbx45tyZoneMondeLangue)) {
            return false;
        }
        Dbx45tyZoneMondeLangue other = (Dbx45tyZoneMondeLangue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyZoneMondeLangue[ id=" + id + " ]";
    }
    
}
