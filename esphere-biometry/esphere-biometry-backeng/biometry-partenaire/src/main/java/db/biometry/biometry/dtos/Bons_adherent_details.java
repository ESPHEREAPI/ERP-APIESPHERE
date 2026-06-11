/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
public class Bons_adherent_details implements Serializable{
     private String assure;
     private String prestataire;
       private Date date_debut ;
       private String prestation;
       private String nom;
       private BigInteger montant=BigInteger.ZERO;
       private BigInteger nombre=BigInteger.ZERO;
       private BigInteger total=BigInteger.ZERO;
       private String taux_couverture="0%";
       
       
}
