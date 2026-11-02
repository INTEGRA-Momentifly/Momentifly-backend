package integra.momentifly.service;

import integra.momentifly.dto.ChangePasswordRequest;
import integra.momentifly.dto.CreateUserRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.model.User;
import integra.momentifly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import integra.momentifly.exception.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();   // real encoder, so hashing is really tested
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
    }

    private CreateUserRequest validRequest() {
        return new CreateUserRequest("paul", "paul@gmail.com", "paul123", 2000.0);
    }

    private User userWithPassword(String rawPassword) {
        User user = new User();
        user.setUsername("paul");
        user.setPassword(passwordEncoder.encode(rawPassword));
        return user;
    }

    @Test
    void changePassword_withCorrectCurrentPassword_savesTheNewHash() {
        User user = userWithPassword("oldpass123");
        when(userRepository.findByUsername("paul")).thenReturn(Optional.of(user));

        userService.changePassword("paul", new ChangePasswordRequest("oldpass123", "newpass456"));

        verify(userRepository).save(user);
        assertNotEquals("newpass456", user.getPassword());                       // never stored in plain text
        assertTrue(passwordEncoder.matches("newpass456", user.getPassword()));   // the new password works
        assertFalse(passwordEncoder.matches("oldpass123", user.getPassword()));  // the old one no longer does
    }

    @Test
    void changePassword_withWrongCurrentPassword_isRejected_andNothingChanges() {
        User user = userWithPassword("oldpass123");
        String originalHash = user.getPassword();
        when(userRepository.findByUsername("paul")).thenReturn(Optional.of(user));

        assertThrows(InvalidDataException.class, () ->
                userService.changePassword("paul", new ChangePasswordRequest("wrongpass", "newpass456")));

        verify(userRepository, never()).save(any());
        assertEquals(originalHash, user.getPassword());
    }

    @Test
    void changePassword_forUnknownUser_throwsNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                userService.changePassword("ghost", new ChangePasswordRequest("oldpass123", "newpass456")));

        verify(userRepository, never()).save(any());
    }

    @Test
    void createUser_hashesPassword_beforeSaving() {
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        userService.createUser(validRequest());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();

        assertNotEquals("paul123", saved.getPassword());                       // not stored in plain text
        assertTrue(passwordEncoder.matches("paul123", saved.getPassword()));   // but it's the right hash
        assertEquals(0, saved.getScore());
    }

    @Test
    void createUser_withDuplicateUsername_isRejected() {
        when(userRepository.existsByUsername("paul")).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> userService.createUser(validRequest()));
        verify(userRepository, never()).save(any());
    }

    @Test
    void getUserByUsername_returnsTheUser() {
        User user = new User();
        user.setUsername("paul");
        user.setPassword("$2a$10$storedhash");
        when(userRepository.findByUsername("paul")).thenReturn(Optional.of(user));

        User result = userService.getUserByUsername("paul");

        assertEquals("paul", result.getUsername());
        assertEquals(user, result);
    }

    @Test
    void getUserByUsername_withUnknownUser_throwsNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserByUsername("ghost"));
    }
}