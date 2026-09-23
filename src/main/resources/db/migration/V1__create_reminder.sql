CREATE TABLE reminder (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    description VARCHAR(250),
    reminder_date TIMESTAMP,
    done BOOLEAN,
    recurrence VARCHAR(30)
);