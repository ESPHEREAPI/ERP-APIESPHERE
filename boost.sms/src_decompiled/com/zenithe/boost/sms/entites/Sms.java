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
 *  javax.persistence.Lob
 *  javax.persistence.ManyToOne
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.Table
 *  javax.persistence.Temporal
 *  javax.persistence.TemporalType
 *  javax.xml.bind.annotation.XmlRootElement
 */
package com.zenithe.boost.sms.entites;

import com.zenithe.boost.sms.entites.Services;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name="sms")
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="Sms.findAll", query="SELECT s FROM Sms s"), @NamedQuery(name="Sms.findById", query="SELECT s FROM Sms s WHERE s.id = :id"), @NamedQuery(name="Sms.findByCodeinte", query="SELECT s FROM Sms s WHERE s.codeinte = :codeinte"), @NamedQuery(name="Sms.findByNumepoli", query="SELECT s FROM Sms s WHERE s.numepoli = :numepoli"), @NamedQuery(name="Sms.findByNumeaven", query="SELECT s FROM Sms s WHERE s.numeaven = :numeaven"), @NamedQuery(name="Sms.findByNumesini", query="SELECT s FROM Sms s WHERE s.numesini = :numesini"), @NamedQuery(name="Sms.findByExersini", query="SELECT s FROM Sms s WHERE s.exersini = :exersini"), @NamedQuery(name="Sms.findByNomassure", query="SELECT s FROM Sms s WHERE s.nomassure = :nomassure"), @NamedQuery(name="Sms.findByTeleassu", query="SELECT s FROM Sms s WHERE s.teleassu = :teleassu"), @NamedQuery(name="Sms.findByNomExpediteur", query="SELECT s FROM Sms s WHERE s.nomExpediteur = :nomExpediteur"), @NamedQuery(name="Sms.findByReponseEnvoiSms", query="SELECT s FROM Sms s WHERE s.reponseEnvoiSms = :reponseEnvoiSms"), @NamedQuery(name="Sms.findByDateEnvoi", query="SELECT s FROM Sms s WHERE s.dateEnvoi = :dateEnvoi"), @NamedQuery(name="Sms.findByDateeche", query="SELECT s FROM Sms s WHERE s.dateeche = :dateeche"), @NamedQuery(name="Sms.findByStatut", query="SELECT s FROM Sms s WHERE s.statut = :statut"), @NamedQuery(name="Sms.findByDatecreation", query="SELECT s FROM Sms s WHERE s.datecreation = :datecreation")})
public class Sms
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="id")
    private Integer id;
    @Basic(optional=false)
    @Lob
    @Column(name="contenu")
    private String contenu;
    @Basic(optional=false)
    @Column(name="codeinte")
    private String codeinte;
    @Basic(optional=false)
    @Column(name="numepoli")
    private String numepoli;
    @Basic(optional=false)
    @Column(name="numeaven")
    private String numeaven;
    private String numeimma;
    @Column(name="numesini")
    private Integer numesini;
    @Column(name="exersini")
    private Integer exersini;
    @Basic(optional=false)
    @Column(name="nomassure")
    private String nomassure;
    @Basic(optional=false)
    @Column(name="teleassu")
    private String teleassu;
    @Column(name="nom_expediteur")
    private String nomExpediteur;
    @Column(name="reponse_envoi_sms")
    private String reponseEnvoiSms;
    @Column(name="date_envoi")
    @Temporal(value=TemporalType.TIMESTAMP)
    private Date dateEnvoi;
    @Basic(optional=false)
    @Column(name="dateeche")
    private String dateeche;
    @Basic(optional=false)
    @Column(name="statut")
    private String statut;
    @Column(name="datecreation")
    @Temporal(value=TemporalType.TIMESTAMP)
    private Date datecreation;
    @JoinColumn(name="service", referencedColumnName="id")
    @ManyToOne(optional=false)
    private Services service;

    public Sms() {
    }

    public Sms(Integer id) {
        this.id = id;
    }

    public Sms(Integer id, String contenu, String codeinte, String numepoli, String numeaven, String nomassure, String teleassu, String dateeche, String statut) {
        this.id = id;
        this.contenu = contenu;
        this.codeinte = codeinte;
        this.numepoli = numepoli;
        this.numeaven = numeaven;
        this.nomassure = nomassure;
        this.teleassu = teleassu;
        this.dateeche = dateeche;
        this.statut = statut;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContenu() {
        return this.contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public String getCodeinte() {
        return this.codeinte;
    }

    public void setCodeinte(String codeinte) {
        this.codeinte = codeinte;
    }

    public String getNumepoli() {
        return this.numepoli;
    }

    public void setNumepoli(String numepoli) {
        this.numepoli = numepoli;
    }

    public String getNumeaven() {
        return this.numeaven;
    }

    public void setNumeaven(String numeaven) {
        this.numeaven = numeaven;
    }

    public Integer getNumesini() {
        return this.numesini;
    }

    public void setNumesini(Integer numesini) {
        this.numesini = numesini;
    }

    public Integer getExersini() {
        return this.exersini;
    }

    public void setExersini(Integer exersini) {
        this.exersini = exersini;
    }

    public String getNomassure() {
        return this.nomassure;
    }

    public void setNomassure(String nomassure) {
        this.nomassure = nomassure;
    }

    public String getTeleassu() {
        return this.teleassu;
    }

    public void setTeleassu(String teleassu) {
        this.teleassu = teleassu;
    }

    public String getNomExpediteur() {
        return this.nomExpediteur;
    }

    public void setNomExpediteur(String nomExpediteur) {
        this.nomExpediteur = nomExpediteur;
    }

    public String getReponseEnvoiSms() {
        return this.reponseEnvoiSms;
    }

    public void setReponseEnvoiSms(String reponseEnvoiSms) {
        this.reponseEnvoiSms = reponseEnvoiSms;
    }

    public Date getDateEnvoi() {
        return this.dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getDateeche() {
        return this.dateeche;
    }

    public void setDateeche(String dateeche) {
        this.dateeche = dateeche;
    }

    public String getStatut() {
        return this.statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Date getDatecreation() {
        return this.datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public Services getService() {
        return this.service;
    }

    public void setService(Services service) {
        this.service = service;
    }

    public String getNumeimma() {
        return this.numeimma;
    }

    public void setNumeimma(String numeimma) {
        this.numeimma = numeimma;
    }

    public int hashCode() {
        int hash = 0;
        return hash += this.id != null ? this.id.hashCode() : 0;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Sms)) {
            return false;
        }
        Sms other = (Sms)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.Sms[ id=" + this.id + " ]";
    }
}
