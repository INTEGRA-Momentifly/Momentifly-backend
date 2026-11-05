package integra.momentifly.controller;

import integra.momentifly.dto.ReminderRequest;
import integra.momentifly.dto.ReminderResponse;
import integra.momentifly.mapper.ReminderMapper;
import integra.momentifly.model.Reminder;
import integra.momentifly.service.ReminderService;
import integra.momentifly.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reminder")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;
    private final UserService userService;
    private final ReminderMapper mapper;

    @GetMapping
    public List<ReminderResponse> getRemindersByUser(Authentication authentication) {
        return reminderService.findByUserId(userService.getUserByUsername(authentication.getName()).getId())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponse> getReminderById(@PathVariable UUID id) {
        return reminderService.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ReminderResponse create(Authentication authentication, @Valid  @RequestBody ReminderRequest request) {
        return mapper.toDto(
                reminderService.createReminder(userService.getUserByUsername(authentication.getName()), request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReminderRequest request) {

        if (reminderService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(mapper.toDto(reminderService.updateReminder(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        if (!reminderService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        reminderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
