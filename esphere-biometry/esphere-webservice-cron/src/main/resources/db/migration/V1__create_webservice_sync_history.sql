CREATE TABLE webservice_sync_history (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    type_synchronisation  VARCHAR(50)  NOT NULL,
    declencheur           VARCHAR(20)  NOT NULL,
    police_filtre         VARCHAR(100) NULL,
    date_debut            DATETIME     NOT NULL,
    date_fin              DATETIME     NULL,
    statut                VARCHAR(20)  NOT NULL,
    nb_traites            INT          NULL,
    nb_echecs             INT          NULL,
    details_echecs        LONGTEXT     NULL,
    message_erreur        LONGTEXT     NULL,

    INDEX idx_sync_history_type (type_synchronisation),
    INDEX idx_sync_history_date_debut (date_debut)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
