
CREATE TABLE email_verification_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT uk_email_verification_token_token UNIQUE (token),
    CONSTRAINT fk_email_verification_token_user
        FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON DELETE CASCADE
);

CREATE INDEX idx_email_verification_token_token
    ON email_verification_token (token);

CREATE INDEX idx_email_verification_token_user_id
    ON email_verification_token (user_id);
