CREATE TABLE loan (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_of_borrow DATE NOT NULL,
    planned_return_date DATE NOT NULL,
    book_id BIGINT,
    user_id BIGINT,
    FOREIGN KEY (book_id) REFERENCES book (id),
    FOREIGN KEY (user_id) REFERENCES user (id)

);