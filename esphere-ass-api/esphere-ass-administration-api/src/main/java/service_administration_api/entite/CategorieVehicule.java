/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author USER01
 */
// ============================================
// CategorieVehicule.java
// ============================================
@Entity
@Table(name = "ZEN_REF_CATEGORIE_VEHICULE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CategorieVehicule {

    @Id
    @Column(name = "CODE", length = 5, nullable = false)
    private String code;

    @Column(name = "LIBELLE", length = 100, nullable = false)
    private String libelle;
}
