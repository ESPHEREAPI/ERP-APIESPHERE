/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "ATTESTATION_RISQUE")
@NamedQueries({
    @NamedQuery(name = "AttestationRisque.findAll", query = "SELECT a FROM AttestationRisque a")})
public class AttestationRisque implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AttestationRisquePK attestationRisquePK;
    @Size(max = 16)
    @Column(name = "NUMEATTE")
    private String numeatte;
    @Column(name = "DATEFFAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateffat;
    @Column(name = "DATECHAT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datechat;
    @Column(name = "NUMAVECO")
    private BigInteger numaveco;
    @Column(name = "INTATTLO")
    private Integer intattlo;
    @Column(name = "CODEBRAN")
    private Short codebran;
    @Column(name = "NUMEPAPI")
    private Long numepapi;
    @Column(name = "CATERISQ")
    private Integer caterisq;
    @Column(name = "CODVILAT")
    private Integer codvilat;
    @Size(max = 2)
    @Column(name = "CODGENAU")
    private String codgenau;
    @Size(max = 20)
    @Column(name = "MARQVEHI")
    private String marqvehi;
    @Size(max = 20)
    @Column(name = "TYPEVEHI")
    private String typevehi;
    @Size(max = 15)
    @Column(name = "NUMEIMMA")
    private String numeimma;
    @Size(max = 20)
    @Column(name = "NUMECHAS")
    private String numechas;
    @Column(name = "PUISVEHI")
    private Short puisvehi;
    @Column(name = "CODEASSU")
    private Long codeassu;
    @Size(max = 80)
    @Column(name = "PROPATTE")
    private String propatte;
    @Size(max = 80)
    @Column(name = "ADREATTE")
    private String adreatte;
    @Column(name = "CODEPROF")
    private Integer codeprof;
    @Column(name = "ANCNUMPA")
    private Long ancnumpa;
    @Column(name = "NUMSEQAL")
    private Long numseqal;
    @Column(name = "GROUIMPR")
    private Short grouimpr;
    @Size(max = 1)
    @Column(name = "NATUDOCU")
    private String natudocu;
    @Size(max = 30)
    @Column(name = "CREE_PAR")
    private String creePar;
    @Column(name = "CREE__LE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creeLe;
    @Column(name = "IMPRATTE")
    private Character impratte;
    @Column(name = "ATT_A_IM")
    private Character attAIm;
    @Size(max = 6)
    @Column(name = "ANCTYPDO")
    private String anctypdo;
    @Size(max = 2)
    @Column(name = "ANCNATDO")
    private String ancnatdo;
    @Column(name = "IDENPARA")
    private Long idenpara;
    @Size(max = 20)
    @Column(name = "MARQREMO")
    private String marqremo;
    @Size(max = 20)
    @Column(name = "TYPEREMO")
    private String typeremo;
    @Size(max = 80)
    @Column(name = "NUMATTRE")
    private String numattre;
    @Size(max = 20)
    @Column(name = "CHASREMO")
    private String chasremo;
    @Size(max = 50)
    @Column(name = "LIBERISQ")
    private String liberisq;
    @Size(max = 1)
    @Column(name = "SEXERISQ")
    private String sexerisq;
    @Column(name = "DATENAIS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datenais;
    @Size(max = 5)
    @Column(name = "CODTYPPE")
    private String codtyppe;
    @Size(max = 30)
    @Column(name = "NUMEPERM")
    private String numeperm;
    @Column(name = "DATDELPE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datdelpe;
    @Size(max = 30)
    @Column(name = "LIEUPERM")
    private String lieuperm;
    @Size(max = 1)
    @Column(name = "TYPEMOTE")
    private String typemote;
    @Size(max = 10)
    @Column(name = "NUMEMOTE")
    private String numemote;
    @Column(name = "POIDVEHI")
    private BigInteger poidvehi;
    @Column(name = "CYLIVEHI")
    private Integer cylivehi;
    @Column(name = "VITEVEHI")
    private Short vitevehi;
    @Column(name = "NOMBPLAC")
    private Short nombplac;
    @Column(name = "DATEENTR")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateentr;
    @Column(name = "DATESORT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datesort;
    @Size(max = 60)
    @Column(name = "ADRERISQ")
    private String adrerisq;
    @Column(name = "CODEVILL")
    private Integer codevill;
    @Size(max = 1)
    @Column(name = "MATIINFL")
    private String matiinfl;
    @Size(max = 1)
    @Column(name = "NATUATTE")
    private String natuatte;
    @Column(name = "ANCSEQAT")
    private Long ancseqat;
    @Size(max = 60)
    @Column(name = "MODI_PAR")
    private String modiPar;
    @Column(name = "MODI__LE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modiLe;
    @Size(max = 1)
    @Column(name = "FLGATTPR")
    private String flgattpr;
    @Size(max = 1)
    @Column(name = "ORIGAFFE")
    private String origaffe;
    @JoinColumns({
        @JoinColumn(name = "CODEINTE", referencedColumnName = "CODEINTE", insertable = false, updatable = false),
        @JoinColumn(name = "NUMEPOLI", referencedColumnName = "NUMEPOLI", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Police_1 police;

    public AttestationRisque() {
    }

    public AttestationRisque(AttestationRisquePK attestationRisquePK) {
        this.attestationRisquePK = attestationRisquePK;
    }

    public AttestationRisque(int codeinte, long numepoli, short numeaven, long coderisq, String naturisq, String codtypdo) {
        this.attestationRisquePK = new AttestationRisquePK(codeinte, numepoli, numeaven, coderisq, naturisq, codtypdo);
    }

    public AttestationRisquePK getAttestationRisquePK() {
        return attestationRisquePK;
    }

    public void setAttestationRisquePK(AttestationRisquePK attestationRisquePK) {
        this.attestationRisquePK = attestationRisquePK;
    }

    public String getNumeatte() {
        return numeatte;
    }

    public void setNumeatte(String numeatte) {
        this.numeatte = numeatte;
    }

    public Date getDateffat() {
        return dateffat;
    }

    public void setDateffat(Date dateffat) {
        this.dateffat = dateffat;
    }

    public Date getDatechat() {
        return datechat;
    }

    public void setDatechat(Date datechat) {
        this.datechat = datechat;
    }

    public BigInteger getNumaveco() {
        return numaveco;
    }

    public void setNumaveco(BigInteger numaveco) {
        this.numaveco = numaveco;
    }

    public Integer getIntattlo() {
        return intattlo;
    }

    public void setIntattlo(Integer intattlo) {
        this.intattlo = intattlo;
    }

    public Short getCodebran() {
        return codebran;
    }

    public void setCodebran(Short codebran) {
        this.codebran = codebran;
    }

    public Long getNumepapi() {
        return numepapi;
    }

    public void setNumepapi(Long numepapi) {
        this.numepapi = numepapi;
    }

    public Integer getCaterisq() {
        return caterisq;
    }

    public void setCaterisq(Integer caterisq) {
        this.caterisq = caterisq;
    }

    public Integer getCodvilat() {
        return codvilat;
    }

    public void setCodvilat(Integer codvilat) {
        this.codvilat = codvilat;
    }

    public String getCodgenau() {
        return codgenau;
    }

    public void setCodgenau(String codgenau) {
        this.codgenau = codgenau;
    }

    public String getMarqvehi() {
        return marqvehi;
    }

    public void setMarqvehi(String marqvehi) {
        this.marqvehi = marqvehi;
    }

    public String getTypevehi() {
        return typevehi;
    }

    public void setTypevehi(String typevehi) {
        this.typevehi = typevehi;
    }

    public String getNumeimma() {
        return numeimma;
    }

    public void setNumeimma(String numeimma) {
        this.numeimma = numeimma;
    }

    public String getNumechas() {
        return numechas;
    }

    public void setNumechas(String numechas) {
        this.numechas = numechas;
    }

    public Short getPuisvehi() {
        return puisvehi;
    }

    public void setPuisvehi(Short puisvehi) {
        this.puisvehi = puisvehi;
    }

    public Long getCodeassu() {
        return codeassu;
    }

    public void setCodeassu(Long codeassu) {
        this.codeassu = codeassu;
    }

    public String getPropatte() {
        return propatte;
    }

    public void setPropatte(String propatte) {
        this.propatte = propatte;
    }

    public String getAdreatte() {
        return adreatte;
    }

    public void setAdreatte(String adreatte) {
        this.adreatte = adreatte;
    }

    public Integer getCodeprof() {
        return codeprof;
    }

    public void setCodeprof(Integer codeprof) {
        this.codeprof = codeprof;
    }

    public Long getAncnumpa() {
        return ancnumpa;
    }

    public void setAncnumpa(Long ancnumpa) {
        this.ancnumpa = ancnumpa;
    }

    public Long getNumseqal() {
        return numseqal;
    }

    public void setNumseqal(Long numseqal) {
        this.numseqal = numseqal;
    }

    public Short getGrouimpr() {
        return grouimpr;
    }

    public void setGrouimpr(Short grouimpr) {
        this.grouimpr = grouimpr;
    }

    public String getNatudocu() {
        return natudocu;
    }

    public void setNatudocu(String natudocu) {
        this.natudocu = natudocu;
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

    public Character getImpratte() {
        return impratte;
    }

    public void setImpratte(Character impratte) {
        this.impratte = impratte;
    }

    public Character getAttAIm() {
        return attAIm;
    }

    public void setAttAIm(Character attAIm) {
        this.attAIm = attAIm;
    }

    public String getAnctypdo() {
        return anctypdo;
    }

    public void setAnctypdo(String anctypdo) {
        this.anctypdo = anctypdo;
    }

    public String getAncnatdo() {
        return ancnatdo;
    }

    public void setAncnatdo(String ancnatdo) {
        this.ancnatdo = ancnatdo;
    }

    public Long getIdenpara() {
        return idenpara;
    }

    public void setIdenpara(Long idenpara) {
        this.idenpara = idenpara;
    }

    public String getMarqremo() {
        return marqremo;
    }

    public void setMarqremo(String marqremo) {
        this.marqremo = marqremo;
    }

    public String getTyperemo() {
        return typeremo;
    }

    public void setTyperemo(String typeremo) {
        this.typeremo = typeremo;
    }

    public String getNumattre() {
        return numattre;
    }

    public void setNumattre(String numattre) {
        this.numattre = numattre;
    }

    public String getChasremo() {
        return chasremo;
    }

    public void setChasremo(String chasremo) {
        this.chasremo = chasremo;
    }

    public String getLiberisq() {
        return liberisq;
    }

    public void setLiberisq(String liberisq) {
        this.liberisq = liberisq;
    }

    public String getSexerisq() {
        return sexerisq;
    }

    public void setSexerisq(String sexerisq) {
        this.sexerisq = sexerisq;
    }

    public Date getDatenais() {
        return datenais;
    }

    public void setDatenais(Date datenais) {
        this.datenais = datenais;
    }

    public String getCodtyppe() {
        return codtyppe;
    }

    public void setCodtyppe(String codtyppe) {
        this.codtyppe = codtyppe;
    }

    public String getNumeperm() {
        return numeperm;
    }

    public void setNumeperm(String numeperm) {
        this.numeperm = numeperm;
    }

    public Date getDatdelpe() {
        return datdelpe;
    }

    public void setDatdelpe(Date datdelpe) {
        this.datdelpe = datdelpe;
    }

    public String getLieuperm() {
        return lieuperm;
    }

    public void setLieuperm(String lieuperm) {
        this.lieuperm = lieuperm;
    }

    public String getTypemote() {
        return typemote;
    }

    public void setTypemote(String typemote) {
        this.typemote = typemote;
    }

    public String getNumemote() {
        return numemote;
    }

    public void setNumemote(String numemote) {
        this.numemote = numemote;
    }

    public BigInteger getPoidvehi() {
        return poidvehi;
    }

    public void setPoidvehi(BigInteger poidvehi) {
        this.poidvehi = poidvehi;
    }

    public Integer getCylivehi() {
        return cylivehi;
    }

    public void setCylivehi(Integer cylivehi) {
        this.cylivehi = cylivehi;
    }

    public Short getVitevehi() {
        return vitevehi;
    }

    public void setVitevehi(Short vitevehi) {
        this.vitevehi = vitevehi;
    }

    public Short getNombplac() {
        return nombplac;
    }

    public void setNombplac(Short nombplac) {
        this.nombplac = nombplac;
    }

    public Date getDateentr() {
        return dateentr;
    }

    public void setDateentr(Date dateentr) {
        this.dateentr = dateentr;
    }

    public Date getDatesort() {
        return datesort;
    }

    public void setDatesort(Date datesort) {
        this.datesort = datesort;
    }

    public String getAdrerisq() {
        return adrerisq;
    }

    public void setAdrerisq(String adrerisq) {
        this.adrerisq = adrerisq;
    }

    public Integer getCodevill() {
        return codevill;
    }

    public void setCodevill(Integer codevill) {
        this.codevill = codevill;
    }

    public String getMatiinfl() {
        return matiinfl;
    }

    public void setMatiinfl(String matiinfl) {
        this.matiinfl = matiinfl;
    }

    public String getNatuatte() {
        return natuatte;
    }

    public void setNatuatte(String natuatte) {
        this.natuatte = natuatte;
    }

    public Long getAncseqat() {
        return ancseqat;
    }

    public void setAncseqat(Long ancseqat) {
        this.ancseqat = ancseqat;
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

    public String getFlgattpr() {
        return flgattpr;
    }

    public void setFlgattpr(String flgattpr) {
        this.flgattpr = flgattpr;
    }

    public String getOrigaffe() {
        return origaffe;
    }

    public void setOrigaffe(String origaffe) {
        this.origaffe = origaffe;
    }

    public Police_1 getPolice() {
        return police;
    }

    public void setPolice(Police_1 police) {
        this.police = police;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (attestationRisquePK != null ? attestationRisquePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AttestationRisque)) {
            return false;
        }
        AttestationRisque other = (AttestationRisque) object;
        if ((this.attestationRisquePK == null && other.attestationRisquePK != null) || (this.attestationRisquePK != null && !this.attestationRisquePK.equals(other.attestationRisquePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.AttestationRisque[ attestationRisquePK=" + attestationRisquePK + " ]";
    }
    
}
