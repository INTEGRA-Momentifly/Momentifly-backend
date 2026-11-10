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
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {

  private final QuestService questService;
  private final QuestCompletionService questCompletionService;
  private final QuestCompletionMapper mapper;

  @PostMapping("/completeQuest")
  public ResponseEntity<QuestCompletionResponse> completeQuest(Authentication authentication, @RequestParam UUID questId) {
    Optional<Quest> quest = questService.getQuestById(questId, authentication.getName());

    if ( !quest.isPresent()){
      return  ResponseEntity.notFound().build();
    }

    Double points = quest.get().getPoints();
    Optional<QuestCompletion> questCompletion = questCompletionService
        .completeQuest(questId, authentication.getName(), points, QuestEnum.COMPLETE, LocalDateTime.now());

    return questCompletion
        .map(completion -> ResponseEntity.ok(mapper.toDto(completion)))
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  @GetMapping("/completedQuests")
  public ResponseEntity<List<UUID>> getCompletedQuests(
      Authentication authentication
  ) {
    return ResponseEntity.ok(
        questCompletionService.getCompletedQuestIds(
            authentication.getName()
        )
    );
  }

  @GetMapping("/weekScore")
  public Integer weekScore(Authentication authentication, @RequestParam LocalDateTime date){
    return questCompletionService.weekScore(date, authentication.getName());
  }

  @PostMapping
  public ResponseEntity<Quest> createQuest(Authentication authentication, @RequestBody CreateQuestRequest quest) {
    return ResponseEntity.ok(questService.createQuest(authentication.getName(), quest));
  }

  @GetMapping
  public ResponseEntity<List<Quest>> getAllQuests(Authentication authentication) {
    return ResponseEntity.ok(questService.getAllQuests(authentication.getName()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Quest> getQuestById(Authentication authentication, @PathVariable UUID id) {
    return questService.getQuestById(id, authentication.getName())
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<Quest> updateQuest(Authentication authentication, @PathVariable UUID id, @RequestBody UpdateQuestRequest quest) {
    try {
      return ResponseEntity.ok(questService.updateQuest(id, quest,authentication.getName()));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteQuest(Authentication authentication,@PathVariable UUID id) {
    questService.deleteQuest(id, authentication.getName());
    return ResponseEntity.noContent().build();
  }
}
