/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO;

/**
 *
 * @author USER01
 */
import jakarta.validation.constraints.*;

public class InfosAdminAgenceDTO {

    private Long id;

    @NotNull(message = "Le code agence est obligatoire")
    private Integer codeAgence;

    @NotBlank(message = "Le libellé agence est obligatoire")
    private String libelleAgence;

    @Email(message = "Email invalide")
    private String email;

    @NotBlank(message = "Le login est obligatoire")
    private String login;

    private String clientName;

    @Min(value = 1, message = "La durée d'expiration doit être ≥ 1")
    private int expiresAt = 24;

    private String officeCode;

    @NotBlank(message = "Le username est obligatoire")
    private String username;

    @NotBlank(message = "Le profil agent est obligatoire")
    private String profilAgent = "PRODUCTEUR";

    private boolean canEdit = false;

    // ---- constructors ----
    public InfosAdminAgenceDTO() {}

    // ---- getters / setters ----
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getCodeAgence() { return codeAgence; }
    public void setCodeAgence(Integer codeAgence) { this.codeAgence = codeAgence; }

    public String getLibelleAgence() { return libelleAgence; }
    public void setLibelleAgence(String libelleAgence) { this.libelleAgence = libelleAgence; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public int getExpiresAt() { return expiresAt; }
    public void setExpiresAt(int expiresAt) { this.expiresAt = expiresAt; }

    public String getOfficeCode() { return officeCode; }
    public void setOfficeCode(String officeCode) { this.officeCode = officeCode; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getProfilAgent() { return profilAgent; }
    public void setProfilAgent(String profilAgent) { this.profilAgent = profilAgent; }

    public boolean isCanEdit() { return canEdit; }
    public void setCanEdit(boolean canEdit) { this.canEdit = canEdit; }
}


