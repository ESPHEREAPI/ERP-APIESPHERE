/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite.pooltpv;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "POOLTPV_CATEGORIE")
@Data
public class Categorie_PoolTPV implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_categorie_pooltpv")
    @SequenceGenerator(name = "seq_categorie_pooltpv", sequenceName = "SEQ_CATEGORIE_POOLTPV", allocationSize = 1)
    private Long id;

    private String code;
    private String libelle;
    @Column(name = "replication", nullable = false)
private Boolean replication = false;
    
}
