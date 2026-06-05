/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.esphere.validation.dto.request;

import lombok.*;

/**
 *
 * @author USER01
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TauxPrestationDTO {
    private Integer id;
    private String  typePrestationId;
    private String  police;
    private short   groupe;
    private Integer taux;
    private Float   plafond;
}