package integra.momentifly.controller;

import integra.momentifly.model.Reminder;
import integra.momentifly.service.ReminderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminder")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @GetMapping
    public List<Reminder> findAll() {
        return reminderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reminder> findById(@PathVariable Long id) {
        return reminderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Reminder> findByUserId(@PathVariable Long userId) {
        return reminderService.findByUserId(userId);
    }

    @PostMapping
    public Reminder create(@RequestBody Reminder reminder) {
        return reminderService.save(reminder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reminder> update(
            @PathVariable Long id,
            @RequestBody Reminder reminder) {

        if (!reminderService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        reminder.setId(id);
        return ResponseEntity.ok(reminderService.save(reminder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (!reminderService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        reminderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
