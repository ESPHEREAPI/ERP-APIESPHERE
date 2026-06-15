package service_administration_api.entite.pooltpv;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Stock d'attestations par bureau et (optionnellement) par type/variante.
 *
 * Règle d'unicité :
 *   (officeCode, certTypeCode, certVariantCode) est une clé métier.
 *   certTypeCode = NULL  → stock global (toutes attestations confondues).
 *   certVariantCode = NULL → toutes variantes d'un même type.
 *
 * Table : ZEN_STOCK_ATTESTATIONS
 */
@Entity
@Table(
    name = "ZEN_STOCK_ATTESTATIONS",
    uniqueConstraints = @UniqueConstraint(
        name = "UK_STOCK_OFFICE_TYPE_VARIANT",
        columnNames = {"OFFICE_CODE", "CERT_TYPE_CODE", "CERT_VARIANT_CODE"}
    )
)
public class StockAttestation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_stock_att")
    @SequenceGenerator(name = "seq_stock_att", sequenceName = "SEQ_STOCK_ATTESTATIONS", allocationSize = 1)
    private Long id;

    // ── Bureau ──────────────────────────────────────────────────
    @Column(name = "OFFICE_CODE", nullable = false, length = 50)
    private String officeCode;

    @Column(name = "OFFICE_NAME", length = 200)
    private String officeName;

    @Column(name = "ORG_CODE", length = 50)
    private String orgCode;

    // ── Type / Variante (nullable = stock global) ────────────────
    @Column(name = "CERT_TYPE_CODE", length = 50)
    private String certTypeCode;

    @Column(name = "CERT_TYPE_NAME", length = 200)
    private String certTypeName;

    @Column(name = "CERT_VARIANT_CODE", length = 50)
    private String certVariantCode;

    @Column(name = "CERT_VARIANT_NAME", length = 200)
    private String certVariantName;

    // ── Quantités ───────────────────────────────────────────────
    @Column(name = "QUANTITE_DISPONIBLE", nullable = false)
    private Integer quantiteDisponible = 0;

    /** Réservées / en cours de traitement — non encore déduites. */
    @Column(name = "QUANTITE_RESERVEE", nullable = false)
    private Integer quantiteReservee = 0;

    @Column(name = "QUANTITE_TOTALE_APPRO", nullable = false)
    private Integer quantiteTotaleApprovisionnee = 0;

    @Column(name = "QUANTITE_TOTALE_CONSO", nullable = false)
    private Integer quantiteTotalConsommee = 0;

    // ── Seuils ──────────────────────────────────────────────────
    /** En-dessous de ce seuil → statut ALERTE. */
    @Column(name = "SEUIL_ALERTE")
    private Integer seuilAlerte = 50;

    /** En-dessous de ce seuil → statut CRITIQUE. */
    @Column(name = "SEUIL_CRITIQUE")
    private Integer seuilCritique = 10;

    // ── Statut calculé (persisté pour requêtes rapides) ─────────
    @Column(name = "STATUT", length = 20, nullable = false)
    private String statut = "NORMAL"; // NORMAL | ALERTE | CRITIQUE | RUPTURE

    // ── Audit ───────────────────────────────────────────────────
    @Column(name = "CREATED_AT")
    private OffsetDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private OffsetDateTime updatedAt;

    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;

    // ── Historique (relation inverse) ───────────────────────────
    @OneToMany(mappedBy = "stockAttestation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MouvementStock> mouvements = new ArrayList<>();

    // ────────────────────────────────────────────────────────────
    // Constructeurs
    // ────────────────────────────────────────────────────────────

    public StockAttestation() {}

    // ────────────────────────────────────────────────────────────
    // Méthodes métier
    // ────────────────────────────────────────────────────────────

    /** Recalcule et persiste le statut selon les seuils définis. */
    public void recalculerStatut() {
        int dispo = quantiteDisponible != null ? quantiteDisponible : 0;
        int critique = seuilCritique != null ? seuilCritique : 10;
        int alerte   = seuilAlerte   != null ? seuilAlerte   : 50;

        if (dispo <= 0)       this.statut = "RUPTURE";
        else if (dispo <= critique) this.statut = "CRITIQUE";
        else if (dispo <= alerte)   this.statut = "ALERTE";
        else                        this.statut = "NORMAL";
    }

    /** Vérifie si une quantité peut être déduite. */
    public boolean peutDeduire(int qte) {
        return quantiteDisponible != null && quantiteDisponible >= qte;
    }

    // ────────────────────────────────────────────────────────────
    // Getters / Setters
    // ────────────────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getOfficeCode() { return officeCode; }
    public void setOfficeCode(String officeCode) { this.officeCode = officeCode; }

    public String getOfficeName() { return officeName; }
    public void setOfficeName(String officeName) { this.officeName = officeName; }

    public String getOrgCode() { return orgCode; }
    public void setOrgCode(String orgCode) { this.orgCode = orgCode; }

    public String getCertTypeCode() { return certTypeCode; }
    public void setCertTypeCode(String certTypeCode) { this.certTypeCode = certTypeCode; }

    public String getCertTypeName() { return certTypeName; }
    public void setCertTypeName(String certTypeName) { this.certTypeName = certTypeName; }

    public String getCertVariantCode() { return certVariantCode; }
    public void setCertVariantCode(String certVariantCode) { this.certVariantCode = certVariantCode; }

    public String getCertVariantName() { return certVariantName; }
    public void setCertVariantName(String certVariantName) { this.certVariantName = certVariantName; }

    public Integer getQuantiteDisponible() { return quantiteDisponible; }
    public void setQuantiteDisponible(Integer quantiteDisponible) { this.quantiteDisponible = quantiteDisponible; }

    public Integer getQuantiteReservee() { return quantiteReservee; }
    public void setQuantiteReservee(Integer quantiteReservee) { this.quantiteReservee = quantiteReservee; }

    public Integer getQuantiteTotaleApprovisionnee() { return quantiteTotaleApprovisionnee; }
    public void setQuantiteTotaleApprovisionnee(Integer q) { this.quantiteTotaleApprovisionnee = q; }

    public Integer getQuantiteTotalConsommee() { return quantiteTotalConsommee; }
    public void setQuantiteTotalConsommee(Integer q) { this.quantiteTotalConsommee = q; }

    public Integer getSeuilAlerte() { return seuilAlerte; }
    public void setSeuilAlerte(Integer seuilAlerte) { this.seuilAlerte = seuilAlerte; }

    public Integer getSeuilCritique() { return seuilCritique; }
    public void setSeuilCritique(Integer seuilCritique) { this.seuilCritique = seuilCritique; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public List<MouvementStock> getMouvements() { return mouvements; }
    public void setMouvements(List<MouvementStock> mouvements) { this.mouvements = mouvements; }
}
