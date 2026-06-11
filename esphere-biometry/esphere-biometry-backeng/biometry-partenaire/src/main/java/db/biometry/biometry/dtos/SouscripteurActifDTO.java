package db.biometry.biometry.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la liste déroulante des souscripteurs actifs.
 * Affiché comme : "NOM (police)"  ex: "CAMTE (1017-2310000100)"
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SouscripteurActifDTO {

    /** Nom du souscripteur (ex: CAMTE) */
    private String souscripteur;

    /** Numéro de police (ex: 1017-2310000100) */
    private String police;

    /** Libellé d'affichage : "NOM (police)" */
    public String getLibelle() {
        return souscripteur + " (" + police + ")";
    }
}
