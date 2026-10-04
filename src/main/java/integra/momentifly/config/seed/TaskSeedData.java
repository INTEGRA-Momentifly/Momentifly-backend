package integra.momentifly.config.seed;

import integra.momentifly.model.Difficulty;
import integra.momentifly.model.Task;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class TaskSeedData {

    public static List<Task> buildTaskList() {
        UUID user1 = UUID.fromString("a1b2c3d4-0000-0000-0000-000000000001");
        UUID user2 = UUID.fromString("a1b2c3d4-0000-0000-0000-000000000002");

        return List.of(
                newTask(user1, "Finish Spring Boot assignment", LocalDate.of(2026, 10, 10), Difficulty.HARD),
                newTask(user1, "Buy groceries", LocalDate.of(2026, 10, 5), Difficulty.EASY),
                newTask(user1, "Clean the apartment", LocalDate.of(2026, 10, 7), Difficulty.MEDIUM),
                newTask(user2, "Prepare presentation slides", LocalDate.of(2026, 10, 12), Difficulty.HARD),
                newTask(user2, "Read chapter 5", LocalDate.of(2026, 10, 6), Difficulty.EASY),
                newTask(user2, "Reply to emails", LocalDate.of(2026, 10, 4), Difficulty.EASY),
                newTask(user1, "Fix login bug", LocalDate.of(2026, 10, 8), Difficulty.MEDIUM),
                newTask(user2, "Write unit tests", LocalDate.of(2026, 10, 9), Difficulty.HARD)
        );
    }

    private static Task newTask(UUID userId, String description, LocalDate dueDate, Difficulty difficulty) {
        Task task = new Task();
        task.setUserId(userId);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setDifficulty(difficulty);
        return task;
    }
}