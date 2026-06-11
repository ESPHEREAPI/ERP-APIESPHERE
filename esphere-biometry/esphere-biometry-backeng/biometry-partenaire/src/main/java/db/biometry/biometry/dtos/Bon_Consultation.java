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
public class Bon_Consultation {
    private String prestation_number;
    private String assure;
    private String prestataire;
    private String malade;
    private String souscripteur;
    private String taux;
    private String type_prestation;
    private Integer montant_valide;
    private Integer part_zenithe;
    private Integer part_assure;
    private String date;
}
