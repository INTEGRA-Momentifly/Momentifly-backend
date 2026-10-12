package integra.momentifly.controller;

import integra.momentifly.dto.CreateUserRequest;
import integra.momentifly.dto.UpdateUserRequest;
import integra.momentifly.model.User;
import integra.momentifly.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void createUser_returns200AndTheCreatedUser() {
        CreateUserRequest request = new CreateUserRequest("paul", "paul@gmail.com", "paul123", 2000.0);
        User created = new User();
        created.setUsername("paul");
        when(userService.createUser(request)).thenReturn(created);

        ResponseEntity<?> response = userController.createUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(created, response.getBody());
    }

    @Test
    void getAllUsers_withoutId_returnsTheList() {
        List<User> users = List.of(new User(), new User());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<?> response = userController.getAllUsers(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void getAllUsers_withId_returnsThatUser() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userService.getUserById(id)).thenReturn(user);

        ResponseEntity<?> response = userController.getAllUsers(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void updateUser_returns200AndTheUpdatedUser() {
        UUID id = UUID.randomUUID();
        UpdateUserRequest request = new UpdateUserRequest("test", "test@gmail.com", 2200.0);
        User updated = new User();
        when(userService.updateUser(id, request)).thenReturn(updated);

        ResponseEntity<?> response = userController.updateUser(id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());
    }

    @Test
    void deleteUser_returns204() {
        UUID id = UUID.randomUUID();

        ResponseEntity<?> response = userController.deleteUser(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser(id);
    }

    @Test
    void me_returnsTheAuthenticatedUser() {
        User user = new User();
        user.setUsername("paul");
        when(userService.getUserByUsername("paul")).thenReturn(user);

        ResponseEntity<?> response = userController.me(new UsernamePasswordAuthenticationToken("paul", null));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }
}