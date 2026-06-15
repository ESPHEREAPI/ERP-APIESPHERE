package service_administration_api.entite.pooltpv;

import jakarta.persistence.*;
import service_administration_api.enums.TypeMouvement;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * Log immuable de chaque mouvement de stock.
 * Un mouvement ne doit jamais être modifié — seules des entrées correctives
 * (AJUSTEMENT_PLUS / AJUSTEMENT_MOINS / ANNULATION) sont admises.
 *
 * Table : ZEN_MOUVEMENTS_STOCK
 */
@Entity
@Table(name = "ZEN_MOUVEMENTS_STOCK")
public class MouvementStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_mouv_stock")
    @SequenceGenerator(name = "seq_mouv_stock", sequenceName = "SEQ_MOUVEMENTS_STOCK", allocationSize = 1)
    private Long id;

    // ── Stock concerné ──────────────────────────────────────────
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STOCK_ID", nullable = false)
    private StockAttestation stockAttestation;

    // ── Dénormalisation pour requêtes historique sans JOIN ───────
    @Column(name = "OFFICE_CODE", nullable = false, length = 50)
    private String officeCode;

    @Column(name = "CERT_TYPE_CODE", length = 50)
    private String certTypeCode;

    @Column(name = "CERT_VARIANT_CODE", length = 50)
    private String certVariantCode;

    // ── Mouvement ───────────────────────────────────────────────
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE_MOUVEMENT", nullable = false, length = 30)
    private TypeMouvement typeMouvement;

    /** Toujours positif — le signe est porté par typeMouvement. */
    @Column(name = "QUANTITE", nullable = false)
    private Integer quantite;

    /** Snapshot du stock avant le mouvement. */
    @Column(name = "QUANTITE_AVANT", nullable = false)
    private Integer quantiteAvant;

    /** Snapshot du stock après le mouvement. */
    @Column(name = "QUANTITE_APRES", nullable = false)
    private Integer quantiteApres;

    // ── Traçabilité ─────────────────────────────────────────────
    /** Référence de la production (DESTOCKAGE/ANNULATION) ou du BL (APPROVISIONNEMENT). */
    @Column(name = "REFERENCE_SOURCE", length = 100)
    private String referenceSource;

    @Column(name = "MOTIF", length = 500)
    private String motif;

    @Column(name = "CREATED_AT", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "CREATED_BY", length = 100)
    private String createdBy;

    // ────────────────────────────────────────────────────────────
    // Constructeurs
    // ────────────────────────────────────────────────────────────

    public MouvementStock() {}

    // ────────────────────────────────────────────────────────────
    // Getters / Setters
    // ────────────────────────────────────────────────────────────

    public Long getId() { return id; }

    public StockAttestation getStockAttestation() { return stockAttestation; }
    public void setStockAttestation(StockAttestation stockAttestation) { this.stockAttestation = stockAttestation; }

    public String getOfficeCode() { return officeCode; }
    public void setOfficeCode(String officeCode) { this.officeCode = officeCode; }

    public String getCertTypeCode() { return certTypeCode; }
    public void setCertTypeCode(String certTypeCode) { this.certTypeCode = certTypeCode; }

    public String getCertVariantCode() { return certVariantCode; }
    public void setCertVariantCode(String certVariantCode) { this.certVariantCode = certVariantCode; }

    public TypeMouvement getTypeMouvement() { return typeMouvement; }
    public void setTypeMouvement(TypeMouvement typeMouvement) { this.typeMouvement = typeMouvement; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public Integer getQuantiteAvant() { return quantiteAvant; }
    public void setQuantiteAvant(Integer quantiteAvant) { this.quantiteAvant = quantiteAvant; }

    public Integer getQuantiteApres() { return quantiteApres; }
    public void setQuantiteApres(Integer quantiteApres) { this.quantiteApres = quantiteApres; }

    public String getReferenceSource() { return referenceSource; }
    public void setReferenceSource(String referenceSource) { this.referenceSource = referenceSource; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
