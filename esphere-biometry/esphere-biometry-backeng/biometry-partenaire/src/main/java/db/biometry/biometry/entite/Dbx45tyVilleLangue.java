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
@Table(name = "dbx45ty_ville_langue")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyVilleLangue.findAll", query = "SELECT d FROM Dbx45tyVilleLangue d"),
    @NamedQuery(name = "Dbx45tyVilleLangue.findById", query = "SELECT d FROM Dbx45tyVilleLangue d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyVilleLangue.findByLangueId", query = "SELECT d FROM Dbx45tyVilleLangue d WHERE d.langueId = :langueId"),
    @NamedQuery(name = "Dbx45tyVilleLangue.findByVilleId", query = "SELECT d FROM Dbx45tyVilleLangue d WHERE d.villeId = :villeId"),
    @NamedQuery(name = "Dbx45tyVilleLangue.findByNom", query = "SELECT d FROM Dbx45tyVilleLangue d WHERE d.nom = :nom")})
public class Dbx45tyVilleLangue implements Serializable {

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
    @Column(name = "ville_id")
    private int villeId;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;

    public Dbx45tyVilleLangue() {
    }

    public Dbx45tyVilleLangue(Integer id) {
        this.id = id;
    }

    public Dbx45tyVilleLangue(Integer id, short langueId, int villeId, String nom) {
        this.id = id;
        this.langueId = langueId;
        this.villeId = villeId;
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

    public int getVilleId() {
        return villeId;
    }

    public void setVilleId(int villeId) {
        this.villeId = villeId;
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
        if (!(object instanceof Dbx45tyVilleLangue)) {
            return false;
        }
        Dbx45tyVilleLangue other = (Dbx45tyVilleLangue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyVilleLangue[ id=" + id + " ]";
    }
    
}
