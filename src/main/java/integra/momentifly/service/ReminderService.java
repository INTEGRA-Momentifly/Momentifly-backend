package integra.momentifly.service;

import integra.momentifly.dto.ReminderRequest;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Reminder;
import integra.momentifly.model.User;
import integra.momentifly.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final UserService userService;

    public List<Reminder> findByUsername(String username) {
        return reminderRepository.findByUser_Username(username);
    }

    public Reminder findById(UUID id, String username) {
        return reminderRepository.findByIdAndUser_Username(id, username)
                .orElseThrow(() -> new NotFoundException("Reminder not found"));
    }

    public Reminder createReminder(String username, ReminderRequest request) {
        Reminder reminder = new Reminder();
        reminder.setUser(userService.getUserByUsername(username));
        reminder.setDescription(request.getDescription());
        reminder.setReminderDate(request.getReminderDate());
        reminder.setDone(request.getDone());
        reminder.setRecurrence(request.getRecurrence());
        return reminderRepository.save(reminder);
    }

    public Reminder updateReminder(UUID id, String username, ReminderRequest request) {
        Reminder reminder = findById(id, username);
        reminder.setDescription(request.getDescription());
        reminder.setReminderDate(request.getReminderDate());
        reminder.setDone(request.getDone());
        reminder.setRecurrence(request.getRecurrence());
        return reminderRepository.save(reminder);
    }

    public void deleteById(UUID id, String username) {
        reminderRepository.delete(findById(id, username));
    }
}
