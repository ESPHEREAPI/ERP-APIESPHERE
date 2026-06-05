package com.esphere.media.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponse {

    private Integer       id;
    private String        visiteId;
    private String        codeAdherent;
    private String        codeAyantDroit;
    private String        prestataireId;
    private String        souscripteur;
    private String        police;
    private String        nomFichier;
    private String        chemin;
    private String        typeMedia;
    private String        extension;
    private Long          taille;
    private Boolean       demandeParSs;
    private Integer       employeId;
    private LocalDateTime dateUpload;
}