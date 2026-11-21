CREATE TABLE challenge (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  creator_id UUID NOT NULL,
  name VARCHAR(100) NOT NULL,
  duration_days INT NOT NULL,
  start_date DATE NOT NULL,
  CONSTRAINT fk_creator_user FOREIGN KEY (creator_id) REFERENCES "user"(id)
);

CREATE TABLE challenge_participant (
  id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
  challenge_id UUID NOT NULL,
  user_id UUID NOT NULL,
  CONSTRAINT fk_challenge_participant_challenge FOREIGN KEY (challenge_id) REFERENCES challenge(id),
  CONSTRAINT fk_challenge_participant_user FOREIGN KEY (user_id) REFERENCES "user"(id),
  CONSTRAINT unique_challenge_participant UNIQUE (challenge_id, user_id)
);

ALTER TABLE quest ADD COLUMN challenge_id UUID;
ALTER TABLE quest ADD CONSTRAINT fk_quest_challenge FOREIGN KEY (challenge_id) REFERENCES challenge(id);
