/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.entite;

import db.biometry.biometry.enums.PasswordMode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "subscribers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "passwordHash")

public class Subscribers implements Serializable {

   @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "policy_number", nullable = false, unique = true, length = 20)
    private String policyNumber;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "phone_number", nullable = false, length = 25)
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "username", nullable = false, unique = true, length = 60)
    private String username;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "password_mode", nullable = false, length = 20)
    @Builder.Default
    private PasswordMode passwordMode = PasswordMode.MANUAL;

    // ── Activation par lien ──────────────────────────────────────────────────
    @Column(name = "activation_token", length = 255)
    private String activationToken;

    @Column(name = "activation_token_expiry")
    private Instant activationTokenExpiry;

    @Column(name = "activation_sent_at")
    private Instant activationSentAt;

    @Column(name = "activation_duration_hrs")
    private Integer activationDurationHrs;

    @Column(name = "account_activated_at")
    private Instant accountActivatedAt;

    // ── Audit ────────────────────────────────────────────────────────────────
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "created_by", length = 60)
    private String createdBy;

    @Column(name = "updated_by", length = 60)
    private String updatedBy;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
    
     @Column(name = "effet_police")
    @Temporal(TemporalType.DATE)
    private Date effetPolice;
    @Column(name = "echeance_police")
    @Temporal(TemporalType.DATE)
    private Date echeancePolice;

    // ── Helper ───────────────────────────────────────────────────────────────
    public boolean isActivationTokenValid() {
        return activationToken != null
                && activationTokenExpiry != null
                && Instant.now().isBefore(activationTokenExpiry);
    }

    public boolean getActive() {
     //   throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
   return active;
    }

    public void setActive(boolean b) {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    this.active=b;
    
    }
    
}
