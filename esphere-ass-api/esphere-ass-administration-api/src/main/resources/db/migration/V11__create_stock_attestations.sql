-- =============================================================
-- V11__create_stock_attestations.sql
-- Objet   : Gestion du stock d attestations par bureau
--           Tables : ZEN_STOCK_ATTESTATIONS
--                    ZEN_MOUVEMENTS_STOCK
--           Sequences + Indexes de performance
-- Oracle  : 11g compatible (DECLARE/BEGIN/EXECUTE IMMEDIATE)
-- =============================================================


-- -------------------------------------------------------------
-- 1. SEQUENCE SEQ_STOCK_ATTESTATIONS
-- -------------------------------------------------------------
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_sequences
    WHERE sequence_name = 'SEQ_STOCK_ATTESTATIONS';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE SEQUENCE SEQ_STOCK_ATTESTATIONS
             START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE';
    END IF;
END;
/


-- -------------------------------------------------------------
-- 2. TABLE ZEN_STOCK_ATTESTATIONS
--    Une ligne = stock courant pour (bureau, type, variante)
--    certTypeCode NULL  = stock global (toutes attestations)
--    certVariantCode NULL = toutes variantes d un type
-- -------------------------------------------------------------
DECLARE
    v_table NUMBER;
    v_cst   NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_table
    FROM user_tables
    WHERE table_name = 'ZEN_STOCK_ATTESTATIONS';

    IF v_table = 0 THEN

        EXECUTE IMMEDIATE '
            CREATE TABLE ZEN_STOCK_ATTESTATIONS (
                ID                         NUMBER(19)      NOT NULL,
                OFFICE_CODE                VARCHAR2(50)    NOT NULL,
                OFFICE_NAME                VARCHAR2(200),
                ORG_CODE                   VARCHAR2(50),
                CERT_TYPE_CODE             VARCHAR2(50),
                CERT_TYPE_NAME             VARCHAR2(200),
                CERT_VARIANT_CODE          VARCHAR2(50),
                CERT_VARIANT_NAME          VARCHAR2(200),
                QUANTITE_DISPONIBLE        NUMBER(10)      DEFAULT 0 NOT NULL,
                QUANTITE_RESERVEE          NUMBER(10)      DEFAULT 0 NOT NULL,
                QUANTITE_TOTALE_APPRO      NUMBER(10)      DEFAULT 0 NOT NULL,
                QUANTITE_TOTALE_CONSO      NUMBER(10)      DEFAULT 0 NOT NULL,
                SEUIL_ALERTE               NUMBER(10)      DEFAULT 50,
                SEUIL_CRITIQUE             NUMBER(10)      DEFAULT 10,
                STATUT                     VARCHAR2(20)    DEFAULT ''NORMAL'' NOT NULL,
                CREATED_AT                 TIMESTAMP WITH TIME ZONE,
                UPDATED_AT                 TIMESTAMP WITH TIME ZONE,
                CREATED_BY                 VARCHAR2(100)
            )';

        -- PK
        SELECT COUNT(*) INTO v_cst
        FROM user_constraints
        WHERE constraint_name = 'PK_ZEN_STOCK_ATT';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE
                'ALTER TABLE ZEN_STOCK_ATTESTATIONS
                 ADD CONSTRAINT PK_ZEN_STOCK_ATT PRIMARY KEY (ID)';
        END IF;

        -- Contrainte CHECK statut
        SELECT COUNT(*) INTO v_cst
        FROM user_constraints
        WHERE constraint_name = 'CHK_STOCK_STATUT';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE
                'ALTER TABLE ZEN_STOCK_ATTESTATIONS
                 ADD CONSTRAINT CHK_STOCK_STATUT
                 CHECK (STATUT IN (''NORMAL'',''ALERTE'',''CRITIQUE'',''RUPTURE''))';
        END IF;

        -- Contrainte d unicite metier : (officeCode, certTypeCode, certVariantCode)
        -- Autoriser NULL dans les deux codes (stock global vs specifique)
        -- Oracle gere les NULL dans les UNIQUE via FBI ou on utilise une UK partielle
        -- Solution : UK sur les colonnes avec NVL pour harmoniser les NULL
        SELECT COUNT(*) INTO v_cst
        FROM user_indexes
        WHERE index_name = 'UK_STOCK_OFFICE_TYPE_VAR';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE
                'CREATE UNIQUE INDEX UK_STOCK_OFFICE_TYPE_VAR
                 ON ZEN_STOCK_ATTESTATIONS (
                     OFFICE_CODE,
                     NVL(CERT_TYPE_CODE,    ''__NULL__''),
                     NVL(CERT_VARIANT_CODE, ''__NULL__'')
                 )';
        END IF;

    END IF;
END;
/


-- -------------------------------------------------------------
-- 3. SEQUENCE SEQ_MOUVEMENTS_STOCK
-- -------------------------------------------------------------
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_sequences
    WHERE sequence_name = 'SEQ_MOUVEMENTS_STOCK';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE SEQUENCE SEQ_MOUVEMENTS_STOCK
             START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE';
    END IF;
END;
/


-- -------------------------------------------------------------
-- 4. TABLE ZEN_MOUVEMENTS_STOCK
--    Log immuable — une ligne par mouvement (entree ou sortie)
-- -------------------------------------------------------------
DECLARE
    v_table NUMBER;
    v_cst   NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_table
    FROM user_tables
    WHERE table_name = 'ZEN_MOUVEMENTS_STOCK';

    IF v_table = 0 THEN

        EXECUTE IMMEDIATE '
            CREATE TABLE ZEN_MOUVEMENTS_STOCK (
                ID                  NUMBER(19)      NOT NULL,
                STOCK_ID            NUMBER(19)      NOT NULL,
                OFFICE_CODE         VARCHAR2(50)    NOT NULL,
                CERT_TYPE_CODE      VARCHAR2(50),
                CERT_VARIANT_CODE   VARCHAR2(50),
                TYPE_MOUVEMENT      VARCHAR2(30)    NOT NULL,
                QUANTITE            NUMBER(10)      NOT NULL,
                QUANTITE_AVANT      NUMBER(10)      NOT NULL,
                QUANTITE_APRES      NUMBER(10)      NOT NULL,
                REFERENCE_SOURCE    VARCHAR2(100),
                MOTIF               VARCHAR2(500),
                CREATED_AT          TIMESTAMP WITH TIME ZONE NOT NULL,
                CREATED_BY          VARCHAR2(100)
            )';

        -- PK
        SELECT COUNT(*) INTO v_cst
        FROM user_constraints
        WHERE constraint_name = 'PK_ZEN_MOUV_STOCK';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE
                'ALTER TABLE ZEN_MOUVEMENTS_STOCK
                 ADD CONSTRAINT PK_ZEN_MOUV_STOCK PRIMARY KEY (ID)';
        END IF;

        -- FK vers ZEN_STOCK_ATTESTATIONS
        SELECT COUNT(*) INTO v_cst
        FROM user_constraints
        WHERE constraint_name = 'FK_MOUV_STOCK_ATT';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE
                'ALTER TABLE ZEN_MOUVEMENTS_STOCK
                 ADD CONSTRAINT FK_MOUV_STOCK_ATT
                 FOREIGN KEY (STOCK_ID)
                 REFERENCES ZEN_STOCK_ATTESTATIONS(ID)';
        END IF;

        -- CHECK type de mouvement
        SELECT COUNT(*) INTO v_cst
        FROM user_constraints
        WHERE constraint_name = 'CHK_MOUV_TYPE';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE
                'ALTER TABLE ZEN_MOUVEMENTS_STOCK
                 ADD CONSTRAINT CHK_MOUV_TYPE
                 CHECK (TYPE_MOUVEMENT IN (
                     ''APPROVISIONNEMENT'',
                     ''DESTOCKAGE'',
                     ''AJUSTEMENT_PLUS'',
                     ''AJUSTEMENT_MOINS'',
                     ''ANNULATION''
                 ))';
        END IF;

        -- CHECK quantite toujours positive
        SELECT COUNT(*) INTO v_cst
        FROM user_constraints
        WHERE constraint_name = 'CHK_MOUV_QTE_POS';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE
                'ALTER TABLE ZEN_MOUVEMENTS_STOCK
                 ADD CONSTRAINT CHK_MOUV_QTE_POS
                 CHECK (QUANTITE > 0)';
        END IF;

    END IF;
END;
/


-- -------------------------------------------------------------
-- 5. INDEX de performance — ZEN_STOCK_ATTESTATIONS
-- -------------------------------------------------------------
DECLARE v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_indexes
    WHERE index_name = 'IDX_STOCK_OFFICE_CODE';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_STOCK_OFFICE_CODE
             ON ZEN_STOCK_ATTESTATIONS (OFFICE_CODE)';
    END IF;
END;
/

DECLARE v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_indexes
    WHERE index_name = 'IDX_STOCK_STATUT';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_STOCK_STATUT
             ON ZEN_STOCK_ATTESTATIONS (STATUT)';
    END IF;
END;
/

DECLARE v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_indexes
    WHERE index_name = 'IDX_STOCK_ORG_CODE';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_STOCK_ORG_CODE
             ON ZEN_STOCK_ATTESTATIONS (ORG_CODE)';
    END IF;
END;
/


-- -------------------------------------------------------------
-- 6. INDEX de performance — ZEN_MOUVEMENTS_STOCK
-- -------------------------------------------------------------
DECLARE v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_indexes
    WHERE index_name = 'IDX_MOUV_OFFICE_CODE';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_MOUV_OFFICE_CODE
             ON ZEN_MOUVEMENTS_STOCK (OFFICE_CODE)';
    END IF;
END;
/

DECLARE v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_indexes
    WHERE index_name = 'IDX_MOUV_CREATED_AT';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_MOUV_CREATED_AT
             ON ZEN_MOUVEMENTS_STOCK (CREATED_AT)';
    END IF;
END;
/

DECLARE v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_indexes
    WHERE index_name = 'IDX_MOUV_TYPE';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_MOUV_TYPE
             ON ZEN_MOUVEMENTS_STOCK (TYPE_MOUVEMENT)';
    END IF;
END;
/

DECLARE v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_indexes
    WHERE index_name = 'IDX_MOUV_REF_SOURCE';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_MOUV_REF_SOURCE
             ON ZEN_MOUVEMENTS_STOCK (REFERENCE_SOURCE)';
    END IF;
END;
/

DECLARE v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count FROM user_indexes
    WHERE index_name = 'IDX_MOUV_STOCK_ID';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_MOUV_STOCK_ID
             ON ZEN_MOUVEMENTS_STOCK (STOCK_ID)';
    END IF;
END;
/

-- Fin V11
