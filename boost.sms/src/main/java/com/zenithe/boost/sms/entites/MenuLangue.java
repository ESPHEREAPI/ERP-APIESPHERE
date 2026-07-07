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

import com.zenithe.boost.sms.entites.Langue;
import com.zenithe.boost.sms.entites.Menu;
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
@Table(name="menu_langue")
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="MenuLangue.findAll", query="SELECT m FROM MenuLangue m"), @NamedQuery(name="MenuLangue.findById", query="SELECT m FROM MenuLangue m WHERE m.id = :id"), @NamedQuery(name="MenuLangue.findByUrl", query="SELECT m FROM MenuLangue m WHERE m.url = :url"), @NamedQuery(name="MenuLangue.findByNom", query="SELECT m FROM MenuLangue m WHERE m.nom = :nom"), @NamedQuery(name="MenuLangue.findByDescCourte", query="SELECT m FROM MenuLangue m WHERE m.descCourte = :descCourte")})
public class MenuLangue
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="id")
    private Integer id;
    @Column(name="url")
    private String url;
    @Basic(optional=false)
    @Column(name="nom")
    private String nom;
    @Column(name="desc_courte")
    private String descCourte;
    @JoinColumn(name="langue_id", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Langue langueId;
    @JoinColumn(name="menu_id", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Menu menuId;

    public MenuLangue() {
    }

    public MenuLangue(Integer id) {
        this.id = id;
    }

    public MenuLangue(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescCourte() {
        return this.descCourte;
    }

    public void setDescCourte(String descCourte) {
        this.descCourte = descCourte;
    }

    public Langue getLangueId() {
        return this.langueId;
    }

    public void setLangueId(Langue langueId) {
        this.langueId = langueId;
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
        if (!(object instanceof MenuLangue)) {
            return false;
        }
        MenuLangue other = (MenuLangue)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.MenuLangue[ id=" + this.id + " ]";
    }
}
