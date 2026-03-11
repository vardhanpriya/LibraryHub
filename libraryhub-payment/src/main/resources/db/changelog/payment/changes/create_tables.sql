
-- Table: SubscriptionPlan

CREATE TABLE subscription_plan (
    plan_id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    duration_days INT NOT NULL CHECK (duration_days > 0),
    price DECIMAL(10,2) NOT NULL CHECK (price >= 0),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_subscriptionplan_status ON subscription_plan(status);


-- Table: UserSubscription

CREATE TABLE user_subscription (
    subscription_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    auto_renew BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT chk_subscription_dates
    CHECK (end_date > start_date),

    CONSTRAINT fk_usersubscription_user
        FOREIGN KEY (user_id)
        REFERENCES "users"(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_usersubscription_plan
        FOREIGN KEY (plan_id)
        REFERENCES subscription_plan(plan_id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_usersubscription_user ON user_subscription(user_id);
CREATE INDEX idx_usersubscription_plan ON user_subscription(plan_id);
CREATE INDEX idx_usersubscription_status ON user_subscription(status);


-- Table: Payment

CREATE TABLE payment (
    payment_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    subscription_id BIGINT,
    amount DECIMAL(10,2) NOT NULL CHECK (amount >= 0),
   currency VARCHAR(10) NOT NULL DEFAULT 'INR',
    payment_for VARCHAR(20) NOT NULL,
    reference_id BIGINT,
    payment_method VARCHAR(50),
    payment_status VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100),
    payment_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_payment_user
        FOREIGN KEY (user_id)
        REFERENCES "users"(user_id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_payment_subscription
        FOREIGN KEY (subscription_id)
        REFERENCES user_subscription(subscription_id)
        ON DELETE SET NULL
);

CREATE INDEX idx_payment_user ON payment(user_id);
CREATE INDEX idx_payment_subscription ON payment(subscription_id);
CREATE INDEX idx_payment_status ON payment(payment_status);
CREATE INDEX idx_payment_date ON payment(payment_date);


CREATE TABLE payment_transaction (
    id BIGSERIAL PRIMARY KEY,
    payment_id BIGINT NOT NULL,

    gateway VARCHAR(50) NOT NULL,
    gateway_order_id VARCHAR(255),
    gateway_payment_id VARCHAR(255),
    gateway_signature VARCHAR(255),

    status VARCHAR(50) NOT NULL,
    gateway_response TEXT,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment
        FOREIGN KEY(payment_id)
        REFERENCES payment(payment_id)
        ON DELETE CASCADE,

        CONSTRAINT uq_gateway_payment UNIQUE(gateway_payment_id)
);
CREATE INDEX idx_payment_transaction_payment ON payment_transaction(payment_id);