CREATE TABLE Book (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL,
                      release_date DATE NOT NULL,
                      isbn VARCHAR(255) NOT NULL,
                      availability VARCHAR(255),
                      created_at TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP,
                      publisher_id INT,
                      FOREIGN KEY (publisher_id) REFERENCES Publisher(id)
);

CREATE TABLE Copy (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      book_id INT NOT NULL,
                      FOREIGN KEY (book_id) REFERENCES Book(id)
);

CREATE TABLE Loan (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      date_of_borrow DATE NOT NULL,
                      planned_return_date DATE NOT NULL,
                      book_id INT NOT NULL,
                      user_id INT NOT NULL,
                      FOREIGN KEY (book_id) REFERENCES Book(id),
                      FOREIGN KEY (user_id) REFERENCES User(id)
);

CREATE TABLE book_author (
                             book_id INT,
                             author_id INT,
                             PRIMARY KEY (book_id, author_id),
                             FOREIGN KEY (book_id) REFERENCES Book(id),
                             FOREIGN KEY (author_id) REFERENCES Author(id)
);

CREATE TABLE book_category (
                               book_id INT,
                               category_id INT,
                               PRIMARY KEY (book_id, category_id),
                               FOREIGN KEY (book_id) REFERENCES Book(id),
                               FOREIGN KEY (category_id) REFERENCES Category(id)
);

CREATE TABLE Book_Library (
                              book_id INT,
                              library_id INT,
                              PRIMARY KEY (book_id, library_id),
                              FOREIGN KEY (book_id) REFERENCES Book(id),
                              FOREIGN KEY (library_id) REFERENCES Library(id)
);