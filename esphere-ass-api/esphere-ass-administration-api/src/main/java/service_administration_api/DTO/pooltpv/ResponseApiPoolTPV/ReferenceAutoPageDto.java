/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApiPoolTPV;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author USER01
 */
@Data
@Builder
public class ReferenceAutoPageDto implements Serializable {
     private int totalItems;
    private int totalPages;
    private int currentPage;
    private List<ReferenceAutoDto> referenceAuto;

}
