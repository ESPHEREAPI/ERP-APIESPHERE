package com.esphere.visite.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisiteResponse {

    private String        id;
    private String        codeAdherent;
    private String        codeAyantDroit;
    private String        prestataireId;
    private Integer       employeId;
    private String        codeCourt;
    private String        telephone;
    private LocalDateTime date;
    
    
}