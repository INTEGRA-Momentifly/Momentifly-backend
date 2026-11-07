package integra.momentifly.service;

import integra.momentifly.model.*;
import integra.momentifly.repository.QuestCompletionRepository;
import integra.momentifly.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
  private final UserService userService;
  private final QuestService questService;

  public List<QuestCompletion> findByUserIdAndQuestId(UUID userId, UUID questId){
    return questCompletionRepository.findByUserIdAndQuestId(userId,questId);
  }

  public List<QuestCompletion> findAll() {
    return questCompletionRepository.findAll();
  }

  public Optional<QuestCompletion> findById(UUID id) {
    return questCompletionRepository.findById(id);
  }

  public Optional<QuestCompletion> completeQuest(UUID questId, String authName, Double points, QuestEnum status, LocalDateTime date){
    UUID userId = userService.getUserByUsername(authName).getId();

    if ( !findByUserIdAndQuestId(userId,questId).isEmpty()){
      return Optional.empty();
    }

    QuestCompletion questCompletion = new QuestCompletion();
    questCompletion.setQuestId(questId);
    questCompletion.setUserId(userId);
    questCompletion.setCompletionStatus(status);
    questCompletion.setCompletionDate(date);
    questCompletion.setPoints(points);
    return Optional.of(questCompletionRepository.save(questCompletion));
  }

  public Integer weekScore(LocalDateTime date, String authName){
    UUID userId = userService.getUserByUsername(authName).getId();

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

    //division by 0
    if (total == 0.0) {
      return 0;
    }

    //presentage
    return (int) Math.round((completed / total) * 100);
  }

  public QuestCompletion save(QuestCompletion questCompletion) {
    return questCompletionRepository.save(questCompletion);
  }

  public void deleteById(UUID id) {
    questCompletionRepository.deleteById(id);
  }

  @Scheduled(cron = "0 0 0 * * *")
  @Transactional
  public void registerMissedQuestsDaily() {
    LocalDateTime now = LocalDateTime.now();
    List<User> users = userService.findAll();

    for (User user : users) {
      List<Quest> userQuests = questService.findByUserId(user.getId());

      for (Quest quest : userQuests) {
        //is this quest completed or not
        boolean alreadyRecorded = !findByUserIdAndQuestId(user.getId(), quest.getId()).isEmpty();

        if (!alreadyRecorded) {
          QuestCompletion openCompletion = new QuestCompletion();
          openCompletion.setQuestId(quest.getId());
          openCompletion.setUserId(user.getId());
          openCompletion.setCompletionStatus(QuestEnum.OPEN);
          openCompletion.setCompletionDate(now);
          openCompletion.setPoints(quest.getPoints());

          questCompletionRepository.save(openCompletion);
        }
      }
    }
  }
}
