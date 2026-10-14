package integra.momentifly.controller;

import integra.momentifly.dto.ReminderRequest;
import integra.momentifly.dto.ReminderResponse;
import integra.momentifly.mapper.ReminderMapper;
import integra.momentifly.model.Reminder;
import integra.momentifly.service.ReminderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public List<ReminderResponse> getAllReminders(@RequestParam(required = false) UUID userId) {
        if(userId!=null){
            return reminderService.findByUserId(userId)
                    .stream()
                    .map(mapper::toDto)
                    .toList();
        }
        return reminderService.findAll()
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
    public ReminderResponse create(@Valid  @RequestBody ReminderRequest reminder) {
        return mapper.toDto(
                reminderService.save(mapper.fromDto(reminder))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReminderRequest reminderRequest) {

        if (reminderService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reminder reminder = mapper.fromDto(reminderRequest);
        reminder.setId(id);

        Reminder updated = reminderService.save(reminder);

        return ResponseEntity.ok(mapper.toDto(updated));
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
