INSERT INTO dbx45ty_parametre (cle, valeur, description) VALUES
('PARTAGE_WHATSAPP_ACTIF', 'true', 'Si true, afficher le bouton de partage WhatsApp apres soumission d''une prestation.'),
('PARTAGE_SMS_ACTIF', 'true', 'Si true, afficher le bouton d''envoi SMS du lien de capture apres soumission d''une prestation.')
ON DUPLICATE KEY UPDATE cle = cle;
