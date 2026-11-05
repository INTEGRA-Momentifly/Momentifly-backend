package integra.momentifly.controller;


import integra.momentifly.dto.CreateQuestRequest;
import integra.momentifly.dto.QuestCompletionResponse;
import integra.momentifly.dto.UpdateQuestRequest;
import integra.momentifly.mapper.QuestCompletionMapper;
import integra.momentifly.model.Quest;
import integra.momentifly.model.QuestCompletion;
import integra.momentifly.model.QuestEnum;
import integra.momentifly.service.QuestCompletionService;
import integra.momentifly.service.QuestService;
import integra.momentifly.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

  private final QuestService questService;
  private final UserService userService;
  private final QuestCompletionService questCompletionService;
  private final QuestCompletionMapper mapper;

  @PostMapping("/completeQuest")
  public QuestCompletionResponse completeQuest(Authentication authentication, @RequestParam UUID questId) {
    //notfound
    UUID userId = userService.getUserByUsername(authentication.getName()).getId();
    Double points = questService.getQuestById(questId).get().getPoints();
    QuestCompletion questCompletion = new QuestCompletion();
    questCompletion = questCompletionService.completeQuest(questId, userId, points, QuestEnum.COMPLETE, LocalDateTime.now());
    return mapper.toDto(questCompletion);
  }

  @GetMapping("/weekScore")
  public Integer weekScore(Authentication authentication, @RequestParam LocalDateTime date){
    UUID userId = userService.getUserByUsername(authentication.getName()).getId();
    return questCompletionService.weekScore(date, userId);
  }

  @PostMapping
  public ResponseEntity<Quest> createQuest(Authentication authentication, @RequestBody CreateQuestRequest quest) {
    UUID userId = userService.getUserByUsername(authentication.getName()).getId();
    return ResponseEntity.ok(questService.createQuest(userId, quest));
  }

  @GetMapping
  public ResponseEntity<List<Quest>> getAllQuests(Authentication authentication) {
    UUID userId = userService.getUserByUsername(authentication.getName()).getId();
    return ResponseEntity.ok(questService.getAllQuests(userId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Quest> getQuestById(@PathVariable UUID id) {
    return questService.getQuestById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Quest> updateQuest(@PathVariable UUID id, @RequestBody UpdateQuestRequest quest) {
    try {
      return ResponseEntity.ok(questService.updateQuest(id, quest));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQuest(@PathVariable UUID id) {
    questService.deleteQuest(id);
    return ResponseEntity.noContent().build();
  }
}
