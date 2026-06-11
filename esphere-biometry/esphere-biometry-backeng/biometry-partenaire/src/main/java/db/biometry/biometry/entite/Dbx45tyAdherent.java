/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "dbx45ty_adherent")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyAdherent.findAll", query = "SELECT d FROM Dbx45tyAdherent d"),
    @NamedQuery(name = "Dbx45tyAdherent.findByNumero", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.numero = :numero"),
    @NamedQuery(name = "Dbx45tyAdherent.findByCodeAdherent", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.codeAdherent = :codeAdherent"),
    @NamedQuery(name = "Dbx45tyAdherent.findByAssurePrincipal", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.assurePrincipal = :assurePrincipal"),
    @NamedQuery(name = "Dbx45tyAdherent.findByNaissance", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.naissance = :naissance"),
    @NamedQuery(name = "Dbx45tyAdherent.findBySexe", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.sexe = :sexe"),
    @NamedQuery(name = "Dbx45tyAdherent.findByMatricule", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.matricule = :matricule"),
    @NamedQuery(name = "Dbx45tyAdherent.findByTelephone", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.telephone = :telephone"),
    @NamedQuery(name = "Dbx45tyAdherent.findByTaux", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.taux = :taux"),
    @NamedQuery(name = "Dbx45tyAdherent.findBySouscripteur", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.souscripteur = :souscripteur"),
    @NamedQuery(name = "Dbx45tyAdherent.findByPolice", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.police = :police"),
    @NamedQuery(name = "Dbx45tyAdherent.findByEffetPolice", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.effetPolice = :effetPolice"),
    @NamedQuery(name = "Dbx45tyAdherent.findByEcheancePolice", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.echeancePolice = :echeancePolice"),
    @NamedQuery(name = "Dbx45tyAdherent.findByGroupe", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.groupe = :groupe"),
    @NamedQuery(name = "Dbx45tyAdherent.findByEnrole", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.enrole = :enrole"),
    @NamedQuery(name = "Dbx45tyAdherent.findByDateEnrole", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.dateEnrole = :dateEnrole"),
    @NamedQuery(name = "Dbx45tyAdherent.findByImprime", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.imprime = :imprime"),
    @NamedQuery(name = "Dbx45tyAdherent.findByStatut", query = "SELECT d FROM Dbx45tyAdherent d WHERE d.statut = :statut")})
public class Dbx45tyAdherent implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "numero")
    private int numero;
    @Id
    @Basic(optional = false)
    @Column(name = "code_adherent")
    private String codeAdherent;
    @Column(name = "assure_principal")
    private String assurePrincipal;
    @Column(name = "naissance")
    @Temporal(TemporalType.DATE)
    private Date naissance;
    @Column(name = "sexe")
    private String sexe;
    @Column(name = "matricule")
    private String matricule;
    @Column(name = "telephone")
    private String telephone;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "taux")
    private Double taux;
    @Column(name = "souscripteur")
    private String souscripteur;
    @Column(name = "police")
    private String police;
    @Column(name = "effet_police")
    @Temporal(TemporalType.DATE)
    private Date effetPolice;
    @Column(name = "echeance_police")
    @Temporal(TemporalType.DATE)
    private Date echeancePolice;
    @Column(name = "groupe")
    private Short groupe;
    @Basic(optional = false)
    @Column(name = "enrole")
    private String enrole;
    @Column(name = "date_enrole")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateEnrole;
    @Basic(optional = false)
    @Column(name = "imprime")
    private String imprime;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
      // double en base → Double en Java (plafond assuré)
    @Column(name = "plafond_assurep")
    private Double plafondAssurep;
    @OneToMany(mappedBy = "codeAdherent")
    private List<Dbx45tyAyantDroit> dbx45tyAyantDroitList;

    public Dbx45tyAdherent() {
    }

    public Dbx45tyAdherent(String codeAdherent) {
        this.codeAdherent = codeAdherent;
    }

    public Dbx45tyAdherent(String codeAdherent, int numero, String enrole, String imprime, String statut) {
        this.codeAdherent = codeAdherent;
        this.numero = numero;
        this.enrole = enrole;
        this.imprime = imprime;
        this.statut = statut;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getCodeAdherent() {
        return codeAdherent;
    }

    public void setCodeAdherent(String codeAdherent) {
        this.codeAdherent = codeAdherent;
    }

    public String getAssurePrincipal() {
        return assurePrincipal;
    }

    public void setAssurePrincipal(String assurePrincipal) {
        this.assurePrincipal = assurePrincipal;
    }

    public Date getNaissance() {
        return naissance;
    }

    public void setNaissance(Date naissance) {
        this.naissance = naissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Double getTaux() {
        return taux;
    }

    public void setTaux(Double taux) {
        this.taux = taux;
    }

    public String getSouscripteur() {
        return souscripteur;
    }

    public void setSouscripteur(String souscripteur) {
        this.souscripteur = souscripteur;
    }

    public String getPolice() {
        return police;
    }

    public void setPolice(String police) {
        this.police = police;
    }

    public Date getEffetPolice() {
        return effetPolice;
    }

    public void setEffetPolice(Date effetPolice) {
        this.effetPolice = effetPolice;
    }

    public Date getEcheancePolice() {
        return echeancePolice;
    }

    public void setEcheancePolice(Date echeancePolice) {
        this.echeancePolice = echeancePolice;
    }

    public Short getGroupe() {
        return groupe;
    }

    public void setGroupe(Short groupe) {
        this.groupe = groupe;
    }

    public String getEnrole() {
        return enrole;
    }

    public void setEnrole(String enrole) {
        this.enrole = enrole;
    }

    public Date getDateEnrole() {
        return dateEnrole;
    }

    public void setDateEnrole(Date dateEnrole) {
        this.dateEnrole = dateEnrole;
    }

    public String getImprime() {
        return imprime;
    }

    public void setImprime(String imprime) {
        this.imprime = imprime;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public List<Dbx45tyAyantDroit> getDbx45tyAyantDroitList() {
        return dbx45tyAyantDroitList;
    }

    public void setDbx45tyAyantDroitList(List<Dbx45tyAyantDroit> dbx45tyAyantDroitList) {
        this.dbx45tyAyantDroitList = dbx45tyAyantDroitList;
    }

    public Double getPlafondAssurep() {
        return plafondAssurep;
    }

    public void setPlafondAssurep(Double plafondAssurep) {
        this.plafondAssurep = plafondAssurep;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeAdherent != null ? codeAdherent.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dbx45tyAdherent)) {
            return false;
        }
        Dbx45tyAdherent other = (Dbx45tyAdherent) object;
        if ((this.codeAdherent == null && other.codeAdherent != null) || (this.codeAdherent != null && !this.codeAdherent.equals(other.codeAdherent))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyAdherent[ codeAdherent=" + codeAdherent + " ]";
    }
    
}
