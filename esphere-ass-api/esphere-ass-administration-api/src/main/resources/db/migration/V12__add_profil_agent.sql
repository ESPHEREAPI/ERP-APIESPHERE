-- V12 : Ajout profil agent et permission d'édition sur ZEN_INFOS_ADMIN_AGENCE
--
-- profil_agent : rôle fonctionnel de l'agent dans son bureau
--   PRODUCTEUR              → accès validation uniquement
--   CHEF_BUREAU_AGENT       → validation + stock (son bureau)
--   CHEF_BUREAU_DIRECT_SIEGE → validation + stock (son bureau)
--   ADMINISTRATEUR          → tous modules, tous bureaux
--
-- can_edit : 1 = autorisé à modifier les champs éditables lors de la validation

ALTER TABLE ZEN_INFOS_ADMIN_AGENCE
  ADD profil_agent VARCHAR2(40) DEFAULT 'PRODUCTEUR' NOT NULL;

ALTER TABLE ZEN_INFOS_ADMIN_AGENCE
  ADD can_edit NUMBER(1,0) DEFAULT 0 NOT NULL;
