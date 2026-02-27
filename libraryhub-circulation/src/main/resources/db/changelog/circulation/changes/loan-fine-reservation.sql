

CREATE TABLE loan (
    loan_id BIGSERIAL PRIMARY KEY,
    issue_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    status VARCHAR(50) NOT NULL,
    renewal_count INT NOT NULL DEFAULT 0,
    max_renewals INT NOT NULL DEFAULT 3,
    copy_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_loan_copy
        FOREIGN KEY (copy_id)
        REFERENCES book_copy(copy_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_loan_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_loan_copy ON loan(copy_id);
CREATE INDEX idx_loan_user ON loan(user_id);
CREATE INDEX idx_loan_status ON loan(status);



CREATE TABLE fine (
    fine_id BIGSERIAL PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
    paid BOOLEAN NOT NULL DEFAULT FALSE,
    paid_date DATE,
    loan_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_fine_loan
        FOREIGN KEY (loan_id)
        REFERENCES loan(loan_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_fine_loan ON fine(loan_id);
CREATE INDEX idx_fine_paid ON fine(paid);


CREATE TABLE reservation (
    reservation_id BIGSERIAL PRIMARY KEY,
    reservation_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    priority INT NOT NULL DEFAULT 1,
    status VARCHAR(50) NOT NULL,
    user_id BIGINT NOT NULL,
    book_id BIGINT NOT NULL,
    branch_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reservation_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_reservation_book
        FOREIGN KEY (book_id)
        REFERENCES book(book_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_reservation_branch
        FOREIGN KEY (branch_id)
        REFERENCES library_branch(branch_id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_reservation_user ON reservation(user_id);
CREATE INDEX idx_reservation_book ON reservation(book_id);
CREATE INDEX idx_reservation_branch ON reservation(branch_id);
CREATE INDEX idx_reservation_status ON reservation(status);