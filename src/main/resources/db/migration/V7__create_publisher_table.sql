CREATE TABLE Publisher (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           address VARCHAR(255) NOT NULL,
                           postalCode CHAR(6) NOT NULL
);

CREATE TABLE Book (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL,
                      publisher_id INT,
                      FOREIGN KEY (publisher_id) REFERENCES Publisher(id)
);