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
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "POOLTPV_QUITTANCE")
@Data
public class Quittance_PoolTPV implements Serializable {

    @Id
    @Column(name = "nume_quittance")
    private String numeQuittance;

    @Column(name = "nume_police")
    private String numePolice;

    @Column(name = "nume_avenant")
    private Integer numeAvenant;

    @Column(name = "nature_mouvement")
    private String natureMouvement;

    @Column(name = "code_assure")
    private String codeAssure;

    @Column(name = "date_encaissement")
    private LocalDateTime dateEncaissement;

    @Column(name = "mode_paiement")
    private String modePaiement;

    @Column(name = "ref_encaissement")
    private String refEncaissement;

    @Column(name = "montant_encaisse")
    private Long montantEncaisse;

    @Column(name = "prime_totale")
    private Long primeTotale;

    @Column(name = "statut")
    private Integer statut;

    @Column(name = "numero_encaissement")
    private String numeroEncaissement;
    @Column(name = "replication", nullable = false)
private Boolean replication = false;

}
