CREATE TABLE questcompletion (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    quest_id UUID NOT NULL,
    points DOUBLE PRECISION NOT NULL,
    completion_status VARCHAR(30),
    completion_date TIMESTAMP,
    CONSTRAINT fk_completequest_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_completequest_quest FOREIGN KEY (quest_id) REFERENCES "quest"(id)
);
