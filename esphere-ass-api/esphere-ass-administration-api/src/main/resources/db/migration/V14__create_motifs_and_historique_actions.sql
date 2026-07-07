-- V14 : Codification des motifs d'action (suspension / annulation / résiliation)
--       + Historique traçabilité des actions exécutées sur les certificats
--       + Ajout profil SUPER_PRODUCTEUR sur ZEN_INFOS_ADMIN_AGENCE

-- ── TABLE 1 : ZEN_MOTIFS_ACTION ───────────────────────────────────────────────
CREATE TABLE ZEN_MOTIFS_ACTION (
    code        VARCHAR2(10)  NOT NULL,
    type_action VARCHAR2(20)  NOT NULL,   -- ANNULATION | SUSPENSION | RESILIATION
    libelle     VARCHAR2(200) NOT NULL,
    actif       NUMBER(1,0)   DEFAULT 1 NOT NULL,
    CONSTRAINT pk_zen_motifs_action PRIMARY KEY (code),
    CONSTRAINT ck_motifs_type CHECK (type_action IN ('ANNULATION','SUSPENSION','RESILIATION'))
);

-- ── Données initiales ─────────────────────────────────────────────────────────
-- ANNULATION
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('ANN001','ANNULATION','Erreur de saisie',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('ANN002','ANNULATION','Non-paiement avant effet',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('ANN003','ANNULATION','Doublon de police',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('ANN004','ANNULATION','Non-conformité tarifaire',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('ANN005','ANNULATION','Fraude détectée',1);

-- RESILIATION
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('RES001','RESILIATION','Vente du véhicule',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('RES002','RESILIATION','Non-paiement de prime',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('RES003','RESILIATION','Sinistralité élevée',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('RES004','RESILIATION','Vol du véhicule',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('RES005','RESILIATION','Perte totale',1);

-- SUSPENSION
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('SUS003','SUSPENSION','Suspicion de fraude',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('SUS004','SUSPENSION','Véhicule immobilisé',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('SUS005','SUSPENSION','Suspension volontaire',1);
INSERT INTO ZEN_MOTIFS_ACTION VALUES ('SUS006','SUSPENSION','Incident technique',1);

COMMIT;

-- ── TABLE 2 : ZEN_HISTORIQUE_ACTIONS ─────────────────────────────────────────
CREATE SEQUENCE SEQ_HISTORIQUE_ACTIONS START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

CREATE TABLE ZEN_HISTORIQUE_ACTIONS (
    id                    NUMBER(19,0)   NOT NULL,
    certificate_reference VARCHAR2(100)  NOT NULL,
    type_action           VARCHAR2(20)   NOT NULL,   -- SUSPENSION | ANNULATION
    motif_code            VARCHAR2(10)   NOT NULL,
    motif_libelle         VARCHAR2(200),
    username_executant    VARCHAR2(100)  NOT NULL,
    profil_executant      VARCHAR2(40),
    office_code           VARCHAR2(50),
    statut_avant          VARCHAR2(50),
    statut_apres          VARCHAR2(50),
    reponse_api           VARCHAR2(500),
    date_action           TIMESTAMP      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_zen_historique_actions PRIMARY KEY (id),
    CONSTRAINT fk_hist_motif FOREIGN KEY (motif_code) REFERENCES ZEN_MOTIFS_ACTION(code)
);

-- ── Ajout SUPER_PRODUCTEUR comme valeur valide ────────────────────────────────
-- (la colonne profil_agent est VARCHAR2(40), pas de contrainte CHECK → pas besoin d'ALTER)
-- On documente juste les valeurs acceptées :
-- PRODUCTEUR | SUPER_PRODUCTEUR | CHEF_BUREAU_AGENT | CHEF_BUREAU_DIRECT_SIEGE | ADMINISTRATEUR

COMMENT ON TABLE ZEN_MOTIFS_ACTION IS 'Codification des raisons de suspension/annulation/résiliation';
COMMENT ON TABLE ZEN_HISTORIQUE_ACTIONS IS 'Traçabilité des actions suspension/annulation sur les certificats';
