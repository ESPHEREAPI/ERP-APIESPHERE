/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApiPoolTPV;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReferenceAutoDto implements Serializable{

    private String marque;
    private String typeVeh;
    private String codeEnergie;
    private String puissance;
    private String nbrePlaces;
}
