

CREATE TABLE loan_policy (
    policy_id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT DEFAULT NULL,
    role_id BIGINT DEFAULT NULL,
    max_books INT NOT NULL CHECK (max_books >= 0),
    loan_days INT NOT NULL CHECK (loan_days > 0),
    grace_days INT NOT NULL DEFAULT 0 CHECK (grace_days >= 0),
    fine_per_day DECIMAL(10,2) NOT NULL CHECK (fine_per_day >= 0),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

     CONSTRAINT fk_loanpolicy_branch
         FOREIGN KEY (branch_id)
         REFERENCES library_branch(branch_id)
         ON DELETE RESTRICT,

     CONSTRAINT fk_loanpolicy_role
         FOREIGN KEY (role_id)
         REFERENCES role(role_id)
         ON DELETE RESTRICT
);

CREATE INDEX idx_loanpolicy_branch ON loan_policy(branch_id);
CREATE INDEX idx_loanpolicy_role ON loan_policy(role_id);
CREATE INDEX idx_loanpolicy_status ON loan_policy(status);

CREATE TABLE holiday (
    holiday_id BIGSERIAL PRIMARY KEY,
    branch_id BIGINT DEFAULT NULL,
    date DATE NOT NULL,
    reason VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_holiday_branch
        FOREIGN KEY (branch_id)
        REFERENCES library_branch(branch_id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_holiday_branch ON holiday(branch_id);
CREATE INDEX idx_holiday_date ON holiday(date);
CREATE INDEX idx_holiday_status ON holiday(status);