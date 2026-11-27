ALTER TABLE quest
    ADD COLUMN user_id UUID NOT NULL,
    ADD CONSTRAINT fk_quest_user
        FOREIGN KEY (user_id) REFERENCES "user"(id);
