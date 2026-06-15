/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  USER01
 * Created: May 15, 2026
 */
-- Suppression de l'ancienne colonne STATE
ALTER TABLE ZEN_CERTIFICATES_PLAYLOAD DROP COLUMN STATE;
-- Ajout des deux nouvelles colonnes
ALTER TABLE ZEN_CERTIFICATES_PLAYLOAD ADD STATE_NAME  VARCHAR2(50);
ALTER TABLE ZEN_CERTIFICATES_PLAYLOAD ADD STATE_LABEL VARCHAR2(100);