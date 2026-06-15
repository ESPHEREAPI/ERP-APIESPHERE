/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO;

import lombok.Data;
import service_administration_api.enums.ProfilType;

/**
 *
 * @author USER01
 */
@Data
public class LoginResponseDTO {

    private String username;
    private String password;
    private ProfilType profilType;
}
