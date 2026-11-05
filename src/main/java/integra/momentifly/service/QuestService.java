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

    public Quest createQuest(UUID userId, CreateQuestRequest request) {

        Quest quest = new Quest();
        quest.setDescription(request.description());
        quest.setPoints(request.points());
        quest.setUserId(userId);
        return questRepository.save(quest);
    }

    public List<Quest> getAllQuests(UUID userId) {
        return questRepository.findByUserId(userId);
    }

    public Optional<Quest> getQuestById(UUID id) {
        return questRepository.findById(id);
    }

    public Quest updateQuest(UUID id, UpdateQuestRequest request) {

        Quest updatedQuest = questRepository.findById(id).get();
        updatedQuest.setDescription(request.description());
        updatedQuest.setPoints(request.points());
        return questRepository.findById(id).map(quest -> {
            quest.setDescription(updatedQuest.getDescription());
            quest.setPoints(updatedQuest.getPoints());
            quest.setUserId(updatedQuest.getUserId());
            return questRepository.save(quest);
        }).orElseThrow(() -> new RuntimeException("Quest not found"));
    }

    public void deleteQuest(UUID id) {
        questRepository.deleteById(id);
    }
}
