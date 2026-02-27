
CREATE TABLE notification (
    notification_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(100),
    message VARCHAR(500),
    status VARCHAR(50),
    retry_count INT DEFAULT 0 CHECK (retry_count >= 0),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at TIMESTAMP,

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
        REFERENCES "users"(user_id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_notification_user ON notification(user_id);
CREATE INDEX idx_notification_status ON notification(status);
CREATE INDEX idx_notification_created_at ON notification(created_at);


CREATE TABLE activity_log (
    log_id BIGSERIAL PRIMARY KEY,
    action VARCHAR(255) NOT NULL,
    entity_type VARCHAR(100),
    entity_id BIGINT,
    ip_address VARCHAR(50),
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,

    CONSTRAINT fk_activitylog_user
        FOREIGN KEY (user_id)
        REFERENCES "users"(user_id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_activitylog_user ON activity_log(user_id);
CREATE INDEX idx_activitylog_entity ON activity_log(entity_type, entity_id);
CREATE INDEX idx_activitylog_timestamp ON activity_log(timestamp);