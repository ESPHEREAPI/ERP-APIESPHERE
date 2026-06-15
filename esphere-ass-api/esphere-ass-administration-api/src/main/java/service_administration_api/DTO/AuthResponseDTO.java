/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO;

import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
public class AuthResponseDTO {
    private String status;
    private String message;
    private UserSessionDTO userSession;

    public AuthResponseDTO(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
