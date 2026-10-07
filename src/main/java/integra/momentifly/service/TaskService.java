package integra.momentifly.service;

import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
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

    public Task getTaskById(UUID taskId) throws NotFoundException {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));
    }

    public Task addTask(UUID userId, String description, LocalDate dueDate, Difficulty difficulty) throws InvalidDataException {
//        ONCE THERE IS A USER CLASS AND MANY-TO-ONE IS IMPLEMENTED:
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFounException("User not found"));

        if (description == null || dueDate == null || difficulty == null)
            throw new InvalidDataException("description, dueDate, and difficulty are all required");

        var task = new Task();
        task.setUserId(userId);
        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setDifficulty(difficulty);
        return taskRepository.save(task);
    }

    public void removeTask(UUID taskId) throws NotFoundException {
        if (!taskRepository.existsById(taskId))
            throw new NotFoundException("Task not found");
        taskRepository.deleteById(taskId);
    }

    public Task updateTask(UUID taskId, String description, LocalDate dueDate,  Difficulty difficulty) throws NotFoundException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (description == null || dueDate == null || difficulty == null)
            throw new IllegalArgumentException("description, dueDate, and difficulty are all required");

        task.setDescription(description);
        task.setDueDate(dueDate);
        task.setDifficulty(difficulty);
        return taskRepository.save(task);
    }
}