/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.persistence.Basic
 *  javax.persistence.CascadeType
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.OneToMany
 *  javax.persistence.Table
 *  javax.xml.bind.annotation.XmlRootElement
 *  javax.xml.bind.annotation.XmlTransient
 */
package com.zenithe.boost.sms.entites;

import com.zenithe.boost.sms.entites.Permission;
import com.zenithe.boost.sms.entites.ProfilLangue;
import com.zenithe.boost.sms.entites.Utilisateur;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="profil")
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="Profil.findAll", query="SELECT p FROM Profil p"), @NamedQuery(name="Profil.findById", query="SELECT p FROM Profil p WHERE p.id = :id"), @NamedQuery(name="Profil.findByCode", query="SELECT p FROM Profil p WHERE p.code = :code"), @NamedQuery(name="Profil.findByStatut", query="SELECT p FROM Profil p WHERE p.statut = :statut"), @NamedQuery(name="Profil.findBySupprime", query="SELECT p FROM Profil p WHERE p.supprime = :supprime")})
public class Profil
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="id")
    private Integer id;
    @Basic(optional=false)
    @Column(name="code")
    private String code;
    @Basic(optional=false)
    @Column(name="statut")
    private String statut;
    @Basic(optional=false)
    @Column(name="supprime")
    private String supprime;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="profilId")
    private List<ProfilLangue> profilLangueList;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="profilId")
    private List<Utilisateur> utilisateurList;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="profilId")
    private List<Permission> permissionList;

    public Profil() {
    }

    public Profil(Integer id) {
        this.id = id;
    }

    public Profil(Integer id, String code, String statut, String supprime) {
        this.id = id;
        this.code = code;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatut() {
        return this.statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getSupprime() {
        return this.supprime;
    }

    public void setSupprime(String supprime) {
        this.supprime = supprime;
    }

    @XmlTransient
    public List<ProfilLangue> getProfilLangueList() {
        return this.profilLangueList;
    }

    public void setProfilLangueList(List<ProfilLangue> profilLangueList) {
        this.profilLangueList = profilLangueList;
    }

    @XmlTransient
    public List<Utilisateur> getUtilisateurList() {
        return this.utilisateurList;
    }

    public void setUtilisateurList(List<Utilisateur> utilisateurList) {
        this.utilisateurList = utilisateurList;
    }

    @XmlTransient
    public List<Permission> getPermissionList() {
        return this.permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public int hashCode() {
        int hash = 0;
        return hash += this.id != null ? this.id.hashCode() : 0;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Profil)) {
            return false;
        }
        Profil other = (Profil)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.Profil[ id=" + this.id + " ]";
    }
}
