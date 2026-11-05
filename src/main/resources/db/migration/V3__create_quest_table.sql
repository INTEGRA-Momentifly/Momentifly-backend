CREATE TABLE quest (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    description VARCHAR(255) NOT NULL,
    points DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_quest_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);
