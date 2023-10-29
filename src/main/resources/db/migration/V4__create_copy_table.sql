CREATE TABLE Copy (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      copy_number INT NOT NULL,
                      purchase_date DATE NOT NULL,
                      book_id INT NOT NULL,
                      FOREIGN KEY (book_id) REFERENCES Book(id),
                      UNIQUE (copy_number)
);