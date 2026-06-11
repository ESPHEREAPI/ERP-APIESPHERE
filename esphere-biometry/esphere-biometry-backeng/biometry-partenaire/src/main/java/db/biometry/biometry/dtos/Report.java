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
public class Report {
    private Long id;
    private String name;
    private Date date_debut ;
    private Date date_fin;
    private String ville;
}
