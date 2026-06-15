package service_administration_api.entite;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
//@Getter
//@Setter
@Entity
@Table(name = "ZEN_ATTDIG_ASAC")

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ZenAttdigAsac implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "IMMATRICULATION_DU_VEHICULE")
    private String immatriculation;

    // Organisation
    @Column(name = "ORGANIZATION_CODE")
    private String organizationCode;

    @Column(name = "OFFICE_CODE")
    private Integer officeCode;

    @Column(name = "CERTIFICATE_TYPE")
    private String certificateType;

    @Column(name = "COULEUR_D_ATTESTATION_A_EDITER")
    private String couleurAttestation;

    @Column(name = "PRIME_RC")
    private Integer primeRc;

    // Véhicule
    @Column(name = "ENERGIE_DU_VEHICULE")
    private String energie;

    @Column(name = "NUMERO_DE_CHASSIS_DU_VEHICULE")
    private String numeroChassis;

    @Column(name = "MODELE_DU_VEHICULE")
    private String modele;

    @Column(name = "GENRE_DU_VEHICULE")
    private String genre;

    @Column(name = "CATEGORIE_DU_VEHICULE")
    private String categorie;

    @Column(name = "USAGE_DU_VEHICULE")
    private String usage;

    @Column(name = "MARQUE_DU_VEHICULE")
    private String marque;

    @Column(name = "TYPE_DU_VEHICULE")
    private String type;

    @Column(name = "NOMBRE_DE_PLACE_DU_VEHICULE")
    private Integer nombrePlaces;

    @Column(name = "PUISSANCE_FISCALE")
    private Integer puissanceFiscale;

    @Column(name = "ZONE_DE_CIRCULATION")
    private String zoneCirculation;

    @Column(name = "DATE_MISE_EN_CIRCULATION")
    private LocalDate dateMiseCirculation;

    // Souscripteur
    @Column(name = "TYPE_DE_SOUSCRIPTEUR")
    private String typeSouscripteur;

    @Column(name = "NUMERO_DE_TEL_DU_SOUSCRIPTEUR")
    private String telSouscripteur;

    @Column(name = "BOITE_POSTALE_DU_SOUSCRIPTEUR")
    private String bpSouscripteur;

    @Column(name = "ADRESSE_EMAIL_DU_SOUSCRIPTEUR")
    private String emailSouscripteur;

    @Column(name = "NOM_DU_SOUSCRIPTEUR")
    private String nomSouscripteur;

    // Assuré
    @Column(name = "TELEPHONE_MOBILE_DE_L_ASSURE")
    private String telAssure;

    @Column(name = "BOITE_POSTALE_DE_L_ASSURE")
    private String bpAssure;

    @Column(name = "ADRESSE_EMAIL_DE_L_ASSURE")
    private String emailAssure;

    @Column(name = "NOM_DE_L_ASSURE")
    private String nomAssure;

    @Column(name = "DATE_DE_NAISSANCE_DE_L_ASSURE")
    private LocalDate dateNaissanceAssure;

    // Contrat
    @Column(name = "DATE_D_EFFET_DU_CONTRAT")
    private LocalDate dateEffet;

    @Column(name = "DATE_D_ECHEANCE_DU_CONTRAT")
    private LocalDate dateEcheance;

    @Column(name = "NUMERO_DE_POLICE")
    private String numeroPolice;

    // Conducteur
    @Column(name = "NOM_PRENOM_CONDUCTEUR")
    private String nomConducteur;

    @Column(name = "DATE_DE_NAISSANCE_CONDUCTEUR")
    private LocalDate dateNaissanceConducteur;

    @Column(name = "NUMERO_PERMIS")
    private String numeroPermis;

    @Column(name = "CATEGORIE_PERMIS")
    private String categoriePermis;

    @Column(name = "DATE_DE_DELIVRANCE_PERMIS")
    private LocalDate dateDelivrancePermis;
    
        @Column(name = "CODE_ASSURE")
    private String codeassure;
        
          @Column(name = "PROFESSION_DE_L_ASSURE")
    private String codeassureprof;
          
            @Column(name = "VILLE_DE_L_ASSURE")
    private String assureville;
           @Column(name = "TAXPAYER_NUMBER")
    private String numerocontribuable;    
           
            @Column(name = "VEHICLE_GROSS_WEIGHT")
    private Integer vehiculpoids;

    public String getCodeassure() {
        return codeassure;
    }

    public void setCodeassure(String codeassure) {
        this.codeassure = codeassure;
    }

    public String getCodeassureprof() {
        return codeassureprof;
    }

    public void setCodeassureprof(String codeassureprof) {
        this.codeassureprof = codeassureprof;
    }

    public String getAssureville() {
        return assureville;
    }

    public void setAssureville(String assureville) {
        this.assureville = assureville;
    }

    public String getNumerocontribuable() {
        return numerocontribuable;
    }

    public void setNumerocontribuable(String numerocontribuable) {
        this.numerocontribuable = numerocontribuable;
    }

    public Integer getVehiculpoids() {
        return vehiculpoids;
    }

    public void setVehiculpoids(Integer vehiculpoids) {
        this.vehiculpoids = vehiculpoids;
    }
            
           

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public Integer getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(Integer officeCode) {
        this.officeCode = officeCode;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCouleurAttestation() {
        return couleurAttestation;
    }

    public void setCouleurAttestation(String couleurAttestation) {
        this.couleurAttestation = couleurAttestation;
    }

    public Integer getPrimeRc() {
        return primeRc;
    }

    public void setPrimeRc(Integer primeRc) {
        this.primeRc = primeRc;
    }

    public String getEnergie() {
        return energie;
    }

    public void setEnergie(String energie) {
        this.energie = energie;
    }

    public String getNumeroChassis() {
        return numeroChassis;
    }

    public void setNumeroChassis(String numeroChassis) {
        this.numeroChassis = numeroChassis;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(Integer nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public Integer getPuissanceFiscale() {
        return puissanceFiscale;
    }

    public void setPuissanceFiscale(Integer puissanceFiscale) {
        this.puissanceFiscale = puissanceFiscale;
    }

    public String getZoneCirculation() {
        return zoneCirculation;
    }

    public void setZoneCirculation(String zoneCirculation) {
        this.zoneCirculation = zoneCirculation;
    }

    public LocalDate getDateMiseCirculation() {
        return dateMiseCirculation;
    }

    public void setDateMiseCirculation(LocalDate dateMiseCirculation) {
        this.dateMiseCirculation = dateMiseCirculation;
    }

    public String getTypeSouscripteur() {
        return typeSouscripteur;
    }

    public void setTypeSouscripteur(String typeSouscripteur) {
        this.typeSouscripteur = typeSouscripteur;
    }

    public String getTelSouscripteur() {
        return telSouscripteur;
    }

    public void setTelSouscripteur(String telSouscripteur) {
        this.telSouscripteur = telSouscripteur;
    }

    public String getBpSouscripteur() {
        return bpSouscripteur;
    }

    public void setBpSouscripteur(String bpSouscripteur) {
        this.bpSouscripteur = bpSouscripteur;
    }

    public String getEmailSouscripteur() {
        return emailSouscripteur;
    }

    public void setEmailSouscripteur(String emailSouscripteur) {
        this.emailSouscripteur = emailSouscripteur;
    }

    public String getNomSouscripteur() {
        return nomSouscripteur;
    }

    public void setNomSouscripteur(String nomSouscripteur) {
        this.nomSouscripteur = nomSouscripteur;
    }

    public String getTelAssure() {
        return telAssure;
    }

    public void setTelAssure(String telAssure) {
        this.telAssure = telAssure;
    }

    public String getBpAssure() {
        return bpAssure;
    }

    public void setBpAssure(String bpAssure) {
        this.bpAssure = bpAssure;
    }

    public String getEmailAssure() {
        return emailAssure;
    }

    public void setEmailAssure(String emailAssure) {
        this.emailAssure = emailAssure;
    }

    public String getNomAssure() {
        return nomAssure;
    }

    public void setNomAssure(String nomAssure) {
        this.nomAssure = nomAssure;
    }

    public LocalDate getDateNaissanceAssure() {
        return dateNaissanceAssure;
    }

    public void setDateNaissanceAssure(LocalDate dateNaissanceAssure) {
        this.dateNaissanceAssure = dateNaissanceAssure;
    }

    public LocalDate getDateEffet() {
        return dateEffet;
    }

    public void setDateEffet(LocalDate dateEffet) {
        this.dateEffet = dateEffet;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getNumeroPolice() {
        return numeroPolice;
    }

    public void setNumeroPolice(String numeroPolice) {
        this.numeroPolice = numeroPolice;
    }

    public String getNomConducteur() {
        return nomConducteur;
    }

    public void setNomConducteur(String nomConducteur) {
        this.nomConducteur = nomConducteur;
    }

    public LocalDate getDateNaissanceConducteur() {
        return dateNaissanceConducteur;
    }

    public void setDateNaissanceConducteur(LocalDate dateNaissanceConducteur) {
        this.dateNaissanceConducteur = dateNaissanceConducteur;
    }

    public String getNumeroPermis() {
        return numeroPermis;
    }

    public void setNumeroPermis(String numeroPermis) {
        this.numeroPermis = numeroPermis;
    }

    public String getCategoriePermis() {
        return categoriePermis;
    }

    public void setCategoriePermis(String categoriePermis) {
        this.categoriePermis = categoriePermis;
    }

    public LocalDate getDateDelivrancePermis() {
        return dateDelivrancePermis;
    }

    public void setDateDelivrancePermis(LocalDate dateDelivrancePermis) {
        this.dateDelivrancePermis = dateDelivrancePermis;
    }
    
    
}