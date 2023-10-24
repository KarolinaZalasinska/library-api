CREATE TABLE Loan (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      dateOfBorrow DATE NOT NULL,
                      plannedReturnDate DATE NOT NULL,
                      book_id INT NOT NULL,
                      user_id INT NOT NULL,
                      FOREIGN KEY (book_id) REFERENCES Book(id),
                      FOREIGN KEY (user_id) REFERENCES User(id)
);