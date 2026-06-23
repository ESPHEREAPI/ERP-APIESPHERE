INSERT INTO dbx45ty_parametre (cle, valeur, description) VALUES
('OBSERVATION_REJET_OBLIGATOIRE', 'false', 'Si true, l''agent SS doit obligatoirement renseigner une observation lors du rejet d''une ligne de prestation.')
ON DUPLICATE KEY UPDATE cle = cle;
