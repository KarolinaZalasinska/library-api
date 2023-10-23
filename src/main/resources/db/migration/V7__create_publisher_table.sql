CREATE TABLE publisher
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    address     VARCHAR(255) NOT NULL,
    postal_code VARCHAR(6)   NOT NULL
);

ALTER TABLE book
    ADD COLUMN publisher_id BIGINT;
ALTER TABLE book
    ADD CONSTRAINT fk_book_publisher FOREIGN KEY (publisher_id) REFERENCES publisher (id);