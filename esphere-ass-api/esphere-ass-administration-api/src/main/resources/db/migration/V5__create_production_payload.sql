/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  USER01
 * Created: Mar 23, 2026
 */

-- ============================================================
-- Flyway Migration V1
-- Création des tables ZEN_PRODUCTION_PLAYLOAD et ZEN_CERTIFICATES_PLAYLOAD
-- Base de données : Oracle 11g
-- ============================================================

-- ============================================================
-- SEQUENCES
-- ============================================================

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_sequences
    WHERE sequence_name = 'SEQ_PRODUCTION_PAYLOAD';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE SEQUENCE SEQ_PRODUCTION_PAYLOAD
                START WITH 1
                INCREMENT BY 1
                NOCACHE
                NOCYCLE
        ';
    END IF;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_sequences
    WHERE sequence_name = 'SEQ_CERTIFICATES';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE SEQUENCE SEQ_CERTIFICATES
                START WITH 1
                INCREMENT BY 1
                NOCACHE
                NOCYCLE
        ';
    END IF;
END;
/

-- ============================================================
-- TABLE : ZEN_PRODUCTION_PLAYLOAD
-- ============================================================

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_tables
    WHERE table_name = 'ZEN_PRODUCTION_PLAYLOAD';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE ZEN_PRODUCTION_PLAYLOAD (
                ID                      NUMBER                      NOT NULL,
                EXTERNAL_ID             VARCHAR2(255),
                REFERENCE               VARCHAR2(255),
                CHANNEL                 VARCHAR2(255),
                QUANTITY                NUMBER(10),
                SENT_TO_STORAGE         NUMBER(1)   DEFAULT 0,
                DOWNLOAD_LINK           VARCHAR2(500),

                USER_ID                 VARCHAR2(255),
                USER_NAME               VARCHAR2(255),
                USER_EMAIL              VARCHAR2(255),

                ORG_ID                  VARCHAR2(255),
                ORG_NAME                VARCHAR2(255),
                ORG_CODE                VARCHAR2(255),

                OFFICE_ID               VARCHAR2(255),
                OFFICE_NAME             VARCHAR2(255),
                OFFICE_CODE             VARCHAR2(255),

                CREATED_AT              TIMESTAMP WITH TIME ZONE,
                UPDATED_AT              TIMESTAMP WITH TIME ZONE,
                FORMATTED_CREATED_AT    VARCHAR2(255),

                CONSTRAINT PK_PRODUCTION_PLAYLOAD PRIMARY KEY (ID),
                CONSTRAINT CHK_PROD_SENT_TO_STORAGE CHECK (SENT_TO_STORAGE IN (0, 1))
            )
        ';
    END IF;
END;
/

-- ============================================================
-- TABLE : ZEN_CERTIFICATES_PLAYLOAD
-- ============================================================

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_tables
    WHERE table_name = 'ZEN_CERTIFICATES_PLAYLOAD';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE TABLE ZEN_CERTIFICATES_PLAYLOAD (
                ID                  NUMBER          NOT NULL,
                PRODUCTION_ID       NUMBER          NOT NULL,

                REFERENCE           VARCHAR2(255),
                STATE               VARCHAR2(255),
                DOWNLOAD_LINK       VARCHAR2(500),
                PDF_BYTES           BLOB,

                CERT_TYPE_CODE      VARCHAR2(255),
                CERT_TYPE_NAME      VARCHAR2(255),
                CERT_VARIANT_CODE   VARCHAR2(255),
                CERT_VARIANT_NAME   VARCHAR2(255),

                LICENCE_PLATE       VARCHAR2(255),
                CHASSIS_NUMBER      VARCHAR2(255),
                POLICE_NUMBER       VARCHAR2(255),

                INSURED_NAME        VARCHAR2(255),
                INSURED_PHONE       VARCHAR2(255),
                INSURED_EMAIL       VARCHAR2(255),

                STARTS_AT           VARCHAR2(255),
                ENDS_AT             VARCHAR2(255),
                PRINTED_AT          VARCHAR2(255),

                ORG_CODE            VARCHAR2(255),
                OFFICE_CODE         VARCHAR2(255),

                USER_CODE           VARCHAR2(255),
                USER_NAME           VARCHAR2(255),
                USER_EMAIL          VARCHAR2(255),
                USER_TELEPHONE      VARCHAR2(255),

                CONSTRAINT PK_CERTIFICATES_PLAYLOAD PRIMARY KEY (ID),
                CONSTRAINT FK_CERT_PRODUCTION FOREIGN KEY (PRODUCTION_ID)
                    REFERENCES ZEN_PRODUCTION_PLAYLOAD (ID) ON DELETE CASCADE
            )
        ';
    END IF;
END;
/

-- ============================================================
-- INDEX sur la clé étrangère
-- ============================================================

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_indexes
    WHERE index_name = 'IDX_CERT_PRODUCTION_ID';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE '
            CREATE INDEX IDX_CERT_PRODUCTION_ID
            ON ZEN_CERTIFICATES_PLAYLOAD (PRODUCTION_ID)
        ';
    END IF;
END;
/

-- ============================================================
-- COMMENTAIRES
-- ============================================================

COMMENT ON TABLE  ZEN_PRODUCTION_PLAYLOAD                       IS 'Productions / commandes groupées de certificats';
COMMENT ON COLUMN ZEN_PRODUCTION_PLAYLOAD.SENT_TO_STORAGE       IS 'Boolean : 0 = false, 1 = true';

COMMENT ON TABLE  ZEN_CERTIFICATES_PLAYLOAD                     IS 'Certificats individuels liés à une production';
COMMENT ON COLUMN ZEN_CERTIFICATES_PLAYLOAD.PDF_BYTES           IS 'Contenu binaire du PDF téléchargé (BLOB)';
COMMENT ON COLUMN ZEN_CERTIFICATES_PLAYLOAD.PRODUCTION_ID       IS 'Clé étrangère vers ZEN_PRODUCTION_PLAYLOAD';