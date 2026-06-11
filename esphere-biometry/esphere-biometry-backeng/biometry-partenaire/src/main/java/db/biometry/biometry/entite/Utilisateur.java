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
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author JIATOU FRANCK
 */
@Entity
@Table(name = "dbx45ty_utilisateur")
@NamedQueries({
    @NamedQuery(name = "Dbx45tyUtilisateur.findAll", query = "SELECT d FROM Utilisateur d"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findById", query = "SELECT d FROM Utilisateur d WHERE d.id = :id"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByGenre", query = "SELECT d FROM Utilisateur d WHERE d.genre = :genre"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByType", query = "SELECT d FROM Utilisateur d WHERE d.type = :type"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByNom", query = "SELECT d FROM Utilisateur d WHERE d.nom = :nom"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByPrenom", query = "SELECT d FROM Utilisateur d WHERE d.prenom = :prenom"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByDateNaissance", query = "SELECT d FROM Utilisateur d WHERE d.dateNaissance = :dateNaissance"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByLieuNaissance", query = "SELECT d FROM Utilisateur d WHERE d.lieuNaissance = :lieuNaissance"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByTelephone", query = "SELECT d FROM Utilisateur d WHERE d.telephone = :telephone"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByTelephoneIso2", query = "SELECT d FROM Utilisateur d WHERE d.telephoneIso2 = :telephoneIso2"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByTelephoneDialCode", query = "SELECT d FROM Utilisateur d WHERE d.telephoneDialCode = :telephoneDialCode"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByEmail", query = "SELECT d FROM Utilisateur d WHERE d.email = :email"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByLogin", query = "SELECT d FROM Utilisateur d WHERE d.login = :login"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByMotPasse", query = "SELECT d FROM Utilisateur d WHERE d.motPasse = :motPasse"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByStatut", query = "SELECT d FROM Utilisateur d WHERE d.statut = :statut"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findBySupprime", query = "SELECT d FROM Utilisateur d WHERE d.supprime = :supprime"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByNewsletter", query = "SELECT d FROM Utilisateur d WHERE d.newsletter = :newsletter"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByDateCreation", query = "SELECT d FROM Utilisateur d WHERE d.dateCreation = :dateCreation"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByOauthProvider", query = "SELECT d FROM Utilisateur d WHERE d.oauthProvider = :oauthProvider"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByOauthUid", query = "SELECT d FROM Utilisateur d WHERE d.oauthUid = :oauthUid"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByLocalisation", query = "SELECT d FROM Utilisateur d WHERE d.localisation = :localisation"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findByActivite", query = "SELECT d FROM Utilisateur d WHERE d.activite = :activite"),
    @NamedQuery(name = "Dbx45tyUtilisateur.findBySituationMatrimoniale", query = "SELECT d FROM Utilisateur d WHERE d.situationMatrimoniale = :situationMatrimoniale")})
public class Utilisateur implements Serializable,UserDetails {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "genre")
    private String genre;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "date_naissance")
    @Temporal(TemporalType.DATE)
    private Date dateNaissance;
    @Column(name = "lieu_naissance")
    private String lieuNaissance;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "telephone_iso2")
    private String telephoneIso2;
    @Column(name = "telephone_dial_code")
    private Integer telephoneDialCode;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "login")
    private String login;
    @Column(name = "mot_passe")
    private String motPasse;
    @Basic(optional = false)
    @Column(name = "statut")
    private String statut;
    @Basic(optional = false)
    @Column(name = "supprime")
    private String supprime;
    @Basic(optional = false)
    @Column(name = "newsletter")
    private String newsletter;
    @Basic(optional = false)
    @Column(name = "date_creation")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreation;
    @Column(name = "oauth_provider")
    private String oauthProvider;
    @Column(name = "oauth_uid")
    private String oauthUid;
    @Column(name = "localisation")
    private String localisation;
    @Column(name = "activite")
    private String activite;
    @Column(name = "situation_matrimoniale")
    private String situationMatrimoniale;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilisateurId")
    private List<Dbx45tyEmploye> dbx45tyEmployeList;
    @JoinColumn(name = "langue_defaut", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Dbx45tyLangue langueDefaut;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "utilisateurId")
    private List<Dbx45tyInternaute> dbx45tyInternauteList;

    public Utilisateur() {
    }

    public Utilisateur(Integer id) {
        this.id = id;
    }

    public Utilisateur(Integer id, String type, String email, String login, String statut, String supprime, String newsletter, Date dateCreation) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.login = login;
        this.statut = statut;
        this.supprime = supprime;
        this.newsletter = newsletter;
        this.dateCreation = dateCreation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getLieuNaissance() {
        return lieuNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getTelephoneIso2() {
        return telephoneIso2;
    }

    public void setTelephoneIso2(String telephoneIso2) {
        this.telephoneIso2 = telephoneIso2;
    }

    public Integer getTelephoneDialCode() {
        return telephoneDialCode;
    }

    public void setTelephoneDialCode(Integer telephoneDialCode) {
        this.telephoneDialCode = telephoneDialCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotPasse() {
        return motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
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

    public String getNewsletter() {
        return newsletter;
    }

    public void setNewsletter(String newsletter) {
        this.newsletter = newsletter;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public String getOauthUid() {
        return oauthUid;
    }

    public void setOauthUid(String oauthUid) {
        this.oauthUid = oauthUid;
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public String getSituationMatrimoniale() {
        return situationMatrimoniale;
    }

    public void setSituationMatrimoniale(String situationMatrimoniale) {
        this.situationMatrimoniale = situationMatrimoniale;
    }

    public List<Dbx45tyEmploye> getDbx45tyEmployeList() {
        return dbx45tyEmployeList;
    }

    public void setDbx45tyEmployeList(List<Dbx45tyEmploye> dbx45tyEmployeList) {
        this.dbx45tyEmployeList = dbx45tyEmployeList;
    }

    public Dbx45tyLangue getLangueDefaut() {
        return langueDefaut;
    }

    public void setLangueDefaut(Dbx45tyLangue langueDefaut) {
        this.langueDefaut = langueDefaut;
    }

    public List<Dbx45tyInternaute> getDbx45tyInternauteList() {
        return dbx45tyInternauteList;
    }

    public void setDbx45tyInternauteList(List<Dbx45tyInternaute> dbx45tyInternauteList) {
        this.dbx45tyInternauteList = dbx45tyInternauteList;
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
        if (!(object instanceof Utilisateur)) {
            return false;
        }
        Utilisateur other = (Utilisateur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "db.biometry.biometry.entite.Dbx45tyUtilisateur[ id=" + id + " ]";
    }
    
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER,ROLE_ADMIN,ROLE_SOUSCRIPTEUR,ROLE_ADHERENT,ROLE_SERVICE_SANTE,")); // adaptez selon vos rôles
    }

    @Override
    public String getPassword() {
        return motPasse; // ← nom de votre champ password
    }

    @Override
    public String getUsername() {
        return login; // ← nom de votre champ login
    }

    // Ces 4 méthodes retournent true par défaut (compte actif)
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    
}
