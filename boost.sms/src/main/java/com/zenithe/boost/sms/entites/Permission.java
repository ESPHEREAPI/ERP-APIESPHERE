/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.persistence.Basic
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.JoinColumn
 *  javax.persistence.ManyToOne
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.Table
 *  javax.xml.bind.annotation.XmlRootElement
 */
package com.zenithe.boost.sms.entites;

import com.zenithe.boost.sms.entites.Menu;
import com.zenithe.boost.sms.entites.Profil;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="permission")
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="Permission.findAll", query="SELECT p FROM Permission p"), @NamedQuery(name="Permission.findById", query="SELECT p FROM Permission p WHERE p.id = :id")})
public class Permission
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="id")
    private Integer id;
    @JoinColumn(name="profil_id", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Profil profilId;
    @JoinColumn(name="menu_id", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Menu menuId;

    public Permission() {
    }

    public Permission(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Profil getProfilId() {
        return this.profilId;
    }

    public void setProfilId(Profil profilId) {
        this.profilId = profilId;
    }

    public Menu getMenuId() {
        return this.menuId;
    }

    public void setMenuId(Menu menuId) {
        this.menuId = menuId;
    }

    public int hashCode() {
        int hash = 0;
        return hash += this.id != null ? this.id.hashCode() : 0;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Permission)) {
            return false;
        }
        Permission other = (Permission)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.Permission[ id=" + this.id + " ]";
    }
}
