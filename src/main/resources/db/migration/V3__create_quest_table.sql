CREATE TABLE quest (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    description VARCHAR(255) NOT NULL,
    points DOUBLE PRECISION NOT NULL
);