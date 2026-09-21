package integra.momentifly.controller;


import integra.momentifly.model.Quest;
import integra.momentifly.service.QuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/quests")
@RequiredArgsConstructor
public class QuestController {
    private final QuestService questService;

    @PostMapping
    public ResponseEntity<Quest> createQuest(@RequestBody Quest quest) {
        return ResponseEntity.ok(questService.createQuest(quest));
    }

    @GetMapping
    public ResponseEntity<List<Quest>> getAllQuests() {
        return ResponseEntity.ok(questService.getAllQuests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quest> getQuestById(@PathVariable UUID id) {
        return questService.getQuestById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Quest> updateQuest(@PathVariable UUID id, @RequestBody Quest quest) {
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
