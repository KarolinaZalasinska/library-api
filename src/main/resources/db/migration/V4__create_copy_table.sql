CREATE TABLE copy (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      copyNumber INT UNIQUE,
                      purchase_date DATE NOT NULL,
                      book_id BIGINT NOT NULL,
                      FOREIGN KEY (book_id) REFERENCES book (id)
);