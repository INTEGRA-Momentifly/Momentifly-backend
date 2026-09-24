package integra.momentifly.controller;

import integra.momentifly.dto.CreateTaskRequest;
import integra.momentifly.dto.UpdateTaskRequest;
import integra.momentifly.model.DifficultyEnum;
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
    void getAllTasks_returns200AndList(){
        Task task1 = new Task();
        Task task2 = new Task();
        when(taskService.getAllTasks()).thenReturn(List.of(task1,task2));

        ResponseEntity<?> response = taskController.getAllTasks();
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(List.of(task1,task2),response.getBody());
    }

    @Test
    void getTasksForUser_returns200AndList(){
        Task task1 = new Task();
        Task task2 = new Task();
        UUID userId = UUID.randomUUID();
        when(taskService.getAllTasksForUser(userId)).thenReturn(List.of(task1,task2));

        ResponseEntity<?> response = taskController.getTasksForUser(userId);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(List.of(task1,task2),response.getBody());
    }

    @Test
    void getTask_returns200_whenFound(){
        UUID taskId = UUID.randomUUID();
        Task task = new Task();
        task.setDescription("Found task");
        when(taskService.getTaskById(taskId)).thenReturn(task);

        ResponseEntity<?> response = taskController.getTask(taskId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    void getTask_returns404_whenNotFound() {
        UUID taskId = UUID.randomUUID();
        when(taskService.getTaskById(taskId)).thenThrow(new RuntimeException("Task not found"));

        ResponseEntity<?> response = taskController.getTask(taskId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Task not found", response.getBody());
    }

    @Test
    void createTask_returns200_whenOk(){
        CreateTaskRequest request = new CreateTaskRequest();
        request.setUserId(UUID.randomUUID());
        request.setDescription("New task");
        request.setDueDate(LocalDate.of(2026, 10, 1));
        request.setDifficulty(DifficultyEnum.MEDIUM);

        Task createdTask = new Task();
        createdTask.setDescription("Created task");
        when(taskService.addTask(any(), any(), any(), any())).thenReturn(createdTask);

        ResponseEntity<?> response = taskController.createTask(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdTask, response.getBody());
    }

    @Test
    void createTask_returns400_whenServiceThrowsIllegalArgument() {
        CreateTaskRequest request = new CreateTaskRequest();
        when(taskService.addTask(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("description, dueDate, and difficulty are all required"));

        ResponseEntity<?> response = taskController.createTask(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteTask_returns204_whenFound(){
        UUID taskId = UUID.randomUUID();

        ResponseEntity<?> response = taskController.deleteTask(taskId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteTask_returns404_whenNotFound() {
        UUID taskId = UUID.randomUUID();
        doThrow(new RuntimeException("Task not found")).when(taskService).removeTask(taskId);

        ResponseEntity<?> response = taskController.deleteTask(taskId);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateTask_returns200_WhenOk(){
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTaskId(UUID.randomUUID());
        request.setDescription("New task");
        request.setDueDate(LocalDate.of(2026, 10, 1));
        request.setDifficulty(DifficultyEnum.MEDIUM);

        Task updatedTask = new  Task();
        when(taskService.updateTask(any(), any(), any(), any())).thenReturn(updatedTask);

        ResponseEntity<?> response = taskController.updateTask(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateTask_returns400_WhenServiceThrowsIllegalArgument(){
        UpdateTaskRequest request = new UpdateTaskRequest();
        when(taskService.updateTask(any(), any(), any(), any()))
                .thenThrow(new IllegalArgumentException("description, dueDate, and difficulty are all required"));

        ResponseEntity<?> response = taskController.updateTask(request);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateTask_returns404_WhenServiceThrowsRuntimeException(){
        UpdateTaskRequest request = new UpdateTaskRequest();
        when(taskService.updateTask(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Task not found"));

        ResponseEntity<?> response = taskController.updateTask(request);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
