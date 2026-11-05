package integra.momentifly.service;

import integra.momentifly.dto.ReminderRequest;
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

    public List<Reminder> findAll() {
        return reminderRepository.findAll();
    }

    public Optional<Reminder> findById(UUID id) {
        return reminderRepository.findById(id);
    }

    public List<Reminder> findByUserId(UUID userId) {
        return reminderRepository.findByUserId(userId);
    }

    public Reminder createReminder(User user, ReminderRequest request) {
        Reminder reminder = new Reminder();
        reminder.setUser(user);
        reminder.setDescription(request.getDescription());
        reminder.setReminderDate(request.getReminderDate());
        reminder.setDone(request.getDone());
        reminder.setRecurrence(request.getRecurrence());
        return reminderRepository.save(reminder);
    }

    public Reminder updateReminder(UUID id, ReminderRequest request) {
        Reminder reminder = findById(id).get();
        reminder.setDescription(request.getDescription());
        reminder.setReminderDate(request.getReminderDate());
        reminder.setDone(request.getDone());
        reminder.setRecurrence(request.getRecurrence());
        return reminderRepository.save(reminder);
    }

    public void deleteById(UUID id) {
        reminderRepository.deleteById(id);
    }
}
