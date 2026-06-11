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
@Table(name = "consultation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consultation.findAll", query = "SELECT c FROM Consultation c"),
    @NamedQuery(name = "Consultation.findByIdConsultation", query = "SELECT c FROM Consultation c WHERE c.idConsultation = :idConsultation"),
    @NamedQuery(name = "Consultation.findByCodePrestataire", query = "SELECT c FROM Consultation c WHERE c.codePrestataire = :codePrestataire"),
    @NamedQuery(name = "Consultation.findByCodeAssurePrincipal", query = "SELECT c FROM Consultation c WHERE c.codeAssurePrincipal = :codeAssurePrincipal"),
    @NamedQuery(name = "Consultation.findByCodeBeneficiaire", query = "SELECT c FROM Consultation c WHERE c.codeBeneficiaire = :codeBeneficiaire"),
    @NamedQuery(name = "Consultation.findByCodeTypePrestation", query = "SELECT c FROM Consultation c WHERE c.codeTypePrestation = :codeTypePrestation"),
    @NamedQuery(name = "Consultation.findByMontant", query = "SELECT c FROM Consultation c WHERE c.montant = :montant"),
    @NamedQuery(name = "Consultation.findByTauxCouverture", query = "SELECT c FROM Consultation c WHERE c.tauxCouverture = :tauxCouverture"),
    @NamedQuery(name = "Consultation.findByDate", query = "SELECT c FROM Consultation c WHERE c.date = :date")})
public class Consultation implements Serializable {

    private static final long serialVersionUID = 1L;
     @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_consultation")
    private int idConsultation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "code_prestataire")
    private String codePrestataire;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "code_assure_principal")
    private String codeAssurePrincipal;
    @Size(max = 100)
    @Column(name = "code_beneficiaire")
    private String codeBeneficiaire;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "code_type_prestation")
    private String codeTypePrestation;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "montant")
    private Double montant;
    @Column(name = "taux_couverture")
    private Double tauxCouverture;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Consultation() {
    }

    public int getIdConsultation() {
        return idConsultation;
    }

    public void setIdConsultation(int idConsultation) {
        this.idConsultation = idConsultation;
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

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public Double getTauxCouverture() {
        return tauxCouverture;
    }

    public void setTauxCouverture(Double tauxCouverture) {
        this.tauxCouverture = tauxCouverture;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
