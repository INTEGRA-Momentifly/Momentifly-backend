package integra.momentifly.service;

import integra.momentifly.model.QuestCompletion;
import integra.momentifly.model.QuestEnum;
import integra.momentifly.model.Reminder;
import integra.momentifly.repository.QuestCompletionRepository;
import integra.momentifly.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestCompletionService {
  private final QuestCompletionRepository questCompletionRepository;

  public List<QuestCompletion> findAll() {
    return questCompletionRepository.findAll();
  }

  public Optional<QuestCompletion> findById(UUID id) {
    return questCompletionRepository.findById(id);
  }

  public QuestCompletion completeQuest(UUID questId, UUID userId, Double points, QuestEnum status, LocalDateTime date){
    QuestCompletion questCompletion = new QuestCompletion();
    questCompletion.setQuestId(questId);
    questCompletion.setUserId(userId);
    questCompletion.setCompletionStatus(status);
    questCompletion.setCompletionDate(date);
    questCompletion.setPoints(points);
    return questCompletionRepository.save(questCompletion);
  }

  public Integer weekScore(LocalDateTime date, UUID userId){
    LocalDateTime lastMonday = date
        .toLocalDate()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .atStartOfDay();

    List<QuestCompletion> questCompletions = questCompletionRepository
        .findByUserIdAndCompletionDateBetween(userId, lastMonday, date);

    Double completed = 0.0, total = 0.0;

    completed = questCompletions.stream()
        .filter(item -> item.getCompletionStatus() == QuestEnum.COMPLETE)
        .mapToDouble(QuestCompletion::getPoints)
        .sum();

    total = questCompletions.stream()
        .mapToDouble(QuestCompletion::getPoints)
        .sum();

    return (int) (completed/total);
  }

  public QuestCompletion save(QuestCompletion questCompletion) {
    return questCompletionRepository.save(questCompletion);
  }

  public void deleteById(UUID id) {
    questCompletionRepository.deleteById(id);
  }
}
