package integra.momentifly.service;

import integra.momentifly.dto.CreateQuestRequest;
import integra.momentifly.dto.UpdateQuestRequest;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Challenge;
import integra.momentifly.model.Quest;
import integra.momentifly.repository.ChallengeRepository;
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
    private final ChallengeRepository challengeRepository;

    public Quest createQuest(CreateQuestRequest request) {

        Quest quest = new Quest();
        quest.setDescription(request.description());
        quest.setPoints(request.points());
        quest.setChallenge(resolveChallenge(request.challengeId()));
        return questRepository.save(quest);
    }

    public List<Quest> getQuestsWithoutChallenge() {
        return questRepository.findByChallengeIsNull();
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
            if (request.challengeId() != null) {
                quest.setChallenge(resolveChallenge(request.challengeId()));
            }
            return questRepository.save(quest);
        }).orElseThrow(() -> new RuntimeException("Quest not found"));
    }

    public void deleteQuest(UUID id) {
        questRepository.deleteById(id);
    }

    private Challenge resolveChallenge(UUID challengeId) {
        if (challengeId == null) {
            return null;
        }
        return challengeRepository.findById(challengeId).orElseThrow(() -> new NotFoundException("Challenge with id " + challengeId + " not found"));
    }
}
