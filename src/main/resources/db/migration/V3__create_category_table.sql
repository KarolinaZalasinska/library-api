CREATE TABLE category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

-- Relacja wiele do wielu z książkami (tabela pośrednia book_category)
CREATE TABLE book_category (
                               book_id BIGINT NOT NULL,
                               category_id BIGINT NOT NULL,
                               PRIMARY KEY (book_id, category_id),
                               FOREIGN KEY (book_id) REFERENCES book (id),
                               FOREIGN KEY (category_id) REFERENCES category (id)
);