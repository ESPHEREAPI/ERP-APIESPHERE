/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite.pooltpv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "ZEN_INFOS_ADMIN_AGENCE")

public class Infos_AdministrateurAgencePayLoad implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Infos_Admin")
    @SequenceGenerator(name = "Infos_Admin", sequenceName = "SEQ_INFOSADMINAGENCE", allocationSize = 1)
    private Long id;
    @Column(name = "code_agence")
    private Integer codeAgence;
    @Column(name = "libelle_agence")
    private String libelleAgence;
    private String email;
    private String login;
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "expires_at")
    private int expiresAt = 24;
     @Column(name = "office_code")
    private String office_code;
       @Column(name = "username_asac", nullable = false, unique = true)
    private String username;

    @Column(name = "profil_agent", nullable = false)
    private String profilAgent = "PRODUCTEUR";

    @Column(name = "can_edit", nullable = false)
    private boolean canEdit = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCodeAgence() {
        return codeAgence;
    }

    public void setCodeAgence(Integer codeAgence) {
        this.codeAgence = codeAgence;
    }

    public String getLibelleAgence() {
        return libelleAgence;
    }

    public void setLibelleAgence(String libelleAgence) {
        this.libelleAgence = libelleAgence;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public int getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(int expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getOffice_code() {
        return office_code;
    }

    public void setOffice_code(String office_code) {
        this.office_code = office_code;
    }

   

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilAgent() {
        return profilAgent;
    }

    public void setProfilAgent(String profilAgent) {
        this.profilAgent = profilAgent;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
}
