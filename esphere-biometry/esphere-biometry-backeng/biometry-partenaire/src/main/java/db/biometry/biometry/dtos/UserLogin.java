/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import java.io.Serializable;
import lombok.Data;
import db.biometry.biometry.enums.ProfilType;

/**
 *
 * @author JIATOU FRANCK
 */
@Data
public class UserLogin implements Serializable {

    private int id;
    private String userName;
    private String passWord;
    private ProfilType profilType;

}
