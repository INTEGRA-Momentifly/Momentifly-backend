package integra.momentifly.controller;

import integra.momentifly.dto.WriteTaskRequest;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Difficulty;
import integra.momentifly.model.Task;
import integra.momentifly.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private final Authentication authentication = new UsernamePasswordAuthenticationToken("paul", null);

    @Test
    void getAllTasks_Returns200AndList() {
        Task task1 = new Task();
        Task task2 = new Task();

        when(taskService.getAllTasksForUser("paul")).thenReturn(List.of(task1, task2));

        ResponseEntity<?> response = taskController.getAllTasks(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(task1, task2), response.getBody());
    }

    @Test
    void getTasksForUser_OnlyAsksForTheLoggedInUsersTasks() {
        Task task1 = new Task();
        Task task2 = new Task();
        Authentication andrei = new UsernamePasswordAuthenticationToken("andrei", null);

        when(taskService.getAllTasksForUser("andrei"))
                .thenReturn(List.of(task1, task2));

        ResponseEntity<?> response = taskController.getAllTasks(andrei);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(task1, task2), response.getBody());
        verify(taskService, never()).getAllTasksForUser("paul");
    }

    @Test
    void getTask_Returns200_WhenFound() {
        UUID taskId = UUID.randomUUID();

        Task task = new Task();
        task.setDescription("Found task");

        when(taskService.getTaskById(taskId, "paul")).thenReturn(task);

        ResponseEntity<?> response = taskController.getTaskById(authentication, taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    void getTask_ThrowsNotFoundException_WhenNotFound() {
        UUID taskId = UUID.randomUUID();

        when(taskService.getTaskById(taskId, "paul"))
                .thenThrow(new NotFoundException("Task not found"));

        assertThrows(
                NotFoundException.class,
                () -> taskController.getTaskById(authentication, taskId)
        );
    }

    @Test
    void createTask_Returns200_WhenOk() {
        WriteTaskRequest request = new WriteTaskRequest(
                "New task",
                LocalDate.of(2026, 10, 1),
                Difficulty.MEDIUM
        );

        Task createdTask = new Task();
        createdTask.setDescription("Created task");

        when(taskService.addTask(eq("paul"), any()))
                .thenReturn(createdTask);

        ResponseEntity<?> response =
                taskController.createTask(authentication, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdTask, response.getBody());
    }


    @Test
    void deleteTask_Returns204_WhenFound() {
        UUID taskId = UUID.randomUUID();

        ResponseEntity<?> response = taskController.deleteTask(authentication, taskId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService).removeTask(taskId, "paul");
    }

    @Test
    void deleteTask_ThrowsNotFoundException_WhenNotFound() {
        UUID taskId = UUID.randomUUID();

        doThrow(new NotFoundException("Task not found"))
                .when(taskService)
                .removeTask(taskId, "paul");

        assertThrows(
                NotFoundException.class,
                () -> taskController.deleteTask(authentication, taskId)
        );
    }

    @Test
    void updateTask_Returns200_WhenOk() {
        UUID taskId = UUID.randomUUID();
        WriteTaskRequest request = new WriteTaskRequest(
                "New task",
                LocalDate.of(2026, 10, 1),
                Difficulty.MEDIUM
        );

        Task updatedTask = new Task();

        when(taskService.updateTask(eq(taskId), eq("paul"), any()))
                .thenReturn(updatedTask);

        ResponseEntity<?> response =
                taskController.updateTask(authentication, taskId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTask, response.getBody());
    }

    @Test
    void updateTask_ThrowsNotFoundException_WhenNotFound() {
        UUID taskId = UUID.randomUUID();
        WriteTaskRequest request = new WriteTaskRequest(
                "New task",
                LocalDate.of(2026, 10, 1),
                Difficulty.MEDIUM
        );

        when(taskService.updateTask(eq(taskId), eq("paul"), any()))
                .thenThrow(new NotFoundException("Task not found"));

        assertThrows(
                NotFoundException.class,
                () -> taskController.updateTask(authentication, taskId, request)
        );
    }
}