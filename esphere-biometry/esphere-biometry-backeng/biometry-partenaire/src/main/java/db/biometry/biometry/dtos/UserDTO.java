/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import db.biometry.biometry.enums.ProfilType;
import java.util.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author USER01
 */
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastname;
    private String userName;
    private String email;
    private String tel;
    private String indicatifPays;
    private String codePays;
    private String address;
    private String profileImageUrl;
    private Date createdAt;
    private Date updatedAt;
    private Date lastlogin;
    private Boolean active;
    private Boolean echeck_connection = Boolean.FALSE;
    private RoleDTO role;
    private ProfilDTO profil;
    private Long roleid;
    private String createdBy;
    private String updateByUserName;
    private String messageEcheck;
  
    private Long boutiqueid;

    private Boolean autorisationDeletes;
     private ProfilType profilType;
     private Date effet;
     private Date echeance;
     private String codeAdherent;

    public void setActive(boolean active) {
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    this.active=active;
    }

}
