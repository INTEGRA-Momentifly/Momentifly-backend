CREATE TABLE friendship (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    friend_id UUID NOT NULL,
    friendship_date TIMESTAMP,
    status VARCHAR(30),
    CONSTRAINT fk_friendship_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_friendship_friend FOREIGN KEY (friend_id) REFERENCES "user"(id),
    CONSTRAINT unique_friendship UNIQUE (user_id, friend_id)
);
