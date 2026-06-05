package com.esphere.visite.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResponse {

    private Integer       id;
    private String        visiteId;
    private Integer       employeValideRejeteId;
    private Double        taux;
    private String        typeConsultation;
    private String        natureConsultation;
    private String        natureAffection;
    private Double        montant;
    private Double        montantModif;
    private LocalDateTime date;
    private LocalDateTime dateValideRejete;
    private String        observations;
    private String        etatConsultation;
}