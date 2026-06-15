/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Entité JPA mappée sur ORASSADM.INTERMEDIAIRE — Oracle 11g
 *
 * Compatibilité :
 *  - Java 17 / 21
 *  - Jakarta Persistence 3.x (ou javax.persistence 2.x selon le projet)
 *  - Hibernate 6.x  (dialecte : OracleDialect / Oracle10gDialect)
 *  - Driver : ojdbc8 (rétrocompatible 11g)
 */
@Entity
@Table(
    name        = "INTERMEDIAIRE",
    //schema      = "ORASSADM",
    uniqueConstraints = @UniqueConstraint(
        name        = "CLE_INTERMEDIAIRE",
        columnNames = {"CODEINTE"}
    )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Intermediaire {

    // ══════════════════════════════════════════════════════
    //  CLÉ PRIMAIRE
    //  Oracle 11g : pas d'IDENTITY → séquence dédiée
    // ══════════════════════════════════════════════════════

    @Id
    @Column(name = "CODEINTE", nullable = false, precision = 5, scale = 0)
    // Si une séquence Oracle alimente CODEINTE, décommentez :
    // @GeneratedValue(strategy = GenerationType.SEQUENCE,
    //                generator  = "SEQ_INTERMEDIAIRE")
    // @SequenceGenerator(name           = "SEQ_INTERMEDIAIRE",
    //                    sequenceName   = "SEQ_INTERMEDIAIRE",
    //                    schema         = "ORASSADM",
    //                    allocationSize = 1)
    private Integer codeInte;

    // ══════════════════════════════════════════════════════
    //  INFORMATIONS GÉNÉRALES
    // ══════════════════════════════════════════════════════

    @Column(name = "RAISOCIN", length = 30)
    private String raiSocIn;

    @Column(name = "ABREINTE", length = 60)
    private String abreInte;

    @Column(name = "ADREINTE", length = 100)
    private String adreInte;

    @Column(name = "ADREMAIL", length = 60)
    private String adreMail;

    @Column(name = "NOM_RESP", length = 100)
    private String nomResp;

    // ══════════════════════════════════════════════════════
    //  COORDONNÉES
    // ══════════════════════════════════════════════════════

    @Column(name = "TELEINTE", length = 40)
    private String teleInte;

    @Column(name = "TELEXINT", length = 20)
    private String telexInt;

    @Column(name = "FAXINTER", length = 40)
    private String faxInter;

    // ══════════════════════════════════════════════════════
    //  IDENTIFIANTS LÉGAUX / FISCAUX
    // ══════════════════════════════════════════════════════

    @Column(name = "NUMEPATE", precision = 10, scale = 0)
    private Long numePate;

    @Column(name = "NUMEIMPO", length = 20)
    private String numeImpo;

    @Column(name = "NUME_TVA", length = 20)
    private String numeTva;

    @Column(name = "NUMECNSS", precision = 8, scale = 0)
    private Integer numeCnss;

    /** NUMBER(15,2) → BigDecimal obligatoire pour la précision monétaire */
    @Column(name = "CAPISOCI", precision = 15, scale = 2)
    private BigDecimal capiSoci;

    @Column(name = "NUMEAGRE", length = 6)
    private String numeAgre;

    @Column(name = "REGICOMM", length = 20)
    private String regiComm;

    @Column(name = "ANCICODE", length = 12)
    private String anciCode;

    // ══════════════════════════════════════════════════════
    //  DATES
    //  IMPORTANT Oracle 11g : DATE = date + heure
    //  → java.util.Date + @Temporal(DATE) pour ne garder que la date
    // ══════════════════════════════════════════════════════

    @Temporal(TemporalType.DATE)
    @Column(name = "DATENOMI")
    private Date dateNomi;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATFINAC")
    private Date datFinAc;

    // ══════════════════════════════════════════════════════
    //  CLASSIFICATION
    // ══════════════════════════════════════════════════════

    /** FK → TYPE_INTERMEDIAIRE.CODTYPIN */
    @Column(name = "CODTYPIN", nullable = false, length = 1)
    private String codTypIn;

    /** FK → VILLE.CODEVILL */
    @Column(name = "CODEVILL", nullable = false, precision = 5, scale = 0)
    private Integer codeVill;

    @Column(name = "CODEREGR", precision = 1, scale = 0)
    private Integer codeRegr;

    // ══════════════════════════════════════════════════════
    //  LIAISONS / RATTACHEMENTS
    // ══════════════════════════════════════════════════════

    /**
     * Auto-référence : rattachement technique.
     * LAZY obligatoire pour éviter les boucles infinies.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name                 = "LIENINTE",
        referencedColumnName = "CODEINTE",
        foreignKey           = @ForeignKey(name = "REF_INTERMEDIAIRE_INTE")
    )
    private Intermediaire lienInte;

    /** Rattachement réassurance (simple code, pas de ManyToOne ici) */
    @Column(name = "LIEINTRE", precision = 5, scale = 0)
    private Integer lieIntRe;

    /** Rattachement comptable */
    @Column(name = "LIEINTCO", precision = 5, scale = 0)
    private Integer lieIntCo;

    // ══════════════════════════════════════════════════════
    //  COMPTABILITÉ — tous NUMBER(15) → Long
    // ══════════════════════════════════════════════════════

    @Column(name = "COMPCOMM", precision = 15, scale = 0)
    private Long compComm;

    @Column(name = "COMCOMAP", precision = 15, scale = 0)
    private Long comComAp;

    @Column(name = "COMPSINI", precision = 15, scale = 0)
    private Long compSini;

    @Column(name = "COMPRECO", precision = 15, scale = 0)
    private Long compReco;

    @Column(name = "RECOASSU", precision = 15, scale = 0)
    private Long recoAssu;

    @Column(name = "COMRECAS", precision = 15, scale = 0)
    private Long comRecAs;

    @Column(name = "COMPENCA", precision = 15, scale = 0)
    private Long compEnca;

    @Column(name = "COMPFRAI", precision = 15, scale = 0)
    private Long compFrai;

    @Column(name = "COMPREMB", precision = 15, scale = 0)
    private Long compRemb;

    @Column(name = "COSECOAS", precision = 2, scale = 0)
    private Integer coSecOas;

    @Column(name = "DEDUBASE", length = 1)
    private String deduBase;

    // ══════════════════════════════════════════════════════
    //  PARAMÉTRAGE / FLAGS
    //  Oracle 11g : pas de BOOLEAN natif → VARCHAR2(1) O/N
    // ══════════════════════════════════════════════════════

    /**
     * Géré par le trigger T_INTERMEDIAIRE :
     *  - INSERT  → forcé à NULL par le trigger
     *  - DELETE  → bloqué si non null
     *  - UPDATE  → remis à 0 si inchangé et non null
     */
    @Column(name = "NUME_LOT")
    private Long numeLot;

    /** FK → JOURNAL_COMPTABLE.CODEJOUR */
    @Column(name = "JOURREAS", length = 3)
    private String jourReas;

    /** Défaut Oracle : 10 */
    @Column(name = "SAUTAUTO")
    @Builder.Default
    private Integer sautAuto = 10;

    @Column(name = "FLAGTIMB", length = 1)
    private String flagTimb;

    /** N = production réelle | O = test — défaut Oracle : 'N' */
    @Column(name = "FLAGTEST", length = 1)
    @Builder.Default
    private String flagTest = "N";

    /** O = fait la production | N = ne fait pas — défaut Oracle : 'O' */
    @Column(name = "FLAGPROD", length = 1)
    @Builder.Default
    private String flagProd = "O";

    /** Activer lien sur feuilles de caisse — défaut Oracle : 'N' */
    @Column(name = "FLAGLIEN", length = 2)
    @Builder.Default
    private String flagLien = "N";

    /** Flag pool TPV — défaut Oracle : 'N' */
    @Column(name = "FLAGPOOL", length = 2)
    @Builder.Default
    private String flagPool = "N";

    /** Délai de paiement en jours — défaut Oracle : 0 */
    @Column(name = "DELAENCA")
    @Builder.Default
    private Integer delaEnca = 0;

    /** Entité gestion comptable O/N — défaut Oracle : 'N' */
    @Column(name = "ENTGESCO", length = 2)
    @Builder.Default
    private String entGesco = "N";

    /** Compte en cage O/N — défaut Oracle : 'O' */
    @Column(name = "CPTENCAG", length = 1)
    @Builder.Default
    private String cptEncAg = "O";

    // ══════════════════════════════════════════════════════
    //  LOGO — BLOB Oracle 11g
    //  fetch LAZY indispensable : ne pas charger le binaire
    //  à chaque requête SELECT sur la table
    // ══════════════════════════════════════════════════════

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "LOGOINTE")
    private byte[] logoInte;

    // ══════════════════════════════════════════════════════
    //  COMMENTAIRES MÉTIER (VARCHAR2 long)
    // ══════════════════════════════════════════════════════

    @Column(name = "COMSOCHP", length = 600)
    private String comSocHp;

    @Column(name = "COMSOCBH", length = 600)
    private String comSocBh;

    @Column(name = "COMSOCBB", length = 600)
    private String comSocBb;

    // ══════════════════════════════════════════════════════
    //  BANCASSURANCE
    // ══════════════════════════════════════════════════════

    @Column(name = "CODBANBA", precision = 6, scale = 0)
    private Integer codBanBa;

    @Column(name = "CODAGEBA", precision = 10, scale = 0)
    private Long codAgeBa;

    // ══════════════════════════════════════════════════════
    //  CYCLE DE VIE — réplication du trigger Oracle
    // ══════════════════════════════════════════════════════

    /**
     * Réplique le comportement INSERT du trigger T_INTERMEDIAIRE :
     * NUME_LOT est toujours null à la création.
     * (Le trigger côté Oracle reste prioritaire, ceci est une sécurité côté applicatif.)
     */
    @PrePersist
    private void prePersist() {
        this.numeLot = null;
    }

    // ══════════════════════════════════════════════════════
    //  equals / hashCode — basés sur la PK métier
    // ══════════════════════════════════════════════════════

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Intermediaire that)) return false;
        return codeInte != null && codeInte.equals(that.codeInte);
    }

    @Override
    public int hashCode() {
        // Stable avant et après persist (recommandation Hibernate)
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Intermediaire{codeInte=%d, raiSocIn='%s', codTypIn='%s', flagProd='%s'}"
                .formatted(codeInte, raiSocIn, codTypIn, flagProd);
    }
}
