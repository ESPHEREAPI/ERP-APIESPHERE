-- ═══════════════════════════════════════════════════════════════════
-- V1 - Création table MEDIA_PRESTATION (nouvelle table v2)
-- Stocke les métadonnées des fichiers uploadés via mobile.
-- Le fichier physique est dans MinIO, seule l'URL est ici.
-- ═══════════════════════════════════════════════════════════════════

CREATE TABLE IF NOT EXISTS dbx45ty_media_prestation (
    id               INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    visite_id        VARCHAR(255)    NOT NULL,
    code_adherent    VARCHAR(255)    NOT NULL,
    code_ayant_droit VARCHAR(255)    NULL,
    prestataire_id   VARCHAR(255)    NOT NULL,
    souscripteur     VARCHAR(255)    NOT NULL,
    police           VARCHAR(255)    NOT NULL,
    nom_fichier      VARCHAR(500)    NOT NULL,
    chemin           VARCHAR(1000)   NOT NULL,
    type_media       VARCHAR(20)     NOT NULL,
    extension        VARCHAR(10)     NOT NULL,
    taille           BIGINT          NOT NULL,
    demande_par_ss   TINYINT(1)      NOT NULL DEFAULT 0,
    employe_id       INT UNSIGNED    NULL,
    date_upload      DATETIME        NOT NULL,
    supprime         VARCHAR(5)      NOT NULL DEFAULT '-1',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
