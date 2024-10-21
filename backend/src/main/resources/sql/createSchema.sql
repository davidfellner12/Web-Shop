CREATE TABLE IF NOT EXISTS customers
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    last_name     VARCHAR(255) NOT NULL,
    date_of_birth DATE         NOT NULL,
    email         VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS articles
(
        id  BIGINT AUTO_INCREMENT PRIMARY KEY,
        designation VARCHAR(255) NOT NULL UNIQUE,
        description VARCHAR(255) NOT NULL,
        price       Integer      NOT NULL,
        image       BLOB
);
