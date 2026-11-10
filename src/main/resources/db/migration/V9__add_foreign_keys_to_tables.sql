alter table appointment
add constraint fk_appointment_user foreign key (user_id) references "user"(id) on delete cascade;

alter table reminder
add constraint fk_reminder_user foreign key (user_id) references "user"(id) on delete cascade;

alter table task
add constraint fk_task_user foreign key (user_id) references "user"(id) on delete cascade;