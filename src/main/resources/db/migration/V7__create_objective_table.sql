CREATE TABLE objective (
                    id uuid primary key default gen_random_uuid(),
                    user_id uuid not null,
                    type varchar(255) not null,
                    value int not null,
                    constraint uq_objective_user_type unique (user_id, type)
)
