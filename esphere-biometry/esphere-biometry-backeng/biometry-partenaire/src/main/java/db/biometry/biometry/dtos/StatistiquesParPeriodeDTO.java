/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import db.biometry.biometry.utils.IdleDate;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
/**
 *
 * @author USER01
 */


@Data
public class StatistiquesParPeriodeDTO {

    private String periode;
    private Long nombreConsultations;
    private BigDecimal totalDepenses;//ticket moderateur des assures 
    private BigDecimal totalRembourse;//consommation prime
    private Date date;

    public StatistiquesParPeriodeDTO(Date periode,
                                     Long nombreConsultations,
                                     Double totalDepenses,
                                     Double totalRembourse) {
        this.date=periode;
       // this.periode = periode.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));;
       this.periode=IdleDate.toString(periode, "yyyy-MM-dd");
        this.nombreConsultations = nombreConsultations != null ? nombreConsultations.longValue() : 0L;
        this.totalDepenses = totalDepenses != null ? new BigDecimal(totalDepenses.doubleValue()) : BigDecimal.ZERO;
        this.totalRembourse = totalRembourse != null ? new BigDecimal(totalRembourse.doubleValue()) : BigDecimal.ZERO;
    }
    
}
