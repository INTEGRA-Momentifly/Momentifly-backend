CREATE TABLE reminder (
    id BIGSERIAL PRIMARY KEY,
    userId BIGINT NOT NULL,
    description VARCHAR(250),
    reminderDate TIMESTAMP,
    done BOOLEAN,
    recurrence VARCHAR(30)
);