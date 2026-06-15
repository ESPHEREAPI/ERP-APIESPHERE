/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite.pooltpv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "POOLTPV_RISQUE")
@Data
public class Risque_PoolTPV implements Serializable {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "code_compagnie")
    private Integer codeCompagnie;

    @Column(name = "code_intermediaire")
    private Integer codeIntermediaire;

    @Column(name = "num_police")
    private String numPolice;

    @Column(name = "flotte")
    private String flotte;

    @Column(name = "code_mouvement")
    private String codeMouvement;

    @Column(name = "avenant")
    private String avenant;

    @Column(name = "num_avenant")
    private String numAvenant;

    @Column(name = "date_emission")
    private LocalDate dateEmission;

    @Column(name = "heure_emission")
    private String heureEmission;

    @Column(name = "date_effet")
    private LocalDate dateEffet;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "code_duree")
    private String codeDuree;

    @Column(name = "duree")
    private String duree;

    @Column(name = "usage")
    private String usage;

    @Column(name = "immatriculation")
    private String immatriculation;

    @Column(name = "puissance")
    private Integer puissance;

    @Column(name = "code_genre_auto")
    private String codeGenreAuto;

    @Column(name = "genre_auto")
    private String genreAuto;

    @Column(name = "libelle_marque")
    private String libelleMarque;

    @Column(name = "type_veh")
    private String typeVeh;

    @Column(name = "nbre_places")
    private Integer nbrePlaces;

    @Column(name = "date_mc")
    private LocalDate dateMc;

    @Column(name = "poids_vide")
    private Double poidsVide;

    @Column(name = "valeur_venale")
    private Double valeurVenale;

    @Column(name = "valeur_neuve")
    private Double valeurNeuve;

    @Column(name = "categorie")
    private Integer categorie;

    @Column(name = "sous_categorie")
    private String sousCategorie;

    @Column(name = "num_chassis")
    private String numChassis;

    @Column(name = "code_client")
    private String codeClient;

    @Column(name = "nom_client")
    private String nomClient;

    @Column(name = "num_client")
    private String numClient;

    @Column(name = "tel_client")
    private String telClient;

    @Column(name = "adresse_client")
    private String adresseClient;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "civilite")
    private String civilite;

    @Column(name = "code_profession")
    private String codeProfession;

    @Column(name = "profession")
    private String profession;

    @Column(name = "sexe")
    private String sexe;

    @Column(name = "type_piece")
    private String typePiece;

    @Column(name = "num_piece")
    private String numPiece;

    @Column(name = "situation_mat")
    private String situationMat;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "rc")
    private Double rc;

    @Column(name = "dr")
    private Double dr;

    @Column(name = "bdg")
    private Double bdg;

    @Column(name = "ipt")
    private Double ipt;

    @Column(name = "inc")
    private Double inc;

    @Column(name = "vol")
    private Double vol;

    @Column(name = "prime_nette")
    private Double primeNette;

    @Column(name = "droit_timbre")
    private Double droitTimbre;

    @Column(name = "prime_ttc")
    private Double primeTtc;

    @Column(name = "attestation")
    private String attestation;

    @Column(name = "carte_rose")
    private String carteRose;

    @Column(name = "code_energie")
    private String codeEnergie;

    @Column(name = "dta")
    private Double dta;

    @Column(name = "dtc")
    private Double dtc;
    @Column(name = "replication", nullable = false)
    private Boolean replication = false;

}
