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
@Table(name = "dbx45ty_permission")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyPermission.findAll", query = "SELECT d FROM Dbx45tyPermission d"),
    @NamedQuery(name = "Dbx45tyPermission.findById", query = "SELECT d FROM Dbx45tyPermission d WHERE d.id = :id")})
public class Dbx45tyPermission implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "profil_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyProfil profilId;
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyMenu menuId;

    public Dbx45tyPermission() {
    }

    public Dbx45tyPermission(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dbx45tyProfil getProfilId() {
        return profilId;
    }

    public void setProfilId(Dbx45tyProfil profilId) {
        this.profilId = profilId;
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
        if (!(object instanceof Dbx45tyPermission)) {
            return false;
        }
        Dbx45tyPermission other = (Dbx45tyPermission) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyPermission[ id=" + id + " ]";
    }
    
}
