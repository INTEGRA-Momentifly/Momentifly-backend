package integra.momentifly.service;

import integra.momentifly.model.DifficultyEnum;
import integra.momentifly.repository.TaskRepository;
import integra.momentifly.model.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task addTask(UUID userId, String description, LocalDate dueDate, DifficultyEnum difficulty) {
//        ONCE THERE IS A USER CLASS AND MANY-TO-ONE IS IMPLEMENTED:
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));

        if (description == null || dueDate == null || difficulty == null)
            throw new IllegalArgumentException("description, dueDate, and difficulty are all required");

        var task = new Task();
        task.setUserId(userId);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setDifficulty(difficulty);
        return taskRepository.save(task);
    }

    public void removeTask(UUID id) {
        if (!taskRepository.existsById(id))
            throw new RuntimeException("Task not found!");
        taskRepository.deleteById(id);
    }


    public Task completeTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setCompleted(true);
        return taskRepository.save(task);
    }

    // update task description / dueDate / diff
    public Task updateTask(UUID taskId, String description, LocalDate dueDate,  DifficultyEnum difficulty) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (description == null || dueDate == null || difficulty == null)
            throw new IllegalArgumentException("description, dueDate, and difficulty are all required");

        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setDifficulty(difficulty);
        return taskRepository.save(task);
    }

    // first group by difficulty, then order by due date
    // earlier => higher priority (date1 < date2 => date1 first)
    public void orderTasks() {

    }

    // might also need a function that constantly checks for tasks that
    // have already reached their dueDate and remove them / have some container
    // with tasks that are not completed => function call everytime on page load / refresh??
    // (user can still update their due date if they want to still do that task ig)
}