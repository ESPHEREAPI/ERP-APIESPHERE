CREATE TABLE IF NOT EXISTS dbx45ty_parametre (
    id                INT UNSIGNED    NOT NULL AUTO_INCREMENT,
    cle               VARCHAR(100)    NOT NULL,
    valeur            VARCHAR(255)    NOT NULL,
    description       TEXT            NULL,
    date_modification DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
                                      ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_parametre_cle (cle)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO dbx45ty_parametre (cle, valeur, description) VALUES
('DOCUMENT_OBLIGATOIRE', 'false',
 'Si true, le document filmé doit être approuvé avant de soumettre une prestation (ordonnance/examen)');
