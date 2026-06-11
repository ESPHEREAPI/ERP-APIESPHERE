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
@Table(name = "dbx45ty_menu_langue")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyMenuLangue.findAll", query = "SELECT d FROM Dbx45tyMenuLangue d"),
    @NamedQuery(name = "Dbx45tyMenuLangue.findById", query = "SELECT d FROM Dbx45tyMenuLangue d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyMenuLangue.findByUrl", query = "SELECT d FROM Dbx45tyMenuLangue d WHERE d.url = :url"),
    @NamedQuery(name = "Dbx45tyMenuLangue.findByNom", query = "SELECT d FROM Dbx45tyMenuLangue d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyMenuLangue.findByDescCourte", query = "SELECT d FROM Dbx45tyMenuLangue d WHERE d.descCourte = :descCourte")})
public class Dbx45tyMenuLangue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "url")
    private String url;
    @Basic(optional = false)
    @Column(name = "nom")
    private String nom;
    @Column(name = "desc_courte")
    private String descCourte;
    @JoinColumn(name = "langue_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyLangue langueId;
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyMenu menuId;

    public Dbx45tyMenuLangue() {
    }

    public Dbx45tyMenuLangue(Integer id) {
        this.id = id;
    }

    public Dbx45tyMenuLangue(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescCourte() {
        return descCourte;
    }

    public void setDescCourte(String descCourte) {
        this.descCourte = descCourte;
    }

    public Dbx45tyLangue getLangueId() {
        return langueId;
    }

    public void setLangueId(Dbx45tyLangue langueId) {
        this.langueId = langueId;
    }

    public Dbx45tyMenu getMenuId() {
        return menuId;
    }

    public void setMenuId(Dbx45tyMenu menuId) {
        this.menuId = menuId;
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
        if (!(object instanceof Dbx45tyMenuLangue)) {
            return false;
        }
        Dbx45tyMenuLangue other = (Dbx45tyMenuLangue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyMenuLangue[ id=" + id + " ]";
    }
    
}
