/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author USER01
 */
@Embeddable
@Data
@NoArgsConstructor @AllArgsConstructor
public class MenuPosteTravailId implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Column(name = "NOM_UTIL", nullable = false, length = 30)
    private String nomUtil;

    @Column(name = "CODEBRAN")
    private Long codeBran;

    @Column(name = "CODEMENU", nullable = false, length = 2)
    private String codeMenu;

    @Column(name = "CODEMODU", length = 2)
    private String codeModu;

    @Column(name = "CODEOPTI", length = 2)
    private String codeOpti;

    @Column(name = "CODSOUOP", length = 2)
    private String codSouOp;


}