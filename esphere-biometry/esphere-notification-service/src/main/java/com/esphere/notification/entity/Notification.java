package com.esphere.notification.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dbx45ty_notification")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // prestataire_id ou employe_id
    @Column(name = "destinataire_id", nullable = false)
    private String destinataireId;

    // prestataire | agent_ss | assure
    @Column(name = "type_dest", nullable = false)
    private String typeDest;

    // sms | email | alerte
    @Column(name = "canal", nullable = false)
    private String canal;

    @Column(name = "sujet")
    private String sujet;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email_dest")
    private String emailDest;

    // en_attente | envoye | echec | lu
    @Column(name = "statut", nullable = false)
    private String statut;

    // prestation_validee | prestation_rejetee | bon_confirme |
    // bon_rejete | prestation_soumise | bon_cree | video_uploadee | manuel
    @Column(name = "event_type", nullable = false)
    private String eventType;

    // ID de la ressource concernée
    @Column(name = "reference_id")
    private String referenceId;

    // 0 = non lu | 1 = lu
    @Column(name = "lu", nullable = false)
    private Boolean lu;

    // NULL = auto | employe_id = manuel
    @Column(name = "envoye_par")
    private Integer envoyePar;

    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    @Column(name = "date_envoi")
    private LocalDateTime dateEnvoi;

    @Column(name = "date_creation", nullable = false)
    private LocalDateTime dateCreation;

    @Column(name = "erreur")
    private String erreur;
}