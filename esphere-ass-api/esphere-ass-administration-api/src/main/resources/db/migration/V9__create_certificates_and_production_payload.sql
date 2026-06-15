/* 
 * Author:  USER01
 * Created: May 15, 2026
 */

-- =============================================================
-- V9__create_certificates_and_production_payload.sql
-- Objet   : Creation des tables ZEN_PRODUCTION_PAYLOAD
--           et ZEN_CERTIFICATES_PAYLOAD
--           + Migration colonne STATE -> STATE_NAME / STATE_LABEL
-- =============================================================

-- -------------------------------------------------------------
-- 1. SEQUENCE SEQ_PRODUCTION_PAYLOAD
-- -------------------------------------------------------------
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count 
    FROM user_sequences 
    WHERE sequence_name = 'SEQ_PRODUCTION_PAYLOAD';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 
            'CREATE SEQUENCE SEQ_PRODUCTION_PAYLOAD 
             START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE';
    END IF;
END;
/


-- -------------------------------------------------------------
-- 2. TABLE ZEN_PRODUCTION_PAYLOAD
-- -------------------------------------------------------------
DECLARE
    v_table  NUMBER;
    v_cst    NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_table 
    FROM user_tables 
    WHERE table_name = 'ZEN_PRODUCTION_PAYLOAD';

    IF v_table = 0 THEN

        EXECUTE IMMEDIATE '
            CREATE TABLE ZEN_PRODUCTION_PAYLOAD (
                ID                   NUMBER(19)    NOT NULL,
                EXTERNAL_ID          VARCHAR2(255),
                REFERENCE            VARCHAR2(255),
                CHANNEL              VARCHAR2(100),
                QUANTITY             NUMBER(10),
                SENT_TO_STORAGE      NUMBER(1)     DEFAULT 0,
                DOWNLOAD_LINK        VARCHAR2(500),
                USER_ID              VARCHAR2(255),
                USER_NAME            VARCHAR2(255),
                USER_EMAIL           VARCHAR2(255),
                ORG_ID               VARCHAR2(255),
                ORG_NAME             VARCHAR2(255),
                ORG_CODE             VARCHAR2(100),
                OFFICE_ID            VARCHAR2(255),
                OFFICE_NAME          VARCHAR2(255),
                OFFICE_CODE          VARCHAR2(100),
                CREATED_AT           TIMESTAMP WITH TIME ZONE,
                UPDATED_AT           TIMESTAMP WITH TIME ZONE,
                FORMATTED_CREATED_AT VARCHAR2(100)
            )';

        -- Ajouter PK seulement si elle n existe pas
        SELECT COUNT(*) INTO v_cst 
        FROM user_constraints 
        WHERE constraint_name = 'PK_ZEN_PROD_PAYLOAD';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE 
                'ALTER TABLE ZEN_PRODUCTION_PAYLOAD 
                 ADD CONSTRAINT PK_ZEN_PROD_PAYLOAD PRIMARY KEY (ID)';
        END IF;

    END IF;
END;
/


-- -------------------------------------------------------------
-- 3. SEQUENCE SEQ_CERTIFICATES
-- -------------------------------------------------------------
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count 
    FROM user_sequences 
    WHERE sequence_name = 'SEQ_CERTIFICATES';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 
            'CREATE SEQUENCE SEQ_CERTIFICATES 
             START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE';
    END IF;
END;
/


-- -------------------------------------------------------------
-- 4. TABLE ZEN_CERTIFICATES_PAYLOAD
-- -------------------------------------------------------------
DECLARE
    v_table  NUMBER;
    v_cst    NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_table 
    FROM user_tables 
    WHERE table_name = 'ZEN_CERTIFICATES_PAYLOAD';

    IF v_table = 0 THEN

        EXECUTE IMMEDIATE '
            CREATE TABLE ZEN_CERTIFICATES_PAYLOAD (
                ID                  NUMBER(19)    NOT NULL,
                PRODUCTION_ID       NUMBER(19)    NOT NULL,
                REFERENCE           VARCHAR2(255),
                STATE_NAME          VARCHAR2(50),
                STATE_LABEL         VARCHAR2(100),
                DOWNLOAD_LINK       VARCHAR2(500),
                PDF_BYTES           BLOB,
                CERT_TYPE_CODE      VARCHAR2(50),
                CERT_TYPE_NAME      VARCHAR2(255),
                CERT_VARIANT_CODE   VARCHAR2(50),
                CERT_VARIANT_NAME   VARCHAR2(255),
                LICENCE_PLATE       VARCHAR2(100),
                CHASSIS_NUMBER      VARCHAR2(100),
                POLICE_NUMBER       VARCHAR2(100),
                INSURED_NAME        VARCHAR2(255),
                INSURED_PHONE       VARCHAR2(50),
                INSURED_EMAIL       VARCHAR2(255),
                STARTS_AT           VARCHAR2(50),
                ENDS_AT             VARCHAR2(50),
                PRINTED_AT          VARCHAR2(50),
                ORG_CODE            VARCHAR2(100),
                OFFICE_CODE         VARCHAR2(100),
                USER_CODE           VARCHAR2(100),
                USER_NAME           VARCHAR2(255),
                USER_EMAIL          VARCHAR2(255),
                USER_TELEPHONE      VARCHAR2(50)
            )';

        -- PK
        SELECT COUNT(*) INTO v_cst 
        FROM user_constraints 
        WHERE constraint_name = 'PK_ZEN_CERT_PAYLOAD';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE 
                'ALTER TABLE ZEN_CERTIFICATES_PAYLOAD 
                 ADD CONSTRAINT PK_ZEN_CERT_PAYLOAD PRIMARY KEY (ID)';
        END IF;

        -- FK
        SELECT COUNT(*) INTO v_cst 
        FROM user_constraints 
        WHERE constraint_name = 'FK_CERT_PROD_PAYLOAD';
        IF v_cst = 0 THEN
            EXECUTE IMMEDIATE 
                'ALTER TABLE ZEN_CERTIFICATES_PAYLOAD 
                 ADD CONSTRAINT FK_CERT_PROD_PAYLOAD 
                 FOREIGN KEY (PRODUCTION_ID) 
                 REFERENCES ZEN_PRODUCTION_PAYLOAD(ID)';
        END IF;

    END IF;
END;
/


-- -------------------------------------------------------------
-- 5. Migration ancienne table ZEN_CERTIFICATES_PLAYLOAD
--    Suppression STATE + ajout STATE_NAME / STATE_LABEL
-- -------------------------------------------------------------
DECLARE
    v_table  NUMBER;
    v_col    NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_table 
    FROM user_tables 
    WHERE table_name = 'ZEN_CERTIFICATES_PLAYLOAD';

    IF v_table > 0 THEN

        -- Supprimer STATE si elle existe
        SELECT COUNT(*) INTO v_col 
        FROM user_tab_columns 
        WHERE table_name  = 'ZEN_CERTIFICATES_PLAYLOAD' 
          AND column_name = 'STATE';
        IF v_col > 0 THEN
            EXECUTE IMMEDIATE 
                'ALTER TABLE ZEN_CERTIFICATES_PLAYLOAD DROP COLUMN STATE';
        END IF;

        -- Ajouter STATE_NAME si elle n existe pas
        SELECT COUNT(*) INTO v_col 
        FROM user_tab_columns 
        WHERE table_name  = 'ZEN_CERTIFICATES_PLAYLOAD' 
          AND column_name = 'STATE_NAME';
        IF v_col = 0 THEN
            EXECUTE IMMEDIATE 
                'ALTER TABLE ZEN_CERTIFICATES_PLAYLOAD 
                 ADD STATE_NAME VARCHAR2(50)';
        END IF;

        -- Ajouter STATE_LABEL si elle n existe pas
        SELECT COUNT(*) INTO v_col 
        FROM user_tab_columns 
        WHERE table_name  = 'ZEN_CERTIFICATES_PLAYLOAD' 
          AND column_name = 'STATE_LABEL';
        IF v_col = 0 THEN
            EXECUTE IMMEDIATE 
                'ALTER TABLE ZEN_CERTIFICATES_PLAYLOAD 
                 ADD STATE_LABEL VARCHAR2(100)';
        END IF;

    END IF;
END;
/