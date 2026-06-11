/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.vues;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "ligne_prestation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LignePrestation.findAll", query = "SELECT l FROM LignePrestation l"),
    @NamedQuery(name = "LignePrestation.findByIdLignePrestation", query = "SELECT l FROM LignePrestation l WHERE l.idLignePrestation = :idLignePrestation"),
    @NamedQuery(name = "LignePrestation.findByIdPrestation", query = "SELECT l FROM LignePrestation l WHERE l.idPrestation = :idPrestation"),
    @NamedQuery(name = "LignePrestation.findByCodePrestataire", query = "SELECT l FROM LignePrestation l WHERE l.codePrestataire = :codePrestataire"),
    @NamedQuery(name = "LignePrestation.findByCodeAssurePrincipal", query = "SELECT l FROM LignePrestation l WHERE l.codeAssurePrincipal = :codeAssurePrincipal"),
    @NamedQuery(name = "LignePrestation.findByCodeBeneficiaire", query = "SELECT l FROM LignePrestation l WHERE l.codeBeneficiaire = :codeBeneficiaire"),
    @NamedQuery(name = "LignePrestation.findByCodeTypePrestation", query = "SELECT l FROM LignePrestation l WHERE l.codeTypePrestation = :codeTypePrestation"),
    @NamedQuery(name = "LignePrestation.findByNom", query = "SELECT l FROM LignePrestation l WHERE l.nom = :nom"),
    @NamedQuery(name = "LignePrestation.findByMontant", query = "SELECT l FROM LignePrestation l WHERE l.montant = :montant"),
    @NamedQuery(name = "LignePrestation.findByTaux", query = "SELECT l FROM LignePrestation l WHERE l.taux = :taux"),
    @NamedQuery(name = "LignePrestation.findByDate", query = "SELECT l FROM LignePrestation l WHERE l.date = :date")})
public class LignePrestation implements Serializable {

    private static final long serialVersionUID = 1L;
     @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_ligne_prestation")
    private int idLignePrestation;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_prestation")
    private int idPrestation;
    @Size(max = 100)
    @Column(name = "code_prestataire")
    private String codePrestataire;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "code_assure_principal")
    private String codeAssurePrincipal;
    @Size(max = 100)
    @Column(name = "code_beneficiaire")
    private String codeBeneficiaire;
    @Size(max = 5)
    @Column(name = "code_type_prestation")
    private String codeTypePrestation;
    @Size(max = 255)
    @Column(name = "nom")
    private String nom;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "taux")
    private Double taux;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public LignePrestation() {
    }

    public int getIdLignePrestation() {
        return idLignePrestation;
    }

    public void setIdLignePrestation(int idLignePrestation) {
        this.idLignePrestation = idLignePrestation;
    }

    public int getIdPrestation() {
        return idPrestation;
    }

    public void setIdPrestation(int idPrestation) {
        this.idPrestation = idPrestation;
    }

    public String getCodePrestataire() {
        return codePrestataire;
    }

    public void setCodePrestataire(String codePrestataire) {
        this.codePrestataire = codePrestataire;
    }

    public String getCodeAssurePrincipal() {
        return codeAssurePrincipal;
    }

    public void setCodeAssurePrincipal(String codeAssurePrincipal) {
        this.codeAssurePrincipal = codeAssurePrincipal;
    }

    public String getCodeBeneficiaire() {
        return codeBeneficiaire;
    }

    public void setCodeBeneficiaire(String codeBeneficiaire) {
        this.codeBeneficiaire = codeBeneficiaire;
    }

    public String getCodeTypePrestation() {
        return codeTypePrestation;
    }

    public void setCodeTypePrestation(String codeTypePrestation) {
        this.codeTypePrestation = codeTypePrestation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
