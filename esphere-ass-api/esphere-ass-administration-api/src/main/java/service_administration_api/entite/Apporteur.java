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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "APPORTEUR")
@NamedQueries({
    @NamedQuery(name = "Apporteur.findAll", query = "SELECT a FROM Apporteur a")})
public class Apporteur implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODEAPPO")
    private Integer codeappo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "RAISSOCI")
    private String raissoci;
    @Size(max = 100)
    @Column(name = "ADREAPPO")
    private String adreappo;
    @Size(max = 40)
    @Column(name = "TELEAPPO")
    private String teleappo;
    @Size(max = 40)
    @Column(name = "FAX_APPO")
    private String faxAppo;
    @Size(max = 20)
    @Column(name = "TELXAPPO")
    private String telxappo;
    @Column(name = "NUMEPATE")
    private Long numepate;
    @Size(max = 20)
    @Column(name = "NUMEIMPO")
    private String numeimpo;
    @Size(max = 20)
    @Column(name = "NUME_TVA")
    private String numeTva;
    @Column(name = "NUMECNSS")
    private Integer numecnss;
    @Size(max = 20)
    @Column(name = "REGICOMM")
    private String regicomm;
    @Column(name = "DATENOMI")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datenomi;
    @Column(name = "DATFINAC")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datfinac;
    @Column(name = "NUME_LOT")
    private BigInteger numeLot;
    @Column(name = "CODAPPNW")
    private BigInteger codappnw;
    @Column(name = "CODEINTE")
    private BigInteger codeinte;
    @Size(max = 2)
    @Column(name = "TYPEAPPO")
    private String typeappo;
    @Size(max = 60)
    @Column(name = "ADREMAIL")
    private String adremail;
    @Size(max = 100)
    @Column(name = "NOM_RESP")
    private String nomResp;
    @Column(name = "COMPCOMM")
    private Long compcomm;
    @Column(name = "COMCOMAP")
    private Long comcomap;
    @Size(max = 15)
    @Column(name = "ANCICODE")
    private String ancicode;
    @Size(max = 3)
    @Column(name = "MODEPAIE")
    private String modepaie;
    @Column(name = "CODEBANQ")
    private Integer codebanq;
    @Column(name = "CODEAGEN")
    private Integer codeagen;
    @Size(max = 30)
    @Column(name = "NUMCPTBA")
    private String numcptba;
    @OneToMany(mappedBy = "lienappo")
    private List<Apporteur> apporteurList;
    @JoinColumn(name = "LIENAPPO", referencedColumnName = "CODEAPPO")
    @ManyToOne
    private Apporteur lienappo;
    @JoinColumn(name = "CODTYPAP", referencedColumnName = "CODTYPAP")
    @ManyToOne
    private TypeApporteur codtypap;
    @JoinColumn(name = "CODEVILL", referencedColumnName = "CODEVILL")
    @ManyToOne
    private Ville codevill;
    @OneToMany(mappedBy = "codeappo")
    private List<Police> policeList;

    public Apporteur() {
    }

    public Apporteur(Integer codeappo) {
        this.codeappo = codeappo;
    }

    public Apporteur(Integer codeappo, String raissoci) {
        this.codeappo = codeappo;
        this.raissoci = raissoci;
    }

    public Integer getCodeappo() {
        return codeappo;
    }

    public void setCodeappo(Integer codeappo) {
        this.codeappo = codeappo;
    }

    public String getRaissoci() {
        return raissoci;
    }

    public void setRaissoci(String raissoci) {
        this.raissoci = raissoci;
    }

    public String getAdreappo() {
        return adreappo;
    }

    public void setAdreappo(String adreappo) {
        this.adreappo = adreappo;
    }

    public String getTeleappo() {
        return teleappo;
    }

    public void setTeleappo(String teleappo) {
        this.teleappo = teleappo;
    }

    public String getFaxAppo() {
        return faxAppo;
    }

    public void setFaxAppo(String faxAppo) {
        this.faxAppo = faxAppo;
    }

    public String getTelxappo() {
        return telxappo;
    }

    public void setTelxappo(String telxappo) {
        this.telxappo = telxappo;
    }

    public Long getNumepate() {
        return numepate;
    }

    public void setNumepate(Long numepate) {
        this.numepate = numepate;
    }

    public String getNumeimpo() {
        return numeimpo;
    }

    public void setNumeimpo(String numeimpo) {
        this.numeimpo = numeimpo;
    }

    public String getNumeTva() {
        return numeTva;
    }

    public void setNumeTva(String numeTva) {
        this.numeTva = numeTva;
    }

    public Integer getNumecnss() {
        return numecnss;
    }

    public void setNumecnss(Integer numecnss) {
        this.numecnss = numecnss;
    }

    public String getRegicomm() {
        return regicomm;
    }

    public void setRegicomm(String regicomm) {
        this.regicomm = regicomm;
    }

    public Date getDatenomi() {
        return datenomi;
    }

    public void setDatenomi(Date datenomi) {
        this.datenomi = datenomi;
    }

    public Date getDatfinac() {
        return datfinac;
    }

    public void setDatfinac(Date datfinac) {
        this.datfinac = datfinac;
    }

    public BigInteger getNumeLot() {
        return numeLot;
    }

    public void setNumeLot(BigInteger numeLot) {
        this.numeLot = numeLot;
    }

    public BigInteger getCodappnw() {
        return codappnw;
    }

    public void setCodappnw(BigInteger codappnw) {
        this.codappnw = codappnw;
    }

    public BigInteger getCodeinte() {
        return codeinte;
    }

    public void setCodeinte(BigInteger codeinte) {
        this.codeinte = codeinte;
    }

    public String getTypeappo() {
        return typeappo;
    }

    public void setTypeappo(String typeappo) {
        this.typeappo = typeappo;
    }

    public String getAdremail() {
        return adremail;
    }

    public void setAdremail(String adremail) {
        this.adremail = adremail;
    }

    public String getNomResp() {
        return nomResp;
    }

    public void setNomResp(String nomResp) {
        this.nomResp = nomResp;
    }

    public Long getCompcomm() {
        return compcomm;
    }

    public void setCompcomm(Long compcomm) {
        this.compcomm = compcomm;
    }

    public Long getComcomap() {
        return comcomap;
    }

    public void setComcomap(Long comcomap) {
        this.comcomap = comcomap;
    }

    public String getAncicode() {
        return ancicode;
    }

    public void setAncicode(String ancicode) {
        this.ancicode = ancicode;
    }

    public String getModepaie() {
        return modepaie;
    }

    public void setModepaie(String modepaie) {
        this.modepaie = modepaie;
    }

    public Integer getCodebanq() {
        return codebanq;
    }

    public void setCodebanq(Integer codebanq) {
        this.codebanq = codebanq;
    }

    public Integer getCodeagen() {
        return codeagen;
    }

    public void setCodeagen(Integer codeagen) {
        this.codeagen = codeagen;
    }

    public String getNumcptba() {
        return numcptba;
    }

    public void setNumcptba(String numcptba) {
        this.numcptba = numcptba;
    }

    public List<Apporteur> getApporteurList() {
        return apporteurList;
    }

    public void setApporteurList(List<Apporteur> apporteurList) {
        this.apporteurList = apporteurList;
    }

    public Apporteur getLienappo() {
        return lienappo;
    }

    public void setLienappo(Apporteur lienappo) {
        this.lienappo = lienappo;
    }

    public TypeApporteur getCodtypap() {
        return codtypap;
    }

    public void setCodtypap(TypeApporteur codtypap) {
        this.codtypap = codtypap;
    }

    public Ville getCodevill() {
        return codevill;
    }

    public void setCodevill(Ville codevill) {
        this.codevill = codevill;
    }

    public List<Police> getPoliceList() {
        return policeList;
    }

    public void setPoliceList(List<Police> policeList) {
        this.policeList = policeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (codeappo != null ? codeappo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Apporteur)) {
            return false;
        }
        Apporteur other = (Apporteur) object;
        if ((this.codeappo == null && other.codeappo != null) || (this.codeappo != null && !this.codeappo.equals(other.codeappo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "service_administration_api.entite.Apporteur[ codeappo=" + codeappo + " ]";
    }
    
}
