
CREATE TABLE book_acquisition (
    acquisition_id BIGSERIAL PRIMARY KEY,
    book_id BIGINT NOT NULL REFERENCES book(book_id) ON DELETE RESTRICT,
    branch_id BIGINT NOT NULL REFERENCES library_branch(branch_id) ON DELETE RESTRICT,
    vendor VARCHAR(255) NOT NULL,
    cost DECIMAL(10,2) NOT NULL CHECK (cost >= 0),
    quantity INT NOT NULL CHECK (quantity > 0),
    purchase_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING, RECEIVED, CANCELLED
    received_by BIGINT, -- optional: user_id of staff who received books
    remarks TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_bookacquisition_book ON book_acquisition(book_id);
CREATE INDEX idx_bookacquisition_branch ON book_acquisition(branch_id);



CREATE TABLE book_copy (
    copy_id BIGSERIAL PRIMARY KEY,
    barcode VARCHAR(100) NOT NULL UNIQUE,
    shelf_location VARCHAR(100),
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE', -- AVAILABLE, LOANED, DAMAGED
    condition VARCHAR(50) NOT NULL DEFAULT 'NEW',    -- NEW, GOOD, FAIR, POOR
    book_id BIGINT NOT NULL REFERENCES book(book_id) ON DELETE RESTRICT,
    branch_id BIGINT NOT NULL REFERENCES library_branch(branch_id) ON DELETE RESTRICT,
    acquisition_id BIGINT REFERENCES book_acquisition(acquisition_id) ON DELETE SET NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_bookcopy_book ON book_copy(book_id);
CREATE INDEX idx_bookcopy_branch ON book_copy(branch_id);
CREATE INDEX idx_bookcopy_acquisition ON book_copy(acquisition_id);
CREATE INDEX idx_bookcopy_status ON book_copy(status);