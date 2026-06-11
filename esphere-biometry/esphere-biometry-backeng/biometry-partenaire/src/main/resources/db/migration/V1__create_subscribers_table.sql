/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  USER01
 * Created: Jun 8, 2026
 */

-- V2__create_subscribers_table.sql
-- (adapte le numéro de version selon tes migrations existantes)

CREATE TABLE IF NOT EXISTS subscribers (
    id                    BIGINT          NOT NULL AUTO_INCREMENT,
    policy_number         VARCHAR(20)     NOT NULL,
    full_name             VARCHAR(150)    NOT NULL,
    phone_number          VARCHAR(25)     NOT NULL,
    email                 VARCHAR(100)    NOT NULL,
    username              VARCHAR(60)     NOT NULL,
    password_hash         VARCHAR(255),
    is_active             TINYINT(1)      NOT NULL DEFAULT 1,
    password_mode         ENUM('ACTIVATION_LINK','MANUAL') NOT NULL DEFAULT 'MANUAL',
    activation_token      VARCHAR(255),
    activation_token_expiry DATETIME(6),
    activation_sent_at    DATETIME(6),
    activation_duration_hrs INT,
    account_activated_at  DATETIME(6),
    created_at            DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at            DATETIME(6)     NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    created_by            VARCHAR(60),
    updated_by            VARCHAR(60),
    version               BIGINT          NOT NULL DEFAULT 0,
    effet_police          DATE,
    echeance_police       DATE,

    PRIMARY KEY (id),
    UNIQUE KEY uk_subscribers_policy_number (policy_number),
    UNIQUE KEY uk_subscribers_email (email),
    UNIQUE KEY uk_subscribers_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;