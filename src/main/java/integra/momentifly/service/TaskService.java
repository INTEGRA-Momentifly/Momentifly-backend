package integra.momentifly.service;

import integra.momentifly.dto.UpdateTaskRequest;
import integra.momentifly.dto.WriteTaskRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.repository.TaskRepository;
import integra.momentifly.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// TODO do i need some of these exceptions???
@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

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

    public Task addTask(UUID userId, WriteTaskRequest writeTaskRequest) throws InvalidDataException {
//        ONCE THERE IS A USER CLASS AND MANY-TO-ONE IS IMPLEMENTED:
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new UserNotFounException("User not found"));

        if (writeTaskRequest.description() == null
                || writeTaskRequest.dueDate() == null
                || writeTaskRequest.difficulty() == null)
            throw new InvalidDataException("description, dueDate, and difficulty are all required");

        var task = new Task();
        task.setUserId(userId);
        task.setDescription(writeTaskRequest.description());
        task.setDueDate(writeTaskRequest.dueDate());
        task.setDifficulty(writeTaskRequest.difficulty());
        return taskRepository.save(task);
    }

    public void removeTask(UUID taskId) throws NotFoundException {
        if (!taskRepository.existsById(taskId))
            throw new NotFoundException("Task not found");
        taskRepository.deleteById(taskId);
    }

    public Task updateTask(UpdateTaskRequest updateTaskRequest) throws NotFoundException {
        Task task = taskRepository.findById(updateTaskRequest.taskId())
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (updateTaskRequest.description() == null ||
                updateTaskRequest.dueDate() == null ||
                updateTaskRequest.difficulty() == null)
            throw new IllegalArgumentException("description, dueDate, and difficulty are all required");

        task.setDescription(updateTaskRequest.description());
        task.setDueDate(updateTaskRequest.dueDate());
        task.setDifficulty(updateTaskRequest.difficulty());
        return taskRepository.save(task);
    }
}