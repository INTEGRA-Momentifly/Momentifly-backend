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

    public Quest createQuest(CreateQuestRequest request) {

        Quest quest = new Quest();
        quest.setDescription(request.description());
        quest.setPoints(request.points());
        return questRepository.save(quest);
    }

    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    public Optional<Quest> getQuestById(UUID id) {
        return questRepository.findById(id);
    }

    public Quest updateQuest(UUID id, UpdateQuestRequest request) {

        Quest updatedQuest = new Quest();
        updatedQuest.setDescription(request.description());
        updatedQuest.setPoints(request.points());
        return questRepository.findById(id).map(quest -> {
            quest.setDescription(updatedQuest.getDescription());
            quest.setPoints(updatedQuest.getPoints());
            return questRepository.save(quest);
        }).orElseThrow(() -> new RuntimeException("Quest not found"));
    }

    public void deleteQuest(UUID id) {
        questRepository.deleteById(id);
    }
}
