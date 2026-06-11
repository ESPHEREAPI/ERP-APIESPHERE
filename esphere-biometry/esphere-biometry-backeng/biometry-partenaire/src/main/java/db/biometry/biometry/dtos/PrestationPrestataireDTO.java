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
@Data
public class PrestationPrestataireDTO {
     private String prestataireId;
    private String nomPrestataire;
    private String categorie;
    private Long nombreLignes;
    private BigDecimal totalMontant;

    public PrestationPrestataireDTO(String prestataireId, String nomPrestataire, String categorie, Long nombreLignes, Double totalMontant) {
        this.prestataireId = prestataireId;
        this.nomPrestataire = nomPrestataire;
        this.categorie = categorie;
        this.nombreLignes = nombreLignes;
        this.totalMontant = new BigDecimal(totalMontant.doubleValue());
    }
    
    
}
