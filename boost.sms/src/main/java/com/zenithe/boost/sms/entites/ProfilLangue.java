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
@Table(name="profil_langue")
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="ProfilLangue.findAll", query="SELECT p FROM ProfilLangue p"), @NamedQuery(name="ProfilLangue.findById", query="SELECT p FROM ProfilLangue p WHERE p.id = :id"), @NamedQuery(name="ProfilLangue.findByNom", query="SELECT p FROM ProfilLangue p WHERE p.nom = :nom")})
public class ProfilLangue
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="id")
    private Integer id;
    @Basic(optional=false)
    @Column(name="nom")
    private String nom;
    @JoinColumn(name="langue_id", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Langue langueId;
    @JoinColumn(name="profil_id", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Profil profilId;

    public ProfilLangue() {
    }

    public ProfilLangue(Integer id) {
        this.id = id;
    }

    public ProfilLangue(Integer id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Langue getLangueId() {
        return this.langueId;
    }

    public void setLangueId(Langue langueId) {
        this.langueId = langueId;
    }

    public Profil getProfilId() {
        return this.profilId;
    }

    public void setProfilId(Profil profilId) {
        this.profilId = profilId;
    }

    public int hashCode() {
        int hash = 0;
        return hash += this.id != null ? this.id.hashCode() : 0;
    }

    public boolean equals(Object object) {
        if (!(object instanceof ProfilLangue)) {
            return false;
        }
        ProfilLangue other = (ProfilLangue)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.ProfilLangue[ id=" + this.id + " ]";
    }
}
