/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Builder
@Data
public class UserDTO {
    private Integer codeagence;

   
    private String nomcomplet;

    private String tel;
    private String indicatifPays;
    private String codePays;
    private String address;
    private String profileImageUrl;
    private Date createdAt;
    private Date updatedAt;
    private Date lastlogin;
    private Boolean isActive;
    private Boolean echeck_connection = Boolean.FALSE;
    //private RoleDTO role;
    private long profil;
    private Long roleid;
    private String createdBy;
    private String updateByUserName;
    private String messageEcheck;

    private Boolean autorisationDeletes;
    
     @JsonProperty("id")
    private Long id;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("firstName")
    private String firstName;
    
    @JsonProperty("lastName")
    private String lastName;
    
    @JsonProperty("role")
    private RoleDTO role;
    
  
}
