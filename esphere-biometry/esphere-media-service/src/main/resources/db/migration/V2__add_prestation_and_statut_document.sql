-- V2 - Ajout champs pour la revue anti-fraude des documents
-- prestation_id : lien vers la prestation (ordonnance/examen)
-- nature_prestation : 'ordonnance' | 'examen'
-- statut_document : 'en_attente_revue' | 'approuve' | 'rejete'
-- commentaire_rejet : texte obligatoire en cas de rejet

ALTER TABLE dbx45ty_media_prestation
    ADD COLUMN prestation_id     INT UNSIGNED  NULL         AFTER visite_id,
    ADD COLUMN nature_prestation VARCHAR(20)   NULL         AFTER prestation_id,
    ADD COLUMN statut_document   VARCHAR(30)   NOT NULL
                                 DEFAULT 'en_attente_revue' AFTER supprime,
    ADD COLUMN commentaire_rejet TEXT          NULL         AFTER statut_document;
