CREATE TABLE appointment (
                             id uuid primary key default gen_random_uuid(),
                             user_id uuid not null,
                             description varchar(255) not null,
                             start_date timestamp not null,
                             end_date timestamp not null
);
