/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import lombok.Data;
import java.math.BigDecimal;

/**
 *
 * @author USER01
 */
@Data
public class StatistiquesGlobalesDTO {
     Long nombreConsultations;
        BigDecimal totalDepenses;
        BigDecimal totalRembourse;// ticket moderateur
        BigDecimal moyenneDepenses;

    public StatistiquesGlobalesDTO(Long nombreConsultations, Double totalDepenses, Double totalRembourse, Double moyenneDepenses) {
        this.nombreConsultations = nombreConsultations;
        this.totalDepenses = new BigDecimal(totalDepenses.doubleValue());
        this.totalRembourse = new BigDecimal(totalRembourse.doubleValue());
        this.moyenneDepenses = new BigDecimal(moyenneDepenses.doubleValue());
    }
        
}
