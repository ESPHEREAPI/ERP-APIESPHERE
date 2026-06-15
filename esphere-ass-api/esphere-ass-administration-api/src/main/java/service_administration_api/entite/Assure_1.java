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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "ASSURE")
@NamedQueries({
    @NamedQuery(name = "Assure_1.findAll", query = "SELECT a FROM Assure_1 a")})
public class Assure_1 implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODEASSU")
    private Long codeassu;
    @Size(max = 50)
    @Column(name = "RAISSOCI")
    private String raissoci;
    @Size(max = 100)
    @Column(name = "ADREASSU")
    private String adreassu;
    @Size(max = 30)
    @Column(name = "TELEASSU")
    private String teleassu;
    @Size(max = 30)
    @Column(name = "NUME_CIN")
    private String numeCin;
    @Size(max = 30)
    @Column(name = "NUMEPATE")
    private String numepate;
    @Size(max = 30)
    @Column(name = "REGICOMM")
    private String regicomm;
    @Size(max = 60)
    @Column(name = "RAISOCAR")
    private String raisocar;
    @Size(max = 140)
    @Column(name = "ADRASSAR")
    private String adrassar;
    @Size(max = 3)
    @Column(name = "CODTYPPI")
    private String codtyppi;
    @Size(max = 30)
    @Column(name = "NUMPIEID")
    private String numpieid;
    @Size(max = 100)
    @Column(name = "FAX_ASSU")
    private String faxAssu;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "GENRASSU")
    private String genrassu;
    @Size(max = 5)
    @Column(name = "VIP_ASSU")
    private String vipAssu;
    @Size(max = 100)
    @Column(name = "NOM_RES1")
    private String nomRes1;
    @Size(max = 100)
    @Column(name = "FONCRES1")
    private String foncres1;
    @Size(max = 100)
    @Column(name = "TELERES1")
    private String teleres1;
    @Size(max = 100)
    @Column(name = "MAILRES1")
    private String mailres1;
    @Size(max = 100)
    @Column(name = "NOM_RES2")
    private String nomRes2;
    @Size(max = 100)
    @Column(name = "FONCRES2")
    private String foncres2;
    @Size(max = 100)
    @Column(name = "TELERES2")
    private String teleres2;
    @Size(max = 100)
    @Column(name = "MAILRES2")
    private String mailres2;
    @Size(max = 100)
    @Column(name = "NOM_RES3")
    private String nomRes3;
    @Size(max = 100)
    @Column(name = "FONCRES3")
    private String foncres3;
    @Size(max = 100)
    @Column(name = "TELERES3")
    private String teleres3;
    @Size(max = 100)
    @Column(name = "MAILRES3")
    private String mailres3;
    @Column(name = "CODEINTE")
    private Integer codeinte;
    @Size(max = 50)
    @Column(name = "PRENASSU")
    private String prenassu;
    @Column(name = "DATECREA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datecrea;
    @Size(max = 60)
    @Column(name = "CHARCLIE")
    private String charclie;
    @Column(name = "CLEFALTE")
    private Long clefalte;
    @Size(max = 30)
    @Column(name = "CREE_PAR")
    private String creePar;
    @Column(name = "CREE__LE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creeLe;
    @Size(max = 30)
    @Column(name = "MODI_PAR")
    private String modiPar;
    @Column(name = "MODI__LE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modiLe;
    @Column(name = "CLEFPRIM")
    private Long clefprim;
    @Column(name = "CODASSTE")
    private Long codasste;
    @Size(max = 60)
    @Column(name = "REFEASSU")
    private String refeassu;
    @Size(max = 25)
    @Column(name = "ORICREAS")
    private String oricreas;
    @Size(max = 20)
    @Column(name = "TELPORAS")
    private String telporas;
    @Column(name = "CLEALTAV")
    private Long clealtav;
    @Column(name = "CLEPRIAV")
    private Long clepriav;
    @Column(name = "CODECOMP")
    private Long codecomp;
    @Size(max = 1)
    @Column(name = "QUIPAYE")
    private String quipaye;
    @Column(name = "COMPSINI")
    private Long compsini;
    @Column(name = "COMPRECO")
    private Long compreco;
    @Size(max = 3)
    @Column(name = "CODELANG")
    private String codelang;
    @Size(max = 1)
    @Column(name = "SEXEASSU")
    private String sexeassu;
    @Column(name = "DATPIEID")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datpieid;
    @Size(max = 30)
    @Column(name = "LIEPIEID")
    private String liepieid;
    @Size(max = 100)
    @Column(name = "LIBPROAS")
    private String libproas;
    @Column(name = "CODEBANQ")
    private Long codebanq;
    @Column(name = "CODEAGEN")
    private Long codeagen;
    @Size(max = 40)
    @Column(name = "NUMCPTBA")
    private String numcptba;
    @Size(max = 2)
    @Column(name = "CODSITFA")
    private String codsitfa;
    @Size(max = 6)
    @Column(name = "CODNATAS")
    private String codnatas;
    @Size(max = 100)
    @Column(name = "MAILASSU")
    private String mailassu;
    @Size(max = 100)
    @Column(name = "LIEUNAIS")
    private String lieunais;
    @Size(max = 600)
    @Column(name = "OBSEASSU")
    private String obseassu;
    @Size(max = 80)
    @Column(name = "FACEBIDE")
    private String facebide;
    @Size(max = 80)
    @Column(name = "TWITEIDE")
    private String twiteide;
    @Size(max = 80)
    @Column(name = "LINKEIDE")
    private String linkeide;
    @JoinTable(name = "ASSURE_DOULE", joinColumns = {
        @JoinColumn(name = "CODASSDE", referencedColumnName = "CODEASSU")}, inverseJoinColumns = {
        @JoinColumn(name = "CODASSSO", referencedColumnName = "CODEASSU")})
    @ManyToMany
    private List<Assure_1> assureList;
    @ManyToMany(mappedBy = "assureList")
    private List<Assure_1> assureList1;
    @OneToMany(mappedBy = "codeassu")
    private List<Police_1> policeList;
    @JoinColumn(name = "CODEACTI", referencedColumnName = "CODEACTI")
    @ManyToOne
    private Activite_1 codeacti;
    @OneToMany(mappedBy = "grouassu")
    private List<Assure_1> assureList2;
    @JoinColumn(name = "GROUASSU", referencedColumnName = "CODEASSU")
    @ManyToOne
    private Assure_1 grouassu;
    @JoinColumn(name = "COCASOPR", referencedColumnName = "COCASOPR")
    @ManyToOne
    private Categoriesocioprof_1 cocasopr;
    @JoinColumn(name = "CODEPAYS", referencedColumnName = "CODEPAYS")
    @ManyToOne
    private Pays_1 codepays;
    @JoinColumn(name = "CODEPROF", referencedColumnName = "CODEPROF")
    @ManyToOne
    private Profession_1 codeprof;
    @JoinColumn(name = "CODEQUAL", referencedColumnName = "CODEQUAL")
    @ManyToOne
    private Qualite_1 codequal;
    @JoinColumn(name = "CODEVILL", referencedColumnName = "CODEVILL")
    @ManyToOne
    private Ville_1 codevill;

    public Assure_1() {
    }

    public Assure_1(Long codeassu) {
        this.codeassu = codeassu;
    }

    public Assure_1(Long codeassu, String genrassu) {
        this.codeassu = codeassu;
        this.genrassu = genrassu;
    }

    public Long getCodeassu() {
        return codeassu;
    }

    public void setCodeassu(Long codeassu) {
        this.codeassu = codeassu;
    }

    public String getRaissoci() {
        return raissoci;
    }

    public void setRaissoci(String raissoci) {
        this.raissoci = raissoci;
    }

    public String getAdreassu() {
        return adreassu;
    }

    public void setAdreassu(String adreassu) {
        this.adreassu = adreassu;
    }

    public String getTeleassu() {
        return teleassu;
    }

    public void setTeleassu(String teleassu) {
        this.teleassu = teleassu;
    }

    public String getNumeCin() {
        return numeCin;
    }

    public void setNumeCin(String numeCin) {
        this.numeCin = numeCin;
    }

    public String getNumepate() {
        return numepate;
    }

    public void setNumepate(String numepate) {
        this.numepate = numepate;
    }

    public String getRegicomm() {
        return regicomm;
    }

    public void setRegicomm(String regicomm) {
        this.regicomm = regicomm;
    }

    public String getRaisocar() {
        return raisocar;
    }

    public void setRaisocar(String raisocar) {
        this.raisocar = raisocar;
    }

    public String getAdrassar() {
        return adrassar;
    }

    public void setAdrassar(String adrassar) {
        this.adrassar = adrassar;
    }

    public String getCodtyppi() {
        return codtyppi;
    }

    public void setCodtyppi(String codtyppi) {
        this.codtyppi = codtyppi;
    }

    public String getNumpieid() {
        return numpieid;
    }

    public void setNumpieid(String numpieid) {
        this.numpieid = numpieid;
    }

    public String getFaxAssu() {
        return faxAssu;
    }

    public void setFaxAssu(String faxAssu) {
        this.faxAssu = faxAssu;
    }

    public String getGenrassu() {
        return genrassu;
    }

    public void setGenrassu(String genrassu) {
        this.genrassu = genrassu;
    }

    public String getVipAssu() {
        return vipAssu;
    }

    public void setVipAssu(String vipAssu) {
        this.vipAssu = vipAssu;
    }

    public String getNomRes1() {
        return nomRes1;
    }

    public void setNomRes1(String nomRes1) {
        this.nomRes1 = nomRes1;
    }

    public String getFoncres1() {
        return foncres1;
    }

    public void setFoncres1(String foncres1) {
        this.foncres1 = foncres1;
    }

    public String getTeleres1() {
        return teleres1;
    }

    public void setTeleres1(String teleres1) {
        this.teleres1 = teleres1;
    }

    public String getMailres1() {
        return mailres1;
    }

    public void setMailres1(String mailres1) {
        this.mailres1 = mailres1;
    }

    public String getNomRes2() {
        return nomRes2;
    }

    public void setNomRes2(String nomRes2) {
        this.nomRes2 = nomRes2;
    }

    public String getFoncres2() {
        return foncres2;
    }

    public void setFoncres2(String foncres2) {
        this.foncres2 = foncres2;
    }

    public String getTeleres2() {
        return teleres2;
    }

    public void setTeleres2(String teleres2) {
        this.teleres2 = teleres2;
    }

    public String getMailres2() {
        return mailres2;
    }

    public void setMailres2(String mailres2) {
        this.mailres2 = mailres2;
    }

    public String getNomRes3() {
        return nomRes3;
    }

    public void setNomRes3(String nomRes3) {
        this.nomRes3 = nomRes3;
    }

    public String getFoncres3() {
        return foncres3;
    }

    public void setFoncres3(String foncres3) {
        this.foncres3 = foncres3;
    }

    public String getTeleres3() {
        return teleres3;
    }

    public void setTeleres3(String teleres3) {
        this.teleres3 = teleres3;
    }

    public String getMailres3() {
        return mailres3;
    }

    public void setMailres3(String mailres3) {
        this.mailres3 = mailres3;
    }

    public Integer getCodeinte() {
        return codeinte;
    }

    public void setCodeinte(Integer codeinte) {
        this.codeinte = codeinte;
    }

    public String getPrenassu() {
        return prenassu;
    }

    public void setPrenassu(String prenassu) {
        this.prenassu = prenassu;
    }

    public Date getDatecrea() {
        return datecrea;
    }

    public void setDatecrea(Date datecrea) {
        this.datecrea = datecrea;
    }

    public String getCharclie() {
        return charclie;
    }

    public void setCharclie(String charclie) {
        this.charclie = charclie;
    }

    public Long getClefalte() {
        return clefalte;
    }

    public void setClefalte(Long clefalte) {
        this.clefalte = clefalte;
    }

    public String getCreePar() {
        return creePar;
    }

    public void setCreePar(String creePar) {
        this.creePar = creePar;
    }

    public Date getCreeLe() {
        return creeLe;
    }

    public void setCreeLe(Date creeLe) {
        this.creeLe = creeLe;
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

    public Long getClefprim() {
        return clefprim;
    }

    public void setClefprim(Long clefprim) {
        this.clefprim = clefprim;
    }

    public Long getCodasste() {
        return codasste;
    }

    public void setCodasste(Long codasste) {
        this.codasste = codasste;
    }

    public String getRefeassu() {
        return refeassu;
    }

    public void setRefeassu(String refeassu) {
        this.refeassu = refeassu;
    }

    public String getOricreas() {
        return oricreas;
    }

    public void setOricreas(String oricreas) {
        this.oricreas = oricreas;
    }

    public String getTelporas() {
        return telporas;
    }

    public void setTelporas(String telporas) {
        this.telporas = telporas;
    }

    public Long getClealtav() {
        return clealtav;
    }

    public void setClealtav(Long clealtav) {
        this.clealtav = clealtav;
    }

    public Long getClepriav() {
        return clepriav;
    }

    public void setClepriav(Long clepriav) {
        this.clepriav = clepriav;
    }

    public Long getCodecomp() {
        return codecomp;
    }

    public void setCodecomp(Long codecomp) {
        this.codecomp = codecomp;
    }

    public String getQuipaye() {
        return quipaye;
    }

    public void setQuipaye(String quipaye) {
        this.quipaye = quipaye;
    }

    public Long getCompsini() {
        return compsini;
    }

    public void setCompsini(Long compsini) {
        this.compsini = compsini;
    }

    public Long getCompreco() {
        return compreco;
    }

    public void setCompreco(Long compreco) {
        this.compreco = compreco;
    }

    public String getCodelang() {
        return codelang;
    }

    public void setCodelang(String codelang) {
        this.codelang = codelang;
    }

    public String getSexeassu() {
        return sexeassu;
    }

    public void setSexeassu(String sexeassu) {
        this.sexeassu = sexeassu;
    }

    public Date getDatpieid() {
        return datpieid;
    }

    public void setDatpieid(Date datpieid) {
        this.datpieid = datpieid;
    }

    public String getLiepieid() {
        return liepieid;
    }

    public void setLiepieid(String liepieid) {
        this.liepieid = liepieid;
    }

    public String getLibproas() {
        return libproas;
    }

    public void setLibproas(String libproas) {
        this.libproas = libproas;
    }

    public Long getCodebanq() {
        return codebanq;
    }

    public void setCodebanq(Long codebanq) {
        this.codebanq = codebanq;
    }

    public Long getCodeagen() {
        return codeagen;
    }

    public void setCodeagen(Long codeagen) {
        this.codeagen = codeagen;
    }

    public String getNumcptba() {
        return numcptba;
    }

    public void setNumcptba(String numcptba) {
        this.numcptba = numcptba;
    }

    public String getCodsitfa() {
        return codsitfa;
    }

    public void setCodsitfa(String codsitfa) {
        this.codsitfa = codsitfa;
    }

    public String getCodnatas() {
        return codnatas;
    }

    public void setCodnatas(String codnatas) {
        this.codnatas = codnatas;
    }

    public String getMailassu() {
        return mailassu;
    }

    public void setMailassu(String mailassu) {
        this.mailassu = mailassu;
    }

    public String getLieunais() {
        return lieunais;
    }

    public void setLieunais(String lieunais) {
        this.lieunais = lieunais;
    }

    public String getObseassu() {
        return obseassu;
    }

    public void setObseassu(String obseassu) {
        this.obseassu = obseassu;
    }

    public String getFacebide() {
        return facebide;
    }

    public void setFacebide(String facebide) {
        this.facebide = facebide;
    }

    public String getTwiteide() {
        return twiteide;
    }

    public void setTwiteide(String twiteide) {
        this.twiteide = twiteide;
    }

    public String getLinkeide() {
        return linkeide;
    }

    public void setLinkeide(String linkeide) {
        this.linkeide = linkeide;
    }

    public List<Assure_1> getAssureList() {
        return assureList;
    }

    public void setAssureList(List<Assure_1> assureList) {
        this.assureList = assureList;
    }

    public List<Assure_1> getAssureList1() {
        return assureList1;
    }

    public void setAssureList1(List<Assure_1> assureList1) {
        this.assureList1 = assureList1;
    }

    public List<Police_1> getPoliceList() {
        return policeList;
    }

    public void setPoliceList(List<Police_1> policeList) {
        this.policeList = policeList;
    }

    public Activite_1 getCodeacti() {
        return codeacti;
    }

    public void setCodeacti(Activite_1 codeacti) {
        this.codeacti = codeacti;
    }

    public List<Assure_1> getAssureList2() {
        return assureList2;
    }

    public void setAssureList2(List<Assure_1> assureList2) {
        this.assureList2 = assureList2;
    }

    public Assure_1 getGrouassu() {
        return grouassu;
    }

    public void setGrouassu(Assure_1 grouassu) {
        this.grouassu = grouassu;
    }

    public Categoriesocioprof_1 getCocasopr() {
        return cocasopr;
    }

    public void setCocasopr(Categoriesocioprof_1 cocasopr) {
        this.cocasopr = cocasopr;
    }

    public Pays_1 getCodepays() {
        return codepays;
    }

    public void setCodepays(Pays_1 codepays) {
        this.codepays = codepays;
    }

    public Profession_1 getCodeprof() {
        return codeprof;
    }

    public void setCodeprof(Profession_1 codeprof) {
        this.codeprof = codeprof;
    }

    public Qualite_1 getCodequal() {
        return codequal;
    }

    public void setCodequal(Qualite_1 codequal) {
        this.codequal = codequal;
    }

    public Ville_1 getCodevill() {
        return codevill;
    }

    public void setCodevill(Ville_1 codevill) {
        this.codevill = codevill;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeassu != null ? codeassu.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Assure_1)) {
            return false;
        }
        Assure_1 other = (Assure_1) object;
        if ((this.codeassu == null && other.codeassu != null) || (this.codeassu != null && !this.codeassu.equals(other.codeassu))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Assure_1[ codeassu=" + codeassu + " ]";
    }
    
}
