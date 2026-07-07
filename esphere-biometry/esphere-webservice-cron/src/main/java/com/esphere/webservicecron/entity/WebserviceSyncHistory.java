package com.esphere.webservicecron.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Trace chaque exécution d'une synchronisation (manuelle ou planifiée) :
 * import adhérents/ayants droit/taux de prestation, désactivations.
 * Permet de visualiser l'historique des passages de la tâche planifiée.
 */
@Entity
@Table(name = "webservice_sync_history")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WebserviceSyncHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ADHERENT | AYANT_DROIT | TAUX_PRESTATION | DESACTIVATION_ADHERENT | DESACTIVATION_AYANT_DROIT
    @Column(name = "type_synchronisation", nullable = false, length = 50)
    private String typeSynchronisation;

    // CRON | MANUEL
    @Column(name = "declencheur", nullable = false, length = 20)
    private String declencheur;

    @Column(name = "police_filtre")
    private String policeFiltre;

    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @Column(name = "date_fin")
    private LocalDateTime dateFin;

    // EN_COURS | SUCCES | ECHEC
    @Column(name = "statut", nullable = false, length = 20)
    private String statut;

    @Column(name = "nb_traites")
    private Integer nbTraites;

    @Column(name = "nb_echecs")
    private Integer nbEchecs;

    // length > 16_777_215 force Hibernate a mapper en LONGTEXT (et non TINYTEXT par defaut)
    @Lob
    @Column(name = "details_echecs", length = 16_777_216)
    private String detailsEchecs;

    @Lob
    @Column(name = "message_erreur", length = 16_777_216)
    private String messageErreur;
}
