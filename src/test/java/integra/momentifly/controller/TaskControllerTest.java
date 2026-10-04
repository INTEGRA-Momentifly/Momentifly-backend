package integra.momentifly.controller;

import integra.momentifly.dto.WriteTaskRequest;
import integra.momentifly.exception.InvalidDataException;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @Test
    void getAllTasks_Returns200AndList() {
        Task task1 = new Task();
        Task task2 = new Task();

        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        ResponseEntity<?> response = taskController.getAllTasks(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(task1, task2), response.getBody());
    }

    @Test
    void getTasksForUser_Returns200AndList() {
        Task task1 = new Task();
        Task task2 = new Task();
        UUID userId = UUID.randomUUID();

        when(taskService.getAllTasksForUser(userId))
                .thenReturn(List.of(task1, task2));

        ResponseEntity<?> response = taskController.getAllTasks(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(task1, task2), response.getBody());
    }

    @Test
    void getTask_Returns200_WhenFound() {
        UUID taskId = UUID.randomUUID();

        Task task = new Task();
        task.setDescription("Found task");

        when(taskService.getTaskById(taskId)).thenReturn(task);

        ResponseEntity<?> response = taskController.getTaskById(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    void getTask_ThrowsNotFoundException_WhenNotFound() {
        UUID taskId = UUID.randomUUID();

        when(taskService.getTaskById(taskId))
                .thenThrow(new NotFoundException("Task not found"));

        assertThrows(
                NotFoundException.class,
                () -> taskController.getTaskById(taskId)
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

        when(taskService.addTask(any(), any(), any(), any()))
                .thenReturn(createdTask);

        ResponseEntity<?> response =
                taskController.createTask(UUID.randomUUID(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdTask, response.getBody());
    }

    @Test
    void createTask_ThrowsInvalidTaskDataException_WhenInvalid() {
        WriteTaskRequest request = new WriteTaskRequest(
                null,
                null,
                null
        );

        when(taskService.addTask(any(), any(), any(), any()))
                .thenThrow(new InvalidDataException(
                        "description, dueDate, and difficulty are all required"
                ));

        assertThrows(
                InvalidDataException.class,
                () -> taskController.createTask(UUID.randomUUID(), request)
        );
    }

    @Test
    void deleteTask_Returns204_WhenFound() {
        UUID taskId = UUID.randomUUID();

        ResponseEntity<?> response = taskController.deleteTask(taskId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteTask_ThrowsNotFoundException_WhenNotFound() {
        UUID taskId = UUID.randomUUID();

        doThrow(new NotFoundException("Task not found"))
                .when(taskService)
                .removeTask(taskId);

        assertThrows(
                NotFoundException.class,
                () -> taskController.deleteTask(taskId)
        );
    }

    @Test
    void updateTask_Returns200_WhenOk() {
        WriteTaskRequest request = new WriteTaskRequest(
                "New task",
                LocalDate.of(2026, 10, 1),
                Difficulty.MEDIUM
        );

        Task updatedTask = new Task();

        when(taskService.updateTask(any(), any(), any(), any()))
                .thenReturn(updatedTask);

        ResponseEntity<?> response =
                taskController.updateTask(UUID.randomUUID(), request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedTask, response.getBody());
    }

    @Test
    void updateTask_ThrowsInvalidTaskDataException_WhenInvalid() {
        WriteTaskRequest request = new WriteTaskRequest(
                null,
                null,
                null
        );

        when(taskService.updateTask(any(), any(), any(), any()))
                .thenThrow(new InvalidDataException(
                        "description, dueDate, and difficulty are all required"
                ));

        assertThrows(
                InvalidDataException.class,
                () -> taskController.updateTask(UUID.randomUUID(), request)
        );
    }

    @Test
    void updateTask_ThrowsNotFoundException_WhenNotFound() {
        WriteTaskRequest request = new WriteTaskRequest(
                "New task",
                LocalDate.of(2026, 10, 1),
                Difficulty.MEDIUM
        );

        when(taskService.updateTask(any(), any(), any(), any()))
                .thenThrow(new NotFoundException("Task not found"));

        assertThrows(
                NotFoundException.class,
                () -> taskController.updateTask(UUID.randomUUID(), request)
        );
    }
}