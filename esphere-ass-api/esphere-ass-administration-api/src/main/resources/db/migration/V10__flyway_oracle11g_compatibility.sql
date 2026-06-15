-- =============================================================
-- V10__flyway_oracle11g_compatibility.sql
-- Objet : Ajustements de compatibilité Flyway / Oracle 11g
--         Index de performance sur les tables de payload
-- =============================================================

-- -------------------------------------------------------------
-- 1. Index sur ZEN_CERTIFICATES_PAYLOAD — recherches fréquentes
-- -------------------------------------------------------------
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_indexes
    WHERE index_name = 'IDX_CERT_USER_CODE';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_CERT_USER_CODE
             ON ZEN_CERTIFICATES_PAYLOAD (USER_CODE)';
    END IF;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_indexes
    WHERE index_name = 'IDX_CERT_LICENCE_PLATE';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_CERT_LICENCE_PLATE
             ON ZEN_CERTIFICATES_PAYLOAD (LICENCE_PLATE)';
    END IF;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_indexes
    WHERE index_name = 'IDX_CERT_PROD_ID';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_CERT_PROD_ID
             ON ZEN_CERTIFICATES_PAYLOAD (PRODUCTION_ID)';
    END IF;
END;
/

-- -------------------------------------------------------------
-- 2. Index sur ZEN_PRODUCTION_PAYLOAD
-- -------------------------------------------------------------
DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_indexes
    WHERE index_name = 'IDX_PROD_USER_ID';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_PROD_USER_ID
             ON ZEN_PRODUCTION_PAYLOAD (USER_ID)';
    END IF;
END;
/

DECLARE
    v_count NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
    FROM user_indexes
    WHERE index_name = 'IDX_PROD_REFERENCE';
    IF v_count = 0 THEN
        EXECUTE IMMEDIATE
            'CREATE INDEX IDX_PROD_REFERENCE
             ON ZEN_PRODUCTION_PAYLOAD (REFERENCE)';
    END IF;
END;
/

-- Fin V10
