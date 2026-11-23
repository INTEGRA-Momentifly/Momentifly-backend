package integra.momentifly.controller;


import integra.momentifly.dto.WriteTaskRequest;
import integra.momentifly.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
