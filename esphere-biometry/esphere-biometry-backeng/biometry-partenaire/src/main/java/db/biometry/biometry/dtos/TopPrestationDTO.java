/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author USER01
 */
//@Data
public class TopPrestationDTO {
  private String  typeId;
    private String nom;
    private String categorie;
    private Long nombreConsultations;
    private BigDecimal totalDepenses;

    public TopPrestationDTO(Integer typeId, String nom, String categorie, Long nombreConsultations, Double totalDepenses) {
        this.typeId =typeId.toString();
        this.nom = nom;
        this.categorie = categorie;
        this.nombreConsultations = nombreConsultations;
        this.totalDepenses = new BigDecimal(totalDepenses.doubleValue());
    }

    public TopPrestationDTO(String typeId, String nom, String categorie, Long nombreConsultations, BigDecimal totalDepenses) {
        this.typeId = typeId;
        this.nom = nom;
        this.categorie = categorie;
        this.nombreConsultations = nombreConsultations;
        this.totalDepenses = totalDepenses;
    }
    

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Long getNombreConsultations() {
        return nombreConsultations;
    }

    public void setNombreConsultations(Long nombreConsultations) {
        this.nombreConsultations = nombreConsultations;
    }

    public BigDecimal getTotalDepenses() {
        return totalDepenses;
    }

    public void setTotalDepenses(BigDecimal totalDepenses) {
        this.totalDepenses = totalDepenses;
    }
    
    
}
