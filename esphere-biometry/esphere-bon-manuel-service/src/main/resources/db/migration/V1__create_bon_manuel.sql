-- V1__create_bon_manuel.sql
-- Création des tables bon manuel (nouvelle fonctionnalité)

CREATE TABLE IF NOT EXISTS dbx45ty_bon_manuel (
    id                  INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    reference           VARCHAR(50)     NOT NULL UNIQUE,
    numero_proforma     VARCHAR(100)    NOT NULL UNIQUE,
    visite_id           VARCHAR(255)    NOT NULL,
    prestataire_id      VARCHAR(255)    NOT NULL,
    code_adherent       VARCHAR(255)    NOT NULL,
    code_ayant_droit    VARCHAR(255)    NULL,
    employe_id          INT UNSIGNED    NULL,
    employe_encaisse_id INT UNSIGNED    NULL,
    montant_proforma    DOUBLE          NOT NULL DEFAULT 0,
    montant_confirme    DOUBLE          NULL,
    type_validation     VARCHAR(20)     NULL,
    statut              VARCHAR(20)     NOT NULL DEFAULT 'en_attente',
    observations        VARCHAR(500)    NULL,
    date_creation       DATETIME        NOT NULL,
    date_confirmation   DATETIME        NULL,
    date_encaissement   DATETIME        NULL,
    supprime            VARCHAR(5)      NOT NULL DEFAULT '-1',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS dbx45ty_bon_manuel_ligne (
    id               INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    bon_manuel_id    INT UNSIGNED    NOT NULL,
    nom              VARCHAR(255)    NOT NULL,
    codification     VARCHAR(255)    NULL,
    quantite         DOUBLE          NOT NULL DEFAULT 1,
    montant_unitaire DOUBLE          NOT NULL DEFAULT 0,
    montant_total    DOUBLE          NOT NULL DEFAULT 0,
    observations     VARCHAR(255)    NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (bon_manuel_id) REFERENCES dbx45ty_bon_manuel(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;