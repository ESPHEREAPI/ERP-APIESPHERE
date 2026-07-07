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
@Table(name="utilisateur")
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="Utilisateur.findAll", query="SELECT u FROM Utilisateur u"), @NamedQuery(name="Utilisateur.findById", query="SELECT u FROM Utilisateur u WHERE u.id = :id"), @NamedQuery(name="Utilisateur.findByNom", query="SELECT u FROM Utilisateur u WHERE u.nom = :nom"), @NamedQuery(name="Utilisateur.findByPrenom", query="SELECT u FROM Utilisateur u WHERE u.prenom = :prenom"), @NamedQuery(name="Utilisateur.findByEmail", query="SELECT u FROM Utilisateur u WHERE u.email = :email"), @NamedQuery(name="Utilisateur.findByLogin", query="SELECT u FROM Utilisateur u WHERE u.login = :login"), @NamedQuery(name="Utilisateur.findByMotPasse", query="SELECT u FROM Utilisateur u WHERE u.motPasse = :motPasse"), @NamedQuery(name="Utilisateur.findByPremierMotPasse", query="SELECT u FROM Utilisateur u WHERE u.premierMotPasse = :premierMotPasse"), @NamedQuery(name="Utilisateur.findByStatut", query="SELECT u FROM Utilisateur u WHERE u.statut = :statut"), @NamedQuery(name="Utilisateur.findBySupprime", query="SELECT u FROM Utilisateur u WHERE u.supprime = :supprime"), @NamedQuery(name="Utilisateur.findByConnexionAppli", query="SELECT u FROM Utilisateur u WHERE u.connexionAppli = :connexionAppli")})
public class Utilisateur
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="id")
    private Integer id;
    @Column(name="nom")
    private String nom;
    @Column(name="prenom")
    private String prenom;
    @Column(name="email")
    private String email;
    @Column(name="login")
    private String login;
    @Column(name="mot_passe")
    private String motPasse;
    @Column(name="premier_mot_passe")
    private String premierMotPasse;
    @Basic(optional=false)
    @Column(name="statut")
    private String statut;
    @Basic(optional=false)
    @Column(name="supprime")
    private String supprime;
    @Basic(optional=false)
    @Column(name="connexion_appli")
    private short connexionAppli;
    @JoinColumn(name="langue_defaut", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Langue langueDefaut;
    @JoinColumn(name="profil_id", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Profil profilId;

    public Utilisateur() {
    }

    public Utilisateur(Integer id) {
        this.id = id;
    }

    public Utilisateur(Integer id, String statut, String supprime, short connexionAppli) {
        this.id = id;
        this.statut = statut;
        this.supprime = supprime;
        this.connexionAppli = connexionAppli;
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

    public String getPrenom() {
        return this.prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotPasse() {
        return this.motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    public String getPremierMotPasse() {
        return this.premierMotPasse;
    }

    public void setPremierMotPasse(String premierMotPasse) {
        this.premierMotPasse = premierMotPasse;
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

    public short getConnexionAppli() {
        return this.connexionAppli;
    }

    public void setConnexionAppli(short connexionAppli) {
        this.connexionAppli = connexionAppli;
    }

    public Langue getLangueDefaut() {
        return this.langueDefaut;
    }

    public void setLangueDefaut(Langue langueDefaut) {
        this.langueDefaut = langueDefaut;
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
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.Utilisateur[ id=" + this.id + " ]";
    }
}
