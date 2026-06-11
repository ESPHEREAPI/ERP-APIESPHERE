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
@Table(name = "dbx45ty_region")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyRegion.findAll", query = "SELECT d FROM Dbx45tyRegion d"),
    @NamedQuery(name = "Dbx45tyRegion.findById", query = "SELECT d FROM Dbx45tyRegion d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyRegion.findByCode", query = "SELECT d FROM Dbx45tyRegion d WHERE d.code = :code"),
    @NamedQuery(name = "Dbx45tyRegion.findByStatut", query = "SELECT d FROM Dbx45tyRegion d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyRegion.findBySupprime", query = "SELECT d FROM Dbx45tyRegion d WHERE d.supprime = :supprime")})
public class Dbx45tyRegion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "code")
    private String code;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "regionId")
    private List<Dbx45tyRegionLangue> dbx45tyRegionLangueList;
    @JoinColumn(name = "pays_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyPays paysId;
    @JoinColumn(name = "chef_lieu_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyVille chefLieuId;

    public Dbx45tyRegion() {
    }

    public Dbx45tyRegion(Integer id) {
        this.id = id;
    }

    public Dbx45tyRegion(Integer id, String code, String statut, String supprime) {
        this.id = id;
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

    public List<Dbx45tyRegionLangue> getDbx45tyRegionLangueList() {
        return dbx45tyRegionLangueList;
    }

    public void setDbx45tyRegionLangueList(List<Dbx45tyRegionLangue> dbx45tyRegionLangueList) {
        this.dbx45tyRegionLangueList = dbx45tyRegionLangueList;
    }

    public Dbx45tyPays getPaysId() {
        return paysId;
    }

    public void setPaysId(Dbx45tyPays paysId) {
        this.paysId = paysId;
    }

    public Dbx45tyVille getChefLieuId() {
        return chefLieuId;
    }

    public void setChefLieuId(Dbx45tyVille chefLieuId) {
        this.chefLieuId = chefLieuId;
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
        if (!(object instanceof Dbx45tyRegion)) {
            return false;
        }
        Dbx45tyRegion other = (Dbx45tyRegion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyRegion[ id=" + id + " ]";
    }
    
}
