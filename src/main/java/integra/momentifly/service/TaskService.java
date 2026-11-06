package integra.momentifly.service;

import integra.momentifly.dto.WriteTaskRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.repository.TaskRepository;
import integra.momentifly.model.Task;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;

    public List<Task> getAllTasksForUser(String username) {
        return taskRepository.findByUser_Username(username);
    }

    public Task getTaskById(UUID taskId, String username) throws NotFoundException {
        return taskRepository.findByIdAndUser_Username(taskId, username)
                .orElseThrow(() -> new NotFoundException("Task not found"));
    }

    public Task addTask(String username, WriteTaskRequest writeTaskRequest) throws InvalidDataException {
        var task = new Task();
        task.setUser(userService.getUserByUsername(username));
        task.setDescription(writeTaskRequest.description());
        task.setDueDate(writeTaskRequest.dueDate());
        task.setDifficulty(writeTaskRequest.difficulty());
        return taskRepository.save(task);
    }

    public void removeTask(UUID taskId, String username) throws NotFoundException {
        taskRepository.delete(getTaskById(taskId, username));
    }

    public Task updateTask(UUID taskId, String username, WriteTaskRequest writeTaskRequest) throws NotFoundException {
        Task task = getTaskById(taskId, username);
        task.setDescription(writeTaskRequest.description());
        task.setDueDate(writeTaskRequest.dueDate());
        task.setDifficulty(writeTaskRequest.difficulty());
        return taskRepository.save(task);
    }
}