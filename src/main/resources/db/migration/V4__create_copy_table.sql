CREATE TABLE copy (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      copy_number INT AUTO_INCREMENT,
                      purchase_date DATE NOT NULL,
                      book_id BIGINT NOT NULL,
                      FOREIGN KEY (book_id) REFERENCES book (id)
);