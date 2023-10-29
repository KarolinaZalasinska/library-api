CREATE TABLE Category (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);

CREATE TABLE Book (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL
);

CREATE TABLE Book_Category (
                               book_id INT,
                               category_id INT,
                               PRIMARY KEY (book_id, category_id),
                               FOREIGN KEY (book_id) REFERENCES Book(id),
                               FOREIGN KEY (category_id) REFERENCES Category(id)
);