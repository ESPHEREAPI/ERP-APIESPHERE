-- ══════════════════════════════════════════════════════════════════════════════
-- V8 — Parametres systeme : documents + notifications + alertes
-- ══════════════════════════════════════════════════════════════════════════════

INSERT INTO dbx45ty_parametre (cle, valeur, description) VALUES

-- Documents de prise en charge
('DOCUMENT_MAX_FICHIERS',
 '5',
 'Nombre maximum de fichiers autorises par prestation (ordonnance/examen)'),

('DOCUMENT_TYPES_AUTORISES',
 'image/jpeg,image/png,application/pdf',
 'Types MIME acceptes pour les documents filmes (separes par des virgules)'),

-- Notification assure / ayant-droit apres traitement prestation
('NOTIF_ASSURE_TRAITEMENT_PRESTATION',
 'true',
 'Si true : envoyer un SMS a l''assure (ou ayant-droit) quand sa prestation est traitee par le Service Sante (validee ou rejetee). Gere par esphere-notification-service.'),

-- Notification Service Sante lors d une nouvelle prestation en ligne
('NOTIF_SS_NOUVELLE_PRESTATION',
 'true',
 'Si true : notifier le Service Sante quand un prestataire soumet une nouvelle prestation en ligne (ordonnance ou examen en attente de validation). Gere par esphere-notification-service.'),

-- Envoi de statistiques par e-mail
('NOTIF_STATS_EMAIL_ACTIF',
 'false',
 'Si true : envoyer periodiquement les statistiques de consommation par e-mail aux administrateurs. Contenu et frequence configures ulterieurement.'),

('NOTIF_STATS_EMAIL_DESTINATAIRES',
 '',
 'Liste des adresses e-mail pour les rapports statistiques (separees par des virgules). Ex : admin@esphere.com,direction@esphere.com'),

-- Alerte consommation abusive d un assure
('NOTIF_ALERTE_CONSO_ABUSIVE',
 'true',
 'Si true : declencher une alerte quand la consommation d''un assure depasse le seuil SEUIL_ALERTE_CONSO (%). Notification vers le Service Sante et par e-mail si actif.'),

('SEUIL_ALERTE_CONSO',
 '80',
 'Seuil en pourcentage (%) de consommation annuelle au-dela duquel une alerte de consommation excessive est emise pour un assure.'),

-- Alerte fraude prestataire
('NOTIF_ALERTE_FRAUDE_PRESTATAIRE',
 'true',
 'Si true : declencher une alerte quand un comportement suspect est detecte sur un prestataire (ex : volume anormal de prestations). Notification vers le Service Sante et par e-mail.'),

('SEUIL_ALERTE_FRAUDE_NB_PRESTATIONS_JOUR',
 '20',
 'Nombre maximum de prestations par jour pour un prestataire avant declenchement de l''alerte de fraude potentielle.')

ON DUPLICATE KEY UPDATE cle = cle;
