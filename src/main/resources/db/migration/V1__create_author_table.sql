DROP TABLE Author;
CREATE TABLE Author
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP
);

-- Relacja wiele do wielu z książkami (tabela pośrednia book_author)
CREATE TABLE Book (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author_id INT,
                      FOREIGN KEY (author_id) REFERENCES Author(id)
);

CREATE TABLE Author_Books (
                              author_id INT,
                              book_id INT,
                              PRIMARY KEY (author_id, book_id),
                              FOREIGN KEY (author_id) REFERENCES Author(id),
                              FOREIGN KEY (book_id) REFERENCES Book(id)
);