CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    cost DOUBLE PRECISION,
    name VARCHAR(255),
    end_date TIMESTAMP
    );