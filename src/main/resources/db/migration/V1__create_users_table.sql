CREATE TABLE IF NOT EXISTS "user" (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    daily_calorie_goal DOUBLE PRECISION
);
