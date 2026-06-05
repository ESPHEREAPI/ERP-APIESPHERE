package com.esphere.media.dto.internal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisiteInfo {
    private String visiteId;
    private String codeAdherent;
    private String codeAyantDroit;
    private String prestataireId;
    private String codeCourt;
}