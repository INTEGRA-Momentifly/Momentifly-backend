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
    private final ReminderMapper mapper;

    @GetMapping
    public List<ReminderResponse> getRemindersByUser(Authentication authentication) {
        return reminderService.findByUsername(authentication.getName())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponse> getReminderById(Authentication authentication, @PathVariable UUID id) {
        return ResponseEntity.ok(mapper.toDto(reminderService.findById(id, authentication.getName())));
    }

    @PostMapping
    public ReminderResponse create(Authentication authentication, @Valid @RequestBody ReminderRequest request) {
        return mapper.toDto(reminderService.createReminder(authentication.getName(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> update(Authentication authentication, @PathVariable UUID id,
                                                   @Valid @RequestBody ReminderRequest request) {
        return ResponseEntity.ok(mapper.toDto(reminderService.updateReminder(id, authentication.getName(), request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication, @PathVariable UUID id) {
        reminderService.deleteById(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
