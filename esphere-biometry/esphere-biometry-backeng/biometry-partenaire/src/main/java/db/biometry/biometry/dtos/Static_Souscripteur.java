/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
public class Static_Souscripteur implements Serializable {

    private String religion;
    private String souscripteur;
    private Integer consultation = 0;
    private Integer exament = 0;
    private Integer ordonance = 0;
    private Long montant_exam;
    private Long montant_consul;
    private Long montant_ordonance;
    private Integer groupe = 0;
}
