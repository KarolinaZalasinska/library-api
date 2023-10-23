CREATE TABLE library (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    postal_code VARCHAR(6) NOT NULL
);

    ALTER TABLE library
    ADD CONSTRAINT UNIQUE (postal_code);