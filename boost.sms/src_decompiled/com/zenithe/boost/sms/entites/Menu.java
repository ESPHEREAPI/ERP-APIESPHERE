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
 *  javax.persistence.JoinColumn
 *  javax.persistence.Lob
 *  javax.persistence.ManyToOne
 *  javax.persistence.NamedQueries
 *  javax.persistence.NamedQuery
 *  javax.persistence.OneToMany
 *  javax.persistence.Table
 *  javax.xml.bind.annotation.XmlRootElement
 *  javax.xml.bind.annotation.XmlTransient
 */
package com.zenithe.boost.sms.entites;

import com.zenithe.boost.sms.entites.MenuLangue;
import com.zenithe.boost.sms.entites.Permission;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name="menu")
@XmlRootElement
@NamedQueries(value={@NamedQuery(name="Menu.findAll", query="SELECT m FROM Menu m"), @NamedQuery(name="Menu.findById", query="SELECT m FROM Menu m WHERE m.id = :id"), @NamedQuery(name="Menu.findByNomControlleur", query="SELECT m FROM Menu m WHERE m.nomControlleur = :nomControlleur"), @NamedQuery(name="Menu.findByNomModule", query="SELECT m FROM Menu m WHERE m.nomModule = :nomModule"), @NamedQuery(name="Menu.findByNomAction", query="SELECT m FROM Menu m WHERE m.nomAction = :nomAction"), @NamedQuery(name="Menu.findByNumeroOrdre", query="SELECT m FROM Menu m WHERE m.numeroOrdre = :numeroOrdre"), @NamedQuery(name="Menu.findByClassImage", query="SELECT m FROM Menu m WHERE m.classImage = :classImage"), @NamedQuery(name="Menu.findByType", query="SELECT m FROM Menu m WHERE m.type = :type"), @NamedQuery(name="Menu.findByPosition", query="SELECT m FROM Menu m WHERE m.position = :position"), @NamedQuery(name="Menu.findByApparaitNav", query="SELECT m FROM Menu m WHERE m.apparaitNav = :apparaitNav"), @NamedQuery(name="Menu.findByApparaitNavBar", query="SELECT m FROM Menu m WHERE m.apparaitNavBar = :apparaitNavBar"), @NamedQuery(name="Menu.findByStatut", query="SELECT m FROM Menu m WHERE m.statut = :statut"), @NamedQuery(name="Menu.findBySupprime", query="SELECT m FROM Menu m WHERE m.supprime = :supprime")})
public class Menu
implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Basic(optional=false)
    @Column(name="id")
    private Integer id;
    @Column(name="nom_controlleur")
    private String nomControlleur;
    @Basic(optional=false)
    @Column(name="nom_module")
    private String nomModule;
    @Column(name="nom_action")
    private String nomAction;
    @Basic(optional=false)
    @Column(name="numero_ordre")
    private short numeroOrdre;
    @Column(name="class_image")
    private String classImage;
    @Basic(optional=false)
    @Column(name="type")
    private String type;
    @Basic(optional=false)
    @Column(name="position")
    private short position;
    @Basic(optional=false)
    @Column(name="apparait_nav")
    private String apparaitNav;
    @Basic(optional=false)
    @Column(name="apparait_nav_bar")
    private String apparaitNavBar;
    @Basic(optional=false)
    @Column(name="statut")
    private String statut;
    @Basic(optional=false)
    @Column(name="supprime")
    private String supprime;
    @Lob
    @Column(name="chemin_pere")
    private String cheminPere;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="menuId")
    private List<Permission> permissionList;
    @OneToMany(mappedBy="pereId")
    private List<Menu> menuList;
    @JoinColumn(name="pere_id", referencedColumnName="id")
    @ManyToOne
    private Menu pereId;
    @OneToMany(cascade={CascadeType.ALL}, mappedBy="menuId")
    private List<MenuLangue> menuLangueList;

    public Menu() {
    }

    public Menu(Integer id) {
        this.id = id;
    }

    public Menu(Integer id, String nomModule, short numeroOrdre, String type, short position, String apparaitNav, String apparaitNavBar, String statut, String supprime) {
        this.id = id;
        this.nomModule = nomModule;
        this.numeroOrdre = numeroOrdre;
        this.type = type;
        this.position = position;
        this.apparaitNav = apparaitNav;
        this.apparaitNavBar = apparaitNavBar;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomControlleur() {
        return this.nomControlleur;
    }

    public void setNomControlleur(String nomControlleur) {
        this.nomControlleur = nomControlleur;
    }

    public String getNomModule() {
        return this.nomModule;
    }

    public void setNomModule(String nomModule) {
        this.nomModule = nomModule;
    }

    public String getNomAction() {
        return this.nomAction;
    }

    public void setNomAction(String nomAction) {
        this.nomAction = nomAction;
    }

    public short getNumeroOrdre() {
        return this.numeroOrdre;
    }

    public void setNumeroOrdre(short numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public String getClassImage() {
        return this.classImage;
    }

    public void setClassImage(String classImage) {
        this.classImage = classImage;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public short getPosition() {
        return this.position;
    }

    public void setPosition(short position) {
        this.position = position;
    }

    public String getApparaitNav() {
        return this.apparaitNav;
    }

    public void setApparaitNav(String apparaitNav) {
        this.apparaitNav = apparaitNav;
    }

    public String getApparaitNavBar() {
        return this.apparaitNavBar;
    }

    public void setApparaitNavBar(String apparaitNavBar) {
        this.apparaitNavBar = apparaitNavBar;
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

    public String getCheminPere() {
        return this.cheminPere;
    }

    public void setCheminPere(String cheminPere) {
        this.cheminPere = cheminPere;
    }

    @XmlTransient
    public List<Permission> getPermissionList() {
        return this.permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    @XmlTransient
    public List<Menu> getMenuList() {
        return this.menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public Menu getPereId() {
        return this.pereId;
    }

    public void setPereId(Menu pereId) {
        this.pereId = pereId;
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
        if (!(object instanceof Menu)) {
            return false;
        }
        Menu other = (Menu)object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    public String toString() {
        return "com.zenithe.boost.sms.entites.Menu[ id=" + this.id + " ]";
    }
}
