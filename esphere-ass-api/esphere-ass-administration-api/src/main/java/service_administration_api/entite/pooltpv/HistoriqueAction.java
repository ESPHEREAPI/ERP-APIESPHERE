package service_administration_api.entite.pooltpv;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ZEN_HISTORIQUE_ACTIONS")
@Getter @Setter
public class HistoriqueAction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hist_seq")
    @SequenceGenerator(name = "hist_seq", sequenceName = "SEQ_HISTORIQUE_ACTIONS", allocationSize = 1)
    private Long id;

    @Column(name = "CERTIFICATE_REFERENCE", nullable = false, length = 100)
    private String certificateReference;

    @Column(name = "TYPE_ACTION", nullable = false, length = 20)
    private String typeAction;          // SUSPENSION | ANNULATION

    @Column(name = "MOTIF_CODE", nullable = false, length = 10)
    private String motifCode;

    @Column(name = "MOTIF_LIBELLE", length = 200)
    private String motifLibelle;

    @Column(name = "USERNAME_EXECUTANT", nullable = false, length = 100)
    private String usernameExecutant;

    @Column(name = "PROFIL_EXECUTANT", length = 40)
    private String profilExecutant;

    @Column(name = "OFFICE_CODE", length = 50)
    private String officeCode;

    @Column(name = "STATUT_AVANT", length = 50)
    private String statutAvant;

    @Column(name = "STATUT_APRES", length = 50)
    private String statutApres;

    @Column(name = "REPONSE_API", length = 500)
    private String reponseApi;

    @Column(name = "DATE_ACTION", nullable = false)
    private LocalDateTime dateAction = LocalDateTime.now();

    public HistoriqueAction() {}
}
