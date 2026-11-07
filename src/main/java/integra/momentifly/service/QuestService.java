package integra.momentifly.service;

import integra.momentifly.dto.CreateQuestRequest;
import integra.momentifly.dto.UpdateQuestRequest;
import integra.momentifly.model.Quest;
import integra.momentifly.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestService {

  private final QuestRepository questRepository;
  private final UserService userService;

  public List<Quest> findByUserId(UUID uuid){ return questRepository.findByUserId(uuid);}
  public Quest createQuest(String authName, CreateQuestRequest request) {
    UUID userId = userService.getUserByUsername(authName).getId();
    Quest quest = new Quest();
    quest.setDescription(request.description());
    quest.setPoints(request.points());
    quest.setUserId(userId);
    return questRepository.save(quest);
  }

  public List<Quest> getAllQuests(String authName) {
    UUID userId = userService.getUserByUsername(authName).getId();
    return questRepository.findByUserId(userId);
  }

  public Optional<Quest> getQuestById(UUID id, String authName) {
    UUID userId = userService.getUserByUsername(authName).getId();
    return questRepository.findByIdAndUserId(id, userId);
  }

  public Quest updateQuest(UUID id, UpdateQuestRequest request, String authName) {
    UUID userId = userService.getUserByUsername(authName).getId();

    return questRepository.findByIdAndUserId(id, userId)
        .map(quest -> {
          quest.setDescription(request.description());
          quest.setPoints(request.points());
          return questRepository.save(quest);
        })
        .orElseThrow(() -> new RuntimeException("Quest not found"));
  }

  public void deleteQuest(UUID id, String authName) {
    UUID userId = userService.getUserByUsername(authName).getId();
    questRepository.deleteByIdAndUserId(id, userId);
  }
}
