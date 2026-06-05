package com.esphere.auth.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "dbx45ty_profil")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type_profil", nullable = false)
    private String typeProfil;

    @Column(name = "type_sous_profil")
    private String typeSousProfil;

    // Code unique : SUP_ADMIN, SERVICE_SANTE, PHARMACIE, LABORATOIRE...
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "supprime", nullable = false)
    private String supprime;

    @OneToMany(mappedBy = "profil", fetch = FetchType.LAZY)
    private List<Permission> permissions;
}
