package integra.momentifly.service;

import integra.momentifly.dto.ChangePasswordRequest;
import integra.momentifly.dto.CreateUserRequest;
import integra.momentifly.dto.UpdateUserRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.User;
import integra.momentifly.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User updateUser(String username, UpdateUserRequest userRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setEmail(userRequest.email());
        user.setDailyCalorieGoal(userRequest.dailyCalorieGoal());
        return userRepository.save(user);
    }

    public User createUser(CreateUserRequest userRequest) {
        if (userRepository.existsByUsername(userRequest.username()))
            throw new InvalidDataException("Username already taken");

        var user = new User();
        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
        user.setDailyCalorieGoal(userRequest.dailyCalorieGoal());
        user.setPassword(passwordEncoder.encode(userRequest.password())); // encode the password using bcrypt, and store it in the db after
        user.setScore(0);
        return userRepository.save(user);
    }

    public void changePassword(String username, ChangePasswordRequest changePasswordRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!passwordEncoder.matches(changePasswordRequest.currentPassword(), user.getPassword()))
            throw new InvalidDataException("Passwords don't match");

        user.setPassword(passwordEncoder.encode(changePasswordRequest.newPassword()));
        userRepository.save(user);
    }

    // good if this involves multiple operations
    // in this case: delete user => delete the tasks of that user, reminders etc
    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.delete(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
