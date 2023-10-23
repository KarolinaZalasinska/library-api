CREATE TABLE Library
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    address    VARCHAR(255) NOT NULL,
    postalCode CHAR(6)      NOT NULL
);

CREATE TABLE Book
(
    id     INT AUTO_INCREMENT PRIMARY KEY,
    title  VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL
);

CREATE TABLE Library_Books
(
    library_id INT,
    book_id    INT,
    PRIMARY KEY (library_id, book_id),
    FOREIGN KEY (library_id) REFERENCES Library (id),
    FOREIGN KEY (book_id) REFERENCES Book (id)
);
