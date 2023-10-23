CREATE TABLE review
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    rating      INTEGER   NOT NULL,
    description TEXT(50)  NOT NULL,
    created_at  TIMESTAMP NOT NULL,
    updated_at  TIMESTAMP,
    book_id     BIGINT    NOT NULL,
    user_id     BIGINT    NOT NULL
);

ALTER TABLE review
    ADD CONSTRAINT fk_review_book FOREIGN KEY (book_id) REFERENCES book (id);
ALTER TABLE review
    ADD CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES user (id);