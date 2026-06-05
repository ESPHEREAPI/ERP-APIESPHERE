/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  USER01
 * Created: May 25, 2026
 */

-- Ajout du serial biométrique sur la table employé (si la colonne n'existe pas déjà)
ALTER TABLE dbx45ty_employe
    ADD COLUMN IF NOT EXISTS serial_biometrie VARCHAR(50) NULL
    COMMENT 'Numéro de série lecteur SecuGen - lu depuis configServiceSecugen.xml';

-- Index pour recherche rapide lors de la vérification OTP (si l'index n'existe pas déjà)
CREATE INDEX IF NOT EXISTS idx_employe_prestataire_serial
    ON dbx45ty_employe(prestataire_id, serial_biometrie);