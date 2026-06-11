/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_menu")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyMenu.findAll", query = "SELECT d FROM Dbx45tyMenu d"),
    @NamedQuery(name = "Dbx45tyMenu.findById", query = "SELECT d FROM Dbx45tyMenu d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyMenu.findByNomControlleur", query = "SELECT d FROM Dbx45tyMenu d WHERE d.nomControlleur = :nomControlleur"),
    @NamedQuery(name = "Dbx45tyMenu.findByNomModule", query = "SELECT d FROM Dbx45tyMenu d WHERE d.nomModule = :nomModule"),
    @NamedQuery(name = "Dbx45tyMenu.findByNomAction", query = "SELECT d FROM Dbx45tyMenu d WHERE d.nomAction = :nomAction"),
    @NamedQuery(name = "Dbx45tyMenu.findByNumeroOrdre", query = "SELECT d FROM Dbx45tyMenu d WHERE d.numeroOrdre = :numeroOrdre"),
    @NamedQuery(name = "Dbx45tyMenu.findByClassImage", query = "SELECT d FROM Dbx45tyMenu d WHERE d.classImage = :classImage"),
    @NamedQuery(name = "Dbx45tyMenu.findByType", query = "SELECT d FROM Dbx45tyMenu d WHERE d.type = :type"),
    @NamedQuery(name = "Dbx45tyMenu.findByPosition", query = "SELECT d FROM Dbx45tyMenu d WHERE d.position = :position"),
    @NamedQuery(name = "Dbx45tyMenu.findByApparaitNav", query = "SELECT d FROM Dbx45tyMenu d WHERE d.apparaitNav = :apparaitNav"),
    @NamedQuery(name = "Dbx45tyMenu.findByApparaitNavBar", query = "SELECT d FROM Dbx45tyMenu d WHERE d.apparaitNavBar = :apparaitNavBar"),
    @NamedQuery(name = "Dbx45tyMenu.findByStatut", query = "SELECT d FROM Dbx45tyMenu d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyMenu.findBySupprime", query = "SELECT d FROM Dbx45tyMenu d WHERE d.supprime = :supprime")})
public class Dbx45tyMenu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nom_controlleur")
    private String nomControlleur;
    @Basic(optional = false)
    @Column(name = "nom_module")
    private String nomModule;
    @Column(name = "nom_action")
    private String nomAction;
    @Basic(optional = false)
    @Column(name = "numero_ordre")
    private short numeroOrdre;
    @Column(name = "class_image")
    private String classImage;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "position")
    private short position;
    @Basic(optional = false)
    @Column(name = "apparait_nav")
    private String apparaitNav;
    @Basic(optional = false)
    @Column(name = "apparait_nav_bar")
    private String apparaitNavBar;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @Lob
    @Column(name = "chemin_pere")
    private String cheminPere;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menuId")
    private List<Dbx45tyMenuLangue> dbx45tyMenuLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menuId")
    private List<Dbx45tyPermission> dbx45tyPermissionList;
    @OneToMany(mappedBy = "pereId")
    private List<Dbx45tyMenu> dbx45tyMenuList;
    @JoinColumn(name = "pere_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyMenu pereId;

    public Dbx45tyMenu() {
    }

    public Dbx45tyMenu(Integer id) {
        this.id = id;
    }

    public Dbx45tyMenu(Integer id, String nomModule, short numeroOrdre, String type, short position, String apparaitNav, String apparaitNavBar, String statut, String supprime) {
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
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomControlleur() {
        return nomControlleur;
    }

    public void setNomControlleur(String nomControlleur) {
        this.nomControlleur = nomControlleur;
    }

    public String getNomModule() {
        return nomModule;
    }

    public void setNomModule(String nomModule) {
        this.nomModule = nomModule;
    }

    public String getNomAction() {
        return nomAction;
    }

    public void setNomAction(String nomAction) {
        this.nomAction = nomAction;
    }

    public short getNumeroOrdre() {
        return numeroOrdre;
    }

    public void setNumeroOrdre(short numeroOrdre) {
        this.numeroOrdre = numeroOrdre;
    }

    public String getClassImage() {
        return classImage;
    }

    public void setClassImage(String classImage) {
        this.classImage = classImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public short getPosition() {
        return position;
    }

    public void setPosition(short position) {
        this.position = position;
    }

    public String getApparaitNav() {
        return apparaitNav;
    }

    public void setApparaitNav(String apparaitNav) {
        this.apparaitNav = apparaitNav;
    }

    public String getApparaitNavBar() {
        return apparaitNavBar;
    }

    public void setApparaitNavBar(String apparaitNavBar) {
        this.apparaitNavBar = apparaitNavBar;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getSupprime() {
        return supprime;
    }

    public void setSupprime(String supprime) {
        this.supprime = supprime;
    }

    public String getCheminPere() {
        return cheminPere;
    }

    public void setCheminPere(String cheminPere) {
        this.cheminPere = cheminPere;
    }

    public List<Dbx45tyMenuLangue> getDbx45tyMenuLangueList() {
        return dbx45tyMenuLangueList;
    }

    public void setDbx45tyMenuLangueList(List<Dbx45tyMenuLangue> dbx45tyMenuLangueList) {
        this.dbx45tyMenuLangueList = dbx45tyMenuLangueList;
    }

    public List<Dbx45tyPermission> getDbx45tyPermissionList() {
        return dbx45tyPermissionList;
    }

    public void setDbx45tyPermissionList(List<Dbx45tyPermission> dbx45tyPermissionList) {
        this.dbx45tyPermissionList = dbx45tyPermissionList;
    }

    public List<Dbx45tyMenu> getDbx45tyMenuList() {
        return dbx45tyMenuList;
    }

    public void setDbx45tyMenuList(List<Dbx45tyMenu> dbx45tyMenuList) {
        this.dbx45tyMenuList = dbx45tyMenuList;
    }

    public Dbx45tyMenu getPereId() {
        return pereId;
    }

    public void setPereId(Dbx45tyMenu pereId) {
        this.pereId = pereId;
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
        if (!(object instanceof Dbx45tyMenu)) {
            return false;
        }
        Dbx45tyMenu other = (Dbx45tyMenu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyMenu[ id=" + id + " ]";
    }
    
}
