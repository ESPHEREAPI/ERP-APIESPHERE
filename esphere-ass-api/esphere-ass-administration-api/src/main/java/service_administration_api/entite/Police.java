/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "POLICE")
@NamedQueries({
    @NamedQuery(name = "Police.findAll", query = "SELECT p FROM Police p")})
public class Police implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PolicePK policePK;
    @Column(name = "AVENMODI")
    private Short avenmodi;
    @Column(name = "DATEEFFE")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date dateeffe;
     private LocalDate dateeffe;
    @Column(name = "DATEECHE")
   // @Temporal(TemporalType.TIMESTAMP)
  //  private Date dateeche;
    private LocalDate dateeche;
    @Column(name = "DATEANNI")
    //@Temporal(TemporalType.TIMESTAMP)
    //private Date dateanni;
    private LocalDate dateanni;
    @Size(max = 1)
    @Column(name = "TYPECONT")
    private String typecont;
    @Size(max = 30)
    @Column(name = "REFEINTE")
    private String refeinte;
    @Size(max = 4)
    @Column(name = "CODTYPTA")
    private String codtypta;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TAUXREDU")
    private BigDecimal tauxredu;
    @Size(max = 1)
    @Column(name = "FLAGREDU")
    private String flagredu;
    @Column(name = "CODINTGE")
    private Integer codintge;
    @Column(name = "NOMTIMDI")
    private Integer nomtimdi;
    @Column(name = "BONUMALU")
    private BigDecimal bonumalu;
    @Column(name = "TAUCOMAP")
    private BigDecimal taucomap;
    @Column(name = "TAUCOMGE")
    private BigDecimal taucomge;
    @Column(name = "DATEVALI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datevali;
    @Column(name = "DATEANNU")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateannu;
    @Column(name = "DATETERM")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateterm;
    @Size(max = 600)
    @Column(name = "OBSESOUS")
    private String obsesous;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Size(max = 1)
    @Column(name = "FLAGANNU")
    private String flagannu;
    @Size(max = 200)
    @Column(name = "OBSEPOLI")
    private String obsepoli;
    @Column(name = "DATESOUS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datesous;
    @Column(name = "DATESAIS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datesais;
    @Column(name = "MONT__CA")
    private BigInteger montCa;
    @Size(max = 1)
    @Column(name = "FLAGMAJO")
    private String flagmajo;
    @Size(max = 30)
    @Column(name = "RAISSOUS")
    private String raissous;
    @Size(max = 40)
    @Column(name = "ADRESOUS")
    private String adresous;
    @Size(max = 30)
    @Column(name = "NOMUTICR")
    private String nomuticr;
    @Size(max = 30)
    @Column(name = "NOMUTIAN")
    private String nomutian;
    @Size(max = 1)
    @Column(name = "GENRCONT")
    private String genrcont;
    @Size(max = 30)
    @Column(name = "MODI_PAR")
    private String modiPar;
    @Column(name = "MODI__LE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modiLe;
    @Column(name = "MONACCPO")
    private BigInteger monaccpo;
    @Column(name = "MONACCRI")
    private BigInteger monaccri;
    @Size(max = 1)
    @Column(name = "FLAGPOFI")
    private String flagpofi;
    @Column(name = "VALEDURE")
    private BigDecimal valedure;
    @Size(max = 1)
    @Column(name = "UNITDURE")
    private String unitdure;
    @Size(max = 30)
    @Column(name = "VALI_PAR")
    private String valiPar;
    @Size(max = 30)
    @Column(name = "DEVALPAR")
    private String devalpar;
    @Column(name = "DEVALILE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date devalile;
    @Size(max = 30)
    @Column(name = "REFBOREM")
    private String refborem;
    @Size(max = 20)
    @Column(name = "REFCONPO")
    private String refconpo;
    @Column(name = "TAUXPROR")
    private BigInteger tauxpror;
    @Column(name = "VALTIMDI")
    private BigDecimal valtimdi;
    @Size(max = 4)
    @Column(name = "FLAGBLOQ")
    private String flagbloq;
    @Size(max = 2)
    @Column(name = "NATUCONT")
    private String natucont;
    @Column(name = "COMAPPAC")
    private BigDecimal comappac;
    @Column(name = "COMGESAC")
    private BigDecimal comgesac;
    @Column(name = "TIMBGRAD")
    private BigInteger timbgrad;
    @Size(max = 1)
    @Column(name = "MOACSOPO")
    private String moacsopo;
    @Size(max = 100)
    @Column(name = "TITRPOLI")
    private String titrpoli;
    @Size(max = 3)
    @Column(name = "CODREDFL")
    private String codredfl;
    @Size(max = 4)
    @Column(name = "FORMCOUV")
    private String formcouv;
    @Column(name = "TAUXCHAN")
    private BigDecimal tauxchan;
    @Size(max = 1)
    @Column(name = "APPLFOCO")
    private String applfoco;
    @Column(name = "DELAENCA")
    private BigInteger delaenca;
    @Column(name = "MONACCSU")
    private Long monaccsu;
    @Size(max = 8)
    @Column(name = "CODSOUCO")
    private String codsouco;
    @Column(name = "COINSOCO")
    private Integer coinsoco;
    @Column(name = "DUREPAIE")
    private BigDecimal durepaie;
    @Size(max = 1)
    @Column(name = "UNIDURPA")
    private String unidurpa;
    @Column(name = "DAT1ECHE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dat1eche;
    @Size(max = 30)
    @Column(name = "CODEPOLI")
    private String codepoli;
    @Column(name = "DATVALBA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datvalba;
    @Size(max = 60)
    @Column(name = "VALPARBA")
    private String valparba;
    @Column(name = "IDENPOLI")
    private BigInteger idenpoli;
    @Column(name = "CODBANBA")
    private Integer codbanba;
    @Column(name = "CODAGEBA")
    private Long codageba;
    @JoinColumn(name = "CODEASSU", referencedColumnName = "CODEASSU")
    @ManyToOne
    private Assure codeassu;
    @JoinColumn(name = "CODEAPPO", referencedColumnName = "CODEAPPO")
    @ManyToOne
    private Apporteur codeappo;
    @JoinColumn(name = "CODEDEVI", referencedColumnName = "CODEDEVI")
    @ManyToOne
    private AsvCodeDevise codedevi;
    @JoinColumn(name = "CODECATE", referencedColumnName = "CODECATE")
    @ManyToOne
    private Categorie codecate;
    @JoinColumn(name = "CODECONV", referencedColumnName = "CODECONV")
    @ManyToOne
    private Convention codeconv;
    @JoinColumn(name = "CODEDURE", referencedColumnName = "CODEDURE")
    @ManyToOne
    private Duree codedure;
    @JoinColumn(name = "FLAEXOTG", referencedColumnName = "FLAEXOTG")
    @ManyToOne
    private Exoneration flaexotg;
    @JoinColumn(name = "CODMOTAN", referencedColumnName = "CODMOTAN")
    @ManyToOne
    private MotifsAnnulation codmotan;
    @JoinColumn(name = "CODEPERI", referencedColumnName = "CODEPERI")
    @ManyToOne
    private PeriodicitePrime codeperi;
    @JoinColumn(name = "CODEREDU", referencedColumnName = "CODEREDU")
    @ManyToOne
    private ReductionAutomobile coderedu;
    @JoinColumn(name = "CODTIMDI", referencedColumnName = "CODTIMDI")
    @ManyToOne
    private TimbreDimension codtimdi;
    @JoinColumn(name = "CODREGBM", referencedColumnName = "CODREGBM")
    @ManyToOne
    private RegimeBonusMalus codregbm;

    public Police() {
    }

    public Police(PolicePK policePK) {
        this.policePK = policePK;
    }

    public Police(int codeinte, long numepoli) {
        this.policePK = new PolicePK(codeinte, numepoli);
    }

    public PolicePK getPolicePK() {
        return policePK;
    }

    public void setPolicePK(PolicePK policePK) {
        this.policePK = policePK;
    }

    public Short getAvenmodi() {
        return avenmodi;
    }

    public void setAvenmodi(Short avenmodi) {
        this.avenmodi = avenmodi;
    }

    public LocalDate getDateeffe() {
        return dateeffe;
    }

    public void setDateeffe(LocalDate dateeffe) {
        this.dateeffe = dateeffe;
    }

    public LocalDate getDateeche() {
        return dateeche;
    }

    public void setDateeche(LocalDate dateeche) {
        this.dateeche = dateeche;
    }

    public LocalDate getDateanni() {
        return dateanni;
    }

    public void setDateanni(LocalDate dateanni) {
        this.dateanni = dateanni;
    }

    
    public String getTypecont() {
        return typecont;
    }

    public void setTypecont(String typecont) {
        this.typecont = typecont;
    }

    public String getRefeinte() {
        return refeinte;
    }

    public void setRefeinte(String refeinte) {
        this.refeinte = refeinte;
    }

    public String getCodtypta() {
        return codtypta;
    }

    public void setCodtypta(String codtypta) {
        this.codtypta = codtypta;
    }

    public BigDecimal getTauxredu() {
        return tauxredu;
    }

    public void setTauxredu(BigDecimal tauxredu) {
        this.tauxredu = tauxredu;
    }

    public String getFlagredu() {
        return flagredu;
    }

    public void setFlagredu(String flagredu) {
        this.flagredu = flagredu;
    }

    public Integer getCodintge() {
        return codintge;
    }

    public void setCodintge(Integer codintge) {
        this.codintge = codintge;
    }

    public Integer getNomtimdi() {
        return nomtimdi;
    }

    public void setNomtimdi(Integer nomtimdi) {
        this.nomtimdi = nomtimdi;
    }

    public BigDecimal getBonumalu() {
        return bonumalu;
    }

    public void setBonumalu(BigDecimal bonumalu) {
        this.bonumalu = bonumalu;
    }

    public BigDecimal getTaucomap() {
        return taucomap;
    }

    public void setTaucomap(BigDecimal taucomap) {
        this.taucomap = taucomap;
    }

    public BigDecimal getTaucomge() {
        return taucomge;
    }

    public void setTaucomge(BigDecimal taucomge) {
        this.taucomge = taucomge;
    }

    public Date getDatevali() {
        return datevali;
    }

    public void setDatevali(Date datevali) {
        this.datevali = datevali;
    }

    public Date getDateannu() {
        return dateannu;
    }

    public void setDateannu(Date dateannu) {
        this.dateannu = dateannu;
    }

    public Date getDateterm() {
        return dateterm;
    }

    public void setDateterm(Date dateterm) {
        this.dateterm = dateterm;
    }

    public String getObsesous() {
        return obsesous;
    }

    public void setObsesous(String obsesous) {
        this.obsesous = obsesous;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public String getFlagannu() {
        return flagannu;
    }

    public void setFlagannu(String flagannu) {
        this.flagannu = flagannu;
    }

    public String getObsepoli() {
        return obsepoli;
    }

    public void setObsepoli(String obsepoli) {
        this.obsepoli = obsepoli;
    }

    public Date getDatesous() {
        return datesous;
    }

    public void setDatesous(Date datesous) {
        this.datesous = datesous;
    }

    public Date getDatesais() {
        return datesais;
    }

    public void setDatesais(Date datesais) {
        this.datesais = datesais;
    }

    public BigInteger getMontCa() {
        return montCa;
    }

    public void setMontCa(BigInteger montCa) {
        this.montCa = montCa;
    }

    public String getFlagmajo() {
        return flagmajo;
    }

    public void setFlagmajo(String flagmajo) {
        this.flagmajo = flagmajo;
    }

    public String getRaissous() {
        return raissous;
    }

    public void setRaissous(String raissous) {
        this.raissous = raissous;
    }

    public String getAdresous() {
        return adresous;
    }

    public void setAdresous(String adresous) {
        this.adresous = adresous;
    }

    public String getNomuticr() {
        return nomuticr;
    }

    public void setNomuticr(String nomuticr) {
        this.nomuticr = nomuticr;
    }

    public String getNomutian() {
        return nomutian;
    }

    public void setNomutian(String nomutian) {
        this.nomutian = nomutian;
    }

    public String getGenrcont() {
        return genrcont;
    }

    public void setGenrcont(String genrcont) {
        this.genrcont = genrcont;
    }

    public String getModiPar() {
        return modiPar;
    }

    public void setModiPar(String modiPar) {
        this.modiPar = modiPar;
    }

    public Date getModiLe() {
        return modiLe;
    }

    public void setModiLe(Date modiLe) {
        this.modiLe = modiLe;
    }

    public BigInteger getMonaccpo() {
        return monaccpo;
    }

    public void setMonaccpo(BigInteger monaccpo) {
        this.monaccpo = monaccpo;
    }

    public BigInteger getMonaccri() {
        return monaccri;
    }

    public void setMonaccri(BigInteger monaccri) {
        this.monaccri = monaccri;
    }

    public String getFlagpofi() {
        return flagpofi;
    }

    public void setFlagpofi(String flagpofi) {
        this.flagpofi = flagpofi;
    }

    public BigDecimal getValedure() {
        return valedure;
    }

    public void setValedure(BigDecimal valedure) {
        this.valedure = valedure;
    }

    public String getUnitdure() {
        return unitdure;
    }

    public void setUnitdure(String unitdure) {
        this.unitdure = unitdure;
    }

    public String getValiPar() {
        return valiPar;
    }

    public void setValiPar(String valiPar) {
        this.valiPar = valiPar;
    }

    public String getDevalpar() {
        return devalpar;
    }

    public void setDevalpar(String devalpar) {
        this.devalpar = devalpar;
    }

    public Date getDevalile() {
        return devalile;
    }

    public void setDevalile(Date devalile) {
        this.devalile = devalile;
    }

    public String getRefborem() {
        return refborem;
    }

    public void setRefborem(String refborem) {
        this.refborem = refborem;
    }

    public String getRefconpo() {
        return refconpo;
    }

    public void setRefconpo(String refconpo) {
        this.refconpo = refconpo;
    }

    public BigInteger getTauxpror() {
        return tauxpror;
    }

    public void setTauxpror(BigInteger tauxpror) {
        this.tauxpror = tauxpror;
    }

    public BigDecimal getValtimdi() {
        return valtimdi;
    }

    public void setValtimdi(BigDecimal valtimdi) {
        this.valtimdi = valtimdi;
    }

    public String getFlagbloq() {
        return flagbloq;
    }

    public void setFlagbloq(String flagbloq) {
        this.flagbloq = flagbloq;
    }

    public String getNatucont() {
        return natucont;
    }

    public void setNatucont(String natucont) {
        this.natucont = natucont;
    }

    public BigDecimal getComappac() {
        return comappac;
    }

    public void setComappac(BigDecimal comappac) {
        this.comappac = comappac;
    }

    public BigDecimal getComgesac() {
        return comgesac;
    }

    public void setComgesac(BigDecimal comgesac) {
        this.comgesac = comgesac;
    }

    public BigInteger getTimbgrad() {
        return timbgrad;
    }

    public void setTimbgrad(BigInteger timbgrad) {
        this.timbgrad = timbgrad;
    }

    public String getMoacsopo() {
        return moacsopo;
    }

    public void setMoacsopo(String moacsopo) {
        this.moacsopo = moacsopo;
    }

    public String getTitrpoli() {
        return titrpoli;
    }

    public void setTitrpoli(String titrpoli) {
        this.titrpoli = titrpoli;
    }

    public String getCodredfl() {
        return codredfl;
    }

    public void setCodredfl(String codredfl) {
        this.codredfl = codredfl;
    }

    public String getFormcouv() {
        return formcouv;
    }

    public void setFormcouv(String formcouv) {
        this.formcouv = formcouv;
    }

    public BigDecimal getTauxchan() {
        return tauxchan;
    }

    public void setTauxchan(BigDecimal tauxchan) {
        this.tauxchan = tauxchan;
    }

    public String getApplfoco() {
        return applfoco;
    }

    public void setApplfoco(String applfoco) {
        this.applfoco = applfoco;
    }

    public BigInteger getDelaenca() {
        return delaenca;
    }

    public void setDelaenca(BigInteger delaenca) {
        this.delaenca = delaenca;
    }

    public Long getMonaccsu() {
        return monaccsu;
    }

    public void setMonaccsu(Long monaccsu) {
        this.monaccsu = monaccsu;
    }

    public String getCodsouco() {
        return codsouco;
    }

    public void setCodsouco(String codsouco) {
        this.codsouco = codsouco;
    }

    public Integer getCoinsoco() {
        return coinsoco;
    }

    public void setCoinsoco(Integer coinsoco) {
        this.coinsoco = coinsoco;
    }

    public BigDecimal getDurepaie() {
        return durepaie;
    }

    public void setDurepaie(BigDecimal durepaie) {
        this.durepaie = durepaie;
    }

    public String getUnidurpa() {
        return unidurpa;
    }

    public void setUnidurpa(String unidurpa) {
        this.unidurpa = unidurpa;
    }

    public Date getDat1eche() {
        return dat1eche;
    }

    public void setDat1eche(Date dat1eche) {
        this.dat1eche = dat1eche;
    }

    public String getCodepoli() {
        return codepoli;
    }

    public void setCodepoli(String codepoli) {
        this.codepoli = codepoli;
    }

    public Date getDatvalba() {
        return datvalba;
    }

    public void setDatvalba(Date datvalba) {
        this.datvalba = datvalba;
    }

    public String getValparba() {
        return valparba;
    }

    public void setValparba(String valparba) {
        this.valparba = valparba;
    }

    public BigInteger getIdenpoli() {
        return idenpoli;
    }

    public void setIdenpoli(BigInteger idenpoli) {
        this.idenpoli = idenpoli;
    }

    public Integer getCodbanba() {
        return codbanba;
    }

    public void setCodbanba(Integer codbanba) {
        this.codbanba = codbanba;
    }

    public Long getCodageba() {
        return codageba;
    }

    public void setCodageba(Long codageba) {
        this.codageba = codageba;
    }

    public Assure getCodeassu() {
        return codeassu;
    }

    public void setCodeassu(Assure codeassu) {
        this.codeassu = codeassu;
    }

    public Apporteur getCodeappo() {
        return codeappo;
    }

    public void setCodeappo(Apporteur codeappo) {
        this.codeappo = codeappo;
    }

    public AsvCodeDevise getCodedevi() {
        return codedevi;
    }

    public void setCodedevi(AsvCodeDevise codedevi) {
        this.codedevi = codedevi;
    }

    public Categorie getCodecate() {
        return codecate;
    }

    public void setCodecate(Categorie codecate) {
        this.codecate = codecate;
    }

    public Convention getCodeconv() {
        return codeconv;
    }

    public void setCodeconv(Convention codeconv) {
        this.codeconv = codeconv;
    }

    public Duree getCodedure() {
        return codedure;
    }

    public void setCodedure(Duree codedure) {
        this.codedure = codedure;
    }

    public Exoneration getFlaexotg() {
        return flaexotg;
    }

    public void setFlaexotg(Exoneration flaexotg) {
        this.flaexotg = flaexotg;
    }

    public MotifsAnnulation getCodmotan() {
        return codmotan;
    }

    public void setCodmotan(MotifsAnnulation codmotan) {
        this.codmotan = codmotan;
    }

    public PeriodicitePrime getCodeperi() {
        return codeperi;
    }

    public void setCodeperi(PeriodicitePrime codeperi) {
        this.codeperi = codeperi;
    }

    public ReductionAutomobile getCoderedu() {
        return coderedu;
    }

    public void setCoderedu(ReductionAutomobile coderedu) {
        this.coderedu = coderedu;
    }

    public TimbreDimension getCodtimdi() {
        return codtimdi;
    }

    public void setCodtimdi(TimbreDimension codtimdi) {
        this.codtimdi = codtimdi;
    }

    public RegimeBonusMalus getCodregbm() {
        return codregbm;
    }

    public void setCodregbm(RegimeBonusMalus codregbm) {
        this.codregbm = codregbm;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (policePK != null ? policePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Police)) {
            return false;
        }
        Police other = (Police) object;
        if ((this.policePK == null && other.policePK != null) || (this.policePK != null && !this.policePK.equals(other.policePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Police[ policePK=" + policePK + " ]";
    }
    
}
