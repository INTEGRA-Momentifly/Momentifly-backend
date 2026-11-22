package integra.momentifly.service;

import integra.momentifly.dto.CreateQuestRequest;
import integra.momentifly.dto.UpdateQuestRequest;
import integra.momentifly.exception.NotFoundException;
import integra.momentifly.model.Challenge;
import integra.momentifly.model.Quest;
import integra.momentifly.repository.ChallengeRepository;
import integra.momentifly.repository.QuestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class QuestServiceTest {

    @Mock
    private QuestRepository questRepository;

    @Mock
    private ChallengeRepository challengeRepository;

    @InjectMocks
    private QuestService questService;

    @Test
    void createQuest_ShouldReturnSavedQuest() {

        CreateQuestRequest request = new CreateQuestRequest("Test Quest", 10.0, null);

        Quest savedQuest = new Quest(UUID.randomUUID(), "Test Quest", 10.0, null);

        when(questRepository.save(any(Quest.class))).thenReturn(savedQuest);

        Quest result = questService.createQuest(request);

        assertNotNull(result.getId());
        assertEquals("Test Quest", result.getDescription());

        verify(questRepository, times(1)).save(any(Quest.class));
    }

    @Test
    void getQuestsWithoutChallenge_ShouldReturnList() {

      Quest quest = new Quest(UUID.randomUUID(), "Test", 10.0, null);
      when(questRepository.findByChallengeIsNull()).thenReturn(List.of(quest));

      List<Quest> result = questService.getQuestsWithoutChallenge();

      assertEquals(1, result.size());
      verify(questRepository, times(1)).findByChallengeIsNull();
    }

    @Test
    void getQuestById_ShouldReturnQuest_WhenExists() {

        UUID id = UUID.randomUUID();
        Quest quest = new Quest(id, "Test", 10.0, null);

        when(questRepository.findById(id)).thenReturn(Optional.of(quest));

        Optional<Quest> result = questService.getQuestById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void updateQuest_ShouldUpdateAndReturnQuest_WhenExists() {

        UUID id = UUID.randomUUID();
        Quest existingQuest = new Quest(id, "Old", 10.0, null);


        UpdateQuestRequest request = new UpdateQuestRequest("New", 20.0, null);

        when(questRepository.findById(id)).thenReturn(Optional.of(existingQuest));
        when(questRepository.save(any(Quest.class))).thenReturn(existingQuest);

        Quest result = questService.updateQuest(id, request);

        assertEquals("New", result.getDescription());
        assertEquals(20.0, result.getPoints());
    }

    @Test
    void updateQuest_ShouldThrowException_WhenNotFound() {

        UUID id = UUID.randomUUID();

        UpdateQuestRequest request = new UpdateQuestRequest("New", 20.0, null);

        when(questRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> questService.updateQuest(id, request));
    }
    @Test
    void deleteQuest_ShouldCallRepositoryDelete() {

        UUID id = UUID.randomUUID();
        questService.deleteQuest(id);
        verify(questRepository, times(1)).deleteById(id);
    }

    @Test
    void createQuest_ShouldAttachChallenge_WhenChallengeIdIsValid() {

      UUID challengeId = UUID.randomUUID();
      Challenge challenge = new Challenge();
      challenge.setId(challengeId);

      CreateQuestRequest request = new CreateQuestRequest("Quest", 10.0, challengeId);

      when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));
      when(questRepository.save(any(Quest.class))).thenAnswer(invocation -> invocation.getArgument(0));

      Quest result = questService.createQuest(request);

      assertSame(challenge, result.getChallenge());
      verify(questRepository, times(1)).save(any(Quest.class));
    }

    @Test
    void createQuest_ShouldThrowNotFound_WhenChallengeDoesNotExist() {

      UUID challengeId = UUID.randomUUID();
      CreateQuestRequest request = new CreateQuestRequest("Quest", 10.0, challengeId);

      when(challengeRepository.findById(challengeId)).thenReturn(Optional.empty());

      assertThrows(NotFoundException.class, () -> questService.createQuest(request));
      verify(questRepository, never()).save(any(Quest.class));
    }

    @Test
    void updateQuest_ShouldSetChallenge_WhenChallengeIdIsProvided() {

      UUID questId = UUID.randomUUID();
      UUID challengeId = UUID.randomUUID();
      Quest existingQuest = new Quest(questId, "Old", 10.0, null);
      Challenge challenge = new Challenge();
      challenge.setId(challengeId);

      UpdateQuestRequest request = new UpdateQuestRequest("New", 20.0, challengeId);

      when(questRepository.findById(questId)).thenReturn(Optional.of(existingQuest));
      when(challengeRepository.findById(challengeId)).thenReturn(Optional.of(challenge));
      when(questRepository.save(any(Quest.class))).thenAnswer(invocation -> invocation.getArgument(0));

      Quest result = questService.updateQuest(questId, request);

      assertSame(challenge, result.getChallenge());
    }

    @Test
    void updateQuest_ShouldKeepExistingChallenge_WhenChallengeIdIsNull() {

      UUID questId = UUID.randomUUID();
      Challenge existingChallenge = new Challenge();
      existingChallenge.setId(UUID.randomUUID());
      Quest existingQuest = new Quest(questId, "Old", 10.0, existingChallenge);

      UpdateQuestRequest request = new UpdateQuestRequest("New", 20.0, null);

      when(questRepository.findById(questId)).thenReturn(Optional.of(existingQuest));
      when(questRepository.save(any(Quest.class))).thenAnswer(invocation -> invocation.getArgument(0));

      Quest result = questService.updateQuest(questId, request);

      assertSame(existingChallenge, result.getChallenge());
      assertEquals("New", result.getDescription());
    }
}
