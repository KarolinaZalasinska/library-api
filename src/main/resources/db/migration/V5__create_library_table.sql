CREATE TABLE Library
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    address    VARCHAR(255) NOT NULL,
    postal_code CHAR(6)      NOT NULL,
    UNIQUE (postal_code)
);

CREATE TABLE Book
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    title  VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL
);

CREATE TABLE Book_Library (
                              book_id INT,
                              library_id INT,
                              PRIMARY KEY (book_id, library_id),
                              FOREIGN KEY (book_id) REFERENCES Book(id),
                              FOREIGN KEY (library_id) REFERENCES Library(id)
);
