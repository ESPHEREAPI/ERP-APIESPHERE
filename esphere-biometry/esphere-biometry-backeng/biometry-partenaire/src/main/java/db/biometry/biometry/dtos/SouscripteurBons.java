/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
public class SouscripteurBons implements Serializable{
    private String souscripteur;
     private String assure;
    private Date date_debut ;
    private Date date_fin;
    private String taux_exam_ordon="0%";
    private String taux_consul="0%";
    private Double exament,ordonance,consultation=0.0;
    private Double part_assure_exam_ordon=0.0;
     private Double part_assure_consul=0.0;
}
