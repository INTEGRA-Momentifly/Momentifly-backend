CREATE TABLE IF NOT EXISTS subscription(
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    cost DOUBLE PRECISION NOT NULL,
    name VARCHAR(255) NOT NULL,
    end_date TIMESTAMP,
    CONSTRAINT fk_subscriptions_user FOREIGN KEY (user_id) REFERENCES "user"(id)
)