/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Entity
@Data
@Table(name = "POSTE_TRAVAIL")
@NamedQueries({
    @NamedQuery(name = "PosteTravail.findAll", query = "SELECT p FROM PosteTravail p"),
    @NamedQuery(name = "PosteTravail.findByNomUtil", query = "SELECT p FROM PosteTravail p WHERE p.nomUtil = :nomUtil"),
    @NamedQuery(name = "PosteTravail.findByCodeinte", query = "SELECT p FROM PosteTravail p WHERE p.codeinte = :codeinte"),
    @NamedQuery(name = "PosteTravail.findByNombacat", query = "SELECT p FROM PosteTravail p WHERE p.nombacat = :nombacat"),
    @NamedQuery(name = "PosteTravail.findByNoefpotr", query = "SELECT p FROM PosteTravail p WHERE p.noefpotr = :noefpotr"),
    @NamedQuery(name = "PosteTravail.findByNatuutil", query = "SELECT p FROM PosteTravail p WHERE p.natuutil = :natuutil"),
    @NamedQuery(name = "PosteTravail.findByNomimpat", query = "SELECT p FROM PosteTravail p WHERE p.nomimpat = :nomimpat"),
    @NamedQuery(name = "PosteTravail.findBySuivsini", query = "SELECT p FROM PosteTravail p WHERE p.suivsini = :suivsini"),
    @NamedQuery(name = "PosteTravail.findByEntigest", query = "SELECT p FROM PosteTravail p WHERE p.entigest = :entigest"),
    @NamedQuery(name = "PosteTravail.findByAucrasva", query = "SELECT p FROM PosteTravail p WHERE p.aucrasva = :aucrasva"),
    @NamedQuery(name = "PosteTravail.findByCtrimpat", query = "SELECT p FROM PosteTravail p WHERE p.ctrimpat = :ctrimpat"),
    @NamedQuery(name = "PosteTravail.findByAczoatpr", query = "SELECT p FROM PosteTravail p WHERE p.aczoatpr = :aczoatpr"),
    @NamedQuery(name = "PosteTravail.findByAbreuser", query = "SELECT p FROM PosteTravail p WHERE p.abreuser = :abreuser"),
    @NamedQuery(name = "PosteTravail.findByForinteq", query = "SELECT p FROM PosteTravail p WHERE p.forinteq = :forinteq"),
    @NamedQuery(name = "PosteTravail.findByOusisusi", query = "SELECT p FROM PosteTravail p WHERE p.ousisusi = :ousisusi"),
    @NamedQuery(name = "PosteTravail.findByForintre", query = "SELECT p FROM PosteTravail p WHERE p.forintre = :forintre"),
    @NamedQuery(name = "PosteTravail.findByFlagfoia", query = "SELECT p FROM PosteTravail p WHERE p.flagfoia = :flagfoia"),
    @NamedQuery(name = "PosteTravail.findByFixerecu", query = "SELECT p FROM PosteTravail p WHERE p.fixerecu = :fixerecu"),
    @NamedQuery(name = "PosteTravail.findByValtecre", query = "SELECT p FROM PosteTravail p WHERE p.valtecre = :valtecre"),
    @NamedQuery(name = "PosteTravail.findByValcomre", query = "SELECT p FROM PosteTravail p WHERE p.valcomre = :valcomre"),
    @NamedQuery(name = "PosteTravail.findByFordevat", query = "SELECT p FROM PosteTravail p WHERE p.fordevat = :fordevat"),
    @NamedQuery(name = "PosteTravail.findByCodelang", query = "SELECT p FROM PosteTravail p WHERE p.codelang = :codelang"),
    @NamedQuery(name = "PosteTravail.findByAgenenca", query = "SELECT p FROM PosteTravail p WHERE p.agenenca = :agenenca"),
    @NamedQuery(name = "PosteTravail.findByCaisrece", query = "SELECT p FROM PosteTravail p WHERE p.caisrece = :caisrece"),
    @NamedQuery(name = "PosteTravail.findByCaisdepe", query = "SELECT p FROM PosteTravail p WHERE p.caisdepe = :caisdepe"),
    @NamedQuery(name = "PosteTravail.findByFlagstoc", query = "SELECT p FROM PosteTravail p WHERE p.flagstoc = :flagstoc"),
    @NamedQuery(name = "PosteTravail.findByChemword", query = "SELECT p FROM PosteTravail p WHERE p.chemword = :chemword"),
    @NamedQuery(name = "PosteTravail.findByAutocomp", query = "SELECT p FROM PosteTravail p WHERE p.autocomp = :autocomp"),
    @NamedQuery(name = "PosteTravail.findByAutaccbe", query = "SELECT p FROM PosteTravail p WHERE p.autaccbe = :autaccbe"),
    @NamedQuery(name = "PosteTravail.findBySitecomp", query = "SELECT p FROM PosteTravail p WHERE p.sitecomp = :sitecomp"),
    @NamedQuery(name = "PosteTravail.findByIntgessi", query = "SELECT p FROM PosteTravail p WHERE p.intgessi = :intgessi"),
    @NamedQuery(name = "PosteTravail.findByTauencco", query = "SELECT p FROM PosteTravail p WHERE p.tauencco = :tauencco"),
    @NamedQuery(name = "PosteTravail.findByDevarapp", query = "SELECT p FROM PosteTravail p WHERE p.devarapp = :devarapp"),
    @NamedQuery(name = "PosteTravail.findByCaischeq", query = "SELECT p FROM PosteTravail p WHERE p.caischeq = :caischeq"),
    @NamedQuery(name = "PosteTravail.findByRecutech", query = "SELECT p FROM PosteTravail p WHERE p.recutech = :recutech"),
    @NamedQuery(name = "PosteTravail.findByMailuser", query = "SELECT p FROM PosteTravail p WHERE p.mailuser = :mailuser"),
    @NamedQuery(name = "PosteTravail.findByGsmUser", query = "SELECT p FROM PosteTravail p WHERE p.gsmUser = :gsmUser"),
    @NamedQuery(name = "PosteTravail.findByCondwher", query = "SELECT p FROM PosteTravail p WHERE p.condwher = :condwher"),
    @NamedQuery(name = "PosteTravail.findByCodageba", query = "SELECT p FROM PosteTravail p WHERE p.codageba = :codageba"),
    @NamedQuery(name = "PosteTravail.findByCodbanba", query = "SELECT p FROM PosteTravail p WHERE p.codbanba = :codbanba"),
    @NamedQuery(name = "PosteTravail.findByNivvisdo", query = "SELECT p FROM PosteTravail p WHERE p.nivvisdo = :nivvisdo")})
public class PosteTravail implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NOM_UTIL")//NOM UTILISATEUR
    private String nomUtil;
    @Column(name = "CODEINTE")//CODE INTERMEDIAIRE
    private Integer codeinte;
    @Column(name = "NOMBACAT")
    private String nombacat;
    @Column(name = "NOEFPOTR")//NOM COMPLET UTILISATEUR
    private String noefpotr;
    @Column(name = "NATUUTIL")
    private String natuutil;
    @Column(name = "NOMIMPAT")
    private String nomimpat;
    @Column(name = "SUIVSINI")
    private String suivsini;
    @Column(name = "ENTIGEST")
    private Integer entigest;
    @Column(name = "AUCRASVA")
    private String aucrasva;
    @Column(name = "CTRIMPAT")
    private String ctrimpat;
    @Column(name = "ACZOATPR")
    private String aczoatpr;
    @Column(name = "ABREUSER")
    private String abreuser;
    @Column(name = "FORINTEQ")//Forcage Saisie Agence Encaissement Quittance
    private String forinteq;
    @Column(name = "OUSISUSI")
    private String ousisusi;
    @Column(name = "FORINTRE")//'si ''N'': Code Int Règlement=Code Inte Sinistre, Si ''O'':COde Int Règlement=Code Inte Poste Travail'
    private String forintre;
    @Column(name = "FLAGFOIA")//Activation Possibilité du forçage impression attestation
    private String flagfoia;
    @Column(name = "FIXERECU")//Pre-Fixe de l''incrementation des recus ou bon de paiement
    private Integer fixerecu;
    @Column(name = "VALTECRE")//Pre-Fixe de l''incrementation des recus ou bon de paiement
    private String valtecre;
    @Column(name = "VALCOMRE")
    private String valcomre;
    @Column(name = "FORDEVAT")
    private String fordevat;
    @Column(name = "CODELANG")
    private String codelang;
    @Column(name = "AGENENCA")
    private Integer agenenca;
    @Column(name = "CAISRECE")//Caisse Recette Affiché Par Défaut pour User 
    private Short caisrece;
    @Column(name = "CAISDEPE")//Caisse Dépense Affiché Par Défaut pour User 
    private Short caisdepe;
    @Column(name = "FLAGSTOC")
    private String flagstoc;
    @Column(name = "CHEMWORD")
    private String chemword;
    @Column(name = "AUTOCOMP")
    private Short autocomp;
    @Column(name = "AUTACCBE")//Accès aux zones Bordereau Emission
    private String autaccbe;
    @Column(name = "SITECOMP")//Site Comptable - Habilitation
    private Integer sitecomp;
    @Column(name = "INTGESSI")//ENTITE SINISTRE
    private Integer intgessi;//Code Intermediaire de Gestion pour le suivi et Numerotation des sinitres
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TAUENCCO")//Autorisation Taux Encaissement Minimum / Bord Commission
    private BigDecimal tauencco;
    @Column(name = "DEVARAPP")//Autorisation dévalidation Rapprochement :FC, QUITT, SIN ...
    private String devarapp;
    @Column(name = "CAISCHEQ")//Caisse Chèque Affiché Par Défaut pour User
    private Integer caischeq;
    @Column(name = "RECUTECH")//Autorisation Saisie Reçu Tech. pour Rapprochement qui ne doit pas etre comptablisé, cas ou recette est deja comptablisé
    private String recutech;
    @Column(name = "MAILUSER")
    private String mailuser;
    @Column(name = "GSM_USER")
    private BigInteger gsmUser;
    @Column(name = "CONDWHER")
    private String condwher;
    @Column(name = "CODAGEBA")//Code Agence Bancassurance Réttaché
    private Long codageba;
    @Column(name = "CODBANBA")
    private Integer codbanba;
    @Column(name = "NIVVISDO")
    private Short nivvisdo;
//    @JsonIgnore
//    @ManyToMany(mappedBy = "posteTravailList")
//    private List<ProfilUser> profilUserList;
    @JoinColumn(name = "CODPROPR", referencedColumnName = "CODEPROF")//PROFIL
     @JsonIgnore
    @ManyToOne
    private ProfilUser codpropr;//Profil Princiapl Utilisateur pour les habilitations 

    public PosteTravail() {
    }

    public PosteTravail(String nomUtil) {
        this.nomUtil = nomUtil;
    }

    
}