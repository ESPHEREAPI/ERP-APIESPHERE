/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "dbx45ty_devise")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyDevise.findAll", query = "SELECT d FROM Dbx45tyDevise d"),
    @NamedQuery(name = "Dbx45tyDevise.findById", query = "SELECT d FROM Dbx45tyDevise d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyDevise.findByCodeIso", query = "SELECT d FROM Dbx45tyDevise d WHERE d.codeIso = :codeIso"),
    @NamedQuery(name = "Dbx45tyDevise.findByCodeIsoNumerique", query = "SELECT d FROM Dbx45tyDevise d WHERE d.codeIsoNumerique = :codeIsoNumerique"),
    @NamedQuery(name = "Dbx45tyDevise.findBySymbole", query = "SELECT d FROM Dbx45tyDevise d WHERE d.symbole = :symbole"),
    @NamedQuery(name = "Dbx45tyDevise.findBySeparateurMilliers", query = "SELECT d FROM Dbx45tyDevise d WHERE d.separateurMilliers = :separateurMilliers"),
    @NamedQuery(name = "Dbx45tyDevise.findBySeparateurDecimaux", query = "SELECT d FROM Dbx45tyDevise d WHERE d.separateurDecimaux = :separateurDecimaux"),
    @NamedQuery(name = "Dbx45tyDevise.findByTauxConversion", query = "SELECT d FROM Dbx45tyDevise d WHERE d.tauxConversion = :tauxConversion"),
    @NamedQuery(name = "Dbx45tyDevise.findByPositionSymbole", query = "SELECT d FROM Dbx45tyDevise d WHERE d.positionSymbole = :positionSymbole"),
    @NamedQuery(name = "Dbx45tyDevise.findByStatut", query = "SELECT d FROM Dbx45tyDevise d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyDevise.findBySupprime", query = "SELECT d FROM Dbx45tyDevise d WHERE d.supprime = :supprime")})
public class Dbx45tyDevise implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Short id;
    @Basic(optional = false)
    @Column(name = "code_iso")
    private String codeIso;
    @Basic(optional = false)
    @Column(name = "code_iso_numerique")
    private String codeIsoNumerique;
    @Basic(optional = false)
    @Column(name = "symbole")
    private String symbole;
    @Basic(optional = false)
    @Column(name = "separateur_milliers")
    private String separateurMilliers;
    @Basic(optional = false)
    @Column(name = "separateur_decimaux")
    private String separateurDecimaux;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @Column(name = "taux_conversion")
    private BigDecimal tauxConversion;
    @Basic(optional = false)
    @Column(name = "position_symbole")
    private String positionSymbole;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;

    public Dbx45tyDevise() {
    }

    public Dbx45tyDevise(Short id) {
        this.id = id;
    }

    public Dbx45tyDevise(Short id, String codeIso, String codeIsoNumerique, String symbole, String separateurMilliers, String separateurDecimaux, BigDecimal tauxConversion, String positionSymbole, String statut, String supprime) {
        this.id = id;
        this.codeIso = codeIso;
        this.codeIsoNumerique = codeIsoNumerique;
        this.symbole = symbole;
        this.separateurMilliers = separateurMilliers;
        this.separateurDecimaux = separateurDecimaux;
        this.tauxConversion = tauxConversion;
        this.positionSymbole = positionSymbole;
        this.statut = statut;
        this.supprime = supprime;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getCodeIso() {
        return codeIso;
    }

    public void setCodeIso(String codeIso) {
        this.codeIso = codeIso;
    }

    public String getCodeIsoNumerique() {
        return codeIsoNumerique;
    }

    public void setCodeIsoNumerique(String codeIsoNumerique) {
        this.codeIsoNumerique = codeIsoNumerique;
    }

    public String getSymbole() {
        return symbole;
    }

    public void setSymbole(String symbole) {
        this.symbole = symbole;
    }

    public String getSeparateurMilliers() {
        return separateurMilliers;
    }

    public void setSeparateurMilliers(String separateurMilliers) {
        this.separateurMilliers = separateurMilliers;
    }

    public String getSeparateurDecimaux() {
        return separateurDecimaux;
    }

    public void setSeparateurDecimaux(String separateurDecimaux) {
        this.separateurDecimaux = separateurDecimaux;
    }

    public BigDecimal getTauxConversion() {
        return tauxConversion;
    }

    public void setTauxConversion(BigDecimal tauxConversion) {
        this.tauxConversion = tauxConversion;
    }

    public String getPositionSymbole() {
        return positionSymbole;
    }

    public void setPositionSymbole(String positionSymbole) {
        this.positionSymbole = positionSymbole;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dbx45tyDevise)) {
            return false;
        }
        Dbx45tyDevise other = (Dbx45tyDevise) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyDevise[ id=" + id + " ]";
    }
    
}
