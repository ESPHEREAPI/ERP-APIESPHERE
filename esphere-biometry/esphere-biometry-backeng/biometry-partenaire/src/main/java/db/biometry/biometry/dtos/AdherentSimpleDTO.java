package db.biometry.biometry.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO allégé pour les listes déroulantes d'adhérents.
 * Utilisé dans la vue Adhérent (DII / Service Santé).
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdherentSimpleDTO {

    /** Code unique de l'adhérent */
    private String codeAdherent;

    /** Nom complet (assurePrincipal) */
    private String nom;

    /** Matricule */
    private String matricule;

    /** Statut */
    private String statut;
}
