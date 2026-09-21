package integra.momentifly.service;

import integra.momentifly.model.Reminder;
import integra.momentifly.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReminderService {
    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public List<Reminder> findAll() {
        return reminderRepository.findAll();
    }

    public Optional<Reminder> findById(Long id) {
        return reminderRepository.findById(id);
    }

    public List<Reminder> findByUserId(Long userId) {
        return reminderRepository.findByUserId(userId);
    }

    public Reminder save(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    public void deleteById(Long id) {
        reminderRepository.deleteById(id);
    }
}
