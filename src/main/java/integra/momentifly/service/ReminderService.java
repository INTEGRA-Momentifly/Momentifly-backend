package integra.momentifly.service;

import integra.momentifly.model.Reminder;
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

    public Reminder save(Reminder reminder) {
        return reminderRepository.save(reminder);
    }

    public void deleteById(UUID id) {
        reminderRepository.deleteById(id);
    }
}
