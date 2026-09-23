package integra.momentifly.controller;

import integra.momentifly.dto.ReminderDtoIn;
import integra.momentifly.dto.ReminderDtoOut;
import integra.momentifly.mapper.ReminderMapper;
import integra.momentifly.model.Reminder;
import integra.momentifly.service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reminder")
public class ReminderController {

    private final ReminderService reminderService;
    private final ReminderMapper mapper;

    public ReminderController(ReminderService reminderService, ReminderMapper mapper) {
        this.reminderService = reminderService;
        this.mapper = mapper;
    }

    @GetMapping
    public List<ReminderDtoOut> findAll() {
        return reminderService.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReminderDtoOut> findById(@PathVariable UUID id) {
        return reminderService.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<ReminderDtoOut> findByUserId(@PathVariable UUID userId) {
        return reminderService.findByUserId(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @PostMapping
    public ReminderDtoOut create(@Valid  @RequestBody ReminderDtoIn reminder) {
        return mapper.toDto(
                reminderService.save(mapper.fromDto(reminder))
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReminderDtoOut> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReminderDtoIn reminderDtoIn) {

        if (reminderService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reminder reminder = mapper.fromDto(reminderDtoIn);
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
