/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
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
public class DetailPosteTravailId implements Serializable {
    
    @Column(name = "NOM_UTIL", length = 30, nullable = false)
    private String nomUtil;
    
    @Column(name = "CODEINTE", precision = 5)
    private Integer codeInte;
    
    @Column(name = "CODEBRAN", precision = 2, nullable = false)
    private Integer codeBran;
    
    @Column(name = "CODECATE", precision = 5)
    private Integer codecate;
    
    
     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailPosteTravailId that = (DetailPosteTravailId) o;
        return Objects.equals(nomUtil, that.nomUtil) &&
               Objects.equals(codeInte, that.codeInte) &&
               Objects.equals(codeBran, that.codeBran) &&
               Objects.equals(codecate, that.codecate);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(nomUtil, codeInte, codeBran, codecate);
    }
    
    @Override
    public String toString() {
        return "DetailPosteTravailId{" +
               "nomUtil='" + nomUtil + '\'' +
               ", codeInte=" + codeInte +
               ", codeBran=" + codeBran +
               ", codecate=" + codecate +
               '}';
    }
   
}
