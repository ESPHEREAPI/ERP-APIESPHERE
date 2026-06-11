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
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_profil")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyProfil.findAll", query = "SELECT d FROM Dbx45tyProfil d"),
    @NamedQuery(name = "Dbx45tyProfil.findById", query = "SELECT d FROM Dbx45tyProfil d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyProfil.findByTypeProfil", query = "SELECT d FROM Dbx45tyProfil d WHERE d.typeProfil = :typeProfil"),
    @NamedQuery(name = "Dbx45tyProfil.findByTypeSousProfil", query = "SELECT d FROM Dbx45tyProfil d WHERE d.typeSousProfil = :typeSousProfil"),
    @NamedQuery(name = "Dbx45tyProfil.findByCode", query = "SELECT d FROM Dbx45tyProfil d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyProfil.findByStatut", query = "SELECT d FROM Dbx45tyProfil d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyProfil.findBySupprime", query = "SELECT d FROM Dbx45tyProfil d WHERE d.supprime = :supprime")})
public class Dbx45tyProfil implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "type_profil")
    private String typeProfil;
    @Basic(optional = false)
    @Column(name = "type_sous_profil")
    private String typeSousProfil;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profilId")
    private List<Dbx45tyEmploye> dbx45tyEmployeList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profilId")
    private List<Dbx45tyProfilLangue> dbx45tyProfilLangueList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "profilId")
    private List<Dbx45tyPermission> dbx45tyPermissionList;

    public Dbx45tyProfil() {
    }

    public Dbx45tyProfil(Integer id) {
        this.id = id;
    }

    public Dbx45tyProfil(Integer id, String typeProfil, String typeSousProfil, String code, String statut, String supprime) {
        this.id = id;
        this.typeProfil = typeProfil;
        this.typeSousProfil = typeSousProfil;
        this.code = code;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeProfil() {
        return typeProfil;
    }

    public void setTypeProfil(String typeProfil) {
        this.typeProfil = typeProfil;
    }

    public String getTypeSousProfil() {
        return typeSousProfil;
    }

    public void setTypeSousProfil(String typeSousProfil) {
        this.typeSousProfil = typeSousProfil;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public List<Dbx45tyEmploye> getDbx45tyEmployeList() {
        return dbx45tyEmployeList;
    }

    public void setDbx45tyEmployeList(List<Dbx45tyEmploye> dbx45tyEmployeList) {
        this.dbx45tyEmployeList = dbx45tyEmployeList;
    }

    public List<Dbx45tyProfilLangue> getDbx45tyProfilLangueList() {
        return dbx45tyProfilLangueList;
    }

    public void setDbx45tyProfilLangueList(List<Dbx45tyProfilLangue> dbx45tyProfilLangueList) {
        this.dbx45tyProfilLangueList = dbx45tyProfilLangueList;
    }

    public List<Dbx45tyPermission> getDbx45tyPermissionList() {
        return dbx45tyPermissionList;
    }

    public void setDbx45tyPermissionList(List<Dbx45tyPermission> dbx45tyPermissionList) {
        this.dbx45tyPermissionList = dbx45tyPermissionList;
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
        if (!(object instanceof Dbx45tyProfil)) {
            return false;
        }
        Dbx45tyProfil other = (Dbx45tyProfil) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyProfil[ id=" + id + " ]";
    }
    
}
