/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_ligne_prestation")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyLignePrestation.findAll", query = "SELECT d FROM Dbx45tyLignePrestation d"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findById", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByTaux", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.taux = :taux"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByDentsConcernees", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.dentsConcernees = :dentsConcernees"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByCodification", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.codification = :codification"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByNom", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByValeur", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.valeur = :valeur"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByNbre", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.nbre = :nbre"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByActePrelevement", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.actePrelevement = :actePrelevement"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByValeurModif", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.valeurModif = :valeurModif"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByNbreModif", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.nbreModif = :nbreModif"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByActePrelevementModif", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.actePrelevementModif = :actePrelevementModif"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByPosologie", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.posologie = :posologie"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByObservations", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.observations = :observations"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByObservationsActePrelevement", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.observationsActePrelevement = :observationsActePrelevement"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByDate", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.date = :date"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByDateValideRejete", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.dateValideRejete = :dateValideRejete"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByDateEncaisse", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.dateEncaisse = :dateEncaisse"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findByEtat", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.etat = :etat"),
    @NamedQuery(name = "Dbx45tyLignePrestation.findBySupprime", query = "SELECT d FROM Dbx45tyLignePrestation d WHERE d.supprime = :supprime")})
public class Dbx45tyLignePrestation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "dents_concernees")
    private String dentsConcernees;
    @Column(name = "codification")
    private String codification;
    @Column(name = "nom")
    private String nom;
    @Column(name = "valeur")
    private Double valeur;
    @Column(name = "nbre")
    private Double nbre;
    @Basic(optional = false)
    @Column(name = "acte_prelevement")
    private double actePrelevement;
    @Column(name = "valeur_modif")
    private Double valeurModif;
    @Column(name = "nbre_modif")
    private Double nbreModif;
    @Basic(optional = false)
    @Column(name = "acte_prelevement_modif")
    private double actePrelevementModif;
    @Column(name = "posologie")
    private String posologie;
    @Column(name = "observations")
    private String observations;
    @Column(name = "observations_acte_prelevement")
    private String observationsActePrelevement;
    @Basic(optional = false)
    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name = "date_valide_rejete")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateValideRejete;
    @Column(name = "date_encaisse")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEncaisse;
    @Basic(optional = false)
    @Column(name = "etat")
    private String etat;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lignePrestationId")
    private List<Dbx45tyLignePrestationAudit> dbx45tyLignePrestationAuditList;
    @JoinColumn(name = "prestation_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyPrestation prestationId;
    @JoinColumn(name = "prestataire_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyPrestataire prestataireId;
    @JoinColumn(name = "employe_valide_rejete_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyEmploye employeValideRejeteId;
    @JoinColumn(name = "type_examen", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyTypePrestation typeExamen;
    @JoinColumn(name = "description_soins", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyTypePrestation descriptionSoins;
    @JoinColumn(name = "medicament_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyMedicament medicamentId;
    @JoinColumn(name = "examen_id", referencedColumnName = "id")
    @ManyToOne
    private Dbx45tyExamen examenId;

    public Dbx45tyLignePrestation() {
    }

    public Dbx45tyLignePrestation(Integer id) {
        this.id = id;
    }

    public Dbx45tyLignePrestation(Integer id, double actePrelevement, double actePrelevementModif, Date date, String etat, String supprime) {
        this.id = id;
        this.actePrelevement = actePrelevement;
        this.actePrelevementModif = actePrelevementModif;
        this.date = date;
        this.etat = etat;
        this.supprime = supprime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public String getDentsConcernees() {
        return dentsConcernees;
    }

    public void setDentsConcernees(String dentsConcernees) {
        this.dentsConcernees = dentsConcernees;
    }

    public String getCodification() {
        return codification;
    }

    public void setCodification(String codification) {
        this.codification = codification;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getValeur() {
        return valeur;
    }

    public void setValeur(Double valeur) {
        this.valeur = valeur;
    }

    public Double getNbre() {
        return nbre;
    }

    public void setNbre(Double nbre) {
        this.nbre = nbre;
    }

    public double getActePrelevement() {
        return actePrelevement;
    }

    public void setActePrelevement(double actePrelevement) {
        this.actePrelevement = actePrelevement;
    }

    public Double getValeurModif() {
        return valeurModif;
    }

    public void setValeurModif(Double valeurModif) {
        this.valeurModif = valeurModif;
    }

    public Double getNbreModif() {
        return nbreModif;
    }

    public void setNbreModif(Double nbreModif) {
        this.nbreModif = nbreModif;
    }

    public double getActePrelevementModif() {
        return actePrelevementModif;
    }

    public void setActePrelevementModif(double actePrelevementModif) {
        this.actePrelevementModif = actePrelevementModif;
    }

    public String getPosologie() {
        return posologie;
    }

    public void setPosologie(String posologie) {
        this.posologie = posologie;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getObservationsActePrelevement() {
        return observationsActePrelevement;
    }

    public void setObservationsActePrelevement(String observationsActePrelevement) {
        this.observationsActePrelevement = observationsActePrelevement;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateValideRejete() {
        return dateValideRejete;
    }

    public void setDateValideRejete(Date dateValideRejete) {
        this.dateValideRejete = dateValideRejete;
    }

    public Date getDateEncaisse() {
        return dateEncaisse;
    }

    public void setDateEncaisse(Date dateEncaisse) {
        this.dateEncaisse = dateEncaisse;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getSupprime() {
        return supprime;
    }

    public void setSupprime(String supprime) {
        this.supprime = supprime;
    }

    public List<Dbx45tyLignePrestationAudit> getDbx45tyLignePrestationAuditList() {
        return dbx45tyLignePrestationAuditList;
    }

    public void setDbx45tyLignePrestationAuditList(List<Dbx45tyLignePrestationAudit> dbx45tyLignePrestationAuditList) {
        this.dbx45tyLignePrestationAuditList = dbx45tyLignePrestationAuditList;
    }

    public Dbx45tyPrestation getPrestationId() {
        return prestationId;
    }

    public void setPrestationId(Dbx45tyPrestation prestationId) {
        this.prestationId = prestationId;
    }

    public Dbx45tyPrestataire getPrestataireId() {
        return prestataireId;
    }

    public void setPrestataireId(Dbx45tyPrestataire prestataireId) {
        this.prestataireId = prestataireId;
    }

    public Dbx45tyEmploye getEmployeValideRejeteId() {
        return employeValideRejeteId;
    }

    public void setEmployeValideRejeteId(Dbx45tyEmploye employeValideRejeteId) {
        this.employeValideRejeteId = employeValideRejeteId;
    }

    public Dbx45tyTypePrestation getTypeExamen() {
        return typeExamen;
    }

    public void setTypeExamen(Dbx45tyTypePrestation typeExamen) {
        this.typeExamen = typeExamen;
    }

    public Dbx45tyTypePrestation getDescriptionSoins() {
        return descriptionSoins;
    }

    public void setDescriptionSoins(Dbx45tyTypePrestation descriptionSoins) {
        this.descriptionSoins = descriptionSoins;
    }

    public Dbx45tyMedicament getMedicamentId() {
        return medicamentId;
    }

    public void setMedicamentId(Dbx45tyMedicament medicamentId) {
        this.medicamentId = medicamentId;
    }

    public Dbx45tyExamen getExamenId() {
        return examenId;
    }

    public void setExamenId(Dbx45tyExamen examenId) {
        this.examenId = examenId;
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
        if (!(object instanceof Dbx45tyLignePrestation)) {
            return false;
        }
        Dbx45tyLignePrestation other = (Dbx45tyLignePrestation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyLignePrestation[ id=" + id + " ]";
    }
    
}
