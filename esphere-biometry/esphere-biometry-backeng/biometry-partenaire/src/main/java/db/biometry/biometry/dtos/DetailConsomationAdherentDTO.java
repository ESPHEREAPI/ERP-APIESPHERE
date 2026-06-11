/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Builder;
import lombok.Data;
import org.springframework.cglib.core.Local;

/**
 *
 * @author USER01
 */
@Builder
@Data
public class DetailConsomationAdherentDTO {
    BigDecimal montant;
    BigDecimal taux;
    BigDecimal montantPriseEnCharge;
    BigDecimal montantAyantDroit;
    String prestation;
    String prestatire;
    String ayantDroit;
    String etat;
    Date date;
    Integer valeur;
}
