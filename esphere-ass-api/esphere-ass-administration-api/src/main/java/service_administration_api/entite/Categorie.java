/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "CATEGORIE")
@NamedQueries({
    @NamedQuery(name = "Categorie.findAll", query = "SELECT c FROM Categorie c"),
    @NamedQuery(name = "Categorie.findByCodecate", query = "SELECT c FROM Categorie c WHERE c.codecate = :codecate"),
    @NamedQuery(name = "Categorie.findByLibecate", query = "SELECT c FROM Categorie c WHERE c.libecate = :libecate"),
    @NamedQuery(name = "Categorie.findByMinirisq", query = "SELECT c FROM Categorie c WHERE c.minirisq = :minirisq"),
    @NamedQuery(name = "Categorie.findByMaxirisq", query = "SELECT c FROM Categorie c WHERE c.maxirisq = :maxirisq"),
    @NamedQuery(name = "Categorie.findByCodregpr", query = "SELECT c FROM Categorie c WHERE c.codregpr = :codregpr"),
    @NamedQuery(name = "Categorie.findByEvalmate", query = "SELECT c FROM Categorie c WHERE c.evalmate = :evalmate"),
    @NamedQuery(name = "Categorie.findByEvalcorp", query = "SELECT c FROM Categorie c WHERE c.evalcorp = :evalcorp"),
    @NamedQuery(name = "Categorie.findByMinigara", query = "SELECT c FROM Categorie c WHERE c.minigara = :minigara"),
    @NamedQuery(name = "Categorie.findByTitrrisq", query = "SELECT c FROM Categorie c WHERE c.titrrisq = :titrrisq"),
    @NamedQuery(name = "Categorie.findByTypecate", query = "SELECT c FROM Categorie c WHERE c.typecate = :typecate"),
    @NamedQuery(name = "Categorie.findByFlagPv", query = "SELECT c FROM Categorie c WHERE c.flagPv = :flagPv"),
    @NamedQuery(name = "Categorie.findByFlagtier", query = "SELECT c FROM Categorie c WHERE c.flagtier = :flagtier"),
    @NamedQuery(name = "Categorie.findByFlaggrou", query = "SELECT c FROM Categorie c WHERE c.flaggrou = :flaggrou"),
    @NamedQuery(name = "Categorie.findByTauxchar", query = "SELECT c FROM Categorie c WHERE c.tauxchar = :tauxchar"),
    @NamedQuery(name = "Categorie.findByFlagrese", query = "SELECT c FROM Categorie c WHERE c.flagrese = :flagrese"),
    @NamedQuery(name = "Categorie.findByAppltari", query = "SELECT c FROM Categorie c WHERE c.appltari = :appltari"),
    @NamedQuery(name = "Categorie.findByFlagctlg", query = "SELECT c FROM Categorie c WHERE c.flagctlg = :flagctlg"),
    @NamedQuery(name = "Categorie.findByTitrdeta", query = "SELECT c FROM Categorie c WHERE c.titrdeta = :titrdeta"),
    @NamedQuery(name = "Categorie.findByTauxcha1", query = "SELECT c FROM Categorie c WHERE c.tauxcha1 = :tauxcha1"),
    @NamedQuery(name = "Categorie.findByFlagevga", query = "SELECT c FROM Categorie c WHERE c.flagevga = :flagevga"),
    @NamedQuery(name = "Categorie.findByFlagflot", query = "SELECT c FROM Categorie c WHERE c.flagflot = :flagflot"),
    @NamedQuery(name = "Categorie.findByCotyavre", query = "SELECT c FROM Categorie c WHERE c.cotyavre = :cotyavre"),
    @NamedQuery(name = "Categorie.findByNumeLot", query = "SELECT c FROM Categorie c WHERE c.numeLot = :numeLot"),
    @NamedQuery(name = "Categorie.findByStocreas", query = "SELECT c FROM Categorie c WHERE c.stocreas = :stocreas"),
    @NamedQuery(name = "Categorie.findByFlagSmp", query = "SELECT c FROM Categorie c WHERE c.flagSmp = :flagSmp"),
    @NamedQuery(name = "Categorie.findByFlagPsq", query = "SELECT c FROM Categorie c WHERE c.flagPsq = :flagPsq"),
    @NamedQuery(name = "Categorie.findByFlagmocp", query = "SELECT c FROM Categorie c WHERE c.flagmocp = :flagmocp"),
    @NamedQuery(name = "Categorie.findByNometadc", query = "SELECT c FROM Categorie c WHERE c.nometadc = :nometadc"),
    @NamedQuery(name = "Categorie.findByFlagedat", query = "SELECT c FROM Categorie c WHERE c.flagedat = :flagedat"),
    @NamedQuery(name = "Categorie.findByNometaat", query = "SELECT c FROM Categorie c WHERE c.nometaat = :nometaat"),
    @NamedQuery(name = "Categorie.findByCodcomca", query = "SELECT c FROM Categorie c WHERE c.codcomca = :codcomca"),
    @NamedQuery(name = "Categorie.findByObseAn", query = "SELECT c FROM Categorie c WHERE c.obseAn = :obseAn"),
    @NamedQuery(name = "Categorie.findByModeeval", query = "SELECT c FROM Categorie c WHERE c.modeeval = :modeeval"),
    @NamedQuery(name = "Categorie.findByFlagpoca", query = "SELECT c FROM Categorie c WHERE c.flagpoca = :flagpoca"),
    @NamedQuery(name = "Categorie.findByFlagpoga", query = "SELECT c FROM Categorie c WHERE c.flagpoga = :flagpoga"),
    @NamedQuery(name = "Categorie.findByFlagpoco", query = "SELECT c FROM Categorie c WHERE c.flagpoco = :flagpoco"),
    @NamedQuery(name = "Categorie.findByTitdetgf", query = "SELECT c FROM Categorie c WHERE c.titdetgf = :titdetgf"),
    @NamedQuery(name = "Categorie.findByCodcatat", query = "SELECT c FROM Categorie c WHERE c.codcatat = :codcatat"),
    @NamedQuery(name = "Categorie.findByTypavepa", query = "SELECT c FROM Categorie c WHERE c.typavepa = :typavepa"),
    @NamedQuery(name = "Categorie.findByFlagrecou", query = "SELECT c FROM Categorie c WHERE c.flagrecou = :flagrecou"),
    @NamedQuery(name = "Categorie.findByTitrgrou", query = "SELECT c FROM Categorie c WHERE c.titrgrou = :titrgrou"),
    @NamedQuery(name = "Categorie.findByCtruniri", query = "SELECT c FROM Categorie c WHERE c.ctruniri = :ctruniri"),
    @NamedQuery(name = "Categorie.findByFlagrent", query = "SELECT c FROM Categorie c WHERE c.flagrent = :flagrent"),
    @NamedQuery(name = "Categorie.findByTypecont", query = "SELECT c FROM Categorie c WHERE c.typecont = :typecont"),
    @NamedQuery(name = "Categorie.findByFlagsimu", query = "SELECT c FROM Categorie c WHERE c.flagsimu = :flagsimu"),
    @NamedQuery(name = "Categorie.findBySeuilSp", query = "SELECT c FROM Categorie c WHERE c.seuilSp = :seuilSp"),
    @NamedQuery(name = "Categorie.findByFlgprnet", query = "SELECT c FROM Categorie c WHERE c.flgprnet = :flgprnet"),
    @NamedQuery(name = "Categorie.findByFlagVers", query = "SELECT c FROM Categorie c WHERE c.flagVers = :flagVers"),
    @NamedQuery(name = "Categorie.findByFlagexon", query = "SELECT c FROM Categorie c WHERE c.flagexon = :flagexon"),
    @NamedQuery(name = "Categorie.findByMiniSp", query = "SELECT c FROM Categorie c WHERE c.miniSp = :miniSp"),
    @NamedQuery(name = "Categorie.findByDurelimi", query = "SELECT c FROM Categorie c WHERE c.durelimi = :durelimi"),
    @NamedQuery(name = "Categorie.findByFlgenqui", query = "SELECT c FROM Categorie c WHERE c.flgenqui = :flgenqui"),
    @NamedQuery(name = "Categorie.findByAgelims1", query = "SELECT c FROM Categorie c WHERE c.agelims1 = :agelims1"),
    @NamedQuery(name = "Categorie.findByFlagcomp", query = "SELECT c FROM Categorie c WHERE c.flagcomp = :flagcomp"),
    @NamedQuery(name = "Categorie.findByAgelims2", query = "SELECT c FROM Categorie c WHERE c.agelims2 = :agelims2"),
    @NamedQuery(name = "Categorie.findByLibaytdr", query = "SELECT c FROM Categorie c WHERE c.libaytdr = :libaytdr"),
    @NamedQuery(name = "Categorie.findByApptarsi", query = "SELECT c FROM Categorie c WHERE c.apptarsi = :apptarsi"),
    @NamedQuery(name = "Categorie.findByFlrepcom", query = "SELECT c FROM Categorie c WHERE c.flrepcom = :flrepcom"),
    @NamedQuery(name = "Categorie.findByFlamopava", query = "SELECT c FROM Categorie c WHERE c.flamopava = :flamopava"),
    @NamedQuery(name = "Categorie.findByOusisusi", query = "SELECT c FROM Categorie c WHERE c.ousisusi = :ousisusi"),
    @NamedQuery(name = "Categorie.findByCatepool", query = "SELECT c FROM Categorie c WHERE c.catepool = :catepool"),
    @NamedQuery(name = "Categorie.findByFlagcati", query = "SELECT c FROM Categorie c WHERE c.flagcati = :flagcati"),
    @NamedQuery(name = "Categorie.findByFlaglirm", query = "SELECT c FROM Categorie c WHERE c.flaglirm = :flaglirm"),
    @NamedQuery(name = "Categorie.findByModdurpl", query = "SELECT c FROM Categorie c WHERE c.moddurpl = :moddurpl"),
    @NamedQuery(name = "Categorie.findByNaopcaca", query = "SELECT c FROM Categorie c WHERE c.naopcaca = :naopcaca"),
    @NamedQuery(name = "Categorie.findByNaopcalc", query = "SELECT c FROM Categorie c WHERE c.naopcalc = :naopcalc"),
    @NamedQuery(name = "Categorie.findByBacalsmp", query = "SELECT c FROM Categorie c WHERE c.bacalsmp = :bacalsmp"),
    @NamedQuery(name = "Categorie.findByFlagvade", query = "SELECT c FROM Categorie c WHERE c.flagvade = :flagvade"),
    @NamedQuery(name = "Categorie.findByTauxcert", query = "SELECT c FROM Categorie c WHERE c.tauxcert = :tauxcert"),
    @NamedQuery(name = "Categorie.findByTeriobse", query = "SELECT c FROM Categorie c WHERE c.teriobse = :teriobse"),
    @NamedQuery(name = "Categorie.findByFlagsust", query = "SELECT c FROM Categorie c WHERE c.flagsust = :flagsust"),
    @NamedQuery(name = "Categorie.findByAgelima1", query = "SELECT c FROM Categorie c WHERE c.agelima1 = :agelima1"),
    @NamedQuery(name = "Categorie.findByAgelima2", query = "SELECT c FROM Categorie c WHERE c.agelima2 = :agelima2"),
    @NamedQuery(name = "Categorie.findByAsscalbm", query = "SELECT c FROM Categorie c WHERE c.asscalbm = :asscalbm"),
    @NamedQuery(name = "Categorie.findByAsscalrm", query = "SELECT c FROM Categorie c WHERE c.asscalrm = :asscalrm"),
    @NamedQuery(name = "Categorie.findByAsscalrf", query = "SELECT c FROM Categorie c WHERE c.asscalrf = :asscalrf"),
    @NamedQuery(name = "Categorie.findByFlarecsi", query = "SELECT c FROM Categorie c WHERE c.flarecsi = :flarecsi"),
    @NamedQuery(name = "Categorie.findByFlaintsi", query = "SELECT c FROM Categorie c WHERE c.flaintsi = :flaintsi"),
    @NamedQuery(name = "Categorie.findByTaupapoo", query = "SELECT c FROM Categorie c WHERE c.taupapoo = :taupapoo"),
    @NamedQuery(name = "Categorie.findByTauappoo", query = "SELECT c FROM Categorie c WHERE c.tauappoo = :tauappoo"),
    @NamedQuery(name = "Categorie.findByFlginire", query = "SELECT c FROM Categorie c WHERE c.flginire = :flginire"),
    @NamedQuery(name = "Categorie.findByTafrgepo", query = "SELECT c FROM Categorie c WHERE c.tafrgepo = :tafrgepo"),
    @NamedQuery(name = "Categorie.findByValtecma", query = "SELECT c FROM Categorie c WHERE c.valtecma = :valtecma"),
    @NamedQuery(name = "Categorie.findByDelaenca", query = "SELECT c FROM Categorie c WHERE c.delaenca = :delaenca"),
    @NamedQuery(name = "Categorie.findByMaxprden", query = "SELECT c FROM Categorie c WHERE c.maxprden = :maxprden"),
    @NamedQuery(name = "Categorie.findByModepror", query = "SELECT c FROM Categorie c WHERE c.modepror = :modepror"),
    @NamedQuery(name = "Categorie.findByFlginiap", query = "SELECT c FROM Categorie c WHERE c.flginiap = :flginiap")})
public class Categorie implements Serializable {

    @OneToMany(mappedBy = "codecate")
    private List<Police_1> policeList;



    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "CODECATE")
    private Integer codecate;
    @Column(name = "LIBECATE")
    private String libecate;
    @Column(name = "MINIRISQ")
    private Integer minirisq;
    @Column(name = "MAXIRISQ")
    private Integer maxirisq;
    @Column(name = "CODREGPR")
    private String codregpr;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "EVALMATE")
    private BigDecimal evalmate;
    @Column(name = "EVALCORP")
    private BigDecimal evalcorp;
    @Column(name = "MINIGARA")
    private Short minigara;
    @Column(name = "TITRRISQ")
    private String titrrisq;
    @Column(name = "TYPECATE")
    private String typecate;
    @Column(name = "FLAG__PV")
    private String flagPv;
    @Column(name = "FLAGTIER")
    private String flagtier;
    @Column(name = "FLAGGROU")
    private String flaggrou;
    @Column(name = "TAUXCHAR")
    private BigDecimal tauxchar;
    @Column(name = "FLAGRESE")
    private String flagrese;
    @Column(name = "APPLTARI")
    private String appltari;
    @Column(name = "FLAGCTLG")
    private String flagctlg;
    @Column(name = "TITRDETA")
    private String titrdeta;
    @Column(name = "TAUXCHA1")
    private BigDecimal tauxcha1;
    @Column(name = "FLAGEVGA")
    private String flagevga;
    @Basic(optional = false)
    @Column(name = "FLAGFLOT")
    private String flagflot;
    @Column(name = "COTYAVRE")
    private Short cotyavre;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Column(name = "STOCREAS")
    private String stocreas;
    @Column(name = "FLAG_SMP")
    private String flagSmp;
    @Column(name = "FLAG_PSQ")
    private String flagPsq;
    @Column(name = "FLAGMOCP")
    private String flagmocp;
    @Column(name = "NOMETADC")
    private String nometadc;
    @Column(name = "FLAGEDAT")
    private String flagedat;
    @Column(name = "NOMETAAT")
    private String nometaat;
    @Column(name = "CODCOMCA")
    private Integer codcomca;
    @Column(name = "OBSE__AN", columnDefinition = "VARCHAR2(4000)")
    private String obseAn;
    @Column(name = "MODEEVAL")
    private String modeeval;
    @Column(name = "FLAGPOCA")
    private String flagpoca;
    @Column(name = "FLAGPOGA")
    private String flagpoga;
    @Column(name = "FLAGPOCO")
    private String flagpoco;
    @Column(name = "TITDETGF")
    private String titdetgf;
    @Column(name = "CODCATAT")
    private String codcatat;
    @Column(name = "TYPAVEPA")
    private Short typavepa;
    @Column(name = "FLAGRECOU")
    private String flagrecou;
    @Column(name = "TITRGROU")
    private String titrgrou;
    @Column(name = "CTRUNIRI")
    private String ctruniri;
    @Column(name = "FLAGRENT")
    private String flagrent;
    @Column(name = "TYPECONT")
    private String typecont;
    @Column(name = "FLAGSIMU")
    private String flagsimu;
    @Column(name = "SEUIL_SP")
    private BigDecimal seuilSp;
    @Column(name = "FLGPRNET")
    private String flgprnet;
    @Column(name = "FLAG_VERS")
    private String flagVers;
    @Column(name = "FLAGEXON")
    private String flagexon;
    @Column(name = "MINI__SP")
    private BigDecimal miniSp;
    @Column(name = "DURELIMI")
    private Short durelimi;
    @Column(name = "FLGENQUI")
    private String flgenqui;
    @Column(name = "AGELIMS1")
    private Short agelims1;
    @Column(name = "FLAGCOMP")
    private String flagcomp;
    @Column(name = "AGELIMS2")
    private Short agelims2;
    @Column(name = "LIBAYTDR")
    private String libaytdr;
    @Column(name = "APPTARSI")
    private String apptarsi;
    @Column(name = "FLREPCOM")
    private String flrepcom;
    @Column(name = "FLAMOPAVA")
    private String flamopava;
    @Column(name = "OUSISUSI")
    private String ousisusi;
    @Column(name = "CATEPOOL")
    private String catepool;
    @Column(name = "FLAGCATI")
    private String flagcati;
    @Column(name = "FLAGLIRM")
    private String flaglirm;
    @Column(name = "MODDURPL")
    private String moddurpl;
    @Column(name = "NAOPCACA")
    private String naopcaca;
    @Column(name = "NAOPCALC")
    private String naopcalc;
    @Column(name = "BACALSMP")
    private String bacalsmp;
    @Column(name = "FLAGVADE")
    private String flagvade;
    @Column(name = "TAUXCERT")
    private BigDecimal tauxcert;
    @Column(name = "TERIOBSE")
    private String teriobse;
    @Column(name = "FLAGSUST")
    private String flagsust;
    @Column(name = "AGELIMA1")
    private Integer agelima1;
    @Column(name = "AGELIMA2")
    private Integer agelima2;
    @Column(name = "ASSCALBM")
    private String asscalbm;
    @Column(name = "ASSCALRM")
    private String asscalrm;
    @Column(name = "ASSCALRF")
    private String asscalrf;
    @Column(name = "FLARECSI")
    private String flarecsi;
    @Column(name = "FLAINTSI")
    private String flaintsi;
    @Column(name = "TAUPAPOO")
    private BigDecimal taupapoo;
    @Column(name = "TAUAPPOO")
    private BigDecimal tauappoo;
    @Column(name = "FLGINIRE")
    private String flginire;
    @Column(name = "TAFRGEPO")
    private BigDecimal tafrgepo;
    @Column(name = "VALTECMA")
    private String valtecma;
    @Column(name = "DELAENCA")
    private Short delaenca;
    @Column(name = "MAXPRDEN")
    private BigDecimal maxprden;
    @Column(name = "MODEPROR")
    private String modepror;
    @Column(name = "FLGINIAP")
    private String flginiap;
    @JoinColumn(name = "CODEBRAN", referencedColumnName = "CODEBRAN")
    @ManyToOne
    private Branche codebran;
    @JoinColumn(name = "CODREGSI", referencedColumnName = "CODREGSI")
    @ManyToOne
    private RegistreSinistre codregsi;
    @JoinColumn(name = "COTYAVTE", referencedColumnName = "CODTYPAV")
    @ManyToOne
    private TypeAvenant cotyavte;

    public Categorie() {
    }

    public Categorie(Integer codecate) {
        this.codecate = codecate;
    }

    public Categorie(Integer codecate, String flagflot) {
        this.codecate = codecate;
        this.flagflot = flagflot;
    }

    public Integer getCodecate() {
        return codecate;
    }

    public void setCodecate(Integer codecate) {
        this.codecate = codecate;
    }

    public String getLibecate() {
        return libecate;
    }

    public void setLibecate(String libecate) {
        this.libecate = libecate;
    }

    public Integer getMinirisq() {
        return minirisq;
    }

    public void setMinirisq(Integer minirisq) {
        this.minirisq = minirisq;
    }

    public Integer getMaxirisq() {
        return maxirisq;
    }

    public void setMaxirisq(Integer maxirisq) {
        this.maxirisq = maxirisq;
    }

    public String getCodregpr() {
        return codregpr;
    }

    public void setCodregpr(String codregpr) {
        this.codregpr = codregpr;
    }

    public BigDecimal getEvalmate() {
        return evalmate;
    }

    public void setEvalmate(BigDecimal evalmate) {
        this.evalmate = evalmate;
    }

    public BigDecimal getEvalcorp() {
        return evalcorp;
    }

    public void setEvalcorp(BigDecimal evalcorp) {
        this.evalcorp = evalcorp;
    }

    public Short getMinigara() {
        return minigara;
    }

    public void setMinigara(Short minigara) {
        this.minigara = minigara;
    }

    public String getTitrrisq() {
        return titrrisq;
    }

    public void setTitrrisq(String titrrisq) {
        this.titrrisq = titrrisq;
    }

    public String getTypecate() {
        return typecate;
    }

    public void setTypecate(String typecate) {
        this.typecate = typecate;
    }

    public String getFlagPv() {
        return flagPv;
    }

    public void setFlagPv(String flagPv) {
        this.flagPv = flagPv;
    }

    public String getFlagtier() {
        return flagtier;
    }

    public void setFlagtier(String flagtier) {
        this.flagtier = flagtier;
    }

    public String getFlaggrou() {
        return flaggrou;
    }

    public void setFlaggrou(String flaggrou) {
        this.flaggrou = flaggrou;
    }

    public BigDecimal getTauxchar() {
        return tauxchar;
    }

    public void setTauxchar(BigDecimal tauxchar) {
        this.tauxchar = tauxchar;
    }

    public String getFlagrese() {
        return flagrese;
    }

    public void setFlagrese(String flagrese) {
        this.flagrese = flagrese;
    }

    public String getAppltari() {
        return appltari;
    }

    public void setAppltari(String appltari) {
        this.appltari = appltari;
    }

    public String getFlagctlg() {
        return flagctlg;
    }

    public void setFlagctlg(String flagctlg) {
        this.flagctlg = flagctlg;
    }

    public String getTitrdeta() {
        return titrdeta;
    }

    public void setTitrdeta(String titrdeta) {
        this.titrdeta = titrdeta;
    }

    public BigDecimal getTauxcha1() {
        return tauxcha1;
    }

    public void setTauxcha1(BigDecimal tauxcha1) {
        this.tauxcha1 = tauxcha1;
    }

    public String getFlagevga() {
        return flagevga;
    }

    public void setFlagevga(String flagevga) {
        this.flagevga = flagevga;
    }

    public String getFlagflot() {
        return flagflot;
    }

    public void setFlagflot(String flagflot) {
        this.flagflot = flagflot;
    }

    public Short getCotyavre() {
        return cotyavre;
    }

    public void setCotyavre(Short cotyavre) {
        this.cotyavre = cotyavre;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getStocreas() {
        return stocreas;
    }

    public void setStocreas(String stocreas) {
        this.stocreas = stocreas;
    }

    public String getFlagSmp() {
        return flagSmp;
    }

    public void setFlagSmp(String flagSmp) {
        this.flagSmp = flagSmp;
    }

    public String getFlagPsq() {
        return flagPsq;
    }

    public void setFlagPsq(String flagPsq) {
        this.flagPsq = flagPsq;
    }

    public String getFlagmocp() {
        return flagmocp;
    }

    public void setFlagmocp(String flagmocp) {
        this.flagmocp = flagmocp;
    }

    public String getNometadc() {
        return nometadc;
    }

    public void setNometadc(String nometadc) {
        this.nometadc = nometadc;
    }

    public String getFlagedat() {
        return flagedat;
    }

    public void setFlagedat(String flagedat) {
        this.flagedat = flagedat;
    }

    public String getNometaat() {
        return nometaat;
    }

    public void setNometaat(String nometaat) {
        this.nometaat = nometaat;
    }

    public Integer getCodcomca() {
        return codcomca;
    }

    public void setCodcomca(Integer codcomca) {
        this.codcomca = codcomca;
    }

    public String getObseAn() {
        return obseAn;
    }

    public void setObseAn(String obseAn) {
        this.obseAn = obseAn;
    }

    public String getModeeval() {
        return modeeval;
    }

    public void setModeeval(String modeeval) {
        this.modeeval = modeeval;
    }

    public String getFlagpoca() {
        return flagpoca;
    }

    public void setFlagpoca(String flagpoca) {
        this.flagpoca = flagpoca;
    }

    public String getFlagpoga() {
        return flagpoga;
    }

    public void setFlagpoga(String flagpoga) {
        this.flagpoga = flagpoga;
    }

    public String getFlagpoco() {
        return flagpoco;
    }

    public void setFlagpoco(String flagpoco) {
        this.flagpoco = flagpoco;
    }

    public String getTitdetgf() {
        return titdetgf;
    }

    public void setTitdetgf(String titdetgf) {
        this.titdetgf = titdetgf;
    }

    public String getCodcatat() {
        return codcatat;
    }

    public void setCodcatat(String codcatat) {
        this.codcatat = codcatat;
    }

    public Short getTypavepa() {
        return typavepa;
    }

    public void setTypavepa(Short typavepa) {
        this.typavepa = typavepa;
    }

    public String getFlagrecou() {
        return flagrecou;
    }

    public void setFlagrecou(String flagrecou) {
        this.flagrecou = flagrecou;
    }

    public String getTitrgrou() {
        return titrgrou;
    }

    public void setTitrgrou(String titrgrou) {
        this.titrgrou = titrgrou;
    }

    public String getCtruniri() {
        return ctruniri;
    }

    public void setCtruniri(String ctruniri) {
        this.ctruniri = ctruniri;
    }

    public String getFlagrent() {
        return flagrent;
    }

    public void setFlagrent(String flagrent) {
        this.flagrent = flagrent;
    }

    public String getTypecont() {
        return typecont;
    }

    public void setTypecont(String typecont) {
        this.typecont = typecont;
    }

    public String getFlagsimu() {
        return flagsimu;
    }

    public void setFlagsimu(String flagsimu) {
        this.flagsimu = flagsimu;
    }

    public BigDecimal getSeuilSp() {
        return seuilSp;
    }

    public void setSeuilSp(BigDecimal seuilSp) {
        this.seuilSp = seuilSp;
    }

    public String getFlgprnet() {
        return flgprnet;
    }

    public void setFlgprnet(String flgprnet) {
        this.flgprnet = flgprnet;
    }

    public String getFlagVers() {
        return flagVers;
    }

    public void setFlagVers(String flagVers) {
        this.flagVers = flagVers;
    }

    public String getFlagexon() {
        return flagexon;
    }

    public void setFlagexon(String flagexon) {
        this.flagexon = flagexon;
    }

    public BigDecimal getMiniSp() {
        return miniSp;
    }

    public void setMiniSp(BigDecimal miniSp) {
        this.miniSp = miniSp;
    }

    public Short getDurelimi() {
        return durelimi;
    }

    public void setDurelimi(Short durelimi) {
        this.durelimi = durelimi;
    }

    public String getFlgenqui() {
        return flgenqui;
    }

    public void setFlgenqui(String flgenqui) {
        this.flgenqui = flgenqui;
    }

    public Short getAgelims1() {
        return agelims1;
    }

    public void setAgelims1(Short agelims1) {
        this.agelims1 = agelims1;
    }

    public String getFlagcomp() {
        return flagcomp;
    }

    public void setFlagcomp(String flagcomp) {
        this.flagcomp = flagcomp;
    }

    public Short getAgelims2() {
        return agelims2;
    }

    public void setAgelims2(Short agelims2) {
        this.agelims2 = agelims2;
    }

    public String getLibaytdr() {
        return libaytdr;
    }

    public void setLibaytdr(String libaytdr) {
        this.libaytdr = libaytdr;
    }

    public String getApptarsi() {
        return apptarsi;
    }

    public void setApptarsi(String apptarsi) {
        this.apptarsi = apptarsi;
    }

    public String getFlrepcom() {
        return flrepcom;
    }

    public void setFlrepcom(String flrepcom) {
        this.flrepcom = flrepcom;
    }

    public String getFlamopava() {
        return flamopava;
    }

    public void setFlamopava(String flamopava) {
        this.flamopava = flamopava;
    }

    public String getOusisusi() {
        return ousisusi;
    }

    public void setOusisusi(String ousisusi) {
        this.ousisusi = ousisusi;
    }

    public String getCatepool() {
        return catepool;
    }

    public void setCatepool(String catepool) {
        this.catepool = catepool;
    }

    public String getFlagcati() {
        return flagcati;
    }

    public void setFlagcati(String flagcati) {
        this.flagcati = flagcati;
    }

    public String getFlaglirm() {
        return flaglirm;
    }

    public void setFlaglirm(String flaglirm) {
        this.flaglirm = flaglirm;
    }

    public String getModdurpl() {
        return moddurpl;
    }

    public void setModdurpl(String moddurpl) {
        this.moddurpl = moddurpl;
    }

    public String getNaopcaca() {
        return naopcaca;
    }

    public void setNaopcaca(String naopcaca) {
        this.naopcaca = naopcaca;
    }

    public String getNaopcalc() {
        return naopcalc;
    }

    public void setNaopcalc(String naopcalc) {
        this.naopcalc = naopcalc;
    }

    public String getBacalsmp() {
        return bacalsmp;
    }

    public void setBacalsmp(String bacalsmp) {
        this.bacalsmp = bacalsmp;
    }

    public String getFlagvade() {
        return flagvade;
    }

    public void setFlagvade(String flagvade) {
        this.flagvade = flagvade;
    }

    public BigDecimal getTauxcert() {
        return tauxcert;
    }

    public void setTauxcert(BigDecimal tauxcert) {
        this.tauxcert = tauxcert;
    }

    public String getTeriobse() {
        return teriobse;
    }

    public void setTeriobse(String teriobse) {
        this.teriobse = teriobse;
    }

    public String getFlagsust() {
        return flagsust;
    }

    public void setFlagsust(String flagsust) {
        this.flagsust = flagsust;
    }

    public Integer getAgelima1() {
        return agelima1;
    }

    public void setAgelima1(Integer agelima1) {
        this.agelima1 = agelima1;
    }

    public Integer getAgelima2() {
        return agelima2;
    }

    public void setAgelima2(Integer agelima2) {
        this.agelima2 = agelima2;
    }

    public String getAsscalbm() {
        return asscalbm;
    }

    public void setAsscalbm(String asscalbm) {
        this.asscalbm = asscalbm;
    }

    public String getAsscalrm() {
        return asscalrm;
    }

    public void setAsscalrm(String asscalrm) {
        this.asscalrm = asscalrm;
    }

    public String getAsscalrf() {
        return asscalrf;
    }

    public void setAsscalrf(String asscalrf) {
        this.asscalrf = asscalrf;
    }

    public String getFlarecsi() {
        return flarecsi;
    }

    public void setFlarecsi(String flarecsi) {
        this.flarecsi = flarecsi;
    }

    public String getFlaintsi() {
        return flaintsi;
    }

    public void setFlaintsi(String flaintsi) {
        this.flaintsi = flaintsi;
    }

    public BigDecimal getTaupapoo() {
        return taupapoo;
    }

    public void setTaupapoo(BigDecimal taupapoo) {
        this.taupapoo = taupapoo;
    }

    public BigDecimal getTauappoo() {
        return tauappoo;
    }

    public void setTauappoo(BigDecimal tauappoo) {
        this.tauappoo = tauappoo;
    }

    public String getFlginire() {
        return flginire;
    }

    public void setFlginire(String flginire) {
        this.flginire = flginire;
    }

    public BigDecimal getTafrgepo() {
        return tafrgepo;
    }

    public void setTafrgepo(BigDecimal tafrgepo) {
        this.tafrgepo = tafrgepo;
    }

    public String getValtecma() {
        return valtecma;
    }

    public void setValtecma(String valtecma) {
        this.valtecma = valtecma;
    }

    public Short getDelaenca() {
        return delaenca;
    }

    public void setDelaenca(Short delaenca) {
        this.delaenca = delaenca;
    }

    public BigDecimal getMaxprden() {
        return maxprden;
    }

    public void setMaxprden(BigDecimal maxprden) {
        this.maxprden = maxprden;
    }

    public String getModepror() {
        return modepror;
    }

    public void setModepror(String modepror) {
        this.modepror = modepror;
    }

    public String getFlginiap() {
        return flginiap;
    }

    public void setFlginiap(String flginiap) {
        this.flginiap = flginiap;
    }

    public Branche getCodebran() {
        return codebran;
    }

    public void setCodebran(Branche codebran) {
        this.codebran = codebran;
    }

    public RegistreSinistre getCodregsi() {
        return codregsi;
    }

    public void setCodregsi(RegistreSinistre codregsi) {
        this.codregsi = codregsi;
    }

    public TypeAvenant getCotyavte() {
        return cotyavte;
    }

    public void setCotyavte(TypeAvenant cotyavte) {
        this.cotyavte = cotyavte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codecate != null ? codecate.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Categorie)) {
            return false;
        }
        Categorie other = (Categorie) object;
        if ((this.codecate == null && other.codecate != null) || (this.codecate != null && !this.codecate.equals(other.codecate))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Categorie[ codecate=" + codecate + " ]";
    }

    public List<Police_1> getPoliceList() {
        return policeList;
    }

    public void setPoliceList(List<Police_1> policeList) {
        this.policeList = policeList;
    }

  
 

}
