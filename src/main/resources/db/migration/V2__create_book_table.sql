CREATE TABLE book (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL,
                      release_date DATE NOT NULL,
                      isbn VARCHAR(255) NOT NULL,
                      availability VARCHAR(255),
                      created_at TIMESTAMP NOT NULL,
                      updated_at TIMESTAMP,
                      publisher_id BIGINT,
                      FOREIGN KEY (publisher_id) REFERENCES publisher (id)
);

-- Create a table for the many-to-many relationship between Book and Author
CREATE TABLE book_author (
                             book_id BIGINT NOT NULL,
                             author_id BIGINT NOT NULL,
                             PRIMARY KEY (book_id, author_id),
                             FOREIGN KEY (book_id) REFERENCES book (id),
                             FOREIGN KEY (author_id) REFERENCES author (id)
);

-- Create a table for the many-to-many relationship between Book and Category
CREATE TABLE book_category (
                               book_id BIGINT NOT NULL,
                               category_id BIGINT NOT NULL,
                               PRIMARY KEY (book_id, category_id),
                               FOREIGN KEY (book_id) REFERENCES book (id),
                               FOREIGN KEY (category_id) REFERENCES category (id)
);

-- Create a table for the many-to-many relationship between Book and Library
CREATE TABLE book_library (
                              book_id BIGINT NOT NULL,
                              library_id BIGINT NOT NULL,
                              PRIMARY KEY (book_id, library_id),
                              FOREIGN KEY (book_id) REFERENCES book (id),
                              FOREIGN KEY (library_id) REFERENCES library (id)
);