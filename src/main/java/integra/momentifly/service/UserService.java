package integra.momentifly.service;

import integra.momentifly.dto.CreateUserRequest;
import integra.momentifly.dto.UpdateUserRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.User;
import integra.momentifly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
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

        if (userRequest.username() == null || userRequest.username().isBlank())
            throw new InvalidDataException("Username is required");

        if (userRequest.email() == null || userRequest.email().isBlank())
            throw new InvalidDataException("Email is required");

        if (userRequest.dailyCalorieGoal() == null || userRequest.dailyCalorieGoal() <= 0)
            throw new InvalidDataException("Daily Calorie Goal cannot be zero or less than 0");

        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
        user.setDailyCalorieGoal(userRequest.dailyCalorieGoal());
        return userRepository.save(user);
    }

    public User createUser(CreateUserRequest userRequest) {
        if (userRequest.username() == null || userRequest.username().isBlank())
            throw new InvalidDataException("Username is required");

        if (userRepository.existsByUsername(userRequest.username()))
            throw new InvalidDataException("Username already taken");

        if (userRequest.password() == null || userRequest.password().isBlank())
            throw new InvalidDataException("Password is required");

        if (userRequest.email() == null || userRequest.email().isBlank())
            throw new InvalidDataException("Email is required");

        if (userRequest.dailyCalorieGoal() == null || userRequest.dailyCalorieGoal() <= 0)
            throw new InvalidDataException("Daily Calorie Goal cannot be zero or less than 0");

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

    @NullMarked
    @Override
    public UserDetails loadUserByUsername(String username) throws NotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}
