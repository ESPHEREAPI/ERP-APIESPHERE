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

import com.zenithe.boost.sms.entites.MenuLangue;
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
@Table(name="langue")
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="Langue.findAll", query="SELECT l FROM Langue l"), @NamedQuery(name="Langue.findById", query="SELECT l FROM Langue l WHERE l.id = :id"), @NamedQuery(name="Langue.findByNom", query="SELECT l FROM Langue l WHERE l.nom = :nom"), @NamedQuery(name="Langue.findByCodeIso", query="SELECT l FROM Langue l WHERE l.codeIso = :codeIso"), @NamedQuery(name="Langue.findByCode", query="SELECT l FROM Langue l WHERE l.code = :code"), @NamedQuery(name="Langue.findByFormatDate", query="SELECT l FROM Langue l WHERE l.formatDate = :formatDate"), @NamedQuery(name="Langue.findByStatut", query="SELECT l FROM Langue l WHERE l.statut = :statut"), @NamedQuery(name="Langue.findBySupprime", query="SELECT l FROM Langue l WHERE l.supprime = :supprime")})
public class Langue
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="id")
    private Short id;
    @Basic(optional=false)
    @Column(name="nom")
    private String nom;
    @Basic(optional=false)
    @Column(name="code_iso")
    private String codeIso;
    @Basic(optional=false)
    @Column(name="code")
    private String code;
    @Basic(optional=false)
    @Column(name="format_date")
    private String formatDate;
    @Basic(optional=false)
    @Column(name="statut")
    private String statut;
    @Basic(optional=false)
    @Column(name="supprime")
    private String supprime;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="langueId")
    private List<ProfilLangue> profilLangueList;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="langueDefaut")
    private List<Utilisateur> utilisateurList;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="langueId")
    private List<MenuLangue> menuLangueList;

    public Langue() {
    }

    public Langue(Short id) {
        this.id = id;
    }

    public Langue(Short id, String nom, String codeIso, String code, String formatDate, String statut, String supprime) {
        this.id = id;
        this.nom = nom;
        this.codeIso = codeIso;
        this.code = code;
        this.formatDate = formatDate;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Short getId() {
        return this.id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCodeIso() {
        return this.codeIso;
    }

    public void setCodeIso(String codeIso) {
        this.codeIso = codeIso;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFormatDate() {
        return this.formatDate;
    }

    public void setFormatDate(String formatDate) {
        this.formatDate = formatDate;
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
    public List<MenuLangue> getMenuLangueList() {
        return this.menuLangueList;
    }

    public void setMenuLangueList(List<MenuLangue> menuLangueList) {
        this.menuLangueList = menuLangueList;
    }

    public int hashCode() {
        int hash = 0;
        return hash += this.id != null ? this.id.hashCode() : 0;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Langue)) {
            return false;
        }
        Langue other = (Langue)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.Langue[ id=" + this.id + " ]";
    }
}
