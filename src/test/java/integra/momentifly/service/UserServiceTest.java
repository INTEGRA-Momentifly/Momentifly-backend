package integra.momentifly.service;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    // adjust the argument order to match your CreateUserRequest record
    private CreateUserRequest validRequest() {
        return new CreateUserRequest("paul", "paul@gmail.com", "paul123", 2000.0);
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
    void createUser_withBlankPassword_isRejected() {
        CreateUserRequest request = new CreateUserRequest("paul", "paul@gmail.com", "  ", 2000.0);

        assertThrows(InvalidDataException.class, () -> userService.createUser(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void loadUserByUsername_returnsTheStoredHash() {
        User user = new User();
        user.setUsername("paul");
        user.setPassword("$2a$10$storedhash");
        when(userRepository.findByUsername("paul")).thenReturn(Optional.of(user));

        UserDetails details = userService.loadUserByUsername("paul");

        assertEquals("paul", details.getUsername());
        assertEquals("$2a$10$storedhash", details.getPassword());   // hash passed through, not re-encoded
    }

    @Test
    void loadUserByUsername_withUnknownUser_throwsUsernameNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("ghost"));
    }
}