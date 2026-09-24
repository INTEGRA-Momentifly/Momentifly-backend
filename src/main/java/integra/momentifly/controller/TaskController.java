package integra.momentifly.controller;

import integra.momentifly.dto.CreateTaskRequest;
import integra.momentifly.dto.UpdateTaskRequest;
import integra.momentifly.model.Task;
import integra.momentifly.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/user")
    public List<Task> getTasksForUser(@RequestParam UUID userId) {
        return taskService.getAllTasksForUser(userId);
    }

    @GetMapping("/task")
    public Task getTask(@RequestParam UUID taskId) {
        return  taskService.getTaskById(taskId);
    }

    @PostMapping
    public Task createTask(@RequestBody CreateTaskRequest request) {
        return taskService.addTask(request.getUserId(),
                request.getDescription(),
                request.getDueDate(),
                request.getDifficulty());
    }

    @DeleteMapping
    public void deleteTask(@RequestParam UUID taskId) {
        taskService.removeTask(taskId);
    }

    @PutMapping
    public Task updateTask(@RequestBody UpdateTaskRequest request) {
        return taskService.updateTask(request.getTaskId(),
                request.getDescription(),
                request.getDueDate(),
                request.getDifficulty());
    }
}
