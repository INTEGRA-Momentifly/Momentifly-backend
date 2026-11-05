-- APPOINTMENT
-- REMINDER
-- TASK

-- IN ALL UPDATES AND DELETES: need userId to check if that object belongs to the current user,
-- meaning I need Authentication object and in the service i do the check -> throw if needed
-- THIS MEANS THAT EVEN IF SUBSCRIPTIONS ALREADY HAS THE RIGHT FK, I MIGHT NEED TO CORRECT
-- ITS SERVICE AND CONTROLLER

alter table appointment
add constraint fk_appointment_user foreign key (user_id) references "user"(id) on delete cascade;

alter table reminder
add constraint fk_reminder_user foreign key (user_id) references "user"(id) on delete cascade;

alter table task
add constraint fk_task_user foreign key (user_id) references "user"(id) on delete cascade;