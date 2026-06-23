package com.esphere.media.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dbx45ty_media_prestation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaPrestation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "visite_id", nullable = false)
    private String visiteId;

    @Column(name = "code_adherent", nullable = false)
    private String codeAdherent;

    @Column(name = "code_ayant_droit")
    private String codeAyantDroit;

    @Column(name = "prestataire_id", nullable = false)
    private String prestataireId;

    @Column(name = "souscripteur", nullable = false)
    private String souscripteur;

    @Column(name = "police", nullable = false)
    private String police;

    @Column(name = "nom_fichier", nullable = false)
    private String nomFichier;

    @Column(name = "chemin", nullable = false)
    private String chemin;

    // image | document | video | autre
    @Column(name = "type_media", nullable = false)
    private String typeMedia;

    @Column(name = "extension", nullable = false)
    private String extension;

    // Taille en octets
    @Column(name = "taille", nullable = false)
    private Long taille;

    // 1 = demandé par agent SS | 0 = upload normal
//    @Column(name = "demande_par_ss", nullable = false)
//    private Integer demandeParSs;
    @Column(name = "employe_id")
    private Integer employeId;

    @Column(name = "date_upload", nullable = false)
    private LocalDateTime dateUpload;

    // supprime : -1 = non supprimé | 1 = supprimé
    @Column(name = "supprime", nullable = false)
    private String supprime;

    @Column(name = "demande_par_ss", nullable = false)
    private Boolean demandeParSs;

    @Column(name = "prestation_id")
    private Integer prestationId;

    @Column(name = "nature_prestation")
    private String naturePrestation;

    // en_attente_revue | approuve | rejete
    @Column(name = "statut_document", nullable = false)
    private String statutDocument;

    @Column(name = "commentaire_rejet", columnDefinition = "TEXT")
    private String commentaireRejet;
}
