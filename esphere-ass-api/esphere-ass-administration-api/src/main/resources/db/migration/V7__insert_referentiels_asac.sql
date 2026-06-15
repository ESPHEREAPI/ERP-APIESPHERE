/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  USER01
 * Created: May 13, 2026
 */

-- ============================================
-- CATEGORIE VEHICULE
-- ============================================
CREATE TABLE ZEN_REF_CATEGORIE_VEHICULE (
    CODE     VARCHAR2(5)   NOT NULL,
    LIBELLE  VARCHAR2(100) NOT NULL,
    CONSTRAINT PK_REF_CAT_VEH PRIMARY KEY (CODE)
);

INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('01', 'Véhicule de tourisme (Personne physique)');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('02', 'Transport pour propre compte');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('03', 'Transport public de marchandises');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('04', 'Transport public de voyageurs');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('05', 'Véhicule motorisé à 2 ou 3 roues');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('06', 'Véhicule des garagistes');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('07', 'Véhicule d''auto-écoles');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('08', 'Véhicule de location');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('09', 'Engin de chantier');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('10', 'Véhicules spéciaux');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('11', 'Catégorie 11');
INSERT INTO ZEN_REF_CATEGORIE_VEHICULE VALUES ('12', 'Véhicule de tourisme (Personne morale)');

-- ============================================
-- GENRE VEHICULE
-- ============================================
CREATE TABLE ZEN_REF_GENRE_VEHICULE (
    CODE     VARCHAR2(5)   NOT NULL,
    LIBELLE  VARCHAR2(100) NOT NULL,
    CONSTRAINT PK_REF_GENRE_VEH PRIMARY KEY (CODE)
);

INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV01', 'Camion');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV02', 'Camionnette');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV03', 'Cyclomoteur (2/3 Roues)');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV04', 'Voiture (4 Roues)');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV05', 'Engins de chantiers');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV06', 'Car');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV07', 'Fourgonnette');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV08', 'Remorque');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV09', 'Scooter');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV10', 'Semi-remorque');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV11', 'Tracteur agricole');
INSERT INTO ZEN_REF_GENRE_VEHICULE VALUES ('GV12', 'Tracteur routier');

-- ============================================
-- TYPE VEHICULE
-- ============================================
CREATE TABLE ZEN_REF_TYPE_VEHICULE (
    CODE     VARCHAR2(5)   NOT NULL,
    LIBELLE  VARCHAR2(100) NOT NULL,
    CONSTRAINT PK_REF_TYPE_VEH PRIMARY KEY (CODE)
);

INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV01', 'Ambulance');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV02', 'Auto Car (Plus de 41 places passager, sans chauffeur)');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV03', 'Corbillard');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV04', 'Mini Car (9 à 40 places passager, sans chauffeur)');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV05', 'Taxi Communaux');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV06', 'Taxi Urbain (MATCA, VTC, ...)');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV07', 'Véhicule Auto-École');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV08', 'Véhicule de Service Public (État, ramassage d''ordures)');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV09', 'Véhicule de Tourisme (max 9 places, avec chauffeur)');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV10', 'Véhicule Particulier (PTAC max 3,5 T)');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV11', 'Véhicule Utilitaire (Van, Fourgonnette, Camionnette, Camion, Tracteur)');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV12', 'Voiture de Location');
INSERT INTO ZEN_REF_TYPE_VEHICULE VALUES ('TV13', 'Cyclomoteur (2/3 Roues)');

-- ============================================
-- USAGE VEHICULE
-- ============================================
CREATE TABLE ZEN_REF_USAGE_VEHICULE (
    CODE     VARCHAR2(5)   NOT NULL,
    LIBELLE  VARCHAR2(100) NOT NULL,
    CONSTRAINT PK_REF_USAGE_VEH PRIMARY KEY (CODE)
);

INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV01', 'Promenade ou Affaire');
INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV02', 'Transport pour propre compte');
INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV03', 'Transport privé de voyageurs');
INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV04', 'Transport public de marchandises');
INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV05', 'Transport public de voyageurs');
INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV06', 'Véhicules Auto-école');
INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV07', 'Véhicules de Location');
INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV08', 'Véhicules Spéciaux');
INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV09', 'Engin de Chantier');
INSERT INTO ZEN_REF_USAGE_VEHICULE VALUES ('UV10', 'Véhicule motorisé 2 à 3 roues');

-- ============================================
-- ENERGIE VEHICULE
-- ============================================
CREATE TABLE ZEN_REF_ENERGIE_VEHICULE (
    CODE     VARCHAR2(5)   NOT NULL,
    LIBELLE  VARCHAR2(50)  NOT NULL,
    CONSTRAINT PK_REF_ENERGIE_VEH PRIMARY KEY (CODE)
);

INSERT INTO ZEN_REF_ENERGIE_VEHICULE VALUES ('SEES', 'Essence');
INSERT INTO ZEN_REF_ENERGIE_VEHICULE VALUES ('SEDI', 'Diesel');
INSERT INTO ZEN_REF_ENERGIE_VEHICULE VALUES ('SEEL', 'Électrique');
INSERT INTO ZEN_REF_ENERGIE_VEHICULE VALUES ('SEHY', 'Hybride');

-- ============================================
-- ZONE DE CIRCULATION
-- ============================================
CREATE TABLE ZEN_REF_ZONE_CIRCULATION (
    CODE     VARCHAR2(2)   NOT NULL,
    LIBELLE  VARCHAR2(50)  NOT NULL,
    CONSTRAINT PK_REF_ZONE_CIRC PRIMARY KEY (CODE)
);

INSERT INTO ZEN_REF_ZONE_CIRCULATION VALUES ('A', 'Zone A');
INSERT INTO ZEN_REF_ZONE_CIRCULATION VALUES ('B', 'Zone B');
INSERT INTO ZEN_REF_ZONE_CIRCULATION VALUES ('C', 'Zone C');

-- ============================================
-- TYPE ASSURE
-- ============================================
CREATE TABLE ZEN_REF_TYPE_ASSURE (
    CODE     VARCHAR2(5)   NOT NULL,
    LIBELLE  VARCHAR2(50)  NOT NULL,
    CONSTRAINT PK_REF_TYPE_ASSURE PRIMARY KEY (CODE)
);

INSERT INTO ZEN_REF_TYPE_ASSURE VALUES ('TAPP', 'Personne Physique');
INSERT INTO ZEN_REF_TYPE_ASSURE VALUES ('TAPM', 'Personne Morale');

-- ============================================
-- PROFESSION ASSURE
-- ============================================
CREATE TABLE ZEN_REF_PROFESSION_ASSURE (
    CODE     VARCHAR2(5)   NOT NULL,
    LIBELLE  VARCHAR2(100) NOT NULL,
    CONSTRAINT PK_REF_PROF_ASSURE PRIMARY KEY (CODE)
);

INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST01', 'Agent commercial');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST02', 'Agent de recouvrement');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST03', 'Agriculteur');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST04', 'Artisan');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST05', 'Conjoint');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST06', 'Employeur');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST07', 'Religieux');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST08', 'Retraité');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST09', 'Salarié');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST10', 'Sans emploi');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST11', 'VRP (Vendeur, Représentant et Placier)');
INSERT INTO ZEN_REF_PROFESSION_ASSURE VALUES ('ST12', 'Autre profession');

COMMIT;