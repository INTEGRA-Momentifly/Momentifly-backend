package integra.momentifly.controller;


import integra.momentifly.dto.WriteTaskRequest;
import integra.momentifly.model.Task;
import integra.momentifly.service.TaskService;
import integra.momentifly.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<?> getAllTasks(Authentication authentication) {
        return ResponseEntity.ok(taskService.getAllTasksForUser(authentication.getName()));
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getTaskById(Authentication authentication, @PathVariable UUID taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId, authentication.getName()));
    }

    @PostMapping()
    public ResponseEntity<?> createTask(Authentication authentication, @Valid @RequestBody WriteTaskRequest request) {
        return ResponseEntity.ok(taskService.addTask(authentication.getName(), request));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<?> updateTask(Authentication authentication, @PathVariable UUID taskId,
                                        @Valid @RequestBody WriteTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(taskId, authentication.getName(), request));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<?> deleteTask(Authentication authentication, @PathVariable UUID taskId) {
        taskService.removeTask(taskId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
