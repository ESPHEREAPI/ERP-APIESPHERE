package com.esphere.bonmanuel.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "dbx45ty_bon_manuel")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BonManuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // BM-{ANNEE}-{ID_PRESTA}-{SEQ}
    @Column(name = "reference", nullable = false, unique = true)
    private String reference;

    // Référence proforma du prestataire - unique globalement
    @Column(name = "numero_proforma", nullable = false, unique = true)
    private String numeroProforma;

    @Column(name = "visite_id", nullable = false)
    private String visiteId;

    @Column(name = "prestataire_id", nullable = false)
    private String prestataireId;

    @Column(name = "code_adherent", nullable = false)
    private String codeAdherent;

    // NULL si assuré direct
    @Column(name = "code_ayant_droit")
    private String codeAyantDroit;

    // Agent SS qui confirme
    @Column(name = "employe_id")
    private Integer employeId;

    // Prestataire qui encaisse
    @Column(name = "employe_encaisse_id")
    private Integer employeEncaisseId;

    // Montant du proforma initial
    @Column(name = "montant_proforma", nullable = false)
    private Double montantProforma;

    // Montant après vérification SS
    @Column(name = "montant_confirme")
    private Double montantConfirme;

    // global | detail
    @Column(name = "type_validation")
    private String typeValidation;

    // en_attente | confirme | encaisse | rejete
    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "observations")
    private String observations;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_confirmation")
    private LocalDateTime dateConfirmation;

    @Column(name = "date_encaissement")
    private LocalDateTime dateEncaissement;

    // supprime : -1 = non supprimé | 1 = supprimé
    @Column(name = "supprime", nullable = false)
    private String supprime;

    @OneToMany(mappedBy = "bonManuel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BonManuelLigne> lignes;
}