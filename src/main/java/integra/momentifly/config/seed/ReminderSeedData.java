package integra.momentifly.config.seed;

import integra.momentifly.model.RecurrenceEnum;
import integra.momentifly.model.Reminder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReminderSeedData {

    public static List<Reminder> createReminderList() {
        // NEED AN ACTUAL USER AFTER FK IS IMPLEMENTED ON THE REMINDER MODEL
        UUID user = UUID.fromString("a1b2c3d4-0000-0000-0000-000000000001");
        return List.of(
            newReminder(user, "Drink water", LocalDateTime.now(), false, RecurrenceEnum.NONE),
            newReminder(user, "Friend birthday", LocalDateTime.now(), false, RecurrenceEnum.YEARLY),
            newReminder(user, "Renew gym subscription", LocalDateTime.now(), false, RecurrenceEnum.MONTHLY),
            newReminder(user, "Build timetable for nxt week", LocalDateTime.now(), false, RecurrenceEnum.WEEKLY)
        );
    }

    private static Reminder newReminder(UUID userId, String description, LocalDateTime date, Boolean done, RecurrenceEnum recurrence) {
        Reminder reminder = new Reminder();
        reminder.setUserId(userId);
        reminder.setDescription(description);
        reminder.setReminderDate(date);
        reminder.setDone(done);
        reminder.setRecurrence(recurrence);
        return reminder;
    }
}
