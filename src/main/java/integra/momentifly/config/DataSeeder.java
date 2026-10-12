package integra.momentifly.config;

import integra.momentifly.model.*;
import integra.momentifly.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final QuestRepository questRepository;
    private final ReminderRepository reminderRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public void run(String... args) {
        seedUsers();   // must run first, the others reference users

        Map<String, User> users = userRepository.findAll().stream()
                .collect(Collectors.toMap(User::getUsername, Function.identity()));

        seedQuests();
        seedTasks(users);
        seedReminders(users);
        seedSubscriptions(users);
    }

    // ---------- USERS ----------

    private void seedUsers() {
        if (userRepository.count() > 0) return;
        userRepository.saveAll(List.of( // seeding hashed passwords by default
                                        // password is "test123" for every user
                newUser("Paul", "paul@gmail.com", "$2a$10$nIc6GDr6l23OnHxrCon0H.ujOiQWf5KEGcJqDqUp3SNiX0rjvxpJO", 120, 2000.0),
                newUser("Andrei", "andrei@gmail.com", "$2a$10$nIc6GDr6l23OnHxrCon0H.ujOiQWf5KEGcJqDqUp3SNiX0rjvxpJO", 200, 2000.0),
                newUser("Andreea", "andreea@gmail.com", "$2a$10$nIc6GDr6l23OnHxrCon0H.ujOiQWf5KEGcJqDqUp3SNiX0rjvxpJO", 300, 2000.0),
                newUser("David", "david@gmail.com", "$2a$10$nIc6GDr6l23OnHxrCon0H.ujOiQWf5KEGcJqDqUp3SNiX0rjvxpJO", 185, 2000.0),
                newUser("Sergiu", "sergiu@gmail.com", "$2a$10$nIc6GDr6l23OnHxrCon0H.ujOiQWf5KEGcJqDqUp3SNiX0rjvxpJO", 500,2000.0),
                newUser("Gabriel", "gabriel@gmail.com", "$2a$10$nIc6GDr6l23OnHxrCon0H.ujOiQWf5KEGcJqDqUp3SNiX0rjvxpJO", 98, 2000.0)
        ));
    }

    private User newUser(String username, String email, String password, int score, Double dailyCalorieGoal) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setScore(score);
        user.setDailyCalorieGoal(dailyCalorieGoal);
        return user;
    }

    // ---------- QUESTS ----------

    private void seedQuests() {
        if (questRepository.count() > 0) return;
        questRepository.saveAll(List.of(
                newQuest("Drink a full glass of water within 30 minutes of waking up.", 10),
                newQuest("Walk at least 8,000 steps today.", 20),
                newQuest("Eat at least 3 servings of vegetables today.", 30),
                newQuest("Finish the day within your daily calorie goal.", 40),
                newQuest("Do 30 minutes of any physical activity", 30),
                newQuest("Log all your meals today, including snacks.", 25),
                newQuest("Go the whole day without sugary drinks or desserts.", 30),
                newQuest("Complete at least one quest every day for 7 days in a row.", 100)
        ));
    }

    private Quest newQuest(String description, int points) {
        Quest quest = new Quest();
        quest.setDescription(description);
        quest.setPoints(points);
        return quest;
    }

    // ---------- TASKS ----------

    private void seedTasks(Map<String, User> users) {
        if (taskRepository.count() > 0) return;

        // NEED AN ACTUAL USER AFTER FK IS IMPLEMENTED ON THE REMINDER MODEL
        UUID user = UUID.fromString("a1b2c3d4-0000-0000-0000-000000000001");

        taskRepository.saveAll(List.of(
                newTask(user, "Finish Spring Boot assignment", LocalDate.of(2026, 10, 10), Difficulty.HARD),
                newTask(user, "Buy groceries", LocalDate.of(2026, 10, 5), Difficulty.EASY),
                newTask(user, "Clean the apartment", LocalDate.of(2026, 10, 7), Difficulty.MEDIUM),
                newTask(user, "Prepare presentation slides", LocalDate.of(2026, 10, 12), Difficulty.HARD),
                newTask(user, "Read chapter 5", LocalDate.of(2026, 10, 6), Difficulty.EASY),
                newTask(user, "Reply to emails", LocalDate.of(2026, 10, 4), Difficulty.EASY),
                newTask(user, "Fix login bug", LocalDate.of(2026, 10, 8), Difficulty.MEDIUM),
                newTask(user, "Write unit tests", LocalDate.of(2026, 10, 9), Difficulty.HARD)
        ));
    }

    private Task newTask(UUID user, String description, LocalDate dueDate, Difficulty difficulty) {
        Task task = new Task();
        task.setUserId(user);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setDifficulty(difficulty);
        return task;
    }

    // ---------- REMINDERS ----------

    private void seedReminders(Map<String, User> users) {
        if (reminderRepository.count() > 0) return;
        // NEED AN ACTUAL USER AFTER FK IS IMPLEMENTED ON THE REMINDER MODEL
        UUID user = UUID.fromString("a1b2c3d4-0000-0000-0000-000000000001");

        reminderRepository.saveAll(List.of(
                newReminder(user, "Drink water", LocalDateTime.now().plusHours(2), RecurrenceEnum.NONE),
                newReminder(user, "Friend birthday", LocalDateTime.now().plusDays(30), RecurrenceEnum.YEARLY),
                newReminder(user, "Renew gym subscription", LocalDateTime.now().plusDays(10), RecurrenceEnum.MONTHLY),
                newReminder(user, "Build timetable for next week", LocalDateTime.now().plusDays(3), RecurrenceEnum.WEEKLY)
        ));
    }

    private Reminder newReminder(UUID user, String description, LocalDateTime date, RecurrenceEnum recurrence) {
        Reminder reminder = new Reminder();
        reminder.setUserId(user);
        reminder.setDescription(description);
        reminder.setReminderDate(date);
        reminder.setDone(false);
        reminder.setRecurrence(recurrence);
        return reminder;
    }

    // ---------- SUBSCRIPTIONS ----------

    private void seedSubscriptions(Map<String, User> users) {
        if (subscriptionRepository.count() > 0) return;
        User paul = users.get("Paul");

        subscriptionRepository.saveAll(List.of(
                newSubscription(paul, 15.2, "Netflix", LocalDateTime.now().plusMonths(1)),
                newSubscription(paul, 10.2, "Crunchyroll", LocalDateTime.now().plusMonths(1)),
                newSubscription(paul, 20.0, "HBO", LocalDateTime.now().plusMonths(1)),
                newSubscription(paul, 10.0, "YouTube Premium", LocalDateTime.now().plusMonths(1))
        ));
    }

    private Subscription newSubscription(User user, Double cost, String name, LocalDateTime endDate) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setCost(cost);
        subscription.setName(name);
        subscription.setEndDate(endDate);
        return subscription;
    }
}