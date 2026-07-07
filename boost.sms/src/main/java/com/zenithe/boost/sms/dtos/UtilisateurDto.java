/*
 * Decompiled with CFR 0.152.
 */
package com.zenithe.boost.sms.dtos;

import java.util.Date;

public class UtilisateurDto {
    private Integer id;
    private String genre;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String lieuNaissance;
    private String telephone;
    private String email;
    private String login;
    private String motPasse;
    private String statut;
    private String supprime;
    private boolean echeck_connection = false;
    private String messageEcheck;
    private String profil;

    public Integer getId() {
        return this.id;
    }

    public String getGenre() {
        return this.genre;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Date getDateNaissance() {
        return this.dateNaissance;
    }

    public String getLieuNaissance() {
        return this.lieuNaissance;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public String getEmail() {
        return this.email;
    }

    public String getLogin() {
        return this.login;
    }

    public String getMotPasse() {
        return this.motPasse;
    }

    public String getStatut() {
        return this.statut;
    }

    public String getSupprime() {
        return this.supprime;
    }

    public boolean isEcheck_connection() {
        return this.echeck_connection;
    }

    public String getMessageEcheck() {
        return this.messageEcheck;
    }

    public String getProfil() {
        return this.profil;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setLieuNaissance(String lieuNaissance) {
        this.lieuNaissance = lieuNaissance;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public void setSupprime(String supprime) {
        this.supprime = supprime;
    }

    public void setEcheck_connection(boolean echeck_connection) {
        this.echeck_connection = echeck_connection;
    }

    public void setMessageEcheck(String messageEcheck) {
        this.messageEcheck = messageEcheck;
    }

    public void setProfil(String profil) {
        this.profil = profil;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UtilisateurDto)) {
            return false;
        }
        UtilisateurDto other = (UtilisateurDto)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isEcheck_connection() != other.isEcheck_connection()) {
            return false;
        }
        Integer this$id = this.getId();
        Integer other$id = other.getId();
        if (this$id == null ? other$id != null : !((Object)this$id).equals(other$id)) {
            return false;
        }
        String this$genre = this.getGenre();
        String other$genre = other.getGenre();
        if (this$genre == null ? other$genre != null : !this$genre.equals(other$genre)) {
            return false;
        }
        String this$nom = this.getNom();
        String other$nom = other.getNom();
        if (this$nom == null ? other$nom != null : !this$nom.equals(other$nom)) {
            return false;
        }
        String this$prenom = this.getPrenom();
        String other$prenom = other.getPrenom();
        if (this$prenom == null ? other$prenom != null : !this$prenom.equals(other$prenom)) {
            return false;
        }
        Date this$dateNaissance = this.getDateNaissance();
        Date other$dateNaissance = other.getDateNaissance();
        if (this$dateNaissance == null ? other$dateNaissance != null : !((Object)this$dateNaissance).equals(other$dateNaissance)) {
            return false;
        }
        String this$lieuNaissance = this.getLieuNaissance();
        String other$lieuNaissance = other.getLieuNaissance();
        if (this$lieuNaissance == null ? other$lieuNaissance != null : !this$lieuNaissance.equals(other$lieuNaissance)) {
            return false;
        }
        String this$telephone = this.getTelephone();
        String other$telephone = other.getTelephone();
        if (this$telephone == null ? other$telephone != null : !this$telephone.equals(other$telephone)) {
            return false;
        }
        String this$email = this.getEmail();
        String other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) {
            return false;
        }
        String this$login = this.getLogin();
        String other$login = other.getLogin();
        if (this$login == null ? other$login != null : !this$login.equals(other$login)) {
            return false;
        }
        String this$motPasse = this.getMotPasse();
        String other$motPasse = other.getMotPasse();
        if (this$motPasse == null ? other$motPasse != null : !this$motPasse.equals(other$motPasse)) {
            return false;
        }
        String this$statut = this.getStatut();
        String other$statut = other.getStatut();
        if (this$statut == null ? other$statut != null : !this$statut.equals(other$statut)) {
            return false;
        }
        String this$supprime = this.getSupprime();
        String other$supprime = other.getSupprime();
        if (this$supprime == null ? other$supprime != null : !this$supprime.equals(other$supprime)) {
            return false;
        }
        String this$messageEcheck = this.getMessageEcheck();
        String other$messageEcheck = other.getMessageEcheck();
        if (this$messageEcheck == null ? other$messageEcheck != null : !this$messageEcheck.equals(other$messageEcheck)) {
            return false;
        }
        String this$profil = this.getProfil();
        String other$profil = other.getProfil();
        return !(this$profil == null ? other$profil != null : !this$profil.equals(other$profil));
    }

    protected boolean canEqual(Object other) {
        return other instanceof UtilisateurDto;
    }

    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isEcheck_connection() ? 79 : 97);
        Integer $id = this.getId();
        result = result * 59 + ($id == null ? 43 : ((Object)$id).hashCode());
        String $genre = this.getGenre();
        result = result * 59 + ($genre == null ? 43 : $genre.hashCode());
        String $nom = this.getNom();
        result = result * 59 + ($nom == null ? 43 : $nom.hashCode());
        String $prenom = this.getPrenom();
        result = result * 59 + ($prenom == null ? 43 : $prenom.hashCode());
        Date $dateNaissance = this.getDateNaissance();
        result = result * 59 + ($dateNaissance == null ? 43 : ((Object)$dateNaissance).hashCode());
        String $lieuNaissance = this.getLieuNaissance();
        result = result * 59 + ($lieuNaissance == null ? 43 : $lieuNaissance.hashCode());
        String $telephone = this.getTelephone();
        result = result * 59 + ($telephone == null ? 43 : $telephone.hashCode());
        String $email = this.getEmail();
        result = result * 59 + ($email == null ? 43 : $email.hashCode());
        String $login = this.getLogin();
        result = result * 59 + ($login == null ? 43 : $login.hashCode());
        String $motPasse = this.getMotPasse();
        result = result * 59 + ($motPasse == null ? 43 : $motPasse.hashCode());
        String $statut = this.getStatut();
        result = result * 59 + ($statut == null ? 43 : $statut.hashCode());
        String $supprime = this.getSupprime();
        result = result * 59 + ($supprime == null ? 43 : $supprime.hashCode());
        String $messageEcheck = this.getMessageEcheck();
        result = result * 59 + ($messageEcheck == null ? 43 : $messageEcheck.hashCode());
        String $profil = this.getProfil();
        result = result * 59 + ($profil == null ? 43 : $profil.hashCode());
        return result;
    }

    public String toString() {
        return "UtilisateurDto(id=" + this.getId() + ", genre=" + this.getGenre() + ", nom=" + this.getNom() + ", prenom=" + this.getPrenom() + ", dateNaissance=" + this.getDateNaissance() + ", lieuNaissance=" + this.getLieuNaissance() + ", telephone=" + this.getTelephone() + ", email=" + this.getEmail() + ", login=" + this.getLogin() + ", motPasse=" + this.getMotPasse() + ", statut=" + this.getStatut() + ", supprime=" + this.getSupprime() + ", echeck_connection=" + this.isEcheck_connection() + ", messageEcheck=" + this.getMessageEcheck() + ", profil=" + this.getProfil() + ")";
    }
}
