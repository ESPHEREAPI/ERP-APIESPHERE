package com.esphere.validation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VisiteInfoResponse {
    private String codeVisite;
    private String codeAdherent;
    private String codeAyantDroit;
    private String nomAssure;
    private String nomAyantDroit;
    private String souscripteur;
    private Short  groupe;
    private String prestataireId;
    
    
    // Ayant droit — affiché seulement si c'est lui le concerné
    // null si assuré principal consulte pour lui-même

    private String lienParente;
}