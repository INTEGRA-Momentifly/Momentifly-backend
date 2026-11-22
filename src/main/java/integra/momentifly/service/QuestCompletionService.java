package integra.momentifly.service;

import integra.momentifly.model.Quest;
import integra.momentifly.model.QuestCompletion;
import integra.momentifly.model.QuestEnum;
import integra.momentifly.model.User;
import integra.momentifly.repository.QuestCompletionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

  public List<UUID> getCompletedQuestIds(String authName) {
    UUID userId = userService.getUserByUsername(authName).getId();
    return questCompletionRepository.findCompletedQuestIds(userId, QuestEnum.COMPLETE);
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

    public int computeStreak(String username) {
        UUID userId = userService.getUserByUsername(username).getId();

        List<LocalDate> completeDays = fullyCompletedDays(questCompletionRepository.findByUserId(userId));

        return streakFrom(completeDays);
    }

    // newest first: a day is fully completed when it has quests and none of them is still OPEN
    static List<LocalDate> fullyCompletedDays(List<QuestCompletion> completions) {
        Set<LocalDate> daysWithQuests = new HashSet<>();
        Set<LocalDate> daysWithOpenQuests = new HashSet<>();

        for (QuestCompletion completion : completions) {
            LocalDate day = dayOf(completion);
            if (day == null) {continue;}
            daysWithQuests.add(day);
            if (completion.getCompletionStatus() != QuestEnum.COMPLETE) {daysWithOpenQuests.add(day);}
        }

        return daysWithQuests.stream()
                .filter(day -> !daysWithOpenQuests.contains(day))
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    // the day a quest belongs to
    private static LocalDate dayOf(QuestCompletion completion) {
        return completion.getCompletionDate() == null ? null : completion.getCompletionDate().toLocalDate();
    }

    // completionDays: newest first
    static int streakFrom(List<LocalDate> completionDays) {
        LocalDate today = LocalDate.now();

        if (completionDays.isEmpty() || completionDays.getFirst().isBefore(today.minusDays(1)))
        {return 0;}   // no quests yet, or the last one was before yesterday

        int streak = 1;   // the newest day counts
        for (int i = 0; i < completionDays.size() - 1; ++i) {
            if (ChronoUnit.DAYS.between(completionDays.get(i + 1), completionDays.get(i)) != 1)
            {break;}
            ++streak;
        }
        return streak;
    }
}
