package integra.momentifly.controller;

import integra.momentifly.dto.CreateTaskRequest;
import integra.momentifly.dto.UpdateTaskRequest;
import integra.momentifly.model.Task;
import integra.momentifly.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/user")
    public ResponseEntity<?> getTasksForUser(@RequestParam UUID userId) {
        return ResponseEntity.ok(taskService.getAllTasksForUser(userId));
    }

    @GetMapping("/task")
    public ResponseEntity<?> getTask(@RequestParam UUID taskId) {
        try {
            return ResponseEntity.ok(taskService.getTaskById(taskId));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody CreateTaskRequest request) {
        try {
            Task task = taskService.addTask(request.getUserId(), request.getDescription(),
                    request.getDueDate(), request.getDifficulty());
            return ResponseEntity.ok(task);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTask(@RequestParam UUID taskId) {
        try {
            taskService.removeTask(taskId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateTask(@RequestBody UpdateTaskRequest request) {
        try{
            return ResponseEntity.ok(taskService.updateTask(request.getTaskId(),
                    request.getDescription(),
                    request.getDueDate(),
                    request.getDifficulty()));
        } catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
}
