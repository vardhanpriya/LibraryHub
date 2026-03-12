CREATE TABLE wish_list (
    wishlist_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    added_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes VARCHAR(500),

    CONSTRAINT fk_wishlist_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_wishlist_book
        FOREIGN KEY (book_id)
        REFERENCES book(book_id)
        ON DELETE RESTRICT,

    CONSTRAINT uq_wishlist_user_book
        UNIQUE (user_id, book_id)
);

CREATE INDEX idx_wishlist_user ON wish_list(user_id);
CREATE INDEX idx_wishlist_book ON wish_list(book_id);
CREATE INDEX idx_wishlist_added_at ON wish_list(added_at);