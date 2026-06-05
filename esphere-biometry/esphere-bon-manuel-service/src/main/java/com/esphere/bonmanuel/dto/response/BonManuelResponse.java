package com.esphere.bonmanuel.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BonManuelResponse {

    private Integer       id;
    private String        reference;
    private String        numeroProforma;
    private String        visiteId;
    private String        prestataireId;
    private String        codeAdherent;
    private String        codeAyantDroit;
    private Integer       employeId;
    private Double        montantProforma;
    private Double        montantConfirme;
    private String        typeValidation;
    private String        statut;
    private String        observations;
    private LocalDateTime dateCreation;
    private LocalDateTime dateConfirmation;
    private LocalDateTime dateEncaissement;
    private List<BonManuelLigneResponse> lignes;
}