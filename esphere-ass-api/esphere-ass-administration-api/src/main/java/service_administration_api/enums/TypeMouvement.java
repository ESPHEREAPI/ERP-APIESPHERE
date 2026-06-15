package service_administration_api.enums;

public enum TypeMouvement {

    /** Entrée de stock : réception d'attestations */
    APPROVISIONNEMENT,

    /** Sortie de stock : consommée par une production */
    DESTOCKAGE,

    /** Correction manuelle positive */
    AJUSTEMENT_PLUS,

    /** Correction manuelle négative */
    AJUSTEMENT_MOINS,

    /** Annulation d'une production — restitution au stock */
    ANNULATION
}
