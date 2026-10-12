package integra.momentifly.service;

import integra.momentifly.dto.CreateUserRequest;
import integra.momentifly.dto.UpdateUserRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.User;
import integra.momentifly.repository.UserRepository;
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User updateUser(UUID id, UpdateUserRequest userRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setUsername(userRequest.username());
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

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) throw new NotFoundException("User not found");
        userRepository.deleteById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
