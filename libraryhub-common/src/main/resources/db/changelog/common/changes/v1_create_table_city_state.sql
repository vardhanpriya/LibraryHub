
CREATE TABLE state (
    state_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    code VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT uq_state_name UNIQUE (name)
);

CREATE TABLE city (
    city_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    state_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_city_state
        FOREIGN KEY (state_id)
        REFERENCES state (state_id)
        ON DELETE RESTRICT,

    CONSTRAINT uq_city_name_state
        UNIQUE (name, state_id)
);

CREATE INDEX idx_city_state ON city(state_id);


CREATE TABLE library_branch (
    branch_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    address TEXT NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(150),
    opening_hours VARCHAR(255),
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    max_capacity INTEGER,
    city_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_branch_city
        FOREIGN KEY (city_id)
        REFERENCES city(city_id)
        ON DELETE RESTRICT,

    CONSTRAINT uq_branch_name_city
        UNIQUE (name, city_id)
);

CREATE INDEX idx_branch_city ON library_branch(city_id);
CREATE INDEX idx_branch_status ON library_branch(status);
CREATE INDEX idx_branch_is_deleted ON library_branch(is_deleted);

-- Insert Indian States
INSERT INTO state (state_id, name, code, status, created_at)
VALUES
(1, 'Maharashtra', 'MH', 'ACTIVE', CURRENT_TIMESTAMP),
(2, 'Karnataka', 'KA', 'ACTIVE', CURRENT_TIMESTAMP),
(3, 'Delhi', 'DL', 'ACTIVE', CURRENT_TIMESTAMP),
(4, 'Tamil Nadu', 'TN', 'ACTIVE', CURRENT_TIMESTAMP),
(5, 'West Bengal', 'WB', 'ACTIVE', CURRENT_TIMESTAMP);


-- Insert Cities corresponding to the above states
INSERT INTO city (city_id, name, state_id, status, created_at)
VALUES
-- Maharashtra
(1, 'Mumbai', 1, 'ACTIVE', CURRENT_TIMESTAMP),
(2, 'Pune', 1, 'ACTIVE', CURRENT_TIMESTAMP),
(3, 'Nagpur', 1, 'ACTIVE', CURRENT_TIMESTAMP),

-- Karnataka
(4, 'Bangalore', 2, 'ACTIVE', CURRENT_TIMESTAMP),
(5, 'Mysore', 2, 'ACTIVE', CURRENT_TIMESTAMP),

-- Delhi
(6, 'New Delhi', 3, 'ACTIVE', CURRENT_TIMESTAMP),

-- Tamil Nadu
(7, 'Chennai', 4, 'ACTIVE', CURRENT_TIMESTAMP),
(8, 'Coimbatore', 4, 'ACTIVE', CURRENT_TIMESTAMP),

-- West Bengal
(9, 'Kolkata', 5, 'ACTIVE', CURRENT_TIMESTAMP),
(10, 'Durgapur', 5, 'ACTIVE', CURRENT_TIMESTAMP);


INSERT INTO library_branch
(branch_id, name, address, city_id, status, created_at)
VALUES
(1, 'Main Branch', '123 Main St', 1, 'ACTIVE', CURRENT_TIMESTAMP),
(3, 'South Branch', '123 Main St', 1, 'ACTIVE', CURRENT_TIMESTAMP),
(2, 'East Branch', '456 East St', 2, 'ACTIVE', CURRENT_TIMESTAMP);

