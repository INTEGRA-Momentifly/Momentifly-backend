CREATE TABLE task (
    id uuid primary key default gen_random_uuid(),
    user_id uuid not null,
    description varchar(255) not null,
    due_date date not null,
    difficulty varchar(255) not null,
    completed boolean not null default false
)