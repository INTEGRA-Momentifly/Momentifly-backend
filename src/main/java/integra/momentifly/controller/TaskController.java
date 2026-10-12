package integra.momentifly.controller;


import integra.momentifly.dto.WriteTaskRequest;
import integra.momentifly.model.Task;
import integra.momentifly.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getAllTasks(@RequestParam(required = false) UUID userId) {
        if (userId == null) return ResponseEntity.ok(taskService.getAllTasks());

        return ResponseEntity.ok(taskService.getAllTasksForUser(userId));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable UUID taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> createTask(@PathVariable UUID userId, @Valid @RequestBody WriteTaskRequest request) {
        Task task = taskService.addTask(userId, request);
        return ResponseEntity.ok(task);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable UUID taskId, @Valid @RequestBody WriteTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID taskId) {
        taskService.removeTask(taskId);
        return ResponseEntity.noContent().build();
    }
}
