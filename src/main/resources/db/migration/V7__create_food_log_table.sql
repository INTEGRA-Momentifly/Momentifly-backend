CREATE TABLE food_log
(
  id            UUID         NOT NULL,
  user_id       UUID         NOT NULL,
  description   VARCHAR(255) NOT NULL,
  food_category SMALLINT     NOT NULL,
  proteins      INTEGER      NOT NULL,
  carbs         INTEGER      NOT NULL,
  fats          INTEGER      NOT NULL,
  calories      INTEGER      NOT NULL,
  date          DATE         NOT NULL,
  CONSTRAINT pk_food_log PRIMARY KEY (id),
  CONSTRAINT fk_food_logs_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);
