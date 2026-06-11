/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Data

public class PrestataireStatsDTO {

    private String prestataireId;
    private Date lastConsultationDate;
    private int daysSinceLastConsultation;
    private String designation;
    private String telephone;
    private String ville;
    private String categorire;
    public PrestataireStatsDTO(String prestataireId, Date lastConsultationDate, 
                              int daysSinceLastConsultation, String designation,String categorie, 
                              String telephone, String ville) {
        this.prestataireId = prestataireId;
        this.lastConsultationDate = lastConsultationDate;
        this.daysSinceLastConsultation = daysSinceLastConsultation;
        this.designation = designation;
        this.telephone = telephone;
        this.ville = ville;
        this.categorire=categorie;
}
}
