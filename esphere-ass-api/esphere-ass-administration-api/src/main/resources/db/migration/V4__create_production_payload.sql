/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  USER01
 * Created: Mar 16, 2026
 */

-- ============================================
-- SEQUENCE PRODUCTION_PAYLOAD
-- ============================================
CREATE SEQUENCE SEQ_PRODUCTION_PAYLOAD START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE;

-- ============================================
-- TABLE PRODUCTION_PAYLOAD
-- ============================================
CREATE TABLE PRODUCTION_PAYLOAD (
    ID                       NUMBER(19,0) NOT NULL,
    CERTIFICATE_VARIANT_CODE VARCHAR2(255),
    RC                       NUMBER(10,0),
    POLICE_NUMBER            VARCHAR2(255),
    STARTS_AT                DATE,
    ENDS_AT                  DATE,
    CUSTOMER_NAME            VARCHAR2(255),
    CUSTOMER_PHONE           VARCHAR2(255),
    CUSTOMER_EMAIL           VARCHAR2(255),
    CUSTOMER_POSTAL_CODE     VARCHAR2(255),
    CUSTOMER_TYPE            VARCHAR2(255),
    INSURED_NAME             VARCHAR2(255),
    INSURED_PHONE            VARCHAR2(255),
    INSURED_EMAIL            VARCHAR2(255),
    INSURED_POSTAL_CODE      VARCHAR2(255),
    LICENCE_PLATE            VARCHAR2(255),
    VEHICLE_CHASSIS          VARCHAR2(255),
    VEHICLE_BRAND            VARCHAR2(255),
    VEHICLE_MODEL            VARCHAR2(255),
    VEHICLE_CATEGORY         VARCHAR2(255),
    VEHICLE_GENRE            VARCHAR2(255),
    VEHICLE_TYPE             VARCHAR2(255),
    VEHICULE_USAGE           VARCHAR2(255),
    VEHICLE_ENERGY           VARCHAR2(255),
    NB_OF_SEATS              NUMBER(10,0),
    FISCAL_POWER             NUMBER(10,0),
    CIRCULATION_ZONE         VARCHAR2(255),
    DRIVER_NAME              VARCHAR2(255),
    DRIVER_BIRTHDATE         DATE,
    DRIVER_LICENCE_ISSUED_AT DATE,
    VEHICLE_HAS_TRAILER      NUMBER(1,0),
    REPLICATION              NUMBER(1) DEFAULT 0 NOT NULL,
    CONSTRAINT PK_PRODUCTION_PAYLOAD PRIMARY KEY (ID)
);