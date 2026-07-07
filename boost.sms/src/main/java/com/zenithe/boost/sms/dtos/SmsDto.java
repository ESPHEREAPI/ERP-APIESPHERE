/*
 * Decompiled with CFR 0.152.
 */
package com.zenithe.boost.sms.dtos;

import java.io.Serializable;
import java.util.Date;

public class SmsDto
implements Serializable {
    private Integer id;
    private String contenu;
    private String codeinte;
    private String numepoli;
    private String numeaven;
    private Integer numesini;
    private Integer exersini;
    private String nomassure;
    private String teleassu;
    private String nomExpediteur;
    private String reponseEnvoiSms;
    private Date dateEnvoi;
    private String numeimma;
    private String dateeche;
    private String statut;
    private Date datecreation;
    private Integer services;

    public Integer getId() {
        return this.id;
    }

    public String getContenu() {
        return this.contenu;
    }

    public String getCodeinte() {
        return this.codeinte;
    }

    public String getNumepoli() {
        return this.numepoli;
    }

    public String getNumeaven() {
        return this.numeaven;
    }

    public Integer getNumesini() {
        return this.numesini;
    }

    public Integer getExersini() {
        return this.exersini;
    }

    public String getNomassure() {
        return this.nomassure;
    }

    public String getTeleassu() {
        return this.teleassu;
    }

    public String getNomExpediteur() {
        return this.nomExpediteur;
    }

    public String getReponseEnvoiSms() {
        return this.reponseEnvoiSms;
    }

    public Date getDateEnvoi() {
        return this.dateEnvoi;
    }

    public String getNumeimma() {
        return this.numeimma;
    }

    public String getDateeche() {
        return this.dateeche;
    }

    public String getStatut() {
        return this.statut;
    }

    public Date getDatecreation() {
        return this.datecreation;
    }

    public Integer getServices() {
        return this.services;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public void setCodeinte(String codeinte) {
        this.codeinte = codeinte;
    }

    public void setNumepoli(String numepoli) {
        this.numepoli = numepoli;
    }

    public void setNumeaven(String numeaven) {
        this.numeaven = numeaven;
    }

    public void setNumesini(Integer numesini) {
        this.numesini = numesini;
    }

    public void setExersini(Integer exersini) {
        this.exersini = exersini;
    }

    public void setNomassure(String nomassure) {
        this.nomassure = nomassure;
    }

    public void setTeleassu(String teleassu) {
        this.teleassu = teleassu;
    }

    public void setNomExpediteur(String nomExpediteur) {
        this.nomExpediteur = nomExpediteur;
    }

    public void setReponseEnvoiSms(String reponseEnvoiSms) {
        this.reponseEnvoiSms = reponseEnvoiSms;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public void setNumeimma(String numeimma) {
        this.numeimma = numeimma;
    }

    public void setDateeche(String dateeche) {
        this.dateeche = dateeche;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public void setServices(Integer services) {
        this.services = services;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SmsDto)) {
            return false;
        }
        SmsDto other = (SmsDto)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        Integer this$numesini = this.getNumesini();
        Integer other$numesini = other.getNumesini();
        if (this$numesini == null ? other$numesini != null : !((Object)this$numesini).equals(other$numesini)) {
            return false;
        }
        Integer this$exersini = this.getExersini();
        Integer other$exersini = other.getExersini();
        if (this$exersini == null ? other$exersini != null : !((Object)this$exersini).equals(other$exersini)) {
            return false;
        }
        Integer this$services = this.getServices();
        Integer other$services = other.getServices();
        if (this$services == null ? other$services != null : !((Object)this$services).equals(other$services)) {
            return false;
        }
        String this$contenu = this.getContenu();
        String other$contenu = other.getContenu();
        if (this$contenu == null ? other$contenu != null : !this$contenu.equals(other$contenu)) {
            return false;
        }
        String this$codeinte = this.getCodeinte();
        String other$codeinte = other.getCodeinte();
        if (this$codeinte == null ? other$codeinte != null : !this$codeinte.equals(other$codeinte)) {
            return false;
        }
        String this$numepoli = this.getNumepoli();
        String other$numepoli = other.getNumepoli();
        if (this$numepoli == null ? other$numepoli != null : !this$numepoli.equals(other$numepoli)) {
            return false;
        }
        String this$numeaven = this.getNumeaven();
        String other$numeaven = other.getNumeaven();
        if (this$numeaven == null ? other$numeaven != null : !this$numeaven.equals(other$numeaven)) {
            return false;
        }
        String this$nomassure = this.getNomassure();
        String other$nomassure = other.getNomassure();
        if (this$nomassure == null ? other$nomassure != null : !this$nomassure.equals(other$nomassure)) {
            return false;
        }
        String this$teleassu = this.getTeleassu();
        String other$teleassu = other.getTeleassu();
        if (this$teleassu == null ? other$teleassu != null : !this$teleassu.equals(other$teleassu)) {
            return false;
        }
        String this$nomExpediteur = this.getNomExpediteur();
        String other$nomExpediteur = other.getNomExpediteur();
        if (this$nomExpediteur == null ? other$nomExpediteur != null : !this$nomExpediteur.equals(other$nomExpediteur)) {
            return false;
        }
        String this$reponseEnvoiSms = this.getReponseEnvoiSms();
        String other$reponseEnvoiSms = other.getReponseEnvoiSms();
        if (this$reponseEnvoiSms == null ? other$reponseEnvoiSms != null : !this$reponseEnvoiSms.equals(other$reponseEnvoiSms)) {
            return false;
        }
        Date this$dateEnvoi = this.getDateEnvoi();
        Date other$dateEnvoi = other.getDateEnvoi();
        if (this$dateEnvoi == null ? other$dateEnvoi != null : !((Object)this$dateEnvoi).equals(other$dateEnvoi)) {
            return false;
        }
        String this$numeimma = this.getNumeimma();
        String other$numeimma = other.getNumeimma();
        if (this$numeimma == null ? other$numeimma != null : !this$numeimma.equals(other$numeimma)) {
            return false;
        }
        String this$dateeche = this.getDateeche();
        String other$dateeche = other.getDateeche();
        if (this$dateeche == null ? other$dateeche != null : !this$dateeche.equals(other$dateeche)) {
            return false;
        }
        String this$statut = this.getStatut();
        String other$statut = other.getStatut();
        if (this$statut == null ? other$statut != null : !this$statut.equals(other$statut)) {
            return false;
        }
        Date this$datecreation = this.getDatecreation();
        Date other$datecreation = other.getDatecreation();
        return !(this$datecreation == null ? other$datecreation != null : !((Object)this$datecreation).equals(other$datecreation));
    }

    protected boolean canEqual(Object other) {
        return other instanceof SmsDto;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        Integer $numesini = this.getNumesini();
        result = result * 59 + ($numesini == null ? 43 : ((Object)$numesini).hashCode());
        Integer $exersini = this.getExersini();
        result = result * 59 + ($exersini == null ? 43 : ((Object)$exersini).hashCode());
        Integer $services = this.getServices();
        result = result * 59 + ($services == null ? 43 : ((Object)$services).hashCode());
        String $contenu = this.getContenu();
        result = result * 59 + ($contenu == null ? 43 : $contenu.hashCode());
        String $codeinte = this.getCodeinte();
        result = result * 59 + ($codeinte == null ? 43 : $codeinte.hashCode());
        String $numepoli = this.getNumepoli();
        result = result * 59 + ($numepoli == null ? 43 : $numepoli.hashCode());
        String $numeaven = this.getNumeaven();
        result = result * 59 + ($numeaven == null ? 43 : $numeaven.hashCode());
        String $nomassure = this.getNomassure();
        result = result * 59 + ($nomassure == null ? 43 : $nomassure.hashCode());
        String $teleassu = this.getTeleassu();
        result = result * 59 + ($teleassu == null ? 43 : $teleassu.hashCode());
        String $nomExpediteur = this.getNomExpediteur();
        result = result * 59 + ($nomExpediteur == null ? 43 : $nomExpediteur.hashCode());
        String $reponseEnvoiSms = this.getReponseEnvoiSms();
        result = result * 59 + ($reponseEnvoiSms == null ? 43 : $reponseEnvoiSms.hashCode());
        Date $dateEnvoi = this.getDateEnvoi();
        result = result * 59 + ($dateEnvoi == null ? 43 : ((Object)$dateEnvoi).hashCode());
        String $numeimma = this.getNumeimma();
        result = result * 59 + ($numeimma == null ? 43 : $numeimma.hashCode());
        String $dateeche = this.getDateeche();
        result = result * 59 + ($dateeche == null ? 43 : $dateeche.hashCode());
        String $statut = this.getStatut();
        result = result * 59 + ($statut == null ? 43 : $statut.hashCode());
        Date $datecreation = this.getDatecreation();
        result = result * 59 + ($datecreation == null ? 43 : ((Object)$datecreation).hashCode());
        return result;
    }

    public String toString() {
        return "SmsDto(id=" + this.getId() + ", contenu=" + this.getContenu() + ", codeinte=" + this.getCodeinte() + ", numepoli=" + this.getNumepoli() + ", numeaven=" + this.getNumeaven() + ", numesini=" + this.getNumesini() + ", exersini=" + this.getExersini() + ", nomassure=" + this.getNomassure() + ", teleassu=" + this.getTeleassu() + ", nomExpediteur=" + this.getNomExpediteur() + ", reponseEnvoiSms=" + this.getReponseEnvoiSms() + ", dateEnvoi=" + this.getDateEnvoi() + ", numeimma=" + this.getNumeimma() + ", dateeche=" + this.getDateeche() + ", statut=" + this.getStatut() + ", datecreation=" + this.getDatecreation() + ", services=" + this.getServices() + ")";
    }
}
