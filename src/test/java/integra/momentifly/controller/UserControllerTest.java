package integra.momentifly.controller;

import integra.momentifly.dto.ChangePasswordRequest;
import integra.momentifly.dto.CreateUserRequest;
import integra.momentifly.dto.UpdateUserRequest;
import integra.momentifly.exception.InvalidDataException;
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
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void changePassword_returns204_forTheAuthenticatedUser() {
        ChangePasswordRequest request = new ChangePasswordRequest("oldpass123", "newpass456");

        ResponseEntity<?> response = userController.changePassword(
                new UsernamePasswordAuthenticationToken("paul", null), request);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).changePassword("paul", request);   // uses the logged-in username, nothing from the request
    }

    @Test
    void changePassword_propagatesInvalidData_whenServiceRejects() {
        ChangePasswordRequest request = new ChangePasswordRequest("wrongpass", "newpass456");
        doThrow(new InvalidDataException("Current password is incorrect"))
                .when(userService).changePassword("paul", request);

        assertThrows(InvalidDataException.class, () -> userController.changePassword(
                new UsernamePasswordAuthenticationToken("paul", null), request));
    }

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
    void updateUser_returns200AndTheUpdatedUser() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("paul");

        UpdateUserRequest request =
                new UpdateUserRequest("test", "test@gmail.com", 2200.0);

        User updated = new User();

        when(userService.updateUser("paul", request)).thenReturn(updated);

        ResponseEntity<?> response =
                userController.updateUser(authentication, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updated, response.getBody());

        verify(userService).updateUser("paul", request);
    }

    @Test
    void deleteUser_returns204() {
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("paul");

        ResponseEntity<?> response = userController.deleteUser(authentication);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService).deleteUser("paul");
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