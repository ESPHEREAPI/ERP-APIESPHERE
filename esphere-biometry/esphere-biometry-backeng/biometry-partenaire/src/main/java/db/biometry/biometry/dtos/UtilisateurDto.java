/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author JIATOU FRANCK
 */
@Data
public class UtilisateurDto {

    private Integer id;
    private String genre;
    private String type;
    private String nom;
    private String prenom;
    private Date dateNaissance;
    private String lieuNaissance;
    private String telephone;
    private String email;
    private String login;
    private String motPasse;
    private String statut;
    private String supprime;
    private boolean echeck_connection=false;
    private String messageEcheck;

}
