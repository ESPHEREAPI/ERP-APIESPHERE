/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package db.biometry.biometry.dtos;

import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
public class PermissionDTO {
     private Long id;
        private String name;
        private String description;
        private String operationType;
}
