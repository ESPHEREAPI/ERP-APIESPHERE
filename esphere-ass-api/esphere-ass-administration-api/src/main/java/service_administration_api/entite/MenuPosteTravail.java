/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Entity
@Table(name = "MENU_POSTE_TRAVAIL")
@Data
public class MenuPosteTravail implements Serializable {

    private static final long serialVersionUID = 1L;
     @EmbeddedId
    private MenuPosteTravailId id;

  

     // Relations avec les autres entités (optionnelles)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODEBRAN", insertable = false, updatable = false)
    private Branche branche;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOM_UTIL", insertable = false, updatable = false)
    private PosteTravail posteTravail;

    // Constructeurs
    public MenuPosteTravail() {}

    public MenuPosteTravail(MenuPosteTravailId id) {
        this.id = id;
    }

    public MenuPosteTravail(String nomUtil, Long codeBran, String codeMenu, 
                           String codeModu, String codeOpti, String codSouOp) {
        this.id = new MenuPosteTravailId(nomUtil, codeBran, codeMenu, codeModu, codeOpti, codSouOp);
    }
    
}
