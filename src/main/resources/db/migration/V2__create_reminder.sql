CREATE TABLE reminder (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    description VARCHAR(250),
    reminder_date TIMESTAMP,
    done BOOLEAN,
    recurrence VARCHAR(30)
);