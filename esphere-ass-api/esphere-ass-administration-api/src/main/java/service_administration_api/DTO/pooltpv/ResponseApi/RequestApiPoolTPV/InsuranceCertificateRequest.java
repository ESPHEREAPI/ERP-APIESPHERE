/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service_administration_api.DTO.pooltpv.ResponseApi.RequestApiPoolTPV;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 *
 * @author USER01
 */
public record InsuranceCertificateRequest(
        @JsonProperty("office_code")
        @NotBlank(message = "Le code bureau est obligatoire")
        String office_code,
        @JsonProperty("organization_code")
        @NotBlank(message = "Le code organisation est obligatoire")
        String organization_code,
        @JsonProperty("certificate_type")
        String certificate_type,
       // @JsonProperty("login")
       // String login,
        @JsonProperty("productions")
    List<ProductionPayloadRequest> productions
        ) {

    public InsuranceCertificateRequest withOfficeCode(String officeCode,String organization_code, String certificate_type) {
        return new InsuranceCertificateRequest(
                officeCode,
                this.organization_code,
                this.certificate_type,
               // this.login,
                this.productions
        );
    }
}
