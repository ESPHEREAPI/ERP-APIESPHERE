package com.esphere.visite.client;

import lombok.Data;

@Data
public class AdherentDto {
    private String codeAdherent;
    private String assurePrincipal;
    private String souscripteur;
    private String statut;
    private String echeancePolice;
    private String effetPolice;
    private Short  groupe;
}