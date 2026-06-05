package com.esphere.bonmanuel.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationDetailRequest {

    @NotEmpty(message = "Les lignes sont obligatoires")
    private List<BonManuelLigneRequest> lignes;

    private String observations;

    private Integer employeId;
}