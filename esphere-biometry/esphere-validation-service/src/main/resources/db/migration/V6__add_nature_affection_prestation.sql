/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  USER01
 * Created: May 27, 2026
 */

-- V6__add_nature_affection_prestation.sql
ALTER TABLE dbx45ty_prestation
ADD COLUMN nature_affection VARCHAR(100) NULL
    AFTER nature_prestation;