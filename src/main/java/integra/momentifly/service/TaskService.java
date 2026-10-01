package integra.momentifly.service;

import integra.momentifly.exception.InvalidTaskDataException;
import integra.momentifly.exception.TaskNotFoundException;
import integra.momentifly.model.Difficulty;
import integra.momentifly.repository.TaskRepository;
import integra.momentifly.model.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getAllTasksForUser(UUID userId) {
        return taskRepository.findByUserId(userId);
    }

    public Task getTaskById(UUID taskId) throws TaskNotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }

    public Task addTask(UUID userId, String description, LocalDate dueDate, Difficulty difficulty) throws InvalidTaskDataException {
//        ONCE THERE IS A USER CLASS AND MANY-TO-ONE IS IMPLEMENTED:
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFounException("User not found"));

        if (description == null || dueDate == null || difficulty == null)
            throw new InvalidTaskDataException("description, dueDate, and difficulty are all required");

        var task = new Task();
        task.setUserId(userId);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setDifficulty(difficulty);
        return taskRepository.save(task);
    }

    public void removeTask(UUID taskId) throws TaskNotFoundException {
        if (!taskRepository.existsById(taskId))
            throw new TaskNotFoundException("Task not found");
        taskRepository.deleteById(taskId);
    }

    public Task updateTask(UUID taskId, String description, LocalDate dueDate,  Difficulty difficulty) throws TaskNotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (description == null || dueDate == null || difficulty == null)
            throw new IllegalArgumentException("description, dueDate, and difficulty are all required");

        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setDifficulty(difficulty);
        return taskRepository.save(task);
    }
}