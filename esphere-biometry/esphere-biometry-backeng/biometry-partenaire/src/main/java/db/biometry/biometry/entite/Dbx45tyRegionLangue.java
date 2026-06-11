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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_region_langue")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyRegionLangue.findAll", query = "SELECT d FROM Dbx45tyRegionLangue d"),
    @NamedQuery(name = "Dbx45tyRegionLangue.findById", query = "SELECT d FROM Dbx45tyRegionLangue d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyRegionLangue.findByNom", query = "SELECT d FROM Dbx45tyRegionLangue d WHERE d.nom = :nom")})
public class Dbx45tyRegionLangue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @JoinColumn(name = "langue_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyLangue langueId;
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyRegion regionId;

    public Dbx45tyRegionLangue() {
    }

    public Dbx45tyRegionLangue(Integer id) {
        this.id = id;
    }

    public Dbx45tyRegionLangue(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Dbx45tyLangue getLangueId() {
        return langueId;
    }

    public void setLangueId(Dbx45tyLangue langueId) {
        this.langueId = langueId;
    }

    public Dbx45tyRegion getRegionId() {
        return regionId;
    }

    public void setRegionId(Dbx45tyRegion regionId) {
        this.regionId = regionId;
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
        if (!(object instanceof Dbx45tyRegionLangue)) {
            return false;
        }
        Dbx45tyRegionLangue other = (Dbx45tyRegionLangue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyRegionLangue[ id=" + id + " ]";
    }
    
}
