/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_taux_prestation")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyTauxPrestation.findAll", query = "SELECT d FROM Dbx45tyTauxPrestation d"),
    @NamedQuery(name = "Dbx45tyTauxPrestation.findById", query = "SELECT d FROM Dbx45tyTauxPrestation d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyTauxPrestation.findByTypePrestationId", query = "SELECT d FROM Dbx45tyTauxPrestation d WHERE d.typePrestationId = :typePrestationId"),
    @NamedQuery(name = "Dbx45tyTauxPrestation.findByPolice", query = "SELECT d FROM Dbx45tyTauxPrestation d WHERE d.police = :police"),
    @NamedQuery(name = "Dbx45tyTauxPrestation.findByGroupe", query = "SELECT d FROM Dbx45tyTauxPrestation d WHERE d.groupe = :groupe"),
    @NamedQuery(name = "Dbx45tyTauxPrestation.findByTaux", query = "SELECT d FROM Dbx45tyTauxPrestation d WHERE d.taux = :taux"),
    @NamedQuery(name = "Dbx45tyTauxPrestation.findByPlafond", query = "SELECT d FROM Dbx45tyTauxPrestation d WHERE d.plafond = :plafond")})
public class Dbx45tyTauxPrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "type_prestation_id")
    private String typePrestationId;
    @Basic(optional = false)
    @Column(name = "police")
    private String police;
    @Basic(optional = false)
    @Column(name = "groupe")
    private short groupe;
    @Column(name = "taux")
    private Integer taux;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "plafond")
    private Float plafond;

    public Dbx45tyTauxPrestation() {
    }

    public Dbx45tyTauxPrestation(Integer id) {
        this.id = id;
    }

    public Dbx45tyTauxPrestation(Integer id, String typePrestationId, String police, short groupe) {
        this.id = id;
        this.typePrestationId = typePrestationId;
        this.police = police;
        this.groupe = groupe;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypePrestationId() {
        return typePrestationId;
    }

    public void setTypePrestationId(String typePrestationId) {
        this.typePrestationId = typePrestationId;
    }

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public short getGroupe() {
        return groupe;
    }

    public void setGroupe(short groupe) {
        this.groupe = groupe;
    }

    public Integer getTaux() {
        return taux;
    }

    public void setTaux(Integer taux) {
        this.taux = taux;
    }

    public Float getPlafond() {
        return plafond;
    }

    public void setPlafond(Float plafond) {
        this.plafond = plafond;
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
        if (!(object instanceof Dbx45tyTauxPrestation)) {
            return false;
        }
        Dbx45tyTauxPrestation other = (Dbx45tyTauxPrestation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyTauxPrestation[ id=" + id + " ]";
    }
    
}
