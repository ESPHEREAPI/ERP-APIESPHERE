/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV;

import java.io.Serializable;

/**
 *
 * @author USER01
 */
public record LoginCertificatRequest(
       // Long id,
      //  Integer codeAgence,
       // String libelleAgence,
        String username
      //  String office_code,
      //  String codeagence,
      //  String clientName,
      //  int expiresAt
) implements Serializable {
    
}
