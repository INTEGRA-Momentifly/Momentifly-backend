CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    cost DOUBLE PRECISION NOT NULL,
    name VARCHAR(255) NOT NULL,
    end_date TIMESTAMP,
    CONSTRAINT fk_subcriptions_user FOREIGN KEY (user_id) REFERENCES users(id)
    );