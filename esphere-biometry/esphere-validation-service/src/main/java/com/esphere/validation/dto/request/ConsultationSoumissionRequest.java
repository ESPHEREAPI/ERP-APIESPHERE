package com.esphere.validation.dto.request;

import lombok.Data;

@Data
public class ConsultationSoumissionRequest {

    private String  visiteId;
    private String  prestataireId;
    private String  typeConsultation;
    private Double  montant;
    private Boolean payante;
    private Integer employeId;
}