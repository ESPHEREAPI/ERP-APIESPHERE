package service_administration_api.entite.pooltpv;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ZEN_MOTIFS_ACTION")
@Getter @Setter
public class MotifAction implements Serializable {

    @Id
    @Column(name = "CODE", length = 10)
    private String code;

    @Column(name = "TYPE_ACTION", nullable = false, length = 20)
    private String typeAction;   // ANNULATION | SUSPENSION | RESILIATION

    @Column(name = "LIBELLE", nullable = false, length = 200)
    private String libelle;

    @Column(name = "ACTIF", nullable = false)
    private Integer actif = 1;

    public MotifAction() {}
}
