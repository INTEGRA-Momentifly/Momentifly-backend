package integra.momentifly.controller;

import integra.momentifly.dto.CreateUserRequest;
import integra.momentifly.dto.UpdateUserRequest;
import integra.momentifly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        return ResponseEntity.ok(userService.loadUserByUsername(authentication.getName()));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody CreateUserRequest createUserRequest) {
        return ResponseEntity.ok(userService.createUser(createUserRequest));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestParam(required = false) UUID userId) {
        if (userId == null) return ResponseEntity.ok(userService.getAllUsers());
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody UpdateUserRequest updateUserRequest) {
        return ResponseEntity.ok(userService.updateUser(id, updateUserRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
