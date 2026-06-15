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
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "POOLTPV_POLICE")
@Data
@Builder
public class Police_PoolTPV implements Serializable {

    private static final long serialVersionUID = 1L;
   @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_police_pooltpv")
    @SequenceGenerator(name = "seq_police_pooltpv", sequenceName = "SEQ_POLICE_POOLTPV", allocationSize = 1)
    private Long id;

     @Column(name = "code_compagnie")
    private Integer codeCompagnie;

    @Column(name = "code_intermediaire")
    private Integer codeIntermediaire;

    @Column(name = "num_police", unique = true)
    private String numPolice;

    @Column(name = "code_categorie")
    private String codeCategorie;

    @Column(name = "flotte")
    private String flotte;

    @Column(name = "nombre_vehicule")
    private Integer nombreVehicule;

    @Column(name = "code_mouvement")
    private String codeMouvement;

    @Column(name = "avenant")
    private String avenant;

    @Column(name = "num_avenant")
    private String numAvenant;

    @Column(name = "date_emission")
    private LocalDate dateEmission;

    @Column(name = "date_effet")
    private LocalDate dateEffet;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "code_duree")
    private String codeDuree;

    @Column(name = "code_assure")
    private String codeAssure;

    @Column(name = "nom_assure")
    private String nomAssure;

    @Column(name = "prenom_assure")
    private String prenomAssure;

    @Column(name = "adresse_assure")
    private String adresseAssure;

    @Column(name = "code_profession")
    private String codeProfession;

    @Column(name = "genre_assure")
    private String genreAssure;

    @Column(name = "numero_quittance")
    private String numeroQuittance;

    @Column(name = "prime_nette")
    private Double primeNette;

    @Column(name = "accessoire")
    private Double accessoire;

    @Column(name = "tva")
    private Double tva;

    @Column(name = "fc")
    private Double fc;

    @Column(name = "carte_rose")
    private Double carteRose;

    @Column(name = "droit_timbre")
    private Double droitTimbre;

    @Column(name = "prime_ttc")
    private Double primeTtc;

    @Column(name = "etat_encaissement")
    private String etatEncaissement;

    @Column(name = "mode_encaissement")
    private String modeEncaissement;
    @Column(name = "replication", nullable = false)
private Boolean replication = false;
    
}
