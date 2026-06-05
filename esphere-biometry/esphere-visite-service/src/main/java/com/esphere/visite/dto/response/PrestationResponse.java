package com.esphere.visite.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrestationResponse {

    private Integer               id;
    private String                visiteId;
    private String                prestataireId;
    private String                naturePrestation;
    private LocalDateTime         date;
    private List<LignePrestationResponse> lignes;
}