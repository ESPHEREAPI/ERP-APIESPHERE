-- ============================================================
--  V15 — Remise à zéro du stock attestations
--  Supprime mouvements puis stocks, recrée les séquences à 1
-- ============================================================

-- 1. Vider l'historique des mouvements (FK d'abord)
DELETE FROM ZEN_MOUVEMENTS_STOCK;

-- 2. Vider les lignes de stock
DELETE FROM ZEN_STOCK_ATTESTATIONS;

-- 3. Réinitialiser les séquences à 1
DROP SEQUENCE SEQ_MOUVEMENTS_STOCK;
CREATE SEQUENCE SEQ_MOUVEMENTS_STOCK
    START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

DROP SEQUENCE SEQ_STOCK_ATTESTATIONS;
CREATE SEQUENCE SEQ_STOCK_ATTESTATIONS
    START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;
