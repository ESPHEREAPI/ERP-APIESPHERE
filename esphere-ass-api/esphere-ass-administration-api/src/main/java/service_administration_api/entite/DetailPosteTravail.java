/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.entite;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Entity
@Data
@Table(name = "DETAIL_POSTE_TRAVAIL")
      
public class DetailPosteTravail {
    
    @EmbeddedId
    private DetailPosteTravailId id;
    
    @Column(name = "VALI__AN", length = 1, columnDefinition = "VARCHAR2(1) DEFAULT 'O'")
    private String valiAn = "O";
    
    @Column(name = "VALI__AV", length = 1, columnDefinition = "VARCHAR2(1) DEFAULT 'O'")
    private String valiAv = "O";
    
    @Column(name = "STOCATTE", length = 1)
    private String stocAtte;
    
    // Relations avec les entités référencées (optionnel)
    // Utilisation d'AttributeOverride pour éviter les conflits
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NOM_UTIL", referencedColumnName = "NOM_UTIL", insertable = false, updatable = false)
    private PosteTravail posteTravail;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODEBRAN", referencedColumnName = "CODEBRAN", insertable = false, updatable = false)
    private Branche branche;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CODECATE", referencedColumnName = "CODECATE", insertable = false, updatable = false)
    private Categorie categorie;
    
    // Constructeurs
    public DetailPosteTravail() {
        this.id = new DetailPosteTravailId();
    }
    
    public DetailPosteTravail(String nomUtil, Integer codeInte, Integer codeBran, Integer codecate) {
        this.id = new DetailPosteTravailId(nomUtil, codeInte, codeBran, codecate);
    }
    
    public DetailPosteTravail(DetailPosteTravailId id) {
        this.id = id;
    }
    
    // Getters et Setters pour l'ID
    public DetailPosteTravailId getId() {
        return id;
    }
    
    public void setId(DetailPosteTravailId id) {
        this.id = id;
    }
    
    // Méthodes utilitaires pour accéder aux champs de l'ID sans exposer l'objet complet
    public String getNomUtil() {
        return id != null ? id.getNomUtil() : null;
    }
    
    public void setNomUtil(String nomUtil) {
        if (id == null) {
            id = new DetailPosteTravailId();
        }
        id.setNomUtil(nomUtil);
    }
    
    public Integer getCodeInte() {
        return id != null ? id.getCodeInte() : null;
    }
    
    public void setCodeInte(Integer codeInte) {
        if (id == null) {
            id = new DetailPosteTravailId();
        }
        id.setCodeInte(codeInte);
    }
    
    public Integer getCodeBran() {
        return id != null ? id.getCodeBran() : null;
    }
    
    public void setCodeBran(Integer codeBran) {
        if (id == null) {
            id = new DetailPosteTravailId();
        }
        id.setCodeBran(codeBran);
    }
    
    public Integer getCodecate() {
        return id != null ? id.getCodecate() : null;
    }
    
    public void setCodecate(Integer codecate) {
        if (id == null) {
            id = new DetailPosteTravailId();
        }
        id.setCodecate(codecate);
    }
    
    // Getters et Setters pour les autres champs
    public String getValiAn() {
        return valiAn;
    }
    
    public void setValiAn(String valiAn) {
        this.valiAn = valiAn;
    }
    
    public String getValiAv() {
        return valiAv;
    }
    
    public void setValiAv(String valiAv) {
        this.valiAv = valiAv;
    }
    
    public String getStocAtte() {
        return stocAtte;
    }
    
    public void setStocAtte(String stocAtte) {
        this.stocAtte = stocAtte;
    }
    
    public PosteTravail getPosteTravail() {
        return posteTravail;
    }
    
    public void setPosteTravail(PosteTravail posteTravail) {
        this.posteTravail = posteTravail;
    }
    
    public Branche getBranche() {
        return branche;
    }
    
    public void setBranche(Branche branche) {
        this.branche = branche;
    }
    
    public Categorie getCategorie() {
        return categorie;
    }
    
    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailPosteTravail that = (DetailPosteTravail) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "DetailPosteTravail{" +
               "id=" + id +
               ", valiAn='" + valiAn + '\'' +
               ", valiAv='" + valiAv + '\'' +
               ", stocAtte='" + stocAtte + '\'' +
               '}';
    }
    
}
