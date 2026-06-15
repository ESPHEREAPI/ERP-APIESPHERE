-- V13 : Mise à jour des profils agents existants
--
-- Les lignes créées avant V12 ont toutes profil_agent = 'PRODUCTEUR' (valeur par défaut).
-- Ce script est un EXEMPLE de correction manuelle.
-- Remplacez 'MON_USERNAME_ADMIN' par le vrai username de l'administrateur en base.
--
-- Exemple :
--   UPDATE ZEN_INFOS_ADMIN_AGENCE SET profil_agent = 'ADMINISTRATEUR' WHERE username_asac = 'ADMIN_PRINCIPAL';
--   UPDATE ZEN_INFOS_ADMIN_AGENCE SET profil_agent = 'CHEF_BUREAU_DIRECT_SIEGE' WHERE username_asac = 'CHEF_DS_001';
--
-- Pour voir tous les agents actuels :
--   SELECT id, username_asac, libelle_agence, profil_agent FROM ZEN_INFOS_ADMIN_AGENCE;
--
-- Ce script ne fait rien par défaut — mettez à jour les profils via l'interface Angular
-- (Gestion des Agences > Modifier > champ Profil).

-- Aucune instruction DML ici volontairement.
-- Utilisez l'interface pour mettre à jour chaque agent.
-- Voir tous les agents et leurs profils actuels
SELECT id, username_asac, libelle_agence, office_code, profil_agent 
FROM ZEN_INFOS_ADMIN_AGENCE;

-- Mettre à jour vos agents (remplacez les usernames par les vrais)
UPDATE ZEN_INFOS_ADMIN_AGENCE 
  SET profil_agent = 'ADMINISTRATEUR' 
  WHERE username_asac = '01KN4FA2FPV4FNQ96SDDC0J2Q0';

UPDATE ZEN_INFOS_ADMIN_AGENCE 
  SET profil_agent = 'CHEF_BUREAU_DIRECT_SIEGE' 
  WHERE username_asac = '01KM0N5RMWJHAZCVT6VRZGSK8E';

COMMIT;