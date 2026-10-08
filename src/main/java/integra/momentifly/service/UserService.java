package integra.momentifly.service;

import integra.momentifly.dto.CreateUserRequest;
import integra.momentifly.dto.UpdateUserRequest;
import integra.momentifly.exception.InvalidDataException;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.User;
import integra.momentifly.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// TODO change some things here after the security config issue

// TODO update password separately?

// TODO should i throw more exceptions here (ex: user inputs -1 or 0 calories for daily goal)
//  considering that the input might be validated in the frontend first?
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder; ill do this after security config issue

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

        if (userRequest.email() == null || userRequest.email().isBlank())
            throw new InvalidDataException("Email is required");

        if (userRequest.dailyCalorieGoal() == null || userRequest.dailyCalorieGoal() <= 0)
            throw new InvalidDataException("Daily Calorie Goal cannot be zero or less than 0");

        var user = new User();
        user.setUsername(userRequest.username());
        user.setEmail(userRequest.email());
        user.setDailyCalorieGoal(userRequest.dailyCalorieGoal());
//        user.setPassword(passwordEncoder.encode(password)); after security config issue
        user.setPassword(userRequest.password());
        user.setScore(0);
        return userRepository.save(user);
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) throw new NotFoundException("User not found");
        userRepository.deleteById(id);
    }
}
