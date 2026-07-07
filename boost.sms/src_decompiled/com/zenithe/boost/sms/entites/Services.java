/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.fasterxml.jackson.annotation.JsonIgnore
 *  javax.persistence.Basic
 *  javax.persistence.CascadeType
 *  javax.persistence.Column
 *  javax.persistence.Entity
 *  javax.persistence.GeneratedValue
 *  javax.persistence.GenerationType
 *  javax.persistence.Id
 *  javax.persistence.Lob
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.OneToMany
 *  javax.persistence.Table
 *  javax.xml.bind.annotation.XmlTransient
 */
package com.zenithe.boost.sms.entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zenithe.boost.sms.entites.Attribut;
import com.zenithe.boost.sms.entites.Sms;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="service")
@NamedQueries(value={@NamedQuery(name="Service.findAll", query="SELECT s FROM Services s"), @NamedQuery(name="Service.findById", query="SELECT s FROM Services s WHERE s.id = :id"), @NamedQuery(name="Service.findByOrigine", query="SELECT s FROM Services s WHERE s.origine = :origine"), @NamedQuery(name="Service.findByNom", query="SELECT s FROM Services s WHERE s.nom = :nom"), @NamedQuery(name="Service.findByCode", query="SELECT s FROM Services s WHERE s.code = :code"), @NamedQuery(name="Service.findByNomVue", query="SELECT s FROM Services s WHERE s.nomVue = :nomVue"), @NamedQuery(name="Service.findByTypeDate", query="SELECT s FROM Services s WHERE s.typeDate = :typeDate"), @NamedQuery(name="Service.findByHeureMinEnvoi", query="SELECT s FROM Services s WHERE s.heureMinEnvoi = :heureMinEnvoi"), @NamedQuery(name="Service.findByStatut", query="SELECT s FROM Services s WHERE s.statut = :statut")})
public class Services
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="id")
    private Integer id;
    @Basic(optional=false)
    @Column(name="origine")
    private String origine;
    @Basic(optional=false)
    @Column(name="nom")
    private String nom;
    @Lob
    @Column(name="description")
    private String description;
    @Basic(optional=false)
    @Column(name="code")
    private String code;
    @Basic(optional=false)
    @Column(name="nom_vue")
    private String nomVue;
    @Lob
    @Column(name="template_message_fr")
    private String templateMessageFr;
    @Lob
    @Column(name="template_message_en")
    private String templateMessageEn;
    @Basic(optional=false)
    @Column(name="type_date")
    private String typeDate;
    @Column(name="heure_min_envoi")
    private String heureMinEnvoi;
    @Basic(optional=false)
    @Column(name="statut")
    private String statut;
    @JsonIgnore
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="serviceId")
    private List<Attribut> attributList;
    @JsonIgnore
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="service")
    private List<Sms> smsList;

    public Services() {
    }

    public Services(Integer id) {
        this.id = id;
    }

    public Services(Integer id, String origine, String nom, String code, String nomVue, String typeDate, String statut) {
        this.id = id;
        this.origine = origine;
        this.nom = nom;
        this.code = code;
        this.nomVue = nomVue;
        this.typeDate = typeDate;
        this.statut = statut;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrigine() {
        return this.origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNomVue() {
        return this.nomVue;
    }

    public void setNomVue(String nomVue) {
        this.nomVue = nomVue;
    }

    public String getTemplateMessageFr() {
        return this.templateMessageFr;
    }

    public void setTemplateMessageFr(String templateMessageFr) {
        this.templateMessageFr = templateMessageFr;
    }

    public String getTemplateMessageEn() {
        return this.templateMessageEn;
    }

    public void setTemplateMessageEn(String templateMessageEn) {
        this.templateMessageEn = templateMessageEn;
    }

    public String getTypeDate() {
        return this.typeDate;
    }

    public void setTypeDate(String typeDate) {
        this.typeDate = typeDate;
    }

    public String getHeureMinEnvoi() {
        return this.heureMinEnvoi;
    }

    public void setHeureMinEnvoi(String heureMinEnvoi) {
        this.heureMinEnvoi = heureMinEnvoi;
    }

    public String getStatut() {
        return this.statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @XmlTransient
    public List<Attribut> getAttributList() {
        return this.attributList;
    }

    public void setAttributList(List<Attribut> attributList) {
        this.attributList = attributList;
    }

    @XmlTransient
    public List<Sms> getSmsList() {
        return this.smsList;
    }

    public void setSmsList(List<Sms> smsList) {
        this.smsList = smsList;
    }

    public int hashCode() {
        int hash = 0;
        return hash += this.id != null ? this.id.hashCode() : 0;
    }

    public boolean equals(Object object) {
        if (!(object instanceof Services)) {
            return false;
        }
        Services other = (Services)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.Service[ id=" + this.id + " ]";
    }
}
