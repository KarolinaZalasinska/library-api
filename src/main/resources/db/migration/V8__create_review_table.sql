CREATE TABLE Review (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        rating INT NOT NULL,
                        description TEXT(50) NOT NULL,
                        created_at TIMESTAMP NOT NULL,
                        updated_at TIMESTAMP,
                        book_id INT NOT NULL,
                        user_id INT NOT NULL,
                        FOREIGN KEY (book_id) REFERENCES Book(id),
                        FOREIGN KEY (user_id) REFERENCES User(id)
);