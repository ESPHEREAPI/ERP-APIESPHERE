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

import com.zenithe.boost.sms.entites.Services;
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
@Table(name="attribut")
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="Attribut.findAll", query="SELECT a FROM Attribut a"), @NamedQuery(name="Attribut.findById", query="SELECT a FROM Attribut a WHERE a.id = :id"), @NamedQuery(name="Attribut.findByNom", query="SELECT a FROM Attribut a WHERE a.nom = :nom"), @NamedQuery(name="Attribut.findByType", query="SELECT a FROM Attribut a WHERE a.type = :type"), @NamedQuery(name="Attribut.findByObligatoire", query="SELECT a FROM Attribut a WHERE a.obligatoire = :obligatoire"), @NamedQuery(name="Attribut.findByStatut", query="SELECT a FROM Attribut a WHERE a.statut = :statut")})
public class Attribut
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
    @Basic(optional=false)
    @Column(name="type")
    private String type;
    @Basic(optional=false)
    @Column(name="obligatoire")
    private String obligatoire;
    @Basic(optional=false)
    @Column(name="statut")
    private String statut;
    @JoinColumn(name="service_id", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Services serviceId;

    public Attribut() {
    }

    public Attribut(Integer id) {
        this.id = id;
    }

    public Attribut(Integer id, String nom, String type, String obligatoire, String statut) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.obligatoire = obligatoire;
        this.statut = statut;
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

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObligatoire() {
        return this.obligatoire;
    }

    public void setObligatoire(String obligatoire) {
        this.obligatoire = obligatoire;
    }

    public String getStatut() {
        return this.statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Services getServiceId() {
        return this.serviceId;
    }

    public void setServiceId(Services serviceId) {
        this.serviceId = serviceId;
    }

    public int hashCode() {
        int hash = 0;
        return hash += this.id != null ? this.id.hashCode() : 0;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Attribut)) {
            return false;
        }
        Attribut other = (Attribut)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.Attribut[ id=" + this.id + " ]";
    }
}
