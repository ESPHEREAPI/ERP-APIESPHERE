package com.esphere.webservicecron.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HistoriqueSyncResponse {
    private Long id;
    private String typeSynchronisation;
    private String declencheur;
    private String policeFiltre;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private Long dureeMs;
    private String statut;
    private Integer nbTraites;
    private Integer nbEchecs;
    private String detailsEchecs;
    private String messageErreur;
}
