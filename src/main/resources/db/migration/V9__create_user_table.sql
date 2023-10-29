CREATE TABLE user
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    email      VARCHAR(50)  NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL CHECK (CHAR_LENGTH(password) >= 10),
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX index_user_email ON user (email);

CREATE TABLE Loan (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      date_of_borrow DATE NOT NULL,
                      planned_return_date DATE NOT NULL,
                      book_id INT NOT NULL,
                      user_id INT NOT NULL,
                      FOREIGN KEY (book_id) REFERENCES Book(id),
                      FOREIGN KEY (user_id) REFERENCES User(id)
);