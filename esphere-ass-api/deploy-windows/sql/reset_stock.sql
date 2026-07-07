-- ============================================================
--  RESET COMPLET DU STOCK ATTESTATIONS
--  Tables : ZEN_MOUVEMENTS_STOCK  (enfant)
--           ZEN_STOCK_ATTESTATIONS (parent)
--  Séquences remises à 1
--
--  ATTENTION : action irréversible — faire un ROLLBACK
--              si le résultat ne convient pas avant COMMIT.
-- ============================================================

-- 1. Supprimer d'abord les mouvements (FK vers ZEN_STOCK_ATTESTATIONS)
DELETE FROM ZEN_MOUVEMENTS_STOCK;

-- 2. Supprimer les lignes de stock
DELETE FROM ZEN_STOCK_ATTESTATIONS;

-- 3. Remettre les séquences à 1
--    (Oracle : on recrée la séquence avec START WITH 1)
DROP   SEQUENCE SEQ_MOUVEMENTS_STOCK;
CREATE SEQUENCE SEQ_MOUVEMENTS_STOCK
  START WITH 1
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;

DROP   SEQUENCE SEQ_STOCK_ATTESTATIONS;
CREATE SEQUENCE SEQ_STOCK_ATTESTATIONS
  START WITH 1
  INCREMENT BY 1
  NOCACHE
  NOCYCLE;

-- 4. Valider
COMMIT;

-- 5. Vérification
SELECT 'ZEN_STOCK_ATTESTATIONS' AS table_name, COUNT(*) AS nb_lignes FROM ZEN_STOCK_ATTESTATIONS
UNION ALL
SELECT 'ZEN_MOUVEMENTS_STOCK',                COUNT(*)                FROM ZEN_MOUVEMENTS_STOCK;
