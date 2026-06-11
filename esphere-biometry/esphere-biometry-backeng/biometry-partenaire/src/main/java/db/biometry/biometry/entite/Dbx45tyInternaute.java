/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_internaute")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyInternaute.findAll", query = "SELECT d FROM Dbx45tyInternaute d"),
    @NamedQuery(name = "Dbx45tyInternaute.findById", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyInternaute.findByTokenActivation", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.tokenActivation = :tokenActivation"),
    @NamedQuery(name = "Dbx45tyInternaute.findByDateTokenActivation", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.dateTokenActivation = :dateTokenActivation"),
    @NamedQuery(name = "Dbx45tyInternaute.findByTokenMotPasseOublie", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.tokenMotPasseOublie = :tokenMotPasseOublie"),
    @NamedQuery(name = "Dbx45tyInternaute.findByLienActiverCompte", query = "SELECT d FROM Dbx45tyInternaute d WHERE d.lienActiverCompte = :lienActiverCompte")})
public class Dbx45tyInternaute implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "token_activation")
    private String tokenActivation;
    @Column(name = "date_token_activation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTokenActivation;
    @Column(name = "token_mot_passe_oublie")
    private String tokenMotPasseOublie;
    @Column(name = "lien_activer_compte")
    private String lienActiverCompte;
    @JoinColumn(name = "utilisateur_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Utilisateur utilisateurId;

    public Dbx45tyInternaute() {
    }

    public Dbx45tyInternaute(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTokenActivation() {
        return tokenActivation;
    }

    public void setTokenActivation(String tokenActivation) {
        this.tokenActivation = tokenActivation;
    }

    public Date getDateTokenActivation() {
        return dateTokenActivation;
    }

    public void setDateTokenActivation(Date dateTokenActivation) {
        this.dateTokenActivation = dateTokenActivation;
    }

    public String getTokenMotPasseOublie() {
        return tokenMotPasseOublie;
    }

    public void setTokenMotPasseOublie(String tokenMotPasseOublie) {
        this.tokenMotPasseOublie = tokenMotPasseOublie;
    }

    public String getLienActiverCompte() {
        return lienActiverCompte;
    }

    public void setLienActiverCompte(String lienActiverCompte) {
        this.lienActiverCompte = lienActiverCompte;
    }

    public Utilisateur getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Utilisateur utilisateurId) {
        this.utilisateurId = utilisateurId;
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
        if (!(object instanceof Dbx45tyInternaute)) {
            return false;
        }
        Dbx45tyInternaute other = (Dbx45tyInternaute) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyInternaute[ id=" + id + " ]";
    }
    
}
